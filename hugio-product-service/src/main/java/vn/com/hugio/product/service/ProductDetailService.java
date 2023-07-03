package vn.com.hugio.product.service;

import vn.com.hugio.product.entity.Product;
import vn.com.hugio.product.request.value.ProductDetailReqDto;

import java.util.List;

public interface ProductDetailService {
    void addOrSaveProductDetail(Product product, List<ProductDetailReqDto> reqDto);
}
