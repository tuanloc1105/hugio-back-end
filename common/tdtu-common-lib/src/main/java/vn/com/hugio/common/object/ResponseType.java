package vn.com.hugio.common.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.MDC;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;

import java.io.Serial;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseType<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6233684696666789109L;
    @JsonProperty("trace")
    private final Trace trace = new Trace(MDC.get("traceId"), MDC.get("cid"));
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("response")
    private T response;

    public ResponseType() {
    }

    public ResponseType(String code, String message, String traceId, T response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }

    public static <T> ResponseType<T> ok(T data) {
        ResponseType<T> responseType = new ResponseType<>();
        responseType.setCode(String.valueOf(ErrorCodeEnum.SUCCESS.getCode()));
        responseType.setMessage(ErrorCodeEnum.SUCCESS.getMessage());
        responseType.setResponse(data);
        return responseType;
    }

    public static <T> ResponseType<T> failed(T data) {
        ResponseType<T> responseType = new ResponseType<>();
        responseType.setCode(String.valueOf(ErrorCodeEnum.FAILURE.getCode()));
        responseType.setMessage(ErrorCodeEnum.FAILURE.getMessage());
        responseType.setResponse(data);
        return responseType;
    }

    public static <T> ResponseTypeBuilder<T> builder() {
        return new ResponseTypeBuilder<>();
    }

    public static <T> ResponseTypeBuilder<T> builder(Class<T>... clz) {
        return new ResponseTypeBuilder<T>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCodeNumber() {
        return Integer.parseInt(code);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Trace getTrace() {
        return trace;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public static class ResponseTypeBuilder<T> {

        private String code;
        private String message;
        private T response;

        public ResponseTypeBuilder() {
        }

        public ResponseTypeBuilder<T> code(String code) {
            this.code = code;
            return this;
        }

        public ResponseTypeBuilder<T> code(int code) {
            this.code = String.valueOf(code);
            return this;
        }

        public ResponseTypeBuilder<T> code(long code) {
            this.code = String.valueOf(code);
            return this;
        }

        public ResponseTypeBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ResponseTypeBuilder<T> response(T response) {
            this.response = response;
            return this;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getResponse() {
            return response;
        }

        public ResponseType<T> build() {
            ResponseType<T> responseType = new ResponseType<>();
            responseType.setCode(this.getCode());
            responseType.setMessage(this.getMessage());
            responseType.setResponse(this.getResponse());
            return responseType;
        }
    }

}
