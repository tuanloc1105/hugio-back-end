package vn.com.hugio.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.aop.HasRoles;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.message.request.ChangeUserDetailRequest;
import vn.com.hugio.user.message.request.CreateUserInfoRequest;
import vn.com.hugio.user.message.request.EditUserInfoRequest;
import vn.com.hugio.user.message.request.UserDetailRequest;
import vn.com.hugio.user.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    //@HasRoles(roles = "ADMIN")
    public ResponseType<String> create(@RequestBody RequestType<CreateUserInfoRequest> request) {
        this.userService.createUser(request.getRequest());
        return ResponseType.ok("OK");
    }

    @PostMapping("/edit")
    //@HasRoles(roles = "ADMIN")
    public ResponseType<String> edit(@RequestBody RequestType<EditUserInfoRequest> request) {
        this.userService.updateUser(request.getRequest());
        return ResponseType.ok("OK");
    }

    @PostMapping("/detail")
    //@HasRoles(roles = "ADMIN")
    public ResponseType<UserInfoDto> detail(@RequestBody RequestType<UserDetailRequest> request) {
        return ResponseType.ok(this.userService.detail(request.getRequest().getUserUid()));
    }

    @PostMapping("/all")
    //@HasRoles(roles = "ADMIN")
    public ResponseType<PageResponse<UserInfoDto>> all(@RequestBody RequestType<PagableRequest> request) {
        return ResponseType.ok(this.userService.getAllUser(request.getRequest()));
    }

    @PostMapping("/delete")
    //@HasRoles(roles = "ADMIN")
    public ResponseType<?> delete(@RequestBody RequestType<ChangeUserDetailRequest> request) {
        return this.userService.deleteUser(request.getRequest().getUserUid());
    }

    @PostMapping("/active")
    //@HasRoles(roles = "ADMIN")
    public ResponseType<?> active(@RequestBody RequestType<ChangeUserDetailRequest> request) {
        return this.userService.activeUser(request.getRequest().getUserUid());
    }

}
