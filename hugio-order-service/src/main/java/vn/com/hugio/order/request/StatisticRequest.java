package vn.com.hugio.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class StatisticRequest {
    private Integer year;
    private Integer month;
}
