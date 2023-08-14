package vn.com.hugio.product.service;

import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.product.dto.ProductDto;
import vn.com.hugio.product.request.CreateProductRequest;
import vn.com.hugio.product.request.DeleteProductRequest;
import vn.com.hugio.product.request.EditProductRequest;
import vn.com.hugio.product.request.ImportProductQuantityRequest;

public interface ProductService {
    void createProduct(CreateProductRequest request);

    void updateProduct(EditProductRequest request);

    PageResponse<ProductDto> getAllProduct(PagableRequest request);

    ProductDto getProductDetail(String uid);

    void removeProduct(DeleteProductRequest request);

    ResponseType<String> getProductQR(String productUid);

    void importProductQuantity(ImportProductQuantityRequest request);
}
