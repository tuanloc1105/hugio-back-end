package vn.com.hugio.inventory.service;

import vn.com.hugio.inventory.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    void saveEntities(List<ProductCategory> productCategories);
}
