package vn.com.hugio.product.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.Product;

import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findByProductName(String productName);

    Optional<Product> findByProductUid(String productUid);

    Page<Product> findByActiveIsTrue(Pageable pageable);

    @Modifying
    void deleteByProductUid(String uid);

    @Query
    @Modifying
    void softDeleteByProductUid(String uid);

}
