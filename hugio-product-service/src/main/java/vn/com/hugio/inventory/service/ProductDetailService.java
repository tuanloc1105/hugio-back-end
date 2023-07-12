package vn.com.hugio.inventory.service;

import vn.com.hugio.inventory.entity.Product;
import vn.com.hugio.inventory.request.value.ProductDetailReqDto;

import java.util.List;

public interface ProductDetailService {
    void addOrSaveProductDetail(Product product, List<ProductDetailReqDto> reqDto);
}
