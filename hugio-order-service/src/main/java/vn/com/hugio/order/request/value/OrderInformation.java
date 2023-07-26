package vn.com.hugio.order.request.value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderInformation {

    @JsonProperty("product_uid")
    private String productUid;

    @JsonProperty("quantity")
    private Integer quantity;
}
