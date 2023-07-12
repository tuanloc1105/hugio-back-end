package vn.com.hugio.inventory.service.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.utils.AesUtil;
import vn.com.hugio.grpc.user.CreateUserInput;
import vn.com.hugio.grpc.user.RequestTypeCreateUserInput;
import vn.com.hugio.grpc.user.RequestTypeUpdateUserStatus;
import vn.com.hugio.grpc.user.RequestTypeUserTokenInput;
import vn.com.hugio.grpc.user.ResponseTypeUpdateUserStatus;
import vn.com.hugio.grpc.user.ResponseTypeUserInfo;
import vn.com.hugio.grpc.user.UpdateUserStatus;
import vn.com.hugio.grpc.user.UserInfo;
import vn.com.hugio.grpc.user.UserServiceGrpc;
import vn.com.hugio.grpc.user.UserTokenInput;
import vn.com.hugio.inventory.dto.UserInfoGrpcDto;
import vn.com.hugio.inventory.message.request.CreateUserInfoRequest;
import vn.com.hugio.proto.common.TraceTypeGRPC;
import vn.com.hugio.proto.utils.GrpcUtil;

import java.util.ArrayList;

@Service
public class AuthServiceGrpcClient {

    private final ManagedChannel authManagedChannel;
    @Value("${aes_secret_key}")
    private String aesKey;

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
        //Metadata header = new Metadata();
        //Metadata.Key<String> key =
        //        Metadata.Key.of("Grps-Matches-Key", Metadata.ASCII_STRING_MARSHALLER);
        //header.put(key, "match.items");
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        //blockingStub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(header));
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

    public UserInfoGrpcDto create(CreateUserInfoRequest request) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        String username, password;
        try {
            username = AesUtil.encrypt(request.getUsername(), this.aesKey);
            password = AesUtil.encrypt(request.getPassword(), this.aesKey);
        } catch (Exception e) {
            throw new InternalServiceException(ErrorCodeEnum.CANNOT_ENCRYPT.getCode(), e.getMessage());
        }
        LOG.info("SEND A GRPC MESSAGE");
        CreateUserInput.Builder tokenInput = CreateUserInput
                .newBuilder()
                .setEncryptUsername(username)
                .setEncryptPassword(password);
        if (request.getRoles().isEmpty()) {
            tokenInput.addRoles("CUSTOMER");
        } else {
            for (String role : request.getRoles()) {
                tokenInput.addRoles(role);
            }
        }
        RequestTypeCreateUserInput requestTypeUserTokenInput = RequestTypeCreateUserInput
                .newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(tokenInput.build())
                .build();
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeUserInfo responseType = blockingStub.createUser(requestTypeUserTokenInput);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        UserInfo userInfo = responseType.getResponse();
        return new UserInfoGrpcDto(userInfo.getUserUid());
    }

    public void changeUserStatus(String userUid, boolean status) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();

        RequestTypeUpdateUserStatus requestTypeUpdateUserStatus = RequestTypeUpdateUserStatus
                .newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(
                        UpdateUserStatus
                                .newBuilder()
                                .setUserUid(userUid)
                                .setStatus(status)
                                .build()
                )
                .build();

        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeUpdateUserStatus responseType = blockingStub.changeUserStatus(requestTypeUpdateUserStatus);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
    }
}
