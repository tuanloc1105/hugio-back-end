package vn.com.hugio.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldGreaterThan;
import vn.com.hugio.common.aop.FieldNotNull;
import vn.com.hugio.product.request.value.ProductDetailReqDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateProductRequest {


    @FieldNotNull
    @JsonProperty("product_name")
    private String name;

    @FieldNotNull
    @JsonProperty("price")
    private Double price;

    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("product_quantity")
    @FieldNotNull
    @FieldGreaterThan(0)
    private Long quantity;

    @JsonProperty("fee")
    private Double fee;

    @JsonProperty("categories")
    private List<String> category;

    @JsonProperty("details")
    private List<ProductDetailReqDto> details;

}
