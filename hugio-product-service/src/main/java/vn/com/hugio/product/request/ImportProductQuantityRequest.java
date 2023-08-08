package vn.com.hugio.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;
import vn.com.hugio.product.request.value.ProductDetailReqDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImportProductQuantityRequest {

    @JsonProperty("product_uid")
    private String productId;

    @JsonProperty("product_quantity")
    @FieldNotNull
    private Long quantity;

}
