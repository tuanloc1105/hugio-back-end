package vn.com.hugio.inventory.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailRequest {

    @JsonProperty("user_uid")
    @FieldNotNull
    private String userUid;

}
