package vn.com.hugio.inventory.entity.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.inventory.dto.ProductQuantityDto;
import vn.com.hugio.inventory.entity.ProductInventory;

import java.util.Optional;

public interface ProductInventoryRepository extends BaseRepository<ProductInventory> {

    Optional<ProductInventory> findByProductUid(String uid);

    @Query("select new vn.com.hugio.inventory.dto.ProductQuantityDto(il.productUid, pi.quantity, sum(il.importedQuantity), sum(il.importedFee)) from ProductInventory pi left join InventoryLog il on il.productUid = pi.productUid where pi.productUid = :productUid group by il.productUid, pi.quantity")
    ProductQuantityDto findQuantityInfo(
            @Param("productUid") String productUid
    );

}
