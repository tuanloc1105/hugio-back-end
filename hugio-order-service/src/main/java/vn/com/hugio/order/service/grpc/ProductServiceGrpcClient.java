package vn.com.hugio.order.service.grpc;

import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.common.TraceTypeGRPC;
import vn.com.hugio.grpc.product.ProductInfoInput;
import vn.com.hugio.grpc.product.ProductInfoOutput;
import vn.com.hugio.grpc.product.ProductServiceGrpc;
import vn.com.hugio.grpc.product.RequestTypeProductInfoInput;
import vn.com.hugio.grpc.product.ResponseTypeProductInfoOutput;
import vn.com.hugio.order.dto.ProductDto;
import vn.com.hugio.proto.utils.GrpcUtil;

@Service
@RequiredArgsConstructor
public class ProductServiceGrpcClient {

    private final ManagedChannel productManagedChannel;

    public ProductDto getDetail(String uid) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        LOG.info("SEND A GRPC MESSAGE");
        ProductInfoInput infoInput = ProductInfoInput.newBuilder()
                .setUid(uid)
                .build();
        RequestTypeProductInfoInput grpcRequest = RequestTypeProductInfoInput.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(infoInput)
                .build();
        ProductServiceGrpc.ProductServiceBlockingStub blockingStub = ProductServiceGrpc.newBlockingStub(this.productManagedChannel);
        ResponseTypeProductInfoOutput responseType = blockingStub.getProductDetail(grpcRequest);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        ProductInfoOutput output = responseType.getResponse();
        return ProductDto.builder()
                .id(output.getId())
                .productUid(output.getProductUid())
                .productName(output.getProductName())
                .rawProductName(output.getRawProductName())
                .price(output.getPrice())
                .discount(output.getDiscount())
                .productDescription(output.getProductDescription())
                .build();
    }
}
