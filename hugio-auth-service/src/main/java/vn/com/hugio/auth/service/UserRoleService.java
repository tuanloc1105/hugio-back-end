package vn.com.hugio.auth.service;

import vn.com.hugio.auth.entity.UserRole;

public interface UserRoleService {
    void saveNew(UserRole userRole);

    void update(UserRole userRole);

    void deleteByUserId(Long userId);
}
