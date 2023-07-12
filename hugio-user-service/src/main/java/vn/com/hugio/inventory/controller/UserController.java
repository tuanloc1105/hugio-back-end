package vn.com.hugio.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.aop.HasRoles;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.inventory.dto.UserInfoDto;
import vn.com.hugio.inventory.message.request.CreateUserInfoRequest;
import vn.com.hugio.inventory.message.request.GetAllUserRequest;
import vn.com.hugio.inventory.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    @HasRoles(roles = "ADMIN")
    public ResponseType<String> create(@RequestBody RequestType<CreateUserInfoRequest> request) {
        this.userService.createUser(request.getRequest());
        return ResponseType.ok("OK");
    }

    @PostMapping("/all")
    @HasRoles(roles = "ADMIN")
    public ResponseType<PageResponse<UserInfoDto>> all(@RequestBody RequestType<GetAllUserRequest> request) {
        return ResponseType.ok(this.userService.getAllUser(request.getRequest()));
    }

    @PostMapping("/delete")
    @HasRoles(roles = "ADMIN")
    public ResponseType<?> delete(@RequestBody RequestType<Long> request) {
        return this.userService.deleteUser(request.getRequest());
    }

    @PostMapping("/active")
    @HasRoles(roles = "ADMIN")
    public ResponseType<?> active(@RequestBody RequestType<Long> request) {
        return this.userService.activeUser(request.getRequest());
    }

}