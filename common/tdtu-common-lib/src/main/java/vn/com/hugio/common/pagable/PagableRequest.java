package vn.com.hugio.common.pagable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.aop.FieldGreaterThan;
import vn.com.hugio.common.aop.FieldNotNull;
import vn.com.hugio.common.pagable.Direction;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagableRequest {

    @JsonProperty("page_number")
    @FieldGreaterThan(value = 0D)
    @FieldNotNull
    private Integer pageNumber;

    @JsonProperty("page_size")
    @FieldNotNull
    private Integer pageSize;

    @JsonProperty("property")
    private String property;

    @JsonProperty("sort")
    @FieldNotNull
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

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
