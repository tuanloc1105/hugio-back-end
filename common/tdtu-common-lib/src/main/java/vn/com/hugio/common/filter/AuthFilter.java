//package vn.com.hugio.common.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.logging.log4j.util.Strings;
//import org.slf4j.MDC;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//import vn.com.hugio.common.exceptions.ErrorCodeEnum;
//import vn.com.hugio.common.object.ResponseType;
//import vn.com.hugio.common.untils.HttpUtil;
//
//import java.io.IOException;
//import java.io.Serial;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//@Order(2)
//public class AuthFilter extends GenericFilterBean {
//
//    //private final WhiteListApiProperty whiteListApiProperty;
//    private final ObjectMapper objectMapper;
//    private final HttpUtil httpUtil;
//    @Value("${server.servlet.context-path:}")
//    private String contextPath;
//    @Value("${security.auth.host:}")
//    private String authHost;
//    @Value("${api.whitelist.api-list:}")
//    private List<String> whiteListApiProperty;
//    @Value("${security.enable:true}")
//    private Boolean securityEnable;
//
//    @Autowired
//    public AuthFilter(ObjectMapper objectMapper,
//                      HttpUtil httpUtil) {
//        this.objectMapper = objectMapper;
//        this.httpUtil = httpUtil;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        String token = ((HttpServletRequest) request).getHeader("Authorization");
//        Authentication authentication = null;
//
//        if (
//                !(this.whiteListApiProperty.contains(((HttpServletRequest) request).getRequestURI().replace(this.contextPath, Strings.EMPTY)))
//                        && this.securityEnable
//        ) {
//            try {
//                if (StringUtils.hasText(token)) {
//                    ResponseType<AuthenResponse> authResp = this.auth(token);
//                    if (Optional.ofNullable(authResp).isPresent()) {
//                        if (!(authResp.getCodeNumber().equals(ErrorCodeEnum.SUCCESS.getCode()))) {
//                            throw new Exception(
//                                    String.format("Fail to authorize user: : %s", authResp.getMessage())
//                            );
//                        }
//                        if (authResp.getResponse().getActive()) {
//                            authentication = new UsernamePasswordAuthenticationToken(authResp.getResponse().getId(), authResp.getResponse(), new ArrayList<>());
//                            MDC.put("username", (String) authentication.getPrincipal());
//                        } else {
//                            throw new Exception("User is not active");
//                        }
//                    }
//                }
//                if (Optional.ofNullable(authentication).isEmpty()) {
//                    throw new Exception("Unauthorized");
//                }
//            } catch (Throwable e) {
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                ((HttpServletResponse) response).setStatus(401);
//                objectMapper.writeValue(
//                        response.getOutputStream(),
//                        ResponseType.builder().code(ErrorCodeEnum.AUTH_FAILURE.getCode()).message(e.getMessage()).response(null).build()
//                );
//                return;
//
//            }
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private ResponseType<AuthenResponse> auth(String token) {
//        try {
//            return httpUtil.callApi(
//                    null,
//                    this.authHost,
//                    HttpMethod.POST,
//                    new HashMap<>() {
//                        @Serial
//                        private static final long serialVersionUID = -8179509069259154183L;
//
//                        {
//                            put("Content-Type", "application/json");
//                            put("Accept", "*/*");
//                            put("Authorization", token);
//                        }
//                    },
//                    new ParameterizedTypeReference<ResponseType<AuthenResponse>>() {
//                    },
//                    false
//            ).getBody();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//}
