package vn.com.hugio.common.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthenResponse {

    @JsonProperty("user_uid")
    private String userUid;

    @JsonProperty("username")
    private String username;

    @JsonProperty("roles")
    private List<String> roles;

}
