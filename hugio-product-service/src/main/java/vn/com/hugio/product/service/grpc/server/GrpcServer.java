package vn.com.hugio.product.service.grpc.server;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.inventory.ResponseTypeVoid;
import vn.com.hugio.grpc.product.ProductInfoOutput;
import vn.com.hugio.grpc.product.ProductServiceGrpc;
import vn.com.hugio.grpc.product.RequestTypeProductInfoInput;
import vn.com.hugio.grpc.product.ResponseTypeProductInfoOutput;
import vn.com.hugio.product.dto.ProductDto;
import vn.com.hugio.product.service.ProductService;
import vn.com.hugio.proto.utils.GrpcUtil;

@GrpcService
@RequiredArgsConstructor
public class GrpcServer extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProductDetail(RequestTypeProductInfoInput request, StreamObserver<ResponseTypeProductInfoOutput> responseObserver) {
        ResponseTypeProductInfoOutput.Builder responseBuilder = ResponseTypeProductInfoOutput.newBuilder();
        try {
            GrpcUtil.getTraceId(request.getTrace());
            LOG.info("RETRIEVE A GRPC MESSAGE");
        } catch (RuntimeException e) {
            responseBuilder.setCode(ErrorCodeEnum.VALIDATE_FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }
        try {
            ProductDto dto = this.productService.getProductDetail(request.getRequest().getUid());
            ProductInfoOutput output = ProductInfoOutput.newBuilder()
                    .setId(dto.getId())
                    .setProductUid(dto.getProductUid())
                    .setProductName(dto.getProductName())
                    .setRawProductName(dto.getRawProductName())
                    .setPrice(dto.getPrice())
                    .setDiscount(dto.getDiscount())
                    .setProductDescription(dto.getProductDescription())
                    .build();
            responseBuilder.setResponse(output);
            responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
            responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        } catch (InternalServiceException e) {
            responseBuilder.setCode(e.getCode());
            responseBuilder.setMessage(e.getMessage());
        } catch (Exception e) {
            responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
