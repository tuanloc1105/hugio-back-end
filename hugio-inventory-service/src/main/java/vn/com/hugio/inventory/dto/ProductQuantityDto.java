package vn.com.hugio.inventory.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductQuantityDto {
    private String productUid;
    private Long quantity;
    private Long importedQuantity;
    private Double fee;

    public ProductQuantityDto(String productUid, Long quantity, Long importedQuantity, Double fee) {
        this.productUid = productUid;
        this.quantity = quantity;
        this.importedQuantity = importedQuantity;
        this.fee = fee;
    }
}
