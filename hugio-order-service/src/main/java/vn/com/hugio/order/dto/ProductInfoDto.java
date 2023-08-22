package vn.com.hugio.order.dto;

import lombok.Data;

@Data
public class ProductInfoDto {
    private String productUid;
    private Long quantity;
    private Double totalPrice;

    public ProductInfoDto(String productUid, Long quantity, Double totalPrice) {
        this.productUid = productUid;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
