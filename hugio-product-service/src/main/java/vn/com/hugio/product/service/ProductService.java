package vn.com.hugio.product.service;

import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.product.dto.ProductDto;
import vn.com.hugio.product.request.CreateProductRequest;
import vn.com.hugio.product.request.EditProductRequest;
import vn.com.hugio.common.pagable.PagableRequest;

public interface ProductService {
    void createProduct(CreateProductRequest request);

    void updateProduct(EditProductRequest request);

    PageResponse<ProductDto> getAllProduct(PagableRequest request);
}
