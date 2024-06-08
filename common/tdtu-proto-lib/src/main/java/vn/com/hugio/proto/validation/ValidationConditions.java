package vn.com.hugio.proto.validation;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

import java.util.Map;

public class ValidationConditions {
    public static void checkRule(boolean b, GeneratedMessage protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue,
                                 Map.Entry<FieldDescriptor, Object> extensionValue) throws MessageValidationException {
        if (!b) {
            throw new MessageValidationException(protoMessage, fieldDescriptor, fieldValue, extensionValue);
        }
    }
}
