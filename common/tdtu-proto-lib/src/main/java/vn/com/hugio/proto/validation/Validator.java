package vn.com.hugio.proto.validation;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessageV3;

import java.util.Map.Entry;

public interface Validator {

    void validate(GeneratedMessageV3 protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue, Entry<FieldDescriptor, Object> extensionValue)
            throws MessageValidationException;
}
