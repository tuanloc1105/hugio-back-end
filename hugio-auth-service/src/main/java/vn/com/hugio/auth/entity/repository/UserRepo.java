package vn.com.hugio.auth.entity.repository;

import vn.com.hugio.auth.entity.User;
import vn.com.hugio.common.entity.repository.BaseRepository;

import java.util.Optional;

public interface UserRepo extends BaseRepository<User> {

    Optional<User> findByUsernameAndActiveIsTrue(String username);

    Optional<User> findByUserUidAndActiveIsTrue(String userUid);

    Optional<User> findByUserUid(String userUid);

    boolean existsByUsername(String username);

}
