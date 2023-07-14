package vn.com.hugio.inventory.service;

import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.inventory.dto.UserInfoDto;
import vn.com.hugio.inventory.message.request.CreateUserInfoRequest;
import vn.com.hugio.inventory.message.request.GetAllUserRequest;

public interface UserService {
    void createUser(CreateUserInfoRequest request);

    PageResponse<UserInfoDto> getAllUser(PagableRequest request);

    ResponseType<?> deleteUser(Long id);

    ResponseType<?> activeUser(Long id);
}
