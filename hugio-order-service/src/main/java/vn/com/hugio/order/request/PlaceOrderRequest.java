package vn.com.hugio.order.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldNotNull;
import vn.com.hugio.order.request.value.OrderInformation;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceOrderRequest {

    @JsonProperty("order_information")
    @FieldNotNull
    private List<OrderInformation> orderInformation;

}
