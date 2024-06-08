package vn.com.hugio.proto.validation;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

import java.util.Map;

public class MessageValidationException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String fieldName;
    private final Object fieldValue;
    private final String protoName;
    private final Object validationRule;

    public MessageValidationException(GeneratedMessage message, FieldDescriptor fieldDescriptor, Object fieldValue,
                                      Map.Entry<Descriptors.FieldDescriptor, Object> validationRule) {
        super(computeMessage(message, fieldDescriptor, fieldValue, validationRule));
        this.protoName = fieldDescriptor.getFile().getName();
        this.fieldName = fieldDescriptor.getFullName();
        this.fieldValue = fieldValue;
        this.validationRule = validationRule;
    }

    private static String computeMessage(GeneratedMessage message, FieldDescriptor fieldDescriptor, Object fieldValue,
                                         Map.Entry<Descriptors.FieldDescriptor, Object> validationRule) {
        StringBuilder b = new StringBuilder();
        b.append(fieldDescriptor.getJsonName());
        b.append(", rule ");
        String validationRuleString;
        if (validationRule.getValue() == null || "".equals(validationRule.getValue())) {
            validationRuleString = validationRule.getKey().toString();
        } else {
            validationRuleString = validationRule.getKey().getJsonName();
        }

        b.append(validationRuleString);
        if (null != fieldValue && !"".equals(fieldValue)) {
            b.append(", invalid field value is ");
            b.append(fieldValue);
        }
        return b.toString();
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getProtoName() {
        return protoName;
    }

    public Object getValidationRule() {
        return validationRule;
    }

}
