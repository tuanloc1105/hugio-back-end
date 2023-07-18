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
import vn.com.hugio.user.service.grpc.client.AuthServiceGrpcClient;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleController {

    private final AuthServiceGrpcClient authServiceGrpcClient;

    @PostMapping("/all")
    @HasRoles(roles = "ADMIN")
    public ResponseType<List<String>> all(@RequestBody RequestType<PagableRequest> request) {
        return ResponseType.ok(this.authServiceGrpcClient.getRoles(request.getRequest()));
    }

}
