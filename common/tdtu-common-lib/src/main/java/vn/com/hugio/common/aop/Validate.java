package vn.com.hugio.common.aop;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.aspectj.lang.JoinPoint;
import vn.com.hugio.common.constant.CommonConstant;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.utils.ObjectUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Validate {

    public static void validateInput(JoinPoint joinPoint) {
        var dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime current = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(CommonConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        LOG.info("DO VALIDATE INPUT VALUE BEFORE PROCEED {}.{} AT {}", joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName(), dateTimeFormatter.format(current));
        var args = new ArrayList<>(List.of(joinPoint.getArgs()));
        var emptyRequiredField = new ArrayList<String>();
        if (!args.isEmpty()) {
            Optional<Object> optionalObject = args.stream().filter(o -> o.getClass().getName().contains("vn.com")).findAny();
            if (optionalObject.isPresent()) {
                var object = optionalObject.get();
                if (!(object instanceof RequestType)) {
                    throw new InternalServiceException(400, "Unknown request");
                }
                RequestType<?> requestType = (RequestType<?>) object;
                if (Optional.ofNullable(requestType.getRequest()).isEmpty()) {
                    throw new InternalServiceException(400, "Null request");
                }
                Object validateObject = requestType.getRequest();
                emptyRequiredField.addAll(validate(validateObject));
                if (!emptyRequiredField.isEmpty()) {
                    String errMsg = String.format(
                            "%1$s can not be empty",
                            emptyRequiredField.stream().collect(Collectors.joining(", ", "[", "]"))
                    );
                    throw new InternalServiceException(400, errMsg);
                }
            }
        }
    }

    private static List<String> validate(Object validateObject) {
        if (!(validateObject.getClass().getName().contains("vn.com"))) {
            return null;
        }
        var emptyRequiredField = new ArrayList<String>();
        var fields = new ArrayList<>(List.of(validateObject.getClass().getDeclaredFields()));
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(validateObject);

                String fieldName = field.getName();
                JsonProperty annotation = field.getAnnotation(JsonProperty.class);
                if (Optional.ofNullable(annotation).isPresent()) {
                    fieldName = annotation.value();
                }

                FieldNotNull fieldNotNull = field.getAnnotation(FieldNotNull.class);
                FieldGreaterThan fieldGreaterThan = field.getAnnotation(FieldGreaterThan.class);
                FieldLessThan fieldLessThan = field.getAnnotation(FieldLessThan.class);
                FieldValue fieldValuePattern = field.getAnnotation(FieldValue.class);

                if (Optional.ofNullable(fieldNotNull).isPresent()) {
                    if (ObjectUtil.isNullOrEmpty(fieldValue)) {
                        emptyRequiredField.add(fieldName);
                    }
                }
                if (
                        !(field.getType().getName().contains("java.")) &&
                                !(field.getType().getName().contains("org.")) &&
                                !(validateObject.getClass().isAssignableFrom(field.getType()))
                ) {
                    if (Optional.ofNullable(fieldValue).isPresent()) {
                        emptyRequiredField.addAll(validate(fieldValue));
                    }
                    continue;
                }

                if (field.getType().isAssignableFrom(String.class) && Optional.ofNullable(fieldValuePattern).isPresent()) {
                    List<String> patterns = Arrays.asList(fieldValuePattern.value());
                    for (String pattern : patterns) {
                        if (Optional.ofNullable(fieldValue).isPresent() && !fieldValue.equals(pattern)) {
                            throw new InternalServiceException(400, fieldName + "'s value must be has value like this: " + patterns);
                        }
                    }
                }

                if (
                        Optional.ofNullable(fieldValue).isPresent()
                                && (
                                Optional.ofNullable(fieldLessThan).isPresent() || Optional.ofNullable(fieldGreaterThan).isPresent()
                        )
                ) {
                    double conditionNumber;
                    double fieldNumber;
                    if (field.getType().isAssignableFrom(BigDecimal.class)) {
                        fieldNumber = ((BigDecimal) fieldValue).doubleValue();
                    } else {
                        fieldNumber = Double.parseDouble(String.valueOf(fieldValue));
                    }
                    if (Optional.ofNullable(fieldLessThan).isPresent()) {
                        conditionNumber = fieldLessThan.value();
                        if (fieldNumber >= conditionNumber) {
                            throw new InternalServiceException(400, fieldName + "'s value must be less than: " + conditionNumber);
                        }
                    }
                    if (Optional.ofNullable(fieldGreaterThan).isPresent()) {
                        conditionNumber = fieldGreaterThan.value();
                        if (fieldNumber <= conditionNumber) {
                            throw new InternalServiceException(400, fieldName + "'s value must be greater than: " + conditionNumber);
                        }
                    }
                }

            } catch (InternalServiceException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                LOG.exception(e);
            }
        }
        return emptyRequiredField;
    }


}
