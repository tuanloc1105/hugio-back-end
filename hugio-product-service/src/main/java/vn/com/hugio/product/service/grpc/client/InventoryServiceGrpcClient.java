package vn.com.hugio.product.service.grpc.client;

import io.grpc.ManagedChannel;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.service.CurrentUserService;
import vn.com.hugio.grpc.inventory.InventoryServiceGrpc;
import vn.com.hugio.grpc.inventory.ProductInput;
import vn.com.hugio.grpc.inventory.ProductQuantityOutput;
import vn.com.hugio.grpc.inventory.RequestTypeInventoryRequest;
import vn.com.hugio.grpc.inventory.RequestTypeProductInput;
import vn.com.hugio.grpc.inventory.ResponseTypeProductQuantityOutput;
import vn.com.hugio.grpc.inventory.ResponseTypeVoid;
import vn.com.hugio.product.dto.ProductQuantityDto;
import vn.com.hugio.product.service.grpc.request.InventoryRequest;
import vn.com.hugio.proto.common.TraceTypeGRPC;
import vn.com.hugio.proto.utils.GrpcUtil;

@Service
public class InventoryServiceGrpcClient {

    private final ManagedChannel inventoryManagedChannel;
    private final CurrentUserService currentUserService;

    public InventoryServiceGrpcClient(ManagedChannel inventoryManagedChannel,
                                      CurrentUserService currentUserService) {
        this.inventoryManagedChannel = inventoryManagedChannel;
        this.currentUserService = currentUserService;
    }

    public void create(InventoryRequest request) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        vn.com.hugio.grpc.inventory.InventoryRequest inventoryRequest = vn.com.hugio.grpc.inventory.InventoryRequest.newBuilder()
                .setProductUid(request.getProductUid())
                .setImportedFrom(request.getImportedFrom())
                .setImportedQuantity(request.getImportedQuantity())
                .setImportedBy(this.currentUserService.getUsername())
                .setNote(request.getNote())
                .build();
        RequestTypeInventoryRequest grpcRequest = RequestTypeInventoryRequest.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(inventoryRequest)
                .build();
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
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        vn.com.hugio.grpc.inventory.InventoryRequest inventoryRequest = vn.com.hugio.grpc.inventory.InventoryRequest.newBuilder()
                .setProductUid(request.getProductUid())
                .setImportedFrom(request.getImportedFrom())
                .setImportedQuantity(request.getImportedQuantity())
                .setImportedBy(this.currentUserService.getUsername())
                .setNote(request.getNote())
                .build();
        RequestTypeInventoryRequest grpcRequest = RequestTypeInventoryRequest.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(inventoryRequest)
                .build();
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
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        vn.com.hugio.grpc.inventory.InventoryRequest inventoryRequest = vn.com.hugio.grpc.inventory.InventoryRequest.newBuilder()
                .setProductUid(request.getProductUid())
                .setImportedFrom(request.getImportedFrom())
                .setImportedQuantity(request.getImportedQuantity())
                .setImportedBy(this.currentUserService.getUsername())
                .setNote(request.getNote())
                .build();
        RequestTypeInventoryRequest grpcRequest = RequestTypeInventoryRequest.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(inventoryRequest)
                .build();
        InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub = InventoryServiceGrpc.newBlockingStub(inventoryManagedChannel);
        ResponseTypeVoid responseTypeVoid = blockingStub.updateProduct(grpcRequest);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseTypeVoid.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseTypeVoid.getCode(), responseTypeVoid.getMessage());
        }
    }

    public ProductQuantityDto getProductQuantity(String productUid) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        ProductInput productInput = ProductInput.newBuilder()
                .setProductUid(productUid)
                .build();
        RequestTypeProductInput grpcRequest = RequestTypeProductInput.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(productInput)
                .build();
        InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub = InventoryServiceGrpc.newBlockingStub(inventoryManagedChannel);
        ResponseTypeProductQuantityOutput responseType = blockingStub.getProductQuantity(grpcRequest);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        ProductQuantityOutput output = responseType.getResponse();
        return ProductQuantityDto.builder()
                .productUid(output.getProductUid())
                .quantity(output.getQuantity())
                .importedQuantity(output.getImportedQuantity())
                .fee(output.getFee())
                .build();
    }
}
