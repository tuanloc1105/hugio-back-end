package vn.com.hugio.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.object.wrapper.MyHttpServletRequestWrapper;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Optional;

@Component
public class FilterUtil {


    private final ObjectMapper objectMapper;

    @Autowired
    public FilterUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void checkCid(MyHttpServletRequestWrapper request) {
        try {
            String json = new String(request.getAllByteInRequest());
            if (!(StringUtils.hasText(json))) {
                return;
            }
            Map<String, Object> map = this.objectMapper.readValue(json, new TypeReference<>() {
            });
            if (Optional.ofNullable(map.get("trace")).isEmpty()) {
                throw new Exception("trace is required");
            }
            @SuppressWarnings("unchecked") Map<String, Object> trace = (Map<String, Object>) map.get("trace");
            String cid = String.valueOf(trace.get("cid"));
            if (!(StringUtils.hasText(cid)) || cid.equalsIgnoreCase("null")) {
                throw new Exception("trace.cid is required");
            }
            trace.put("sid", MDC.get("traceId"));
            MDC.put("cid", cid);
            map.put("trace", trace);
            String decryptedPayload = objectMapper.writeValueAsString(map);
            ByteArrayOutputStream baoData = new ByteArrayOutputStream(1024);
            baoData.write(decryptedPayload.getBytes());
            request.setByteArrayOutputStream(baoData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addCorsHeader(ServletResponse servletResponse) {
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Methods", "GET,POST,PATCH,PUT,DELETE,OPTIONS");
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Max-Age", "3600");
        //((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,Content-Length,X-Requested-With,credential,X-XSRF-TOKEN,Access-Control-Allow-Headers: Origin,Content-Type,X-Auth-Token");
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,Authorization");
    }

    public void returnErrorResponse(ServletResponse servletResponse, Throwable e) {
        try {
            LOG.error("\n" + ExceptionStackTraceUtil.getStackTrace(e));
            String message;
            Optional<Throwable> t = Optional.ofNullable(e.getCause());
            if (t.isEmpty()) {
                message = StringUtils.hasText(e.getMessage()) ? e.getMessage() : "Unknown error";
            } else {
                message = StringUtils.hasText(t.get().getMessage()) ? t.get().getMessage() : "Unknown error";
            }
            servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ((HttpServletResponse) servletResponse).setStatus(200);
            objectMapper.writeValue(
                    servletResponse.getOutputStream(),
                    ResponseType.builder().code(ErrorCodeEnum.FAILURE.getCode()).message(message).response(null).build()
            );
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
