package vn.com.hugio.inventory.service.grpc.client;

import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceGrpcClient {

    private final ManagedChannel inventoryManagedChannel;

    public InventoryServiceGrpcClient(ManagedChannel inventoryManagedChannel) {
        this.inventoryManagedChannel = inventoryManagedChannel;
    }

    public void create() {

    }

    public void importProduct() {

    }

    public void updateProduct() {

    }
}
