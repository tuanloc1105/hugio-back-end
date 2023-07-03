package vn.com.hugio.product.service.impl;

import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.product.entity.Category;
import vn.com.hugio.product.entity.repository.CategoryRepository;
import vn.com.hugio.product.request.CreateCategoryRequest;
import vn.com.hugio.product.request.EditCategoryRequest;
import vn.com.hugio.product.service.CategoryService;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl extends BaseService<Category, CategoryRepository> implements CategoryService {

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

    @Override
    public void create(CreateCategoryRequest request) {
        Optional<Category> optionalCategory = this.repository.findByCategoryName(request.getCategoryName());
        if (optionalCategory.isPresent()) {
            throw new InternalServiceException(ErrorCodeEnum.EXISTS.getCode(), "category already exists");
        }
        Category category = Category.builder()
                .categoryUid(UUID.randomUUID().toString())
                .categoryName(request.getCategoryName())
                .build();
        this.save(category);
    }

    @Override
    public void edit(EditCategoryRequest request) {
        Category category = this.repository.findByCategoryUid(request.getCategoryId())
                .orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "category not exist"));
        category.setCategoryName(request.getCategoryName());
        this.save(category);
    }
}
