package vn.com.hugio.inventory.entiry.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.inventory.entiry.ProductInventory;

import java.util.Optional;

public interface ProductInventoryRepository extends BaseRepository<ProductInventory> {

    Optional<ProductInventory> findByProductUid(String uid);

}
