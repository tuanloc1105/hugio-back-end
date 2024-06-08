package vn.com.hugio.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.wrapper.MyHttpServletRequestWrapper;
import vn.com.hugio.common.object.wrapper.MyHttpServletResponseWrapper;
import vn.com.hugio.common.utils.FilterUtil;

import java.io.IOException;

import static vn.com.hugio.common.utils.LoggingUtil.beautifyJson;
import static vn.com.hugio.common.utils.LoggingUtil.maskValue;

@Component
@Order(1)
public class LoggingFilter extends GenericFilterBean {

    private static final String MESSAGE_NOT_WRITE_HARDCODE = "{\"message\": \"MESSAGE NOT WRITE DUE TO MESSAGE LENGTH IS EXCEED LIMIT\"}";

    private final FilterUtil filterUtil;

    @Autowired
    public LoggingFilter(FilterUtil filterUtil) {
        this.filterUtil = filterUtil;
    }

    public static boolean isJson(String input) {
        try {
            new JSONObject(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        MyHttpServletRequestWrapper request = new MyHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        MyHttpServletResponseWrapper response = new MyHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        /*
         * this.filterUtil.addCorsHeader(servletResponse);
         * try {
         *     if (!(request.getRequestURI().contains("swagger") || request.getRequestURI().contains("api-docs"))) {
         *         this.checkCid(request);
         *     }
         * } catch (Exception e) {
         *     this.returnErrorResponse(servletResponse, e);
         *     return;
         * }
         * String reqId = UUID.randomUUID().toString().replace("-", Strings.EMPTY);
         * String requestString = new String(request.getAllByteInRequest());
         * MDC.put("traceId", reqId);
         * MDC.put("X-B3-TraceId", reqId);
         * String operatorName = request.getRequestURI();
         */
        if (!(StringUtils.hasText(MDC.get("username")))) {
            MDC.put("username", "anonymous");
        }
        MDC.put("httpMethod", request.getMethod());
        try {
            if (!(request.getRequestURI().contains("swagger") || request.getRequestURI().contains("api-docs"))) {
                writeRequestLog(request);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            this.filterUtil.returnErrorResponse(servletResponse, e);
            return;
        }
        if (!(request.getRequestURI().contains("swagger") || request.getRequestURI().contains("api-docs"))) {
            writeResponseLog(response);
        }
        servletResponse.getOutputStream().write(response.getAllByteInResponse());
    }

    private void writeRequestLog(MyHttpServletRequestWrapper request) {
        String message = maskValue(new String(request.getAllByteInRequest()));
        if (StringUtils.hasText(message)) {
            try {
                message = beautifyJson(message);
            } catch (JsonProcessingException e) {
                LOG.warn("BEAUTIFY JSON FAIL [{}]. LOG JSON IN NORMAL FORMAT", e.getMessage());
            }
        }
        if (message.length() > 65535) {
            message = MESSAGE_NOT_WRITE_HARDCODE;
        }
        MDC.put("http", request.getMethod());
        MDC.put("url", request.getRequestURI());
        LOG.info(message);
    }

    private void writeResponseLog(MyHttpServletResponseWrapper response) {
        String message = maskValue(new String(response.getAllByteInResponse()));
        if (StringUtils.hasText(message)) {
            try {
                message = beautifyJson(message);
            } catch (JsonProcessingException e) {
                LOG.warn("BEAUTIFY JSON FAIL [{}]. LOG JSON IN NORMAL FORMAT", e.getMessage());
            }
        }
        if (message.length() > 65535) {
            message = MESSAGE_NOT_WRITE_HARDCODE;
        }
        LOG.info(message);
    }


}
