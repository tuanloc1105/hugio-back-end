package vn.com.hugio.user.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.user.entity.UserInfo;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserInfoRepo extends BaseRepository<UserInfo> {

    @Query("select ui " +
            "from UserInfo ui " +
            "where ui.cif like :content " +
            "or ui.fullName like :content " +
            "or ui.email like :content " +
            "or ui.phoneNumber like :content")
    Page<UserInfo> findByContent(@Param("content") String content, Pageable pageable);

    Integer countByCreatedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

    Optional<UserInfo> findByUserUidAndActiveIsTrue(String userUid);

    Optional<UserInfo> findByUserUid(String userUid);

    @Query("update UserInfo set " +
            "email = :email, " +
            "address = :address, " +
            "fullName = :fullName, " +
            "phoneNumber = :phoneNumber, " +
            "updatedAt = :updatedAt, " +
            "updatedBy = :updatedBy " +
            "where userUid = :userUid and active = true")
    @Modifying
    Integer updateUserInfo(
            @Param("email") String email,
            @Param("address") String address,
            @Param("fullName") String fullName,
            @Param("phoneNumber") String phoneNumber,
            @Param("updatedAt") LocalDateTime updatedAt,
            @Param("updatedBy") String updatedBy,
            @Param("userUid") String userUid
    );

}
