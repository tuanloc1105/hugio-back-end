package vn.com.hugio.product.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.product.entity.Product;
import vn.com.hugio.product.entity.ProductDetail;
import vn.com.hugio.product.entity.repository.ProductDetailRepository;
import vn.com.hugio.product.request.value.ProductDetailReqDto;
import vn.com.hugio.product.service.ProductDetailService;

import java.util.List;

@Service
@Transactional(rollbackFor = {InternalServiceException.class, RuntimeException.class, Exception.class, Throwable.class})
public class ProductDetailServiceImpl extends BaseService<ProductDetail, ProductDetailRepository> implements ProductDetailService {
    public ProductDetailServiceImpl(ProductDetailRepository repository) {
        super(repository);
    }

    @Override
    public void addOrSaveProductDetail(Product product, List<ProductDetailReqDto> reqDto) {
        if (reqDto == null || reqDto.isEmpty()) {
            return;
        }
        reqDto.forEach(dto -> {
            ProductDetail productDetail = repository.findByProduct_IdAndKey(product.getId(), dto.getKey())
                    .orElse(
                            ProductDetail.builder()
                                    .product(product)
                                    .key(dto.getKey())
                                    .value(dto.getValue())
                                    .build()
                    );
            repository.save(productDetail);
        });
    }
}
