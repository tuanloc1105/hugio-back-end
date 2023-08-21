package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TotalSaleEachDayDto {

    @JsonProperty("date")
    private String date;

    @JsonProperty("total")
    private Long total;
}
