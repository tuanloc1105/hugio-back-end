package vn.com.hugio.auth.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "ROLE")
@NamedEntityGraphs(
        value = {
                @NamedEntityGraph(
                        name = "Role.userRoles",
                        attributeNodes = {
                                @NamedAttributeNode("userRoles")
                        }
                ),
        }
)
@EqualsAndHashCode(callSuper=false)
public class Role extends BaseEntity implements Serializable {

    @Column(name = "ROLE_NAME", nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.ALL})
    private List<UserRole> userRoles;

    public Role(String roleName, List<UserRole> userRoles) {
        this.roleName = roleName;
        this.userRoles = userRoles;
    }

    @Builder
    public Role(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, String roleName, List<UserRole> userRoles) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.roleName = roleName;
        this.userRoles = userRoles;
    }
}
