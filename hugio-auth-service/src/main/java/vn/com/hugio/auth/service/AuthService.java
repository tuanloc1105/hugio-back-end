package vn.com.hugio.auth.service;

import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.message.request.CreateUserRequest;
import vn.com.hugio.auth.message.response.LoginResponse;

public interface AuthService {
    LoginResponse login(String username, String password);

    UserDto authorize(String token);

    UserDto createUser(CreateUserRequest request);

    void deleteUser(String userUid);

    void activeUser(String userUid);
}
