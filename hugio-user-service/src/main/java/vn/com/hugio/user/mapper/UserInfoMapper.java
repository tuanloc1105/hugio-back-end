package vn.com.hugio.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.entity.UserInfo;

@Mapper(
        componentModel = "spring",
        imports = {
        }
)
public interface UserInfoMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "updatedBy", target = "updatedBy")
    @Mapping(target = "username", ignore = true)
    UserInfoDto userInfoDtoMapper(UserInfo userInfo);

}
