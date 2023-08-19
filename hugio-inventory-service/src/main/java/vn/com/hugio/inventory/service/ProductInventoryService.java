package vn.com.hugio.inventory.service;

import vn.com.hugio.inventory.dto.ProductQuantityDto;
import vn.com.hugio.inventory.request.InventoryRequest;
import vn.com.hugio.inventory.request.ReduceProductQuantityRequest;

import java.util.List;

public interface ProductInventoryService {
    void create(InventoryRequest request);

    void importProduct(InventoryRequest request);

    void updateProduct(InventoryRequest request);

    ProductQuantityDto getProductQuantity(InventoryRequest request);

    void reduceProductQuantity(List<ReduceProductQuantityRequest> request);
}
