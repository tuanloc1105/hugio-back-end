package vn.com.hugio.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldGreaterThan;
import vn.com.hugio.common.pagable.Direction;

@AllArgsConstructor
@NoArgsConstructor
public class GetProductRequest {

    @JsonProperty("page_number")
    @FieldGreaterThan(value = 0D)
    private Integer pageNumber;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("sort")
    private Direction sort;

    public Integer getPageNumber() {
        return pageNumber > 0 ? pageNumber - 1 : 0;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Direction getSort() {
        return sort != null ? sort : Direction.ASC;
    }

    public void setSort(Direction sort) {
        this.sort = sort;
    }
}
