package vn.com.hugio.product.service;

import vn.com.hugio.product.request.CreateCategoryRequest;
import vn.com.hugio.product.request.EditCategoryRequest;

public interface CategoryService {
    void create(CreateCategoryRequest request);

    void edit(EditCategoryRequest request);
}
