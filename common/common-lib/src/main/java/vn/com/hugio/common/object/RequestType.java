package vn.com.hugio.common.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class RequestType<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5573440474654974940L;

    @JsonProperty("trace")
    private Trace trace;

    @JsonProperty("request")
    private T request;

}
