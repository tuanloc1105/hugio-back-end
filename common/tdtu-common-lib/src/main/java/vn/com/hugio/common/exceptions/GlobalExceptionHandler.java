package vn.com.hugio.common.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.utils.ObjectUtil;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InternalServiceException.class})
    public ResponseEntity<Object> handleException(InternalServiceException ex, WebRequest request) {
        String message = ObjectUtil.isNullOrEmpty(ex.getMessage()) ? "Null pointer exception" : ex.getMessage();
        LOG.error(
                "[EXCEPTION] %s",
                message
        );
        return ResponseEntity.ok(
                ResponseType.builder()
                        .code(ex.getCode())
                        .message(ex.getMessage())
                        .build()
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String message = ObjectUtil.isNullOrEmpty(ex.getMessage()) ? "Unknown error" : ex.getMessage();
        LOG.error(
                "[EXCEPTION] {}",
                message
        );
        return ResponseEntity.ok(
                ResponseType.builder()
                        .code(statusCode.value())
                        .message(message)
                        .response(null)
                        .build()
        );
    }
}
