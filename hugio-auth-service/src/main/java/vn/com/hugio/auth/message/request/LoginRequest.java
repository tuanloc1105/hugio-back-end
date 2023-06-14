package vn.com.hugio.auth.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    @JsonProperty("username")
    @FieldNotNull
    private String username;

    @JsonProperty("password")
    @FieldNotNull
    private String password;

}
