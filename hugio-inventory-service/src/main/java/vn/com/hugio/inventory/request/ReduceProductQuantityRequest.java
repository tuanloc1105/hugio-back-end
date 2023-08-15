package vn.com.hugio.inventory.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReduceProductQuantityRequest {

    @JsonProperty("product_uid")
    private String productUid;

    @JsonProperty("quantity")
    private Long quantity;
}
