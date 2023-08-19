package vn.com.hugio.user.mapper;

import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.entity.UserInfo;

public final class UserMapper {

    public static UserInfoDto map(UserInfo userInfo) {
        return UserInfoDto.builder()
                .id(userInfo.getId())
                .active(userInfo.isActive())
                .createdAt(userInfo.getCreatedAt())
                .updatedAt(userInfo.getUpdatedAt())
                .createdBy(userInfo.getCreatedBy())
                .updatedBy(userInfo.getUpdatedBy())
                .userUid(userInfo.getUserUid())
                .cif(userInfo.getCif())
                .email(userInfo.getEmail())
                .address(userInfo.getAddress())
                .fullName(userInfo.getFullName())
                .phoneNumber(userInfo.getPhoneNumber())
                .build();
    }

}
