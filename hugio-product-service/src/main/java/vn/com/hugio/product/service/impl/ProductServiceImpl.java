package vn.com.hugio.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.common.utils.ExceptionStackTraceUtil;
import vn.com.hugio.common.utils.HttpUtil;
import vn.com.hugio.common.utils.StringUtil;
import vn.com.hugio.product.dto.ProductDto;
import vn.com.hugio.product.dto.QrCodeReqDto;
import vn.com.hugio.product.entity.Category;
import vn.com.hugio.product.entity.Product;
import vn.com.hugio.product.entity.ProductCategory;
import vn.com.hugio.product.entity.repository.ProductRepository;
import vn.com.hugio.product.enums.InventoryCallMethod;
import vn.com.hugio.product.mapper.ProductMapper;
import vn.com.hugio.product.request.CreateProductRequest;
import vn.com.hugio.product.request.DeleteProductRequest;
import vn.com.hugio.product.request.EditProductRequest;
import vn.com.hugio.product.service.CategoryService;
import vn.com.hugio.product.service.ProductCategoryService;
import vn.com.hugio.product.service.ProductDetailService;
import vn.com.hugio.product.service.ProductService;
import vn.com.hugio.product.service.grpc.client.InventoryServiceGrpcClient;
import vn.com.hugio.product.service.grpc.request.InventoryRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {InternalServiceException.class, RuntimeException.class, Exception.class, Throwable.class})
public class ProductServiceImpl extends BaseService<Product, ProductRepository> implements ProductService {

    private final ProductDetailService productDetailService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final ProductCategoryService productCategoryService;
    private final InventoryServiceGrpcClient inventoryServiceGrpcClient;
    private final HttpUtil httpUtil;
    private final ObjectMapper objectMapper;

    @Value("${qr.code.api-url}")
    private String qrCodeApiUrl;

    public ProductServiceImpl(ProductRepository repository,
                              ProductDetailService productDetailService,
                              ProductMapper productMapper,
                              CategoryService categoryService,
                              ProductCategoryService productCategoryService,
                              InventoryServiceGrpcClient inventoryServiceGrpcClient,
                              HttpUtil httpUtil,
                              ObjectMapper objectMapper) {
        super(repository);
        this.productDetailService = productDetailService;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
        this.productCategoryService = productCategoryService;
        this.inventoryServiceGrpcClient = inventoryServiceGrpcClient;
        this.httpUtil = httpUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void createProduct(CreateProductRequest request) {
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
        product = this.save(product);
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
            //this.save(product);
        }
    }

    @Override
    public void updateProduct(EditProductRequest request) {
        Product product = this.repository.findByProductUid(
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
        product = this.save(product);
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
            this.save(product);
        }
    }

    @Override
    public PageResponse<ProductDto> getAllProduct(PagableRequest request) {
        PageLink pageLink = PageLink.create(request.getPageSize(), request.getPageNumber(), request.getSort());
        Page<Product> products = this.repository.findByActiveIsTrue(pageLink.toPageable());
        //List<ProductDto> dtos = products.stream()
        //        .map(productMapper::productEntityToProductDto)
        //        .toList();
        return PageResponse.create(products, productMapper::productEntityToProductDto, true);
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
        if (request.getIsPermanent()) {
            Product product = this.repository.findByProductUid(request.getProductId()).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "product does not exist"));
            this.repository.delete(product);
        } else {
            this.repository.softDeleteByProductUid(request.getProductId());
        }
        LOG.info("REMOVED A PRODUCT WITH UID %s", request.getProductId());
    }

    //@Override

    /**
     * byte[] encoded = Base64.getEncoder().encode("Hello".getBytes());
     * println(new String(encoded));   // Outputs "SGVsbG8="
     *
     * byte[] decoded = Base64.getDecoder().decode(encoded);
     * println(new String(decoded))    // Outputs "Hello"
     * @param request
     */
    public String generateQrCode(ProductDto request) {
        try {
            QrCodeReqDto dto = QrCodeReqDto.builder()
                    .frameName("no-frame")
                    .qrCodeText(this.objectMapper.writeValueAsString(request))
                    .imageFormat("SVG")
                    .qrCodeLogo("scan-me-square")
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
                    false
            ).getBody();
            return new String(Base64.getEncoder().encode(bytes));
        } catch (Exception e) {
            LOG.info(ExceptionStackTraceUtil.getStackTrace(e));
            return Strings.EMPTY;
        }
    }

    private void callInventory(InventoryRequest request, InventoryCallMethod method) {
        switch (method) {
            case CREATE -> this.inventoryServiceGrpcClient.create(request);
            case IMPORT -> this.inventoryServiceGrpcClient.importProduct(request);
            case UPDATE -> this.inventoryServiceGrpcClient.updateProduct(request);
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
