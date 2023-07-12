package vn.com.hugio.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.inventory.dto.ProductDto;
import vn.com.hugio.inventory.request.CreateProductRequest;
import vn.com.hugio.inventory.request.DeleteProductRequest;
import vn.com.hugio.inventory.request.EditProductRequest;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.inventory.service.ProductService;

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
    public ResponseType<PageResponse<ProductDto>> getAllProduct(@RequestBody RequestType<PagableRequest> request) {
        return ResponseType.ok(this.productService.getAllProduct(request.getRequest()));
    }

    @PostMapping("/delete")
    public ResponseType<String> deleteProduct(@RequestBody RequestType<DeleteProductRequest> request) {
        this.productService.removeProduct(request.getRequest());
        return ResponseType.ok("ok");
    }

}
