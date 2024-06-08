package vn.com.hugio.common.pagable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SortOrder {
    private String property;
    private Direction direction;

    public SortOrder(String property) {
        this(property, Direction.ASC);
    }

}
