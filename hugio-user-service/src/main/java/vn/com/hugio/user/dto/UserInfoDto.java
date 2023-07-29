package vn.com.hugio.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserInfoDto extends BaseEntityDto {
    private String userUid;
    private String cif;
    private String email;
    private String address;
    private String fullName;
    private List<String> roles;
}
