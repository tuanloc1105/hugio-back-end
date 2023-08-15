package vn.com.hugio.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseEntityDto {

    @JsonProperty("product_uid")
    private String productUid;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("raw_product_name")
    private String rawProductName;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("product_description")
    private String productDescription;

    @Builder
    public ProductDto(Long id,
                      boolean active,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      String createdBy,
                      String updatedBy,
                      String productUid,
                      String productName,
                      String rawProductName,
                      Double price,
                      Double discount,
                      String productDescription) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.productUid = productUid;
        this.productName = productName;
        this.rawProductName = rawProductName;
        this.price = price;
        this.discount = discount;
        this.productDescription = productDescription;
    }
}
