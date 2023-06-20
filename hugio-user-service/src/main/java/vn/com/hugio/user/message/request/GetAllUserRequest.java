package vn.com.hugio.user.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUserRequest {

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page_number")
    private Integer pageNumber;

}
