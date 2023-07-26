package vn.com.hugio.order.request.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderInformation {

    @JsonProperty("product_uid")
    @FieldNotNull
    private String productUid;

    @JsonProperty("quantity")
    @FieldNotNull
    private Integer quantity;
}
