package vn.com.hugio.auth.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.auth.dto.UserDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {

    @JsonProperty("access_token")
    private String token;

    @JsonProperty("info")
    private UserDto info;

}

