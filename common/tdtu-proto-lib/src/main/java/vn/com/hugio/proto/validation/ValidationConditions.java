package vn.com.hugio.proto.validation;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessageV3;

import java.util.Map;

public class ValidationConditions {
    public static void checkRule(boolean b, GeneratedMessageV3 protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue,
                                 Map.Entry<FieldDescriptor, Object> extensionValue) throws MessageValidationException {
        if (!b) {
            throw new MessageValidationException(protoMessage, fieldDescriptor, fieldValue, extensionValue);
        }
    }
}
