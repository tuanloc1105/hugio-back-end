package vn.com.hugio.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductQuantityDto {
    private String productUid;
    private Long quantity;
    private Long importedQuantity;
    private Double fee;
}
