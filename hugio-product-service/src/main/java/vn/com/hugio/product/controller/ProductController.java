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
import vn.com.hugio.product.request.EditProductRequest;
import vn.com.hugio.product.request.GetProductRequest;
import vn.com.hugio.product.service.ProductService;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseType<String> createProduct(@RequestBody RequestType<CreateProductRequest> request) {
        this.productService.createProduct(request.getRequest());
        return ResponseType.ok(null);
    }

    @PostMapping("/edit")
    public ResponseType<String> editProduct(@RequestBody RequestType<EditProductRequest> request) {
        this.productService.updateProduct(request.getRequest());
        return ResponseType.ok(null);
    }

    @PostMapping("/all")
    public ResponseType<String> getAllProduct(@RequestBody RequestType<GetProductRequest> request) {
        return ResponseType.ok(null);
    }

}
