package vn.com.hugio.auth.service.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.entity.Role;
import vn.com.hugio.auth.mapper.UserMapper;
import vn.com.hugio.auth.message.request.CreateUserRequest;
import vn.com.hugio.auth.message.request.UpdateUserRequest;
import vn.com.hugio.auth.service.AuthService;
import vn.com.hugio.auth.service.RoleService;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.pagable.Direction;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.utils.StringUtil;
import vn.com.hugio.grpc.user.*;
import vn.com.hugio.proto.utils.GrpcUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServer extends UserServiceGrpc.UserServiceImplBase {

    private final AuthService authService;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final RoleService roleService;

    @Override
    public void findUserByToken(RequestTypeUserTokenInput request, StreamObserver<ResponseTypeUserInfo> responseObserver) {
        ResponseTypeUserInfo.Builder responseBuilder = ResponseTypeUserInfo.newBuilder();
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
        if (StringUtil.isEmpty(request.getRequest().getToken())) {
            responseBuilder.setCode(ErrorCodeEnum.VALIDATE_FAILURE.getCode().toString());
            responseBuilder.setMessage("Token is null");
        } else {
            String token = request.getRequest().getToken();
            try {
                UserDto userInfo = this.authService.authorize(token);
                responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
                responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
                UserInfo.Builder info = UserInfo
                        .newBuilder()
                        .setUserUid(userInfo.getUserUid().toString())
                        .setUsername(userInfo.getUsername());
                userInfo.getRoles().forEach(info::addRole);
                responseBuilder.setResponse(info.build());
            } catch (InternalServiceException e) {
                LOG.exception(e);
                responseBuilder.setCode(e.getCode());
                responseBuilder.setMessage(e.getMessage());
            } catch (Exception e) {
                LOG.exception(e);
                responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
                responseBuilder.setMessage(e.getMessage());
            }
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(RequestTypeCreateUserInput request, StreamObserver<ResponseTypeUserInfo> responseObserver) {
        ResponseTypeUserInfo.Builder responseBuilder = ResponseTypeUserInfo.newBuilder();
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
            CreateUserInput input = request.getRequest();
            CreateUserRequest createUserRequest = this.userMapper.createUserRequestMapper(input);
            UserDto userInfo = this.authService.createUser(createUserRequest);
            responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
            responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
            UserInfo.Builder info = UserInfo
                    .newBuilder()
                    .setUserUid(userInfo.getUserUid().toString())
                    .setUsername(userInfo.getUsername());
            userInfo.getRoles().forEach(info::addRole);
            responseBuilder.setResponse(info.build());
        } catch (InternalServiceException e) {
            LOG.exception(e);
            responseBuilder.setCode(e.getCode());
            responseBuilder.setMessage(e.getMessage());
        } catch (Exception e) {
            LOG.exception(e);
            responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(RequestTypeUpdateUserInput request, StreamObserver<ResponseTypeVoid> responseObserver) {
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
            UpdateUserInput input = request.getRequest();
            UpdateUserRequest updateUserRequest = new UpdateUserRequest();
            updateUserRequest.setUserUid(input.getUserId());
            updateUserRequest.setRoles(new ArrayList<>(input.getRolesList()));
            this.authService.updateUser(updateUserRequest);
            responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
            responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        } catch (InternalServiceException e) {
            LOG.exception(e);
            responseBuilder.setCode(e.getCode());
            responseBuilder.setMessage(e.getMessage());
        } catch (Exception e) {
            LOG.exception(e);
            responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void changeUserStatus(RequestTypeUpdateUserStatus request, StreamObserver<ResponseTypeUpdateUserStatus> responseObserver) {
        ResponseTypeUpdateUserStatus.Builder responseBuilder = ResponseTypeUpdateUserStatus.newBuilder();
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
            UpdateUserStatus input = request.getRequest();
            if (input.getStatus()) {
                this.authService.activeUser(input.getUserUid());
            } else {
                this.authService.deleteUser(input.getUserUid());
            }
            responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
            responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        } catch (InternalServiceException e) {
            LOG.exception(e);
            responseBuilder.setCode(e.getCode());
            responseBuilder.setMessage(e.getMessage());
        } catch (Exception e) {
            LOG.exception(e);
            responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getRoles(RequestTypePageableInput request, StreamObserver<ResponseTypeRoleOutput> responseObserver) {
        ResponseTypeRoleOutput.Builder responseBuilder = ResponseTypeRoleOutput.newBuilder();
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
            PageableInput input = request.getRequest();
            PagableRequest request1 = PagableRequest.builder()
                    .pageNumber(input.getPageNumber())
                    .pageSize(input.getPageSize())
                    .property(input.getProperty())
                    .sort(Direction.valueOf(input.getDirection()))
                    .build();
            Page<Role> page = this.roleService.all(request1);
            RoleOutput.Builder builder = RoleOutput.newBuilder();
            page.getContent().forEach(role -> builder.addRoleName(role.getRoleName()));
            responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
            responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
            responseBuilder.setResponse(builder);
        } catch (InternalServiceException e) {
            LOG.exception(e);
            responseBuilder.setCode(e.getCode());
            responseBuilder.setMessage(e.getMessage());
        } catch (Exception e) {
            LOG.exception(e);
            responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserRole(RequestTypeUserInfoInput request, StreamObserver<ResponseTypeUserInfo> responseObserver) {
        ResponseTypeUserInfo.Builder responseBuilder = ResponseTypeUserInfo.newBuilder();
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
            UserDto userInfo = this.authService.getInfo(request.getRequest().getUserUid());
            responseBuilder.setCode(ErrorCodeEnum.SUCCESS.getCode().toString());
            responseBuilder.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
            UserInfo.Builder info = UserInfo
                    .newBuilder()
                    .setUserUid(userInfo.getUserUid().toString())
                    .setUsername(userInfo.getUsername());
            userInfo.getRoles().forEach(info::addRole);
            responseBuilder.setResponse(info.build());
        } catch (InternalServiceException e) {
            LOG.exception(e);
            responseBuilder.setCode(e.getCode());
            responseBuilder.setMessage(e.getMessage());
        } catch (Exception e) {
            LOG.exception(e);
            responseBuilder.setCode(ErrorCodeEnum.FAILURE.getCode().toString());
            responseBuilder.setMessage(e.getMessage());
        }
        responseBuilder.setTrace(GrpcUtil.createTraceTypeGrpc());
        LOG.info("RETURN GRPC RESULT");
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
