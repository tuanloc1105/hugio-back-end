package vn.com.hugio.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryDto extends BaseEntityDto {

    @JsonProperty("category_uid")
    private String categoryUid;

    @JsonProperty("category_name")
    private String categoryName;

}
