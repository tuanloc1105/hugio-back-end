package vn.com.hugio.common.pagable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8325398541544489844L;

    @JsonProperty("page_number")
    private Integer pageNumber;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("number_of_elements")
    private Integer numberOfElements;

    @JsonProperty("total_elements")
    private Long totalElements;

    @JsonProperty("first_page")
    private Boolean firstPage;

    @JsonProperty("last_page")
    private Boolean lastPage;

    @JsonProperty("content")
    private List<T> content;

    public PageResponse() {
    }

    public static <T, V> PageResponse<V> create(Page<T> page, Function<List<T>, List<V>> function, boolean... isIncreasePageNumber) {
        return PageResponse
                .<V>builder()
                .pageNumber(isIncreasePageNumber.length > 0 && isIncreasePageNumber[0] ? page.getNumber() + 1 : page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .firstPage(page.isFirst())
                .lastPage(page.isLast())
                .content(function.apply(page.getContent()))
                .build();
    }

    public static <T, V> PageResponse<V> create(Page<T> page, Handler<T, V> handler, boolean... isIncreasePageNumber) {
        return PageResponse
                .<V>builder()
                .pageNumber(isIncreasePageNumber.length > 0 && isIncreasePageNumber[0] ? page.getNumber() + 1 : page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .firstPage(page.isFirst())
                .lastPage(page.isLast())
                .content(page.getContent().stream().map(handler::handle).collect(Collectors.toList()))
                .build();
    }

    public static <V> PageResponse<V> create(List<V> list, int totalElements, int pageNumber, int pageSize, boolean... isIncreasePageNumber) {
        return PageResponse
                .<V>builder()
                .pageNumber(isIncreasePageNumber.length > 0 && isIncreasePageNumber[0] ? pageNumber + 1 : pageNumber)
                .pageSize(pageSize)
                .totalPages(list.isEmpty() ? 0 : totalElements / list.size())
                .numberOfElements(list.size())
                .totalElements((long) totalElements)
                .firstPage(pageNumber == 0)
                .lastPage(((pageNumber + 1) * (pageSize) == totalElements) || (pageSize > totalElements))
                .content(list)
                .build();
    }

    public static <V> PageResponse<V> create(Page<?> page, List<V> list, boolean... isIncreasePageNumber) {
        return PageResponse
                .<V>builder()
                .pageNumber(isIncreasePageNumber.length > 0 && isIncreasePageNumber[0] ? page.getNumber() + 1 : page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .firstPage(page.isFirst())
                .lastPage(page.isLast())
                .content(list)
                .build();
    }

    public interface Handler<T, V> {
        V handle(T input);
    }

}
