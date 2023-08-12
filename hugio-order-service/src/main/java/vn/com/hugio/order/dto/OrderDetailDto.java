package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDto extends BaseEntityDto {
    @JsonProperty("product")
    private ProductDto productDto;

    @JsonProperty("quantity")
    private Integer quantity;
}
