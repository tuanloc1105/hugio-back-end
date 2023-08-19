package vn.com.hugio.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaleStatisticDto {
    private String customerName;
    private Long quantity;

    public SaleStatisticDto(String customerName, Long quantity) {
        this.customerName = customerName;
        this.quantity = quantity;
    }
}
