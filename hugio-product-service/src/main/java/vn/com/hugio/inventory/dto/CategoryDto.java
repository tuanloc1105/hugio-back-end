package vn.com.hugio.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.dto.BaseEntityDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto extends BaseEntityDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("category_uid")
    private String categoryUid;

    @JsonProperty("category_name")
    private String categoryName;

}
