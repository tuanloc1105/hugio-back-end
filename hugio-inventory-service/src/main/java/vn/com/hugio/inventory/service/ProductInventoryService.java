package vn.com.hugio.inventory.service;

import vn.com.hugio.inventory.request.InventoryRequest;

public interface ProductInventoryService {
    void create(InventoryRequest request);

    void importProduct(InventoryRequest request);

    void updateProduct(InventoryRequest request);
}
