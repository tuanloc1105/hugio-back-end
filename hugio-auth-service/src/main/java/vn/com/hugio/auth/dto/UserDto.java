package vn.com.hugio.auth.dto;

import lombok.*;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto extends BaseEntityDto {

    private UUID userUid;
    private String username;
    private List<String> roles;

}
