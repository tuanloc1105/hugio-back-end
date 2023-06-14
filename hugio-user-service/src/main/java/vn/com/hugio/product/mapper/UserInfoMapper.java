package vn.com.hugio.product.mapper;

import org.mapstruct.Mapper;
import vn.com.hugio.product.dto.UserInfoDto;
import vn.com.hugio.product.entity.UserInfo;

@Mapper(
        componentModel = "spring",
        imports = {
        }
)
public interface UserInfoMapper {

    UserInfoDto userInfoDtoMapper(UserInfo userInfo);

}
