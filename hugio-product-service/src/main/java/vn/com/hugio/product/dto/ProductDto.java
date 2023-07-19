package vn.com.hugio.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class ProductDto extends BaseEntityDto {

    @JsonProperty("id")
    private Long id;

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

    @JsonProperty("product_details")
    private List<ProductDetailDto> productDetailDtos;

    @JsonProperty("categories")
    private List<CategoryDto> categoryDtos;

}
