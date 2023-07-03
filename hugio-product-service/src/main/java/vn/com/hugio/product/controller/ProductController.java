package vn.com.hugio.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.product.request.CreateProductRequest;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    @PostMapping("/create")
    public ResponseType<String> createProduct(@RequestBody RequestType<CreateProductRequest> request) {
        return ResponseType.ok(null);
    }

    @PostMapping("/edit")
    public ResponseType<String> editProduct(@RequestBody RequestType<CreateProductRequest> request) {
        return ResponseType.ok(null);
    }

}
