package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class StatisticDto {

    @JsonProperty("total_order_in_month")
    private Integer totalOrderInMonth;

    @JsonProperty("total_sale_in_month")
    private Double totalSaleInMonth;

    @JsonProperty("total_cancel_order")
    private Integer totalCancelOrder;

    @JsonProperty("sale_each_day")
    private List<TotalSaleEachDayDto> saleEachDay;

}
