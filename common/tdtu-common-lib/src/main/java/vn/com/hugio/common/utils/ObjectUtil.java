package vn.com.hugio.common.utils;


import vn.com.hugio.common.exceptions.InternalServiceException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectUtil {

    public static <T> T mergeObjects(T first, T second, boolean nullReplace) {
        if (!first.getClass().equals(second.getClass())) {
            throw new InternalServiceException(-1, "Object must be equal");
        }
        // get class of 'first'
        Class<?> clas = first.getClass();
        // get all fields (properties/attribute) of 'clas'
        List<Field> fields = new ArrayList<>(Arrays.asList(clas.getDeclaredFields()));
        // check if class has super class, then add all super field
        if (clas.getSuperclass() != null) {
            List<Field> superClassField = Arrays.asList(clas.getSuperclass().getDeclaredFields());
            fields.addAll(superClassField);
        }
        Object result = null;
        try {
            // put all fields of 'clas' to 'result'
            result = clas.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                // get field value of 'first'
                Object value1 = field.get(first);
                // get field value of 'second'
                Object value2 = field.get(second);
                Object value;
                if (nullReplace) {
                    value = (value1 != null) ? value1 : value2;
                } else {
                    value = (value2 != null) ? value2 : value1;
                }
                field.set(result, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) result;
    }

    public static <SOURCE, TARGET> TARGET mapObjects(SOURCE source, Class<TARGET> target) {
        // Get all field of source class and target class
        List<Field> sourceClassFields = new ArrayList<>(Arrays.asList(source.getClass().getDeclaredFields()));
        List<Field> targetClassFields = new ArrayList<>(Arrays.asList(target.getDeclaredFields()));
        // Get all field of super class of both
        if (source.getClass().getSuperclass() != null) {
            List<Field> superClassField = Arrays.asList(source.getClass().getSuperclass().getDeclaredFields());
            sourceClassFields.addAll(superClassField);
        }
        if (target.getSuperclass() != null) {
            List<Field> superClassField = Arrays.asList(target.getSuperclass().getDeclaredFields());
            targetClassFields.addAll(superClassField);
        }
        // Filter: only get fields in source that its name is included in target
        List<String> targetClassFieldNames = targetClassFields.stream().map(Field::getName).collect(Collectors.toList());
        sourceClassFields = sourceClassFields
                .stream()
                .filter(field -> targetClassFieldNames.contains(field.getName()))
                .collect(Collectors.toList());
        targetClassFields.sort(Comparator.comparing(Field::getName));
        sourceClassFields.sort(Comparator.comparing(Field::getName));
        TARGET result;
        try {
            result = target.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
        // Start mapping
        // Lưu ý: khi dùng field.set(ObjectToSet, valueToSetToFeild) thì ObjectToSet phải là Object mà field đó thuộc về, nó hoạt động giống như setter,
        // tương tự với value = field.get(ObjectToGet), nó hoạt động giống như getter (phải dùng hàm field.setAccessible(true) trước)
        sourceClassFields.forEach(field -> {
            field.setAccessible(true);
            Object value;
            try {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    value = field.get(source);
                    if (value != null) {
                        Field fieldToSet = targetClassFields.stream().filter(field1 -> field1.getName().equals(field.getName())).collect(Collectors.toList()).get(0);
                        fieldToSet.setAccessible(true);
                        fieldToSet.set(result, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public static <SOURCE, TARGET> TARGET mapObjectsDetail(SOURCE source, Class<TARGET> target) {
        // Get all field of source class and target class
        List<Field> sourceClassFields = new ArrayList<>(Arrays.asList(source.getClass().getDeclaredFields()));
        List<Field> targetClassFields = new ArrayList<>(Arrays.asList(target.getDeclaredFields()));
        // Get all field of super class of both
        if (source.getClass().getSuperclass() != null) {
            List<Field> superClassField = Arrays.asList(source.getClass().getSuperclass().getDeclaredFields());
            sourceClassFields.addAll(superClassField);
        }
        if (target.getSuperclass() != null) {
            List<Field> superClassField = Arrays.asList(target.getSuperclass().getDeclaredFields());
            targetClassFields.addAll(superClassField);
        }
        // Filter: only get fields in source that its name is included in target
        List<String> targetClassFieldNames = targetClassFields.stream().map(Field::getName).collect(Collectors.toList());
        sourceClassFields = sourceClassFields
                .stream()
                .filter(field -> targetClassFieldNames.contains(field.getName()))
                .collect(Collectors.toList());
        targetClassFields.sort(Comparator.comparing(Field::getName));
        sourceClassFields.sort(Comparator.comparing(Field::getName));
        TARGET result;
        try {
            result = target.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
        sourceClassFields.forEach(field -> {
            field.setAccessible(true);
            Object value;
            try {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    Field fieldToSet = targetClassFields.stream().filter(field1 -> field1.getName().equals(field.getName())).collect(Collectors.toList()).get(0);
                    value = field.get(source);
                    if (value != null) {
                        if (
                                (
                                        !value.getClass().getName().contains("java.")
                                                && !value.getClass().getName().contains("org.")
                                )
                                        || value instanceof Iterable<?>
                        ) {
                            if (field.get(source) instanceof Iterable<?> valueArr) {
                                List<Object> valueElement = new ArrayList<>();
                                for (Object o : valueArr) {
                                    Class<?> listType = (Class<?>) ((ParameterizedType) fieldToSet.getGenericType()).getActualTypeArguments()[0];
                                    valueElement.add(mapObjects(o, listType));
                                }
                                value = valueElement;
                            } else {
                                value = mapObjects(field.get(source), fieldToSet.getType());
                            }
                        }
                        fieldToSet.setAccessible(true);
                        fieldToSet.set(result, fieldToSet.getType().cast(value));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public static <O> O createEmptyField(O source) {
        List<Field> sourceClassFields = new ArrayList<>(Arrays.asList(source.getClass().getDeclaredFields()));
        if (source.getClass().getSuperclass() != null) {
            List<Field> superClassField = Arrays.asList(source.getClass().getSuperclass().getDeclaredFields());
            sourceClassFields.addAll(superClassField);
        }
        for (Field field : sourceClassFields) {
            field.setAccessible(true);
            try {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                    if (
                            field.getType().getName().contains("vn.com")
                                    && !(field.getType().isEnum())
                    ) {
                        if (source.getClass().isAssignableFrom(field.getType())) {
                            continue;
                        }
                        if (field.get(source) == null) {
                            Object object = createEmptyField(field.getType().getDeclaredConstructor().newInstance());
                            field.set(source, object);
                        } else {
                            Object object = createEmptyField(field.get(source));
                            field.set(source, object);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return source;
    }

    public static boolean isNullOrEmpty(Object object) {
        if (Optional.ofNullable(object).isEmpty()) {
            return true;
        }
        if (object instanceof Iterable) {
            Collection<?> objectChecking = (Collection<?>) object;
            return objectChecking.isEmpty();
        }
        if (object instanceof String objectChecking) {
            return objectChecking.trim().isEmpty();
        }
        return false;
    }

    public static boolean isNotNullAndNotEmpty(Object object) {
        return !(isNullOrEmpty(object));
    }


}
