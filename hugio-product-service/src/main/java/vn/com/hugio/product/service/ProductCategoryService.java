package vn.com.hugio.product.service;

import vn.com.hugio.product.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    void saveEntities(List<ProductCategory> productCategories);
}
