package vn.com.hugio.product.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.PageResponse;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.common.utils.StringUtil;
import vn.com.hugio.product.dto.ProductDto;
import vn.com.hugio.product.entity.Category;
import vn.com.hugio.product.entity.Product;
import vn.com.hugio.product.entity.ProductCategory;
import vn.com.hugio.product.entity.repository.ProductRepository;
import vn.com.hugio.product.mapper.ProductMapper;
import vn.com.hugio.product.request.CreateProductRequest;
import vn.com.hugio.product.request.EditProductRequest;
import vn.com.hugio.product.request.GetProductRequest;
import vn.com.hugio.product.service.CategoryService;
import vn.com.hugio.product.service.ProductCategoryService;
import vn.com.hugio.product.service.ProductDetailService;
import vn.com.hugio.product.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {InternalServiceException.class, RuntimeException.class, Exception.class, Throwable.class})
public class ProductServiceImpl extends BaseService<Product, ProductRepository> implements ProductService {

    private final ProductDetailService productDetailService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository repository,
                              ProductDetailService productDetailService,
                              ProductMapper productMapper,
                              CategoryService categoryService) {
        super(repository);
        this.productDetailService = productDetailService;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
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
            //this.productCategoryService.saveEntities(productCategories);
            this.save(product);
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
    }

    public PageResponse<ProductDto> getAllProduct(GetProductRequest request) {
        PageLink pageLink = PageLink.create(request.getPageSize(), request.getPageNumber(), request.getSort());
        Page<Product> products = this.repository.findByActiveIsTrue(pageLink.toPageable());
        //List<ProductDto> dtos = products.stream()
        //        .map(productMapper::productEntityToProductDto)
        //        .toList();
        return PageResponse.create(products, productMapper::productEntityToProductDto);
    }
}
