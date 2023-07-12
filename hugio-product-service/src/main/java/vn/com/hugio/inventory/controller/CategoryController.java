package vn.com.hugio.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.inventory.dto.CategoryDto;
import vn.com.hugio.inventory.request.CreateCategoryRequest;
import vn.com.hugio.inventory.service.CategoryService;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseType<String> createCategory(@RequestBody RequestType<CreateCategoryRequest> request) {
        this.categoryService.create(request.getRequest());
        return ResponseType.ok("ok");
    }

    @PostMapping("/all")
    public ResponseType<PageResponse<CategoryDto>> getCategory(@RequestBody RequestType<PagableRequest> request) {
        return ResponseType.ok(this.categoryService.getCategory(request.getRequest()));
    }

}
