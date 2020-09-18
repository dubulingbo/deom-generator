package edu.dublbo.generator.common.utils;

import org.apache.commons.lang3.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author DubLBo
 * @since 2020-09-06 19:55
 * i believe i can i do
 */
public class ObjectUtils implements Serializable {

    private static final Set<String> extraFieldNames = new HashSet<>();

    public ObjectUtils() {}

    public static String field2SetMethodName(String fieldName){
        if(fieldName == null || fieldName.trim().length() == 0){
            return null;
        }
        return "set" + fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1);
    }

    public static String field2GetMethodName(String fieldName){
        if(fieldName == null || fieldName.trim().length() == 0){
            return null;
        }
        return "get" + fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1);
    }

    public static Map<String, Field> getAllFieldMap(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();

        List<Class<?>> allClass = ClassUtils.getAllSuperclasses(clazz);
        allClass.add(clazz);
        Iterator<Class<?>> it = allClass.iterator();

        while (true) {
            Class<?> c;
            do {
                if (!it.hasNext()) {
                    return fieldMap;
                }

                c = it.next();
            } while (Object.class.getName().equals(c.getName())); // 已经到根类 Object（所有对象都会继承Object）

            Field[] declaredFields = c.getDeclaredFields();

            for(Field field : declaredFields){
                if(!extraFieldNames.contains(field.getName())){
                    fieldMap.put(field.getName(), field);
                }
            }
        }
    }

    static{
        extraFieldNames.add("serialVersionUID");
        extraFieldNames.add("createdTime");
        extraFieldNames.add("updatedTime");
        extraFieldNames.add("updatedMan");
        extraFieldNames.add("createdMan");
    }
}
