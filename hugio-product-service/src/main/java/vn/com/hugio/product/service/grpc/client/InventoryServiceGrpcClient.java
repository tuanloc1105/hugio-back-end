package vn.com.hugio.product.service.grpc.client;

import io.grpc.ManagedChannel;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.inventory.InventoryServiceGrpc;
import vn.com.hugio.grpc.inventory.RequestTypeInventoryRequest;
import vn.com.hugio.grpc.inventory.ResponseTypeVoid;
import vn.com.hugio.product.service.grpc.request.InventoryRequest;

@Service
public class InventoryServiceGrpcClient {

    private final ManagedChannel inventoryManagedChannel;

    public InventoryServiceGrpcClient(ManagedChannel inventoryManagedChannel) {
        this.inventoryManagedChannel = inventoryManagedChannel;
    }

    public void create(InventoryRequest request) {
        vn.com.hugio.grpc.inventory.InventoryRequest inventoryRequest = vn.com.hugio.grpc.inventory.InventoryRequest.newBuilder()
                .setProductUid(request.getProductUid())
                .setImportedFrom(request.getImportedFrom())
                .setImportedQuantity(request.getImportedQuantity())
                .setImportedBy(MDC.get("username"))
                .setNote(request.getNote())
                .build();
        RequestTypeInventoryRequest grpcRequest = RequestTypeInventoryRequest.newBuilder().build();
        InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub = InventoryServiceGrpc.newBlockingStub(inventoryManagedChannel);
        ResponseTypeVoid responseTypeVoid = blockingStub.create(grpcRequest);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseTypeVoid.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseTypeVoid.getCode(), responseTypeVoid.getMessage());
        }
    }

    public void importProduct(InventoryRequest request) {
        vn.com.hugio.grpc.inventory.InventoryRequest inventoryRequest = vn.com.hugio.grpc.inventory.InventoryRequest.newBuilder()
                .setProductUid(request.getProductUid())
                .setImportedFrom(request.getImportedFrom())
                .setImportedQuantity(request.getImportedQuantity())
                .setImportedBy(MDC.get("username"))
                .setNote(request.getNote())
                .build();
        RequestTypeInventoryRequest grpcRequest = RequestTypeInventoryRequest.newBuilder().build();
        InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub = InventoryServiceGrpc.newBlockingStub(inventoryManagedChannel);
        ResponseTypeVoid responseTypeVoid = blockingStub.importProduct(grpcRequest);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseTypeVoid.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseTypeVoid.getCode(), responseTypeVoid.getMessage());
        }
    }

    public void updateProduct(InventoryRequest request) {
        vn.com.hugio.grpc.inventory.InventoryRequest inventoryRequest = vn.com.hugio.grpc.inventory.InventoryRequest.newBuilder()
                .setProductUid(request.getProductUid())
                .setImportedFrom(request.getImportedFrom())
                .setImportedQuantity(request.getImportedQuantity())
                .setImportedBy(MDC.get("username"))
                .setNote(request.getNote())
                .build();
        RequestTypeInventoryRequest grpcRequest = RequestTypeInventoryRequest.newBuilder().build();
        InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub = InventoryServiceGrpc.newBlockingStub(inventoryManagedChannel);
        ResponseTypeVoid responseTypeVoid = blockingStub.updateProduct(grpcRequest);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseTypeVoid.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseTypeVoid.getCode(), responseTypeVoid.getMessage());
        }
    }
}
