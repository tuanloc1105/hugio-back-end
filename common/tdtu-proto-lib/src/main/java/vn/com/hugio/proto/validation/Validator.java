package vn.com.hugio.proto.validation;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;

import java.util.Map.Entry;

public interface Validator {

    void validate(GeneratedMessage protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue, Entry<FieldDescriptor, Object> extensionValue)
            throws MessageValidationException;
}
