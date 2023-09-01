package vn.com.hugio.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.auth.entity.UserRole;
import vn.com.hugio.auth.entity.repository.UserRoleRepo;
import vn.com.hugio.auth.service.UserRoleService;
import vn.com.hugio.common.service.BaseService;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class UserRoleServiceImpl extends BaseService<UserRole, UserRoleRepo> implements UserRoleService {

    @Autowired
    public UserRoleServiceImpl(UserRoleRepo repository) {
        super(repository);
    }

    @Override
    public void saveNew(UserRole userRole) {
        this.repository.save(userRole);
    }

    @Override
    public void update(UserRole userRole) {
        this.repository.save(userRole);
    }

    @Override
    public void deleteByUserId(Long userId) {
        this.repository.deleteByUser_Id(userId);
    }

}
