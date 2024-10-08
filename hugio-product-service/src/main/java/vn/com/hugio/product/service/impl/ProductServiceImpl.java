package vn.com.hugio.product.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.common.service.CurrentUserService;
import vn.com.hugio.common.utils.ExceptionStackTraceUtil;
import vn.com.hugio.common.utils.HttpUtil;
import vn.com.hugio.common.utils.StringUtil;
import vn.com.hugio.product.dto.ProductDto;
import vn.com.hugio.product.dto.ProductQuantityDto;
import vn.com.hugio.product.dto.QrCodeReqDto;
import vn.com.hugio.product.entity.Category;
import vn.com.hugio.product.entity.Product;
import vn.com.hugio.product.entity.ProductCategory;
import vn.com.hugio.product.entity.repository.ProductRepository;
import vn.com.hugio.product.enums.InventoryCallMethod;
import vn.com.hugio.product.mapper.ProductMapper;
import vn.com.hugio.product.redis.RedisCacheService;
import vn.com.hugio.product.request.CreateProductRequest;
import vn.com.hugio.product.request.DeleteProductRequest;
import vn.com.hugio.product.request.EditProductRequest;
import vn.com.hugio.product.request.ImportProductQuantityRequest;
import vn.com.hugio.product.service.CategoryService;
import vn.com.hugio.product.service.ProductCategoryService;
import vn.com.hugio.product.service.ProductDetailService;
import vn.com.hugio.product.service.ProductService;
import vn.com.hugio.product.service.grpc.client.InventoryServiceGrpcClient;
import vn.com.hugio.product.service.grpc.request.InventoryRequest;
import vn.com.hugio.product.service.kafka.KafkaProductService;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {InternalServiceException.class, RuntimeException.class, Exception.class, Throwable.class})
public class ProductServiceImpl extends BaseService<Product, ProductRepository> implements ProductService {

    private static String REDIS_KEY_FORMAT = "all_%s_%s_%s_%s";

    private final ProductDetailService productDetailService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductCategoryService productCategoryService;
    private final InventoryServiceGrpcClient inventoryServiceGrpcClient;
    private final HttpUtil httpUtil;
    private final ObjectMapper objectMapper;
    private final CurrentUserService currentUserService;
    private final RedisCacheService redisCacheService;
    private final Gson gson;
    private final KafkaProductService kafkaProductService;

    @Value("${qr.code.api-url}")
    private String qrCodeApiUrl;

    public ProductServiceImpl(ProductRepository repository,
                              ProductDetailService productDetailService,
                              ProductMapper productMapper,
                              CategoryService categoryService,
                              ProductCategoryService productCategoryService,
                              InventoryServiceGrpcClient inventoryServiceGrpcClient,
                              HttpUtil httpUtil,
                              ObjectMapper objectMapper,
                              CurrentUserService currentUserService,
                              RedisCacheService redisCacheService,
                              Gson gson,
                              KafkaProductService kafkaProductService) {
        super(repository);
        this.productDetailService = productDetailService;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
        this.productCategoryService = productCategoryService;
        this.inventoryServiceGrpcClient = inventoryServiceGrpcClient;
        this.httpUtil = httpUtil;
        this.objectMapper = objectMapper;
        this.currentUserService = currentUserService;
        this.redisCacheService = redisCacheService;
        this.gson = gson;
        this.kafkaProductService = kafkaProductService;
    }

    @Override
    public void createProduct(CreateProductRequest request) {
        this.redisCacheService.delete("all");
        Optional<Product> optionalProduct = this.repository.findByProductName(request.getName());
        if (optionalProduct.isPresent()) {
            throw new InternalServiceException(ErrorCodeEnum.EXISTS.getCode(), "this product has been existed");
        }
        Product product = Product.builder()
                .productUid(UUID.randomUUID().toString())
                .productName(request.getName())
                .rawProductName(StringUtil.removeAccent(request.getName()))
                .productDescription(request.getProductDescription())
                .discount(0D)
                .price(request.getPrice())
                .build();
        byte[] image = this.generateQrCode(this.productMapper.productEntityToProductDto(product));
        product.setProductQr(image);
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .productUid(product.getProductUid())
                .importedBy(this.currentUserService.getUsername())
                .importedQuantity(request.getQuantity())
                .importedFee(request.getFee())
                .importedFrom(Strings.EMPTY)
                .note(Strings.EMPTY)
                .build();
        this.repository.save(product);
        this.callInventory(inventoryRequest, InventoryCallMethod.CREATE);
        LOG.info("SAVE PRODUCT {} SUCCESS, SAVE PRODUCT DETAIL", request.getName());
        this.productDetailService.addOrSaveProductDetail(product, request.getDetails());
        if (request.getCategory() != null && !(request.getCategory().isEmpty())) {
            LOG.info("SAVE CATEGORY FOR NEW PRODUCT {}", request.getName());
            List<Category> categories = this.categoryService.getCategoryByNameOrUid(request.getCategory());
            Product finalProduct = product;
            List<ProductCategory> productCategories = categories.stream().map(
                    category -> ProductCategory.builder()
                            .product(finalProduct)
                            .category(category)
                            .build()
            ).collect(Collectors.toList());
            product.setProductCategories(productCategories);
            this.productCategoryService.saveEntities(productCategories);
            this.repository.save(product);
        }
    }

    @Override
    public void updateProduct(EditProductRequest request) {
        this.redisCacheService.delete("all");
        Product product = this.repository.findByProductUidAndActiveIsTrue(
                request.getProductId()
        ).orElseThrow(
                () -> new InternalServiceException(ErrorCodeEnum.EXISTS.getCode(), "this product does not exist")
        );
        product.setProductName(
                StringUtil.isNotEmpty(request.getName()) ?
                        request.getName() : product.getProductName()
        );
        product.setRawProductName(
                StringUtil.isNotEmpty(request.getName()) ?
                        StringUtil.removeAccent(request.getName()) : product.getRawProductName()
        );
        product.setPrice(
                request.getPrice() != null && request.getPrice() > 0D ?
                        request.getPrice() : product.getPrice()
        );
        product.setDiscount(
                request.getDiscount() != null && request.getDiscount() > 0D ?
                        request.getDiscount() : product.getDiscount()
        );
        product.setProductDescription(
                StringUtil.isNotEmpty(request.getProductDescription()) ?
                        request.getProductDescription() : product.getProductDescription()
        );
        byte[] image = this.generateQrCode(this.productMapper.productEntityToProductDto(product));
        product.setProductQr(image);
        product = this.repository.save(product);
        LOG.info("UPDATE PRODUCT {} SUCCESS, UPDATE PRODUCT DETAIL", request.getName());
        if (request.getDetails() != null && !(request.getDetails().isEmpty())) {
            LOG.info("UPDATE PRODUCT DETAIL", request.getName());
            this.productDetailService.addOrSaveProductDetail(product, request.getDetails());
        }
        if (request.getCategory() != null && !(request.getCategory().isEmpty())) {
            LOG.info("UPDATE CATEGORY FOR PRODUCT {}", request.getName());
            this.productCategoryService.deleteEntities(product.getId());
            List<Category> categories = this.categoryService.getCategoryByNameOrUid(request.getCategory());
            Product finalProduct = product;
            List<ProductCategory> productCategories = categories.stream().map(
                    category -> ProductCategory.builder()
                            .product(finalProduct)
                            .category(category)
                            .build()
            ).collect(Collectors.toList());
            product.setProductCategories(productCategories);
            this.repository.save(product);
        }
        if (request.getQuantity() != null && request.getQuantity() > 0) {
            InventoryRequest inventoryRequest = InventoryRequest.builder()
                    .productUid(product.getProductUid())
                    .importedBy(this.currentUserService.getUsername())
                    .importedQuantity(request.getQuantity())
                    .importedFrom(Strings.EMPTY)
                    .importedFee(request.getFee())
                    .note(Strings.EMPTY)
                    .build();
            this.callInventory(inventoryRequest, InventoryCallMethod.IMPORT);
        }
    }

    @Override
    public PageResponse<ProductDto> getAllProduct(PagableRequest request) {
        if (StringUtils.isBlank(request.getContent())) {
            return this.getWithNoContent(request);
        } else {
            return this.getWithContent(request);
        }
    }

    private PageResponse<ProductDto> getWithContent(PagableRequest request) {
        PageResponse<ProductDto> pageResponse;
        PageLink pageLink = PageLink.create(request.getPageSize(), request.getPageNumber(), request.getSort());
        Page<Product> products = this.repository.findByContent(request.getContent(), pageLink.toPageable());
        List<ProductDto> dtoList = products.stream()
                .map(productMapper::productEntityToProductDto)
                .toList().stream().peek(dto -> {
                    ProductQuantityDto dto1 = inventoryServiceGrpcClient.getProductQuantity(dto.getProductUid());
                    dto.setQuantity(dto1.getQuantity());
                    dto.setImportedQuantity(dto1.getImportedQuantity());
                    dto.setFee(dto1.getFee());
                })
                .toList();
        pageResponse = PageResponse.create(products, dtoList, true);
        return pageResponse;
    }

    private PageResponse<ProductDto> getWithNoContent(PagableRequest request) {
        String redisKey = String.format(
                REDIS_KEY_FORMAT,
                request.getPageNumber(),
                request.getPageSize(),
                StringUtils.isNotEmpty(request.getProperty()) ? request.getProperty() : "none",
                request.getSort().name()
        );
        PageResponse<ProductDto> pageResponse;
        // try get data from redis
        // pageResponse = this.redisCacheService.get(redisKey, new TypeReference<>() {
        // });
        pageResponse = null;
        if (pageResponse == null) {
            PageLink pageLink = PageLink.create(request.getPageSize(), request.getPageNumber(), request.getSort());
            Page<Product> products = this.repository.findByActiveIsTrue(pageLink.toPageable());
            List<ProductDto> dtoList = products.stream()
                    .map(productMapper::productEntityToProductDto)
                    .toList().stream().peek(dto -> {
                        ProductQuantityDto dto1 = inventoryServiceGrpcClient.getProductQuantity(dto.getProductUid());
                        dto.setQuantity(dto1.getQuantity());
                        dto.setImportedQuantity(dto1.getImportedQuantity());
                        dto.setFee(dto1.getFee());
                    })
                    .toList();
            pageResponse = PageResponse.create(products, dtoList, true);
            try {
                String dataJson = this.objectMapper.writeValueAsString(pageResponse);
                this.redisCacheService.set(
                        redisKey,
                        dataJson,
                        Duration.ofMinutes(30)
                );
            } catch (Exception e) {
                LOG.warn(ExceptionStackTraceUtil.getStackTrace(e));
            }
        }
        return pageResponse;
    }

    @Override
    public ProductDto getProductDetail(String uid) {
        LOG.info("finding a product with uid %s", uid);
        Product product = this.repository.findByProductUid(uid).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));
        ProductDto dto = this.productMapper.productEntityToProductDto(product);
        //String qrCode = this.generateQrCode(dto);
        return dto;
    }

    @Override
    public void removeProduct(DeleteProductRequest request) {
        this.redisCacheService.delete("all");
        /*
        if (request.getIsPermanent()) {
            Product product = this.repository.findByProductUid(request.getProductId()).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "product does not exist"));
            this.repository.delete(product);
        } else {
            this.repository.softDeleteByProductUid(request.getProductId());
        }
        */
        this.repository.softDeleteByProductUid(request.getProductId());
        LOG.info("REMOVED A PRODUCT WITH UID %s", request.getProductId());
    }

    @Override
    public ResponseType<String> getProductQR(String productUid) {
        Product product = this.repository.findByProductUidAndActiveIsTrue(productUid).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "product does not exist"));
        byte[] byteData = new byte[0];
        if (product.getProductQr() != null && product.getProductQr().length != 0) {
            byteData = product.getProductQr();
        } else {
            ProductDto dto = ProductDto.builder()
                    .productUid(product.getProductUid())
                    .build();
            byteData = this.generateQrCode(dto);
            product.setProductQr(byteData);
            this.repository.save(product);
        }
        String base64 = Base64.getEncoder().encodeToString(byteData);
        return ResponseType.ok(base64);
    }

    @Override
    public void importProductQuantity(ImportProductQuantityRequest request) {
        this.redisCacheService.delete("all");
        this.repository.findByProductUidAndActiveIsTrue(request.getProductId()).orElseThrow(
                () -> new InternalServiceException(ErrorCodeEnum.EXISTS.getCode(), "this product does not exist or removed")
        );
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .productUid(request.getProductId())
                .importedBy(this.currentUserService.getUsername())
                .importedQuantity(request.getQuantity())
                .importedFrom(Strings.EMPTY)
                .note(Strings.EMPTY)
                .build();
        this.callInventory(inventoryRequest, InventoryCallMethod.IMPORT);
        LOG.info("REMOVED A PRODUCT WITH UID %s", request.getProductId());
    }

    //@Override

    /**
     * byte[] encoded = Base64.getEncoder().encode("Hello".getBytes());
     * println(new String(encoded));   // Outputs "SGVsbG8="
     * <p>
     * byte[] decoded = Base64.getDecoder().decode(encoded);
     * println(new String(decoded))    // Outputs "Hello"
     *
     * @param request
     */
    public byte[] generateQrCode(ProductDto request) {
        try {

            QrCodeReqDto dto = QrCodeReqDto.builder()
                    .frameName("no-frame")
                    //.qrCodeText(this.gson.toJson(request))
                    .qrCodeText(request.getProductUid())
                    .imageFormat("PNG")
                    .imageWidth(1000)
                    //.qrCodeLogo("scan-me-square")
                    .qrCodeLogo("no-logo")
                    .frameText("scan me")
                    .frameIconName("mobile")
                    .build();
            byte[] bytes = this.httpUtil.callApi(
                    dto,
                    this.qrCodeApiUrl,
                    HttpMethod.POST,
                    new HashMap<>() {
                        {
                            put("Content-Type", "application/json");
                            put("Accept", "*/*");
                            put("Accept-Encoding", "gzip, deflate, br");
                            put("Connection", "keep-alive");
                        }
                    },
                    new ParameterizedTypeReference<byte[]>() {
                    },
                    false,
                    true, false
            ).getBody();
            //return new String(Base64.getEncoder().encode(bytes));
            return bytes;
        } catch (Exception e) {
            LOG.info(ExceptionStackTraceUtil.getStackTrace(e));
            //return Strings.EMPTY;
            return null;
        }
    }

    private void callInventory(InventoryRequest request, InventoryCallMethod method) {
        if (request.getImportedQuantity() == null || request.getImportedQuantity() == 0L) {
            return;
        }
        switch (method) {
            case CREATE -> this.kafkaProductService.send(request, "inventory_create");
            case IMPORT -> this.kafkaProductService.send(request, "inventory_import_product");
            case UPDATE -> this.kafkaProductService.send(request, "inventory_update_product");
        }
        /*
        switch (method) {
            case CREATE:
                this.inventoryServiceGrpcClient.create(request);
                break;
            case IMPORT:
                this.inventoryServiceGrpcClient.importProduct(request);
                break;
            case UPDATE:
                this.inventoryServiceGrpcClient.updateProduct(request);
                break;
        }
        */
    }
}
