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


    @FieldNotNull
    @JsonProperty("name")
    private String name;

    @FieldNotNull
    @JsonProperty("price")
    private Double price;

    @FieldNotNull
    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("details")
    private List<ProductDetailReqDto> details;

}
