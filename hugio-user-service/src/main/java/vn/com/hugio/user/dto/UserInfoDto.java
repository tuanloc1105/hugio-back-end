package vn.com.hugio.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("user_uid")
    private String userUid;

    @JsonProperty("cif")
    private String cif;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("roles")
    private List<String> roles;
}
