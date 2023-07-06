package vn.com.hugio.product.service;

import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.product.dto.UserInfoDto;
import vn.com.hugio.product.message.request.CreateUserInfoRequest;
import vn.com.hugio.product.message.request.GetAllUserRequest;

public interface UserService {
    void createUser(CreateUserInfoRequest request);

    PageResponse<UserInfoDto> getAllUser(GetAllUserRequest request);

    ResponseType<?> deleteUser(Long id);

    ResponseType<?> activeUser(Long id);
}
