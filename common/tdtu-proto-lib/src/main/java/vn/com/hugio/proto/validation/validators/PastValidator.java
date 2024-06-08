package vn.com.hugio.proto.validation.validators;

/*-
 * #%L
 * Protobuf validator
 * %%
 * Copyright (C) 2017 - 2018 Original authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import vn.com.hugio.proto.validation.MessageValidationException;
import vn.com.hugio.proto.validation.ValidationConditions;
import vn.com.hugio.proto.validation.Validator;

import java.util.Map;

/**
 * @author Serious
 * @author seime
 * @date 2017/6/28
 */
public class PastValidator implements Validator {
    @Override
    public void validate(GeneratedMessage protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue, Map.Entry<FieldDescriptor, Object> rule)
            throws MessageValidationException {
        Boolean extensionValueBoolean = (Boolean) rule.getValue();
        Long fieldValueLong = (Long) fieldValue;
        if (extensionValueBoolean) {
            long now = System.currentTimeMillis();
            ValidationConditions.checkRule(now > fieldValueLong, protoMessage, fieldDescriptor, fieldValue, rule);
        }
    }
}
