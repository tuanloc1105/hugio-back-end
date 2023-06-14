package vn.com.hugio.user.mapper;

import org.mapstruct.Mapper;
import vn.com.hugio.user.dto.UserInfoDto;
import vn.com.hugio.user.entity.UserInfo;

@Mapper(
        componentModel = "spring",
        imports = {
        }
)
public interface UserInfoMapper {

    UserInfoDto userInfoDtoMapper(UserInfo userInfo);

}
