package vn.com.hugio.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.filter.AuthenResponse;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.aop.HasRoles;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class HasRoleCheck {

    @Around("@annotation(vn.com.hugio.common.aop.HasRoles) && execution(public * vn.com..*.controller.*.*(..))")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        var isAccept = false;
        LOG.info("CHECK PERMISSION OF METHOD [%s]", joinPoint.getSignature().getName());
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal().equals("anonymousUser"))) {
            var authInfo = (AuthenResponse) authentication.getCredentials();
            var clas = Class.forName(joinPoint.getSignature().getDeclaringTypeName());
            var methods = new ArrayList<>(List.of(clas.getMethods()));
            var optionalMethod = methods.stream().filter(m -> m.getName().equals(joinPoint.getSignature().getName())).findAny();
            if (optionalMethod.isPresent()) {
                var method = optionalMethod.get();
                HasRoles roles = method.getAnnotation(HasRoles.class);
                var acceptRole = new ArrayList<>(List.of(roles.roles()));
                var userRole = authInfo.getRoles();
                for (String s : userRole) {
                    if (acceptRole.contains(s)) {
                        isAccept = true;
                        break;
                    }
                }
            }
        } else {
            throw new InternalServiceException(ErrorCodeEnum.AUTH_FAILURE.getCode(), "Can not get info from security context");
        }
        if (isAccept) {
            return joinPoint.proceed();
        }
        throw new InternalServiceException(ErrorCodeEnum.AUTH_FAILURE.getCode(), "User does not have permission to access this resource");
    }

}
