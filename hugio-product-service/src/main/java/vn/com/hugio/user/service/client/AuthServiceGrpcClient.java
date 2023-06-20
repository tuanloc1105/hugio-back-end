package vn.com.hugio.user.service.client;

import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.grpc.user.RequestTypeUserTokenInput;
import vn.com.hugio.grpc.user.ResponseTypeUserInfo;
import vn.com.hugio.grpc.user.UserInfo;
import vn.com.hugio.grpc.user.UserServiceGrpc;
import vn.com.hugio.grpc.user.UserTokenInput;
import vn.com.hugio.proto.common.TraceTypeGRPC;
import vn.com.hugio.proto.utils.GrpcUtil;

import java.util.ArrayList;

@Service
public class AuthServiceGrpcClient {

    @Value("${aes_secret_key}")
    private String aesKey;

    private final ManagedChannel authManagedChannel;

    @Autowired
    public AuthServiceGrpcClient(ManagedChannel authManagedChannel) {
        this.authManagedChannel = authManagedChannel;
    }

    public AuthenResponse auth(String token) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        LOG.info("SEND A GRPC MESSAGE");
        UserTokenInput tokenInput = UserTokenInput
                .newBuilder()
                .setToken(token)
                .build();
        RequestTypeUserTokenInput requestTypeUserTokenInput = RequestTypeUserTokenInput
                .newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(tokenInput)
                .build();
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeUserInfo responseType = blockingStub.findUserByToken(requestTypeUserTokenInput);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        UserInfo userInfo = responseType.getResponse();
        return AuthenResponse
                .builder()
                .userUid(userInfo.getUserUid())
                .username(userInfo.getUsername())
                .roles(new ArrayList<>(userInfo.getRoleList()))
                .build();
    }

}
