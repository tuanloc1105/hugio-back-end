package vn.com.hugio.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import vn.com.hugio.grpc.user.CreateUserInput;
import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.entity.User;
import vn.com.hugio.auth.message.request.CreateUserRequest;

import javax.annotation.processing.Generated;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        imports = {
                Generated.class,
                Component.class,
                UserDto.class,
                User.class,
                Collectors.class
        }
)
public interface UserMapper {

    @Mapping(source = "userUid", target = "userUid")
    @Mapping(source = "username", target = "username")
    @Mapping(target = "roles", expression = "java( user.getUserRoles().stream().map(userRole -> userRole.getRole().getRoleName()).collect(Collectors.toList()) )")
    UserDto userDtoMapper(User user);

    @Mapping(source = "encryptUsername", target = "encryptUsername")
    @Mapping(source = "encryptPassword", target = "encryptPassword")
    @Mapping(source = "rolesList", target = "roles")
    CreateUserRequest createUserRequestMapper(CreateUserInput user);

}
