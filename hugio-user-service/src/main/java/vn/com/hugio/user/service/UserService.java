package vn.com.hugio.user.service;

import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.message.request.CreateUserInfoRequest;
import vn.com.hugio.user.message.request.EditUserInfoRequest;

public interface UserService {
    void createUser(CreateUserInfoRequest request);

    UserInfoDto detail(String uid);

    PageResponse<UserInfoDto> getAllUser(PagableRequest request);

    void updateUser(EditUserInfoRequest request);

    ResponseType<?> deleteUser(String id);

    ResponseType<?> activeUser(String id);
}
