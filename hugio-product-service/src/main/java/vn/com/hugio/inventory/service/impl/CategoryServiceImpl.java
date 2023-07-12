package vn.com.hugio.inventory.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.inventory.dto.CategoryDto;
import vn.com.hugio.inventory.entity.Category;
import vn.com.hugio.inventory.entity.repository.CategoryRepository;
import vn.com.hugio.inventory.mapper.CategoryMapper;
import vn.com.hugio.inventory.request.CreateCategoryRequest;
import vn.com.hugio.inventory.request.EditCategoryRequest;
import vn.com.hugio.inventory.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(rollbackFor = {InternalServiceException.class, RuntimeException.class, Exception.class, Throwable.class})
public class CategoryServiceImpl extends BaseService<Category, CategoryRepository> implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository repository,
                               CategoryMapper categoryMapper) {
        super(repository);
        this.categoryMapper = categoryMapper;
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

    @Override
    public List<Category> getCategoryByNameOrUid(List<String> input) {
        return this.repository.findByCategoryNameInOrCategoryUidIn(input, input);
    }

    @Override
    public PageResponse<CategoryDto> getCategory(PagableRequest input) {
        Page<Category> categoryPage = this.repository.findByActiveIsTrue(PageLink.create(input).toPageable());
        return PageResponse.create(categoryPage, categoryMapper::categoryEntityToDto, true);
    }
}
