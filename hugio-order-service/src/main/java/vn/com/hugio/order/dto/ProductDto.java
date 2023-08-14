package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class ProductDto extends BaseEntityDto {

    @JsonProperty("product_uid")
    private String productUid;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("raw_product_name")
    private String rawProductName;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("product_description")
    private String productDescription;

}
