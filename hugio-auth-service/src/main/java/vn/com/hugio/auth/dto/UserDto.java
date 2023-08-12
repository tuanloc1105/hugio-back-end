package vn.com.hugio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto extends BaseEntityDto {

    private String userUid;
    private String username;
    private List<String> roles;

    @Builder
    public UserDto(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, String userUid, String username, List<String> roles) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.userUid = userUid;
        this.username = username;
        this.roles = roles;
    }
}
