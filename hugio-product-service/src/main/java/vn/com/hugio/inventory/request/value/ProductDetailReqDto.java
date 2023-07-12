package vn.com.hugio.inventory.request.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetailReqDto {

    @FieldNotNull
    @JsonProperty("key")
    private String key;

    @FieldNotNull
    @JsonProperty("value")
    private String value;

}
