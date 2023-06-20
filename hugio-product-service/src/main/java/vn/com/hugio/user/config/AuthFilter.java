package vn.com.hugio.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.user.service.client.AuthServiceGrpcClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Order(0)
public class AuthFilter extends GenericFilterBean {

    private final ObjectMapper objectMapper;
    private final AuthServiceGrpcClient authServiceGrpcClient;

    @Value("${api.whitelist.api-list:}")
    private List<String> whiteListApiProperty;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired
    public AuthFilter(ObjectMapper objectMapper,
                      AuthServiceGrpcClient authServiceGrpcClient) {
        this.objectMapper = objectMapper;
        this.authServiceGrpcClient = authServiceGrpcClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader("Authorization");
        Authentication authentication = null;
        MDC.put("username", "anonymous");

        if (
                !(this.whiteListApiProperty.contains(((HttpServletRequest) request).getRequestURI().replace(this.contextPath, Strings.EMPTY)))
                        && !(((HttpServletRequest) request).getMethod().equals(HttpMethod.GET.name()))
        ) {
            try {
                if (StringUtils.hasText(token)) {
                    AuthenResponse authResp = this.auth(token);
                    if (Optional.ofNullable(authResp).isPresent()) {
                        authentication = new UsernamePasswordAuthenticationToken(authResp.getUsername(), authResp, new ArrayList<>());
                        MDC.put("username", (String) authentication.getPrincipal());
                    }
                }
                if (Optional.ofNullable(authentication).isEmpty()) {
                    throw new Exception("Unauthorized");
                }
            } catch (Throwable e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ((HttpServletResponse) response).setStatus(401);
                objectMapper.writeValue(
                        response.getOutputStream(),
                        ResponseType.builder().code(ErrorCodeEnum.AUTH_FAILURE.getCode()).message(e.getMessage()).response(null).build()
                );
                return;

            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    private AuthenResponse auth(String token) {
        return this.authServiceGrpcClient.auth(token);
    }
}
