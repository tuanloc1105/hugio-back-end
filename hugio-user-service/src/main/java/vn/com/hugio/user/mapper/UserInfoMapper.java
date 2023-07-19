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
    UserInfoDto userInfoDtoMapper(UserInfo userInfo);

}
