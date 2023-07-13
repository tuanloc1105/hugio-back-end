package vn.com.hugio.inventory.entity.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.inventory.entity.ProductInventory;

import java.util.Optional;

public interface ProductInventoryRepository extends BaseRepository<ProductInventory> {

    Optional<ProductInventory> findByProductUid(String uid);

}
