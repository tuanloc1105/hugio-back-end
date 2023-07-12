package vn.com.hugio.inventory.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.inventory.entiry.ProductInventory;
import vn.com.hugio.inventory.entiry.repository.ProductInventoryRepository;
import vn.com.hugio.inventory.service.ProductInventoryService;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class ProductInventoryServiceImpl extends BaseService<ProductInventory, ProductInventoryRepository> implements ProductInventoryService {

    public ProductInventoryServiceImpl(ProductInventoryRepository repository) {
        super(repository);
    }

    public void create(String productUid, Long quantity) {
        ProductInventory productInventory = this.save(
                ProductInventory.builder()
                        .productUid(productUid)
                        .quantity(quantity)
                        .build()
        );

    }
}
