package vn.com.hugio.product.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.Product;

import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findByProductName(String productName);

    Page<Product> findByActiveIsTrue(Pageable pageable);

}
