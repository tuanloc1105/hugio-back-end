package vn.com.hugio.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseEntityDto {

    @JsonProperty("product_uid")
    @SerializedName("product_uid")
    private String productUid;

    @JsonProperty("product_name")
    @SerializedName("product_name")
    private String productName;

    @JsonProperty("product_quantity")
    @SerializedName("product_quantity")
    private Long quantity;

    @JsonProperty("raw_product_name")
    @SerializedName("raw_product_name")
    private String rawProductName;

    @JsonProperty("price")
    @SerializedName("price")
    private Double price;

    @JsonProperty("discount")
    @SerializedName("discount")
    private Double discount;

    @JsonProperty("product_description")
    @SerializedName("product_description")
    private String productDescription;

    @JsonProperty("product_details")
    @SerializedName("product_details")
    private List<ProductDetailDto> productDetailDtos;

    @JsonProperty("categories")
    @SerializedName("categories")
    private List<CategoryDto> categoryDtos;

    @Builder
    public ProductDto(Long id,
                      boolean active,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      String createdBy,
                      String updatedBy,
                      String productUid,
                      String productName,
                      Long quantity,
                      String rawProductName,
                      Double price,
                      Double discount,
                      String productDescription,
                      List<ProductDetailDto> productDetailDtos,
                      List<CategoryDto> categoryDtos) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.productUid = productUid;
        this.productName = productName;
        this.quantity = quantity;
        this.rawProductName = rawProductName;
        this.price = price;
        this.discount = discount;
        this.productDescription = productDescription;
        this.productDetailDtos = productDetailDtos;
        this.categoryDtos = categoryDtos;
    }
}
