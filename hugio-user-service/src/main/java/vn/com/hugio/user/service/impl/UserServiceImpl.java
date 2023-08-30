package vn.com.hugio.user.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.common.service.CurrentUserService;
import vn.com.hugio.common.utils.DateTimeUtil;
import vn.com.hugio.common.utils.ExceptionStackTraceUtil;
import vn.com.hugio.common.utils.StringUtil;
import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.dto.UserInfoGrpcDto;
import vn.com.hugio.user.entity.UserInfo;
import vn.com.hugio.user.entity.repository.UserInfoRepo;
import vn.com.hugio.user.mapper.UserInfoMapper;
import vn.com.hugio.user.mapper.UserMapper;
import vn.com.hugio.user.message.request.CreateUserInfoRequest;
import vn.com.hugio.user.message.request.EditUserInfoRequest;
import vn.com.hugio.user.service.UserService;
import vn.com.hugio.user.service.grpc.client.AuthServiceGrpcClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class UserServiceImpl extends BaseService<UserInfo, UserInfoRepo> implements UserService {

    private final AuthServiceGrpcClient authServiceGrpcClient;
    private final UserInfoMapper userInfoMapper;
    private final CurrentUserService currentUserService;

    @Autowired
    public UserServiceImpl(UserInfoRepo repository,
                           AuthServiceGrpcClient authServiceGrpcClient,
                           UserInfoMapper userInfoMapper,
                           CurrentUserService currentUserService) {
        super(repository);
        this.authServiceGrpcClient = authServiceGrpcClient;
        this.userInfoMapper = userInfoMapper;
        this.currentUserService = currentUserService;
    }

    @Override
    public void createUser(CreateUserInfoRequest request) {
        LocalDate currentDate = DateTimeUtil.generateCurrentLocalDateDefault();
        UserInfoGrpcDto grpcDto = this.authServiceGrpcClient.create(request);
        var userInfo = new UserInfo();
        userInfo.setEmail(request.getEmail());
        userInfo.setAddress(request.getAddress());
        userInfo.setFullName(request.getFullName());
        userInfo.setPhoneNumber(request.getPhoneNumber());
        Integer numberUserCreatedInDay = this.repository.countByCreatedAtBetween(
                currentDate.atTime(LocalTime.MIN),
                currentDate.atTime(LocalTime.MAX)
        );
        if (numberUserCreatedInDay > 999) {
            throw new InternalServiceException(ErrorCodeEnum.FAILURE.getCode(), "Max user create in a day");
        }
        //Long cif = (Long.parseLong(currentDate.format(DateTimeFormatter.ofPattern("yyMMdd"))) * 1000) + numberUserCreatedInDay;
        //userInfo.setCif(cif);
        userInfo.setCif(StringUtil.addZeroLeadingNumber(numberUserCreatedInDay == 0 ? numberUserCreatedInDay + 1 : numberUserCreatedInDay, "C"));
        userInfo.setUserUid(grpcDto.getUserUid());
        this.repository.save(userInfo);
    }

    @Override
    public UserInfoDto detail(String uid) {
        UserInfo userInfo = this.repository.findByUserUidAndActiveIsTrue(uid).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));
        List<String> roles = this.authServiceGrpcClient.getUserRole(uid);
        UserInfoDto dto = this.userInfoMapper.userInfoDtoMapper(userInfo);
        dto.setRoles(roles);
        return dto;
    }

    @Override
    public PageResponse<UserInfoDto> getAllUser(PagableRequest request) {
        PageLink pageLink = new PageLink(request);
        Page<UserInfo> page = StringUtils.isNotBlank(request.getContent()) ?
                this.repository.findByContent(request.getContent(), pageLink.toPageable()) : this.repository.findAll(pageLink.toPageable());
        List<UserInfoDto> dto = page.getContent()
                .stream()
                .map(UserMapper::map)
                .toList()
                .stream()
                .peek(user -> {
                    try {
                        UserInfoDto dto1 = authServiceGrpcClient.getUserInfo(user.getUserUid());
                        user.setRoles(dto1.getRoles());
                        user.setUsername(dto1.getUsername());
                    } catch (InternalServiceException e) {
                        LOG.warn(ExceptionStackTraceUtil.getStackTrace(e));
                    }
                })
                .toList();
        return PageResponse.create(page, dto, true);
    }

    @Override
    public void updateUser(EditUserInfoRequest request) {
        Integer result = this.repository.updateUserInfo(
                request.getEmail(),
                request.getAddress(),
                request.getFullName(),
                request.getPhoneNumber(),
                LocalDateTime.now(),
                currentUserService.getUsername(),
                request.getUserUid()
        );
        if (result == null || result == 0) {
            throw new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "user not exist");
        }
    }

    @Override
    public ResponseType<?> deleteUser(String id) {
        this.changeUserStatus(id, false);
        return ResponseType.ok(null);
    }

    @Override
    public ResponseType<?> activeUser(String id) {
        this.changeUserStatus(id, true);
        return ResponseType.ok(null);
    }

    private void changeUserStatus(String id, boolean status) {
        UserInfo userInfo;
        if (!status) {
            userInfo = this.repository.findByUserUidAndActiveIsTrue(id).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "User not exist"));
        } else {
            userInfo = this.repository.findByUserUid(id).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "User not exist"));
        }
        userInfo.setActive(status);
        this.authServiceGrpcClient.changeUserStatus(userInfo.getUserUid(), status);
        this.repository.save(userInfo);
    }
}
