package vn.com.hugio.inventory.entity.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.inventory.entity.ProductDetail;

import java.util.Optional;

public interface ProductDetailRepository extends BaseRepository<ProductDetail> {

    Optional<ProductDetail> findByProduct_IdAndKey(Long productId, String key);

    Optional<ProductDetail> findByProduct_ProductUidAndKey(String productId, String key);

}
