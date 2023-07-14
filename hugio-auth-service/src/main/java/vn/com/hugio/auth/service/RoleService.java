package vn.com.hugio.auth.service;

import vn.com.hugio.auth.dto.RoleDto;
import vn.com.hugio.auth.entity.Role;
import vn.com.hugio.common.pagable.PagableRequest;

import java.util.List;

public interface RoleService {
    List<RoleDto> findRoleByListName(List<String> roleName);

    List<Role> findRoleByList(List<String> roleName);

    List<Role> all(PagableRequest request);
}
