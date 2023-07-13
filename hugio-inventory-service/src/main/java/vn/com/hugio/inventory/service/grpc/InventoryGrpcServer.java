package vn.com.hugio.inventory.service.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.inventory.InventoryServiceGrpc;
import vn.com.hugio.grpc.inventory.RequestTypeInventoryRequest;
import vn.com.hugio.grpc.inventory.ResponseTypeVoid;
import vn.com.hugio.inventory.request.InventoryRequest;
import vn.com.hugio.inventory.service.ProductInventoryService;
import vn.com.hugio.proto.utils.GrpcUtil;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcServer extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final ProductInventoryService productInventoryService;

    @Override
    public void create(RequestTypeInventoryRequest request, StreamObserver<ResponseTypeVoid> responseObserver) {
        ResponseTypeVoid.Builder responseBuilder = ResponseTypeVoid.newBuilder();
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
            InventoryRequest request1 = InventoryRequest.builder()
                    .productUid(request.getRequest().getProductUid())
                    .importedFrom(request.getRequest().getImportedFrom())
                    .importedQuantity(request.getRequest().getImportedQuantity())
                    .importedBy(request.getRequest().getImportedBy())
                    .note(request.getRequest().getNote())
                    .build();
            this.productInventoryService.create(request1);
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
