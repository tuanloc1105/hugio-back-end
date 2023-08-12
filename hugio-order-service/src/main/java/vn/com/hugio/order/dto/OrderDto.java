package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto extends BaseEntityDto {

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("order_detail")
    private List<OrderDetailDto> orderDetails;

}
