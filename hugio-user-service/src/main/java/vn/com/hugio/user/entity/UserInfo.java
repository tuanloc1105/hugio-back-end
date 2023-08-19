package vn.com.hugio.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "USER_INFO")
@EqualsAndHashCode(callSuper = false)
public class UserInfo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 611933429121974217L;

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

    @Column(name = "PHONE_NUMBER", nullable = false, updatable = false)
    private String phoneNumber;

    public UserInfo(String userUid) {
        this.userUid = userUid;
    }

    @Builder
    public UserInfo(Long id,
                    boolean active,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt,
                    String createdBy,
                    String updatedBy,
                    String userUid,
                    String cif,
                    String email,
                    String address,
                    String fullName,
                    String phoneNumber) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.userUid = userUid;
        this.cif = cif;
        this.email = email;
        this.address = address;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
