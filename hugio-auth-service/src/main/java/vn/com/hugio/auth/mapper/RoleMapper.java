package vn.com.hugio.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.com.hugio.auth.dto.RoleDto;
import vn.com.hugio.auth.entity.Role;

@Mapper(
        componentModel = "spring",
        imports = {
        }
)
public interface RoleMapper {

    @Mapping(source = "roleName", target = "roleName")
    RoleDto roleDtoMapper(Role user);

}
