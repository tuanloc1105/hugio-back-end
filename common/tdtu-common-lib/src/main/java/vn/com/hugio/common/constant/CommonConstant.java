package vn.com.hugio.common.constant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;

public class CommonConstant {

    public static final String DEFAULT_TIMEZONE = "Asia/Ho_Chi_Minh";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper() {
        @Serial
        private static final long serialVersionUID = -2832088530758291739L;

        {
            findAndRegisterModules();
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            setSerializationInclusion(JsonInclude.Include.ALWAYS);
        }
    };


}
