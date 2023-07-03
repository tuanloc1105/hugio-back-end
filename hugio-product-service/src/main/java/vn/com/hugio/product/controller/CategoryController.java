package vn.com.hugio.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.product.request.CreateCategoryRequest;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    public void createCategory(@RequestBody RequestType<CreateCategoryRequest> request) {

    }

}
