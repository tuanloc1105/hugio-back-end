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
public class EditProductRequest {

    @JsonProperty("product_uid")
    private String productId;

    @FieldNotNull
    @JsonProperty("product_name")
    private String name;

    @JsonProperty("product_quantity")
    private Long quantity;

    @FieldNotNull
    @JsonProperty("price")
    private Double price;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("fee")
    private Double fee;

    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("categories")
    private List<String> category;

    @JsonProperty("details")
    private List<ProductDetailReqDto> details;

}
