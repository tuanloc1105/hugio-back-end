package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;
import vn.com.hugio.order.enums.OrderStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto extends BaseEntityDto {

    @JsonProperty("order_code")
    private String orderCode;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("customer_phone_number")
    private String customerPhoneNumber;

    @JsonProperty("total_price")
    private Double totalPrice;

    @JsonProperty("order_status")
    private OrderStatus orderStatus;

    @JsonProperty("order_detail")
    private List<OrderDetailDto> orderDetails;

}
