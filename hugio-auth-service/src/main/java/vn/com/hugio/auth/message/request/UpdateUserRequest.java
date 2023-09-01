package vn.com.hugio.auth.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateUserRequest {

    @JsonProperty("user_uid")
    private String userUid;

    @JsonProperty("roles")
    private List<String> roles;

}
