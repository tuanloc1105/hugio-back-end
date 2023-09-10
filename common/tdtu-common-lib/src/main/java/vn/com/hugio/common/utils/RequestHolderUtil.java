package vn.com.hugio.common.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vn.com.hugio.common.object.wrapper.MyHttpServletRequestWrapper;

import java.util.Enumeration;

import static vn.com.hugio.common.utils.LoggingUtil.maskValue;


public class RequestHolderUtil {

    public static HttpSession session() {
        ServletRequestAttributes attr = RequestHolderUtil.getServletRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public static String generateCurl() {
        ServletRequestAttributes attr = RequestHolderUtil.getServletRequestAttributes();
        MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(attr.getRequest());
        StringBuilder result = new StringBuilder();

        result.append("curl --location --request ");

        // output method
        result.append(requestWrapper.getMethod()).append(" ");

        // output url
        result.append("\"")
                .append(requestWrapper.getRequestURL().toString())
                .append("\"");

        // output headers
        for (Enumeration<String> headerNames = requestWrapper.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String headerName = (String) headerNames.nextElement();
            result
                    .append(" -H \"")
                    .append(headerName).append(": ")
                    .append(requestWrapper.getHeader(headerName))
                    .append("\"");
        }

        // output parameters
        for (Enumeration<String> parameterNames = requestWrapper.getParameterNames(); parameterNames.hasMoreElements(); ) {
            String parameterName = (String) parameterNames.nextElement();
            result.append(" -d \"")
                    .append(parameterName)
                    .append("=")
                    .append(requestWrapper.getParameter(parameterName))
                    .append("\"");
        }

        // output body
        if (RequestMethod.POST.name().equalsIgnoreCase(requestWrapper.getMethod())) {
            String body = maskValue(new String(requestWrapper.getAllByteInRequest()));
            if (body.length() > 0) {
                result
                        .append(" -d \'")
                        .append(body)
                        .append("\'");
            }
        }

        return result.toString();

    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

}
