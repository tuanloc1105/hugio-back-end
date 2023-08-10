package vn.com.hugio.user.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeUserDetailRequest {

    @JsonProperty("user_uid")
    @FieldNotNull
    private String userUid;
}
