package vn.com.hugio.product.entity.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.product.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory> {

    @Modifying
    @Query("delete from ProductCategory pc where pc.product.id = :id")
    void deleteByProduct_Id(@Param("id") Long id);

    List<ProductCategory> findByProduct_Id(Long id);
}
