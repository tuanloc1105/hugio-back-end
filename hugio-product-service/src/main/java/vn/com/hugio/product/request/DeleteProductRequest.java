package vn.com.hugio.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteProductRequest {

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("is_permanent")
    private Boolean isPermanent;

}
