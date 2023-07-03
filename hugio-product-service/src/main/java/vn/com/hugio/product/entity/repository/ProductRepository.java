package vn.com.hugio.product.entity.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.Product;

import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findByProductName(String productName);

}
