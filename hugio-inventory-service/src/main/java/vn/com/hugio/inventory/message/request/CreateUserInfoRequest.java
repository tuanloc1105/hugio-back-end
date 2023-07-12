package vn.com.hugio.inventory.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserInfoRequest {

    @JsonProperty("username")
    @FieldNotNull
    private String username;

    @JsonProperty("password")
    @FieldNotNull
    private String password;

    @JsonProperty("email")
    @FieldNotNull
    private String email;

    @JsonProperty("address")
    @FieldNotNull
    private String address;

    @JsonProperty("full_name")
    @FieldNotNull
    private String fullName;

    @JsonProperty("roles")
    private List<String> roles;

}
