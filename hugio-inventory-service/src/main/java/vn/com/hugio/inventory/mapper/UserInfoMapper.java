package vn.com.hugio.inventory.mapper;

import org.mapstruct.Mapper;
import vn.com.hugio.inventory.dto.UserInfoDto;
import vn.com.hugio.inventory.entity.UserInfo;

@Mapper(
        componentModel = "spring",
        imports = {
        }
)
public interface UserInfoMapper {

    UserInfoDto userInfoDtoMapper(UserInfo userInfo);

}
