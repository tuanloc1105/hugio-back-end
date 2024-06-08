package vn.com.hugio.order.service.grpc;

import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.common.TraceTypeGRPC;
import vn.com.hugio.grpc.inventory.InventoryServiceGrpc;
import vn.com.hugio.grpc.inventory.ProductInfo;
import vn.com.hugio.grpc.inventory.ProductInput;
import vn.com.hugio.grpc.inventory.ProductQuantityOutput;
import vn.com.hugio.grpc.inventory.ReduceProductInput;
import vn.com.hugio.grpc.inventory.RequestTypeProductInput;
import vn.com.hugio.grpc.inventory.RequestTypeReduceProductInput;
import vn.com.hugio.grpc.inventory.ResponseTypeProductQuantityOutput;
import vn.com.hugio.grpc.inventory.ResponseTypeVoid;
import vn.com.hugio.order.dto.ProductQuantityDto;
import vn.com.hugio.order.request.value.OrderInformation;
import vn.com.hugio.proto.utils.GrpcUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceGrpcClient {

    private final ManagedChannel inventoryManagedChannel;

    public void reduceProductQuantity(List<OrderInformation> orderInformation) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        LOG.info("SEND A GRPC MESSAGE");
        ReduceProductInput.Builder reduceProductInputBuilder = ReduceProductInput.newBuilder();
        orderInformation.forEach(info -> {
            reduceProductInputBuilder.addProductInfo(
                    ProductInfo.newBuilder()
                            .setProductUid(info.getProductUid())
                            .setQuantity(info.getQuantity())
                            .build()
            );
        });
        RequestTypeReduceProductInput grpcRequest = RequestTypeReduceProductInput.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(reduceProductInputBuilder.build())
                .build();
        InventoryServiceGrpc.InventoryServiceBlockingStub blockingStub = InventoryServiceGrpc.newBlockingStub(this.inventoryManagedChannel);
        ResponseTypeVoid responseTypeVoid = blockingStub.reduceProductQuantity(grpcRequest);
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
