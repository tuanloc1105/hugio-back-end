package vn.com.hugio.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.message.request.LoginRequest;
import vn.com.hugio.auth.message.response.LoginResponse;
import vn.com.hugio.auth.service.AuthService;
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
    public ResponseType<UserDto> retrieveInfo(@RequestHeader("Authorization") String token) {
        return ResponseType.ok(this.authService.authorize(token));
    }

}
