package vn.com.hugio.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "USER_INFO")
public class UserInfo extends BaseEntity implements Serializable {

    @Column(name = "USER_UID", nullable = false, updatable = false)
    private String userUid;

    @Column(name = "CIF", nullable = false, updatable = false)
    private String cif;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ADDRESS", nullable = false, updatable = false)
    private String address;

    @Column(name = "FULL_NAME", nullable = false, updatable = false)
    private String fullName;

    public UserInfo(String userUid) {
        this.userUid = userUid;
    }

    @Builder
    public UserInfo(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, String userUid) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.userUid = userUid;
    }
}
