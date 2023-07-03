package vn.com.hugio.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailDto extends BaseEntityDto {

    @JsonProperty("key")
    private String key;

    @JsonProperty("value")
    private String value;

}
