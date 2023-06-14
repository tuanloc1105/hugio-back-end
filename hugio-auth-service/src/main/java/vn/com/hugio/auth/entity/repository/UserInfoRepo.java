package vn.com.hugio.auth.entity.repository;

import vn.com.hugio.auth.entity.UserInfo;
import vn.com.hugio.common.entity.repository.BaseRepository;

import java.time.LocalDateTime;

public interface UserInfoRepo extends BaseRepository<UserInfo> {

    Integer countByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

}
