package vn.com.hugio.common.pagable;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.com.hugio.common.utils.ObjectUtil;

import static vn.com.hugio.common.pagable.Direction.ASC;


@Data
public class PageLink {
    private static final String DEFAULT_SORT_PROPERTY = "id";

    private static final Direction DEFAULT_DIRECTION = ASC;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, DEFAULT_SORT_PROPERTY);

    private static final int DEFAULT_PAGE_SIZE = 10000;

    private static final int DEFAULT_PAGE = 0;

    private int pageSize;

    private int page;

    private SortOrder sortOrder;

    public PageLink() {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.page = DEFAULT_PAGE;
    }

    public PageLink(Integer pageSize, Integer page, SortOrder sortOrder) {
        this.pageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        this.page = page != null ? page : DEFAULT_PAGE;
        this.sortOrder = sortOrder;
    }

    public PageLink(Integer pageSize, Integer page, String property, Direction direction) {
        this.pageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        this.page = page != null ? page : DEFAULT_PAGE;
        this.sortOrder = new SortOrder(property, direction);
    }

    public PageLink(Integer pageSize, Integer page, Direction direction) {
        this.pageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        this.page = page != null ? page : DEFAULT_PAGE;
        this.sortOrder = new SortOrder(DEFAULT_SORT_PROPERTY, direction);
    }

    public PageLink(Integer pageSize, Integer page) {
        this.pageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
        this.page = page != null ? page : DEFAULT_PAGE;
        this.sortOrder = null;
    }

    public PageLink(PagableRequest pagableRequest) {
        this.pageSize = pagableRequest.getPageSize() != null ? pagableRequest.getPageSize() : DEFAULT_PAGE_SIZE;
        this.page = pagableRequest.getPageNumber() != null ? pagableRequest.getPageNumber() : DEFAULT_PAGE;
        this.sortOrder = new SortOrder(pagableRequest.getProperty(), pagableRequest.getSort());
    }

    public static PageLink create(Integer pageSize, Integer page, SortOrder sortOrder) {
        return new PageLink(pageSize, page, sortOrder);
    }

    public static PageLink create(Integer pageSize, Integer page, String property, Direction direction) {
        return new PageLink(pageSize, page, property, direction);
    }

    public static PageLink create(Integer pageSize, Integer page, Direction direction) {
        return new PageLink(pageSize, page, direction);
    }

    public static PageLink create(Integer pageSize, Integer page) {
        return new PageLink(pageSize, page);
    }

    public static PageLink create(PagableRequest pagableRequest) {
        return new PageLink(pagableRequest);
    }

    public Pageable toPageable() {
        return PageRequest.of(this.page, this.pageSize, toSort());
    }

    public Sort toSort() {
        if (this.sortOrder == null || this.sortOrder.getProperty() == null || this.sortOrder.getProperty().isEmpty()) {
            return DEFAULT_SORT;
        }
        if (this.sortOrder.getDirection() == null) {
            return Sort.by(Sort.Direction.fromString(DEFAULT_DIRECTION.name()), this.sortOrder.getProperty());
        }
        return Sort.by(Sort.Direction.fromString(this.sortOrder.getDirection().name()), this.sortOrder.getProperty());
    }

    public String toSQL() {
        String paging = Strings.EMPTY;
        if (ObjectUtil.isNotNullAndNotEmpty(page) && ObjectUtil.isNotNullAndNotEmpty(pageSize)) {
            paging = String.format(
                    "LIMIT %1$s OFFSET %2$s",
                    pageSize,
                    page * pageSize
            );
        }
        String order;
        if (ObjectUtil.isNullOrEmpty(sortOrder)) {
            order = "ORDER BY id ASC";
        } else {
            StringBuilder stringBuilder = (new StringBuilder()).append("ORDER BY ");
            if (ObjectUtil.isNullOrEmpty(sortOrder.getProperty())) {
                stringBuilder.append("id");
            } else {
                stringBuilder.append(sortOrder.getProperty());
            }
            stringBuilder.append(" " + sortOrder.getDirection().name());
            order = stringBuilder.toString();
        }
        return paging + order;
    }

}
