package vn.com.hugio.product.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.Product;

import java.util.Optional;

public interface ProductRepository extends BaseRepository<Product> {

    Optional<Product> findByProductName(String productName);

    Optional<Product> findByProductUid(String productUid);

    Page<Product> findByActiveIsTrue(Pageable pageable);

    @Modifying
    void deleteByProductUid(String uid);

    @Query(value = "update Product p set p.active = false where p.productUid = :productUid")
    @Modifying
    void softDeleteByProductUid(@Param("productUid") String productUid);

}
