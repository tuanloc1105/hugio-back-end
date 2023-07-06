package vn.com.hugio.product.service;

import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.product.dto.CategoryDto;
import vn.com.hugio.product.entity.Category;
import vn.com.hugio.product.request.CreateCategoryRequest;
import vn.com.hugio.product.request.EditCategoryRequest;

import java.util.List;

public interface CategoryService {
    void create(CreateCategoryRequest request);

    void edit(EditCategoryRequest request);

    List<Category> getCategoryByNameOrUid(List<String> input);

    PageResponse<CategoryDto> getCategory(PagableRequest input);
}
