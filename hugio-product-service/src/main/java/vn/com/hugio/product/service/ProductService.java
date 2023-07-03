package vn.com.hugio.product.service;

import vn.com.hugio.product.request.CreateProductRequest;
import vn.com.hugio.product.request.EditProductRequest;

public interface ProductService {
    void createProduct(CreateProductRequest request);

    void updateProduct(EditProductRequest request);
}
