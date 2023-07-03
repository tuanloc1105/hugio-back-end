package vn.com.hugio.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.message.request.LoginRequest;
import vn.com.hugio.auth.message.response.LoginResponse;
import vn.com.hugio.auth.service.AuthService;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseType<LoginResponse> login(@RequestBody RequestType<LoginRequest> request) {
        return ResponseType.ok(this.authService.login(request.getRequest().getUsername(), request.getRequest().getPassword()));
    }

    @PostMapping("/retrieve-info")
    public ResponseType<AuthenResponse> retrieveInfo(@RequestHeader("Authorization") String token) {
        UserDto dto = this.authService.authorize(token);
        return ResponseType.ok(AuthenResponse.builder()
                .roles(dto.getRoles())
                .username(dto.getUsername())
                .userUid(dto.getUserUid().toString())
                .build());
    }

}
