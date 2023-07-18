package vn.com.hugio.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.product.entity.ProductCategory;
import vn.com.hugio.product.entity.repository.ProductCategoryRepository;
import vn.com.hugio.product.service.ProductCategoryService;

import java.util.List;

@Service
@Transactional(rollbackFor = {InternalServiceException.class, RuntimeException.class, Exception.class, Throwable.class})
public class ProductCategoryServiceImpl extends BaseService<ProductCategory, ProductCategoryRepository> implements ProductCategoryService {

    public ProductCategoryServiceImpl(ProductCategoryRepository repository) {
        super(repository);
    }

    @Override
    public void saveEntities(List<ProductCategory> productCategories) {
        this.saveAll(productCategories);
    }

    @Override
    public void deleteEntities(Long productId) {
        this.repository.deleteByProduct_Id(productId);
        }

}
