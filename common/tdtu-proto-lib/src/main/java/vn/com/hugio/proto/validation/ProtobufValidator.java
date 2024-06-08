package vn.com.hugio.proto.validation;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessageV3;

import java.util.Map;

/**
 * @author Serious
 * @author seime
 */
public class ProtobufValidator {

    private static final ProtobufValidator globalProtobufValidator = new ProtobufValidator();

    private final ValidatorRegistry validatorRegistry;

    /**
     * @param validatorRegistry The {@link ValidatorRegistry} which should be used for validation.
     */
    public ProtobufValidator(ValidatorRegistry validatorRegistry) {
        this.validatorRegistry = validatorRegistry;
    }

    /**
     * The default constructor which uses the global {@link ValidatorRegistry}.
     */
    public ProtobufValidator() {
        this(ValidatorRegistry.globalValidatorRegistry());
    }

    /**
     * @return The globally shared {@link ProtobufValidator}.
     */
    public static ProtobufValidator globalValidator() {
        return globalProtobufValidator;
    }

    /**
     * @return A {@link ProtobufValidator} with a default {@link ValidatorRegistry}.
     */
    public static ProtobufValidator createDefaultValidator() {
        return new ProtobufValidator(ValidatorRegistry.createDefaultRegistry());
    }

    private void doValidate(GeneratedMessageV3 message, FieldDescriptor fieldDescriptor, Object fieldValue, DescriptorProtos.FieldOptions options)
            throws IllegalArgumentException, MessageValidationException {
        for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : options.getAllFields().entrySet()) {
            try {
                validatorRegistry.getValidator(entry.getKey()).validate(message, fieldDescriptor, fieldValue, entry);
            } catch (UnsupportedOperationException e) {
                // Add more info and rethrow
                throw new UnsupportedOperationException("Error validating field " + fieldDescriptor + " with value " + fieldValue + " and rule " + entry + " due to " + e.getMessage(), e);
            }
        }
    }

    /**
     * @param protoMessage The protobuf message object to validate
     * @throws MessageValidationException Further information about the failed field
     */
    public void validate(GeneratedMessageV3 protoMessage) throws MessageValidationException {
        for (Descriptors.FieldDescriptor fieldDescriptor : protoMessage.getDescriptorForType().getFields()) {

            Object fieldValue;
            if (fieldDescriptor.isRepeated()) {
                fieldValue = protoMessage.getField(fieldDescriptor);
            } else {
                fieldValue = protoMessage.hasField(fieldDescriptor) ? protoMessage.getField(fieldDescriptor) : null;
            }

            if (protoMessage.getField(fieldDescriptor) instanceof GeneratedMessageV3) {
                doValidate(protoMessage, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
                if (fieldDescriptor.isRepeated() || protoMessage.hasField(fieldDescriptor)) {
                    GeneratedMessageV3 subMessageV3 = (GeneratedMessageV3) protoMessage.getField(fieldDescriptor);
                    validate(subMessageV3);
                }
            } else if (!fieldDescriptor.getOptions().getAllFields().isEmpty()) {
                doValidate(protoMessage, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
            }
        }
    }

}
