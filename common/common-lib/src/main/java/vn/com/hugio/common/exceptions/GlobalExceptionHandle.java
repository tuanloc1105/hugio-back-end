//package vn.com.hugio.common.exceptions;
//
//import jakarta.validation.ConstraintViolationException;
//import lombok.RequiredArgsConstructor;
//import org.apache.logging.log4j.util.Strings;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import vn.com.hugio.common.object.ResponseType;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestControllerAdvice
//@RequiredArgsConstructor
//public class GlobalExceptionHandle {
//
//    @ExceptionHandler({Exception.class})
//    public ResponseType<?> handleAll(Exception ex) {
//        return ResponseType.failed(ex.getMessage());
//    }
//
//    @ExceptionHandler({MethodArgumentNotValidException.class})
//    public ResponseType<?> handleArgumentException(MethodArgumentNotValidException ex) {
//        List<String> errors = new ArrayList<>();
//        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
//            String defaultMessage = fieldError.getDefaultMessage();
//            errors.add(defaultMessage);
//        }
//        return ResponseType.failed(Strings.join(errors, '\n'));
//    }
//
//    @ExceptionHandler({ConstraintViolationException.class})
//    public ResponseType<?> handleConstraintException(ConstraintViolationException ex) {
//        return ResponseType.failed(ex.getMessage());
//    }
//}
