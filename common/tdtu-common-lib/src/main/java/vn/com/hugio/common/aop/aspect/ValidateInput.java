package vn.com.hugio.common.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.aop.Validate;

@Aspect
@Component
public class ValidateInput {

    @Around("execution(public * vn.com..*.controller.*.*(..))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Validate.validateInput(joinPoint);
        } catch (Throwable e) {
            LOG.exception(e);
            throw e;
        }
        return joinPoint.proceed();
    }

}
