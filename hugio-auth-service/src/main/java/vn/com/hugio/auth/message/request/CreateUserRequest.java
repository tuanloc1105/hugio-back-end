package vn.com.hugio.auth.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequest {

    @JsonProperty("encrypt_username")
    private String encryptUsername;

    @JsonProperty("encrypt_password")
    private String encryptPassword;

    @JsonProperty("roles")
    private List<String> roles;

}
