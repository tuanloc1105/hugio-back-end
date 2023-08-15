package vn.com.hugio.order.service.grpc;

import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.product.ProductInfoInput;
import vn.com.hugio.grpc.product.ProductInfoOutput;
import vn.com.hugio.grpc.product.ProductServiceGrpc;
import vn.com.hugio.grpc.product.RequestTypeProductInfoInput;
import vn.com.hugio.grpc.product.ResponseTypeProductInfoOutput;
import vn.com.hugio.order.dto.ProductDto;
import vn.com.hugio.proto.common.TraceTypeGRPC;
import vn.com.hugio.proto.utils.GrpcUtil;

@Service
@RequiredArgsConstructor
public class InventoryServiceGrpcClient {

    private final ManagedChannel inventoryManagedChannel;

    public void reduceProductQuantity() {

    }
}
