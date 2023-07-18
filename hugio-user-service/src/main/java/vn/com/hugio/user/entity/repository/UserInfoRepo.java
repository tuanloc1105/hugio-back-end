package vn.com.hugio.user.entity.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.user.entity.UserInfo;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserInfoRepo extends BaseRepository<UserInfo> {

    Integer countByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

    Optional<UserInfo> findByUserUidAndActiveIsTrue(String userUid);

}
