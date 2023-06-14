package vn.com.hugio.auth.service;

import vn.com.hugio.auth.dto.UserDto;

public interface JwtService {
    String generateJWTToken(UserDto userDto);

    UserDto validateJwtToken(String token);
}
