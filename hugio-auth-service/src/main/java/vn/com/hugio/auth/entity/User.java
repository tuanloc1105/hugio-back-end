package vn.com.hugio.auth.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "USERS")
public class User extends BaseEntity implements Serializable {

    @Column(name = "USER_UID", nullable = false, updatable = false)
    private String userUid;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

    public User(String userUid, String username, String password, List<UserRole> userRoles) {
        this.userUid = userUid;
        this.username = username;
        this.password = password;
        this.userRoles = userRoles;
    }

    @Builder
    public User(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, String userUid, String username, String password, List<UserRole> userRoles) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.userUid = userUid;
        this.username = username;
        this.password = password;
        this.userRoles = userRoles;
    }
}
