package vn.com.hugio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoleDto extends BaseEntityDto {

    private String roleName;

}
