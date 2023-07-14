package vn.com.hugio.auth.service;

import org.springframework.data.domain.Page;
import vn.com.hugio.auth.dto.RoleDto;
import vn.com.hugio.auth.entity.Role;
import vn.com.hugio.common.pagable.PagableRequest;

import java.util.List;

public interface RoleService {
    List<RoleDto> findRoleByListName(List<String> roleName);

    List<Role> findRoleByList(List<String> roleName);

    Page<Role> all(PagableRequest request);
}
