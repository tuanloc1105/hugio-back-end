package vn.com.hugio.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeleteProductRequest {

    @JsonProperty("product_id")
    @FieldNotNull
    private String productId;

    @JsonProperty("is_permanent")
    @FieldNotNull
    private Boolean isPermanent;

}
