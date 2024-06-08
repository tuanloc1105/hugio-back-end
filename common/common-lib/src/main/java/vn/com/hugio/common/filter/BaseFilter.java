package vn.com.hugio.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import vn.com.hugio.common.object.wrapper.MyHttpServletRequestWrapper;
import vn.com.hugio.common.object.wrapper.MyHttpServletResponseWrapper;
import vn.com.hugio.common.utils.FilterUtil;

import java.io.IOException;

@Component
@Order(-1)
public class BaseFilter extends GenericFilterBean {

    private final FilterUtil filterUtil;

    @Autowired
    public BaseFilter(FilterUtil filterUtil) {
        this.filterUtil = filterUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        MyHttpServletRequestWrapper request = new MyHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        MyHttpServletResponseWrapper response = new MyHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        this.filterUtil.addCorsHeader(servletResponse);
        try {
            if (!(request.getRequestURI().contains("swagger") || request.getRequestURI().contains("api-docs"))) {
                this.filterUtil.checkCid(request);
            }
        } catch (Exception e) {
            this.filterUtil.returnErrorResponse(servletResponse, e);
            return;
        }
        chain.doFilter(request, response);
        servletResponse.getOutputStream().write(response.getAllByteInResponse());
    }
}
