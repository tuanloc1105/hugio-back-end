package vn.com.hugio.user.service.grpc.client;

import io.grpc.ManagedChannel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.utils.AesUtil;
import vn.com.hugio.grpc.user.*;
import vn.com.hugio.proto.common.TraceTypeGRPC;
import vn.com.hugio.proto.utils.GrpcUtil;
import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.dto.UserInfoGrpcDto;
import vn.com.hugio.user.message.request.CreateUserInfoRequest;
import vn.com.hugio.user.service.grpc.input.UpdateUserRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceGrpcClient {

    private final ManagedChannel authManagedChannel;
    @Value("${aes_secret_key}")
    private String aesKey;
    @Value("${default_password}")
    private String defaultPassword;

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
            password = AesUtil.encrypt(request.getPassword() == null ? defaultPassword : request.getPassword(), this.aesKey);
        } catch (Exception e) {
            throw new InternalServiceException(ErrorCodeEnum.CANNOT_ENCRYPT.getCode(), e.getMessage());
        }
        LOG.info("SEND A GRPC MESSAGE");
        CreateUserInput.Builder tokenInput = CreateUserInput
                .newBuilder()
                .setEncryptUsername(username)
                .setEncryptPassword(password);
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
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

    public void update(UpdateUserRequest request) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();
        LOG.info("SEND A GRPC MESSAGE");
        UpdateUserInput.Builder grpcInput = UpdateUserInput
                .newBuilder()
                .setUserId(request.getUserUid());
        if (request.getRoles().isEmpty()) {
            grpcInput.addRoles("CUSTOMER");
        } else {
            for (String role : request.getRoles()) {
                grpcInput.addRoles(role);
            }
        }
        if (request.getRoles().isEmpty()) {
            grpcInput.addRoles("CUSTOMER");
        } else {
            for (String role : request.getRoles()) {
                grpcInput.addRoles(role);
            }
        }
        RequestTypeUpdateUserInput build = RequestTypeUpdateUserInput
                .newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(grpcInput.build())
                .build();
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeVoid responseType = blockingStub.updateUser(build);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
    }

    public List<String> getRoles(PagableRequest request) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();

        RequestTypePageableInput requestType = RequestTypePageableInput.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(
                        PageableInput.newBuilder()
                                .setPageNumber(request.getPageNumber() != null ? request.getPageNumber() : 0)
                                .setPageSize(request.getPageSize() != null ? request.getPageSize() : 100)
                                .setProperty(request.getProperty() != null ? request.getProperty() : Strings.EMPTY)
                                .setDirection(request.getSort() != null ? request.getSort().name() : Strings.EMPTY)
                                .build()
                )
                .build();
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeRoleOutput responseType = blockingStub.getRoles(requestType);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        return new ArrayList<>(responseType.getResponse().getRoleNameList());
    }

    public List<String> getUserRole(String userUid) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();

        RequestTypeUserInfoInput requestType = RequestTypeUserInfoInput.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(
                        UserInfoInput.newBuilder()
                                .setUserUid(userUid)
                                .build()
                )
                .build();
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeUserInfo responseType = blockingStub.getUserRole(requestType);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        return new ArrayList<>(responseType.getResponse().getRoleList());
    }

    public UserInfoDto getUserInfo(String userUid) {
        TraceTypeGRPC traceTypeGRPC = GrpcUtil.createTraceTypeGrpc();

        RequestTypeUserInfoInput requestType = RequestTypeUserInfoInput.newBuilder()
                .setTrace(traceTypeGRPC)
                .setRequest(
                        UserInfoInput.newBuilder()
                                .setUserUid(userUid)
                                .build()
                )
                .build();
        UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(authManagedChannel);
        ResponseTypeUserInfo responseType = blockingStub.getUserRole(requestType);
        LOG.info("RETRIEVE A GRPC MESSAGE");
        if (
                !(responseType.getCode().equals(ErrorCodeEnum.SUCCESS.getCode().toString()))
        ) {
            throw new InternalServiceException(responseType.getCode(), responseType.getMessage());
        }
        return UserInfoDto.builder()
                .userUid(responseType.getResponse().getUserUid())
                .username(responseType.getResponse().getUsername())
                .roles(responseType.getResponse().getRoleList())
                .build();
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
