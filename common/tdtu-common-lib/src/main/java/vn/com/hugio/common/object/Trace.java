package vn.com.hugio.common.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trace {

    @JsonProperty("sid")
    private String sid;

    @JsonProperty("cid")
    private String cid;

}
