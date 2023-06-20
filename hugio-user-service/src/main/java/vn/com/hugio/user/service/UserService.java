package vn.com.hugio.user.service;

import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.message.request.CreateUserInfoRequest;
import vn.com.hugio.user.message.request.GetAllUserRequest;
import vn.com.hugio.common.object.PageResponse;
import vn.com.hugio.common.object.ResponseType;

public interface UserService {
    void createUser(CreateUserInfoRequest request);

    PageResponse<UserInfoDto> getAllUser(GetAllUserRequest request);

    ResponseType<?> deleteUser(Long id);

    ResponseType<?> activeUser(Long id);
}
