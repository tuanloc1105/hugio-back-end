package vn.com.hugio.auth.mapper;

import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.entity.User;

import java.util.stream.Collectors;

public final class UserMap {

    public static UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .userUid(user.getUserUid())
                .username(user.getUsername())
                .roles(user.getUserRoles().stream().map(userRole -> userRole.getRole().getRoleName()).collect(Collectors.toList()))
                .build();
    }

}
