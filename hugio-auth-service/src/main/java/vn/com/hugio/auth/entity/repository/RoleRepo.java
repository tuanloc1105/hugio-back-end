package vn.com.hugio.auth.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.hugio.auth.entity.Role;
import vn.com.hugio.common.entity.repository.BaseRepository;

import java.util.List;

public interface RoleRepo extends BaseRepository<Role> {

    List<Role> findByRoleNameIn(List<String> roleName);

    Page<Role> findByActiveIsTrue(Pageable pageable);

}
