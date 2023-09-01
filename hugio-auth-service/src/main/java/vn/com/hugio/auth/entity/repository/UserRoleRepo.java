package vn.com.hugio.auth.entity.repository;

import org.springframework.data.jpa.repository.Modifying;
import vn.com.hugio.auth.entity.UserRole;
import vn.com.hugio.common.entity.repository.BaseRepository;

public interface UserRoleRepo extends BaseRepository<UserRole> {

    @Modifying
    void deleteByUser_Id(Long user);

}
