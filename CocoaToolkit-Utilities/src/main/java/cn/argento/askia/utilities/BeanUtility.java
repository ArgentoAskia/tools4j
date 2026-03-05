package cn.argento.askia.utilities;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * java.bean.*封装, 提供JavaBean属性复制, JavaBean序列化等功能, propertiesManager等功能
 */
public final class BeanUtility {

    // 获取JavaBean property的值！
    public static <V> V getProperty(Object bean, String propertyName, Class<V> valueClass){
        final Object propertyValue = getProperty(bean, propertyName);
        return valueClass.cast(propertyValue);
    }

    // 获取JavaBean的值，
    public static Object getProperty(Object bean, String propertyName){
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getPropertyOrDefault(Object bean, String propertyName, Object defaultValue){
        return null;
    }

    // todo how to judge if it's JavaBean
    public static boolean isJavaBean(Class<?> beanClass){
        //
        return false;
    }


    public static <ST, DT> boolean copyFieldProperty(Object src, String srcPropertyName,
                                                Object dest, String destPropertyName,
                                                Function<ST, DT> typeAdapterFunction) {
        try {
            final Field srcField = getJavaBeanProperty(src, srcPropertyName);
            final Field descField = getJavaBeanProperty(dest, destPropertyName);
            // copy from property to property
            srcField.setAccessible(true);
            descField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final ST st = (ST) srcField.get(src);
            final DT dt = typeAdapterFunction.apply(st);
            descField.set(dest, dt);
            return true;
        }catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <ST, DT> boolean copyFieldsProperties(Object src, Objects dest, BiFunction<Class<?>, Class<?>, Function<ST, DT>> mapper) throws IllegalAccessException {
        final Field[] srcFields = src.getClass().getDeclaredFields();
        final Field[] destFields = dest.getClass().getDeclaredFields();
        final Map<String, Field> srcFieldsMap = Stream.of(srcFields).collect(Collectors.toMap(Field::getName, field -> field));
        final Map<String, Field> destFieldsMap = Stream.of(destFields).collect(Collectors.toMap(Field::getName, field -> field));
        // 参数相同且长度相同
        final Set<String> srcFieldsKeys = srcFieldsMap.keySet();
        if (srcFieldsKeys.containsAll(destFieldsMap.keySet()) && srcFields.length == destFields.length){
            for (String key : srcFieldsKeys){
                final Field srcField = srcFieldsMap.get(key);
                final Field destField = destFieldsMap.get(key);
                final Function<ST, DT> typeExchangeFunction = mapper.apply(srcField.getType(), destField.getType());
                srcField.setAccessible(true);
                destField.setAccessible(true);
                @SuppressWarnings("unchecked")
                final ST st = (ST) srcField.get(src);
                final DT dt = typeExchangeFunction.apply(st);
                destField.set(dest, dt);
            }
            return true;
        }
        return false;
    }


    // todo
    public static <ST, DT> boolean copyProperty(Object src, String srcPropertyName, Class<ST> srcPropertyClass,
                                       Object dest, String destPropertyName, Class<DT> destPropertyClass,
                                       Function<ST, DT> typeAdapterFunction) throws IllegalAccessException, InvocationTargetException {
        try {
            final Field srcField = getJavaBeanProperty(src, srcPropertyName);
            try{
                final Field descField = getJavaBeanProperty(dest, destPropertyName);
                // copy from property to property
                srcField.setAccessible(true);
                descField.setAccessible(true);
                if (!srcPropertyClass.isAssignableFrom(srcField.getType())){
                    throw new IllegalAccessException("srcProperties和提供的类型不匹配");
                }
                if (!destPropertyClass.isAssignableFrom(descField.getType())){
                    throw new IllegalAccessException("descProperties和提供的类型不匹配");
                }
                final ST st = srcPropertyClass.cast(srcField.get(src));
                final DT dt = typeAdapterFunction.apply(st);
                descField.set(dest, dt);
                return true;
            }catch (NoSuchFieldException e){
                try {
                    final Method descSetter = getJavaBeanSetter(dest, destPropertyName);
                    srcField.setAccessible(true);
                    descSetter.setAccessible(true);
                    if (!srcPropertyClass.isAssignableFrom(srcField.getType())){
                        throw new IllegalAccessException("srcProperties和提供的类型不匹配");
                    }
                    if (!destPropertyClass.isAssignableFrom(descSetter.getParameterTypes()[0])){
                        throw new IllegalAccessException("descProperties和提供的类型不匹配");
                    }
                    final ST st = srcPropertyClass.cast(srcField.get(src));
                    final DT dt = typeAdapterFunction.apply(st);
                    descSetter.invoke(dest, dt);
                    return true;
                } catch (NoSuchMethodException noSuchMethodException) {
                    // todo BeanPropertyCopyFailedException
                    throw new RuntimeException();
                }
            }

        } catch (NoSuchFieldException e) {
            // 字段不存在，则找方法
            try {
                final Method srcGetter = getJavaBeanGetter(src, srcPropertyName);
                srcGetter.setAccessible(true);
                final Object srcResult = srcGetter.invoke(null);
                if (!srcPropertyClass.isAssignableFrom(srcResult.getClass())){
                    throw new IllegalAccessException("srcProperties和提供的类型不匹配");
                }

            } catch (NoSuchMethodException noSuchMethodException) {
                // todo BeanPropertyCopyFailedException
                throw new RuntimeException();
            }
        }
        return false;

    }
    private static Field getJavaBeanProperty(Object obj, String propertyName) throws NoSuchFieldException {
        try {
            final Field field = obj.getClass().getField(propertyName);
            return field;
        } catch (NoSuchFieldException e) {
            // OK, so we begin to find private Field
            final Field declaredField = obj.getClass().getDeclaredField(propertyName);
            return declaredField;
        }
    }
    private static Method getJavaBeanGetter(Object obj, String propertyName) throws NoSuchMethodException {
        String methodName = "get" + StringUtilities.upperFirstLetter(propertyName);
        String booleanMethodName = "is" + StringUtilities.upperFirstLetter(propertyName);
        try {
            return obj.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            // maybe is boolean?
            try {
                return obj.getClass().getMethod(booleanMethodName);
            } catch (NoSuchMethodException noSuchMethodException) {
                // maybe is private
                try {
                    return obj.getClass().getDeclaredMethod(methodName);
                } catch (NoSuchMethodException suchMethodException) {
                    return obj.getClass().getDeclaredMethod(booleanMethodName);
                }
            }
        }
    }
    private static Method getJavaBeanSetter(Object obj, String propertyName) throws NoSuchMethodException {
        String methodName = "set" + StringUtilities.upperFirstLetter(propertyName);
        try {
            final Method method = obj.getClass().getMethod(methodName);
            // 只有一个参数，那就是了！
            if (method.getParameterCount() == 1){
                return method;
            }
            // 否则直接继续抛出异常并立刻捕获
            throw new NoSuchMethodException(method.toString());
        } catch (NoSuchMethodException e) {
            // maybe is private?
            final Method declaredMethod = obj.getClass().getDeclaredMethod(methodName);
            if (declaredMethod.getParameterCount() == 1){
                return declaredMethod;
            }
            final NoSuchMethodException noSuchMethodException = new NoSuchMethodException(declaredMethod.toString());
            noSuchMethodException.initCause(e);
            throw noSuchMethodException;
        }
    }





}
