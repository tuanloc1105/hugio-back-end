package vn.com.hugio.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AccessTokenUtil {
    
    private final static ObjectMapper MAPPER;
    
    static {
        MAPPER = new ObjectMapper() {

            @Serial
            private static final long serialVersionUID = 5826881185744924605L;

            {
                findAndRegisterModules();
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                setSerializationInclusion(JsonInclude.Include.ALWAYS);
            }
        };
    }
    
    public static Map<String, String> extractJWT(String accessToken) {
        Map<String, String> result = new HashMap<>();

        String[] split_string = accessToken.split("\\.");
        if (split_string.length != 3) {
            return result;
        }
        String header = split_string[0];
        String body = split_string[1];
        String signature = split_string[2];

        Base64 base64Url = new Base64(true);
        result.put("header", new String(base64Url.decode(header)));
        result.put("body", new String(base64Url.decode(body)));
        result.put("signature", new String(base64Url.decode(signature)));
        return result;
    }

    public static <T> T extractJwtWithStructure(String accessToken, TypeReference<T> typeReference) {
        Map<String, String> result = extractJWT(accessToken);
        String body = Optional.ofNullable(result.get("body")).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.AUTH_FAILURE));
        try {
            return MAPPER.readValue(body, typeReference);
        } catch (Exception e) {
            LOG.error(ExceptionStackTraceUtil.getStackTrace(e));
            return null;
        }
    }
}
