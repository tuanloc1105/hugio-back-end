package vn.com.hugio.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.product.request.CreateCategoryRequest;
import vn.com.hugio.product.service.CategoryService;

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
    public ResponseType<String> getCategory(@RequestBody RequestType<CreateCategoryRequest> request) {
        this.categoryService.create(request.getRequest());
        return ResponseType.ok("ok");
    }

}
