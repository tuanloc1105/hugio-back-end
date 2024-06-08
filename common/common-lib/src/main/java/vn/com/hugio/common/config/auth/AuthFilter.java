package vn.com.hugio.common.config.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import vn.com.hugio.common.annotation.Exclude;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.utils.HttpUtil;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Order(0)
@Exclude
public class AuthFilter extends GenericFilterBean {

    private final ObjectMapper objectMapper;
    private final HttpUtil httpUtil;

    @Value("${api.whitelist.api-list:}")
    private List<String> whiteListApiProperty;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${auth.endpoint}")
    private String authEndpoint;

    @Autowired
    public AuthFilter(ObjectMapper objectMapper,
                      HttpUtil httpUtil) {
        this.objectMapper = objectMapper;
        this.httpUtil = httpUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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

        chain.doFilter(request, response);

    }

    private AuthenResponse auth(String token) {
        try {
            var response = this.httpUtil.callApi(
                    null,
                    authEndpoint,
                    HttpMethod.POST,
                    new HashMap<>() {
                        @Serial
                        private static final long serialVersionUID = -1342466708918905425L;

                        {
                            put("Authorization", token);
                        }
                    },
                    new ParameterizedTypeReference<ResponseType<AuthenResponse>>() {
                    },
                    false
            ).getBody();
            assert response != null;
            if (response.getCodeNumber().intValue() != ErrorCodeEnum.SUCCESS.getCode().intValue()) {
                return null;
            }
            return response.getResponse();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
