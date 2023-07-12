package vn.com.hugio.inventory.service;

import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.inventory.dto.ProductDto;
import vn.com.hugio.inventory.request.CreateProductRequest;
import vn.com.hugio.inventory.request.DeleteProductRequest;
import vn.com.hugio.inventory.request.EditProductRequest;
import vn.com.hugio.common.pagable.PagableRequest;

public interface ProductService {
    void createProduct(CreateProductRequest request);

    void updateProduct(EditProductRequest request);

    PageResponse<ProductDto> getAllProduct(PagableRequest request);

    void removeProduct(DeleteProductRequest request);
}
