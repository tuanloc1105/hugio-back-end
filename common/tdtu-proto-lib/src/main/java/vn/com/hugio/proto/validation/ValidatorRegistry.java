package vn.com.hugio.proto.validation;

import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;
import vn.com.hugio.grpc.validation.Validation;
import vn.com.hugio.proto.validation.validators.*;

import java.util.Map;

/**
 * @author Serious
 * @author seime
 */
public class ValidatorRegistry {

    private static final ValidatorRegistry globalValidatorRegistry = createDefaultRegistry();

    private final Map<Descriptors.FieldDescriptor, Validator> validatorMap;

    /**
     * Constructor which builds a {@link ValidatorRegistry} with predefined validators from the validatorMap.
     */
    public ValidatorRegistry(Map<Descriptors.FieldDescriptor, Validator> validatorMap) {
        this.validatorMap = validatorMap;
    }

    /**
     * The default constructor which builds a empty {@link ValidatorRegistry}.
     */
    public ValidatorRegistry() {
        this(Maps.newConcurrentMap());
    }

    /**
     * @return The globally shared {@link ValidatorRegistry}.
     */
    public static ValidatorRegistry globalValidatorRegistry() {
        return globalValidatorRegistry;
    }

    /**
     * @return A {@link ValidatorRegistry} with all built-in validators.
     */
    public static ValidatorRegistry createDefaultRegistry() {

        ValidatorRegistry validatorRegistry = new ValidatorRegistry();

        Map<Descriptors.FieldDescriptor, Validator> validatorMap = validatorRegistry.validatorMap;
        validatorMap.put(Validation.max.getDescriptor(), new MaxValidator());
        validatorMap.put(Validation.min.getDescriptor(), new MinValidator());
        validatorMap.put(Validation.repeatMax.getDescriptor(), new RepeatMaxValidator());
        validatorMap.put(Validation.repeatMin.getDescriptor(), new RepeatMinValidator());
        validatorMap.put(Validation.future.getDescriptor(), new FutureValidator());
        validatorMap.put(Validation.past.getDescriptor(), new PastValidator());
        validatorMap.put(Validation.regex.getDescriptor(), new RegexValidator());
        validatorMap.put(Validation.required.getDescriptor(), new RequiredValidator());
        validatorMap.put(Validation.forbidden.getDescriptor(), new ForbiddenValidator());

        return validatorRegistry;
    }

    /**
     * @param descriptor The descriptor for which the {@link Validator} should be removed.
     * @return {@link Validator} The validator for a {@link com.google.protobuf.Descriptors.FieldDescriptor} or
     * null if none is registered.
     */
    public Validator getValidator(Descriptors.FieldDescriptor descriptor) {
        return validatorMap.get(descriptor);
    }

    /**
     * Adds a {@link Validator} for a {@link com.google.protobuf.Descriptors.FieldDescriptor}.
     * If there is already a {@link Validator} for a {@link com.google.protobuf.Descriptors.FieldDescriptor}
     * the previous {@link Validator} will be overridden.
     *
     * @param fieldDescriptor The {@link com.google.protobuf.Descriptors.FieldDescriptor} to add
     * @param validator       The {@link Validator} to add in context with the fieldDescriptor
     */
    public void addValidator(Descriptors.FieldDescriptor fieldDescriptor, Validator validator) {
        validatorMap.put(fieldDescriptor, validator);
    }

    /**
     * Adds multiple {@link Validator}s in context to their {@link com.google.protobuf.Descriptors.FieldDescriptor}.
     * If there is already a {@link Validator} for a {@link com.google.protobuf.Descriptors.FieldDescriptor}
     * the previous {@link Validator} will be overridden.
     *
     * @param validators The {@link com.google.protobuf.Descriptors.FieldDescriptor} to add
     */
    public void addValidators(Map<Descriptors.FieldDescriptor, Validator> validators) {
        validatorMap.putAll(validators);
    }

    /**
     * @param fieldDescriptor The {@link com.google.protobuf.Descriptors.FieldDescriptor}
     *                        for which the {@link Validator} should be removed.
     */
    public void removeValidator(Descriptors.FieldDescriptor fieldDescriptor) {
        validatorMap.remove(fieldDescriptor);
    }

    /**
     * @param fieldDescriptors The {@link com.google.protobuf.Descriptors.FieldDescriptor}s
     *                         for which their {@link Validator}s should be removed.
     */
    public void removeValidator(Iterable<Descriptors.FieldDescriptor> fieldDescriptors) {
        for (Descriptors.FieldDescriptor fieldDescriptor : fieldDescriptors) {
            validatorMap.remove(fieldDescriptor);
        }
    }
}
