package vn.com.hugio.product.entity.repository;

import vn.com.hugio.product.entity.UserInfo;
import vn.com.hugio.common.entity.repository.BaseRepository;

import java.time.LocalDateTime;

public interface UserInfoRepo extends BaseRepository<UserInfo> {

    Integer countByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

}
