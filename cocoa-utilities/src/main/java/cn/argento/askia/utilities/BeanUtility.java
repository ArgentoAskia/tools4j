package cn.argento.askia.utilities;


import java.beans.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * java.bean.*封装, 提供JavaBean属性复制, JavaBean序列化等功能, propertiesManager等功能
 */
public final class BeanUtility {

    private BeanUtility() {}
    // 获取JavaBean property的值！
    public static <V> V getProperty(Object bean, String propertyName, Class<V> valueClass){
        final Object propertyValue = getProperty(bean, propertyName);
        return valueClass.cast(propertyValue);
    }

    // 获取JavaBean的值，
    public static Object getProperty(Object bean, String propertyName, Object... args){
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            final Optional<PropertyDescriptor> property = Arrays.stream(propertyDescriptors).parallel()
                    .filter(propertyDescriptor -> propertyDescriptor.getName().equals(propertyName))
                    .findFirst();
            final PropertyDescriptor propertyDescriptor = property.orElseThrow((Supplier<Throwable>) () -> new IntrospectionException("property: " + propertyName + "not found, create by reflect API"));
            final Method readMethod = propertyDescriptor.getReadMethod();
            return readMethod.invoke(propertyName, args);
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                final Field field = bean.getClass().getField(propertyName);
                field.setAccessible(true);
                return field.get(bean);
            } catch (NoSuchFieldException noSuchFieldException) {
                // private field?
                try {
                    final Field declaredField = bean.getClass().getDeclaredField(propertyName);
                    if (!declaredField.isAccessible()) {
                        declaredField.setAccessible(true);
                    }
                    return declaredField.get(bean);
                } catch (NoSuchFieldException suchFieldException) {
                    // FIXME: getXXX()
                    // 可能这个属性没有实际的属性
                    // 我们还有一种方法获取, 即想办法获取他的getXXX()方法
                    return null;
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                    return null;
                }
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
                return null;
            }
        }
    }

    public static Object getPropertyOrDefault(Object bean, String propertyName, Object defaultValue){
        return null;
    }

    // todo how to judge if it's JavaBean
    public static boolean isJavaBean(Class<?> beanClass, boolean checkEvents){
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        } catch (IntrospectionException e) {
            e.printStackTrace();
            // judge itself
        }


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

    public static <ST, DT> int copyFieldsProperties(Object src, Object dest, BiFunction<Class<?>, Class<?>, Function<ST, DT>> mapper) throws IllegalAccessException {
        final Field[] srcFields = src.getClass().getDeclaredFields();
        final Field[] destFields = dest.getClass().getDeclaredFields();
        final Map<String, Field> srcFieldsMap = Stream.of(srcFields).collect(Collectors.toMap(Field::getName, field -> field));
        final Map<String, Field> destFieldsMap = Stream.of(destFields).collect(Collectors.toMap(Field::getName, field -> field));
        // 参数相同且长度相同
        final Set<String> srcFieldsKeys = srcFieldsMap.keySet();
        Set<String> destFieldsKeys = destFieldsMap.keySet();
        // 交集
        Set<String> intersection = srcFieldsKeys.stream().filter(destFieldsKeys::contains).collect(Collectors.toSet());
        int count = 0;
        for (String key : intersection){
            final Field srcField = srcFieldsMap.get(key);
            final Field destField = destFieldsMap.get(key);
            final Function<ST, DT> typeExchangeFunction = mapper.apply(srcField.getType(), destField.getType());
            srcField.setAccessible(true);
            destField.setAccessible(true);
            @SuppressWarnings("unchecked")
            final ST st = (ST) srcField.get(src);
            if (st == null){
                destField.set(dest, null);
            }
            else{
                final DT dt = typeExchangeFunction.apply(st);
                destField.set(dest, dt);
            }
            count++;
        }
        return count;
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
        String methodName = "get" + StringUtility.upperFirstLetter(propertyName);
        String booleanMethodName = "is" + StringUtility.upperFirstLetter(propertyName);
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
        String methodName = "set" + StringUtility.upperFirstLetter(propertyName);
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

    public static Method[] getJavaBeanAllSetters(Object obj){
        List<Method> methodList = new ArrayList<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method writeMethod = propertyDescriptor.getWriteMethod();
                methodList.add(writeMethod);
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
            // 所以我们就只能靠反射API来获取了
            Method[] declaredMethods = obj.getClass().getDeclaredMethods();
            for (Method m: declaredMethods) {
                if (m.getName().startsWith("set") && m.getParameterCount() == 1){
                    methodList.add(m);
                }
            }
        }
        return methodList.toArray(new Method[0]);
    }


    public static PropertyDescriptor[] getJavaBeanPropertyDescriptors(Object obj){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
            Method[] javaBeanAllSetters = getJavaBeanAllSetters(obj);
            // todo valueOf(Method) to PropertyDescriptors
            return new PropertyDescriptor[javaBeanAllSetters.length];
        }
    }

    public static PropertyDescriptor[] getInheritedJavaBeanPropertyDescriptors(Object obj, Class<?> stopClass){
        try {
            final BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass(), stopClass);
            return beanInfo.getPropertyDescriptors();
        } catch (IntrospectionException e) {
            e.printStackTrace();
            Class<?> cl = obj.getClass();
            List<PropertyDescriptor> propertyDescriptorsList = new ArrayList<>();
            do {
                final Field[] declaredFields = cl.getDeclaredFields();
                for (Field field: declaredFields){
                    try {
                        final Method beanGetter = getJavaBeanGetter(cl, field.getName());
                        final Method beanSetter = getJavaBeanSetter(cl, field.getName());
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), beanGetter, beanSetter);
                        propertyDescriptorsList.add(propertyDescriptor);
                    } catch (NoSuchMethodException | IntrospectionException noSuchMethodException) {
                        // this will not happen!
                    }
                }
                cl = cl.getSuperclass();
            }
            while(cl != stopClass);
            return propertyDescriptorsList.toArray(new PropertyDescriptor[0]);
        }
    }

    public static PropertyDescriptor[] getInheritedJavaBeanPropertyDescriptors(Object obj){
        return getInheritedJavaBeanPropertyDescriptors(obj, Object.class);
    }

    public static int getPropertyCounts(Class<?> cls){
        return getJavaBeanPropertyDescriptors(cls).length;
    }


    private static boolean checkForCollectionType(Class<?> className){
        if (className.isArray()){
            return true;
        }
        if (className == List.class || List.class.isAssignableFrom(className)){
            return true;
        }
        if (className == Set.class || Set.class.isAssignableFrom(className)){
            return true;
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    public static int copyProperties(Object src, Object dest){
        int count = 0;
        Map<String, Object> srcPropertiesMap = new HashMap<>();
        // src的所有属性
        if (!(src instanceof Map)){
            PropertyDescriptor[] srcProperties = getInheritedJavaBeanPropertyDescriptors(src);
            srcPropertiesMap = Arrays.stream(srcProperties).parallel()
                    .collect(Collectors.toMap(FeatureDescriptor::getName, propertyDescriptor -> propertyDescriptor));
        }
        else {
            srcPropertiesMap = (Map<String, Object>) src;
        }
        if (dest instanceof Map){
            Map<String, Object> destMap = (Map<String, Object>) dest;
            destMap.putAll(srcPropertiesMap);
            count += destMap.size();
        }
        else{
            final PropertyDescriptor[] descProperties = getInheritedJavaBeanPropertyDescriptors(dest);
            for (PropertyDescriptor propertyDescriptor : descProperties){
                final String name = propertyDescriptor.getName();
                final Object o = srcPropertiesMap.get(name);
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                try {
                    if (writeMethod.getParameterCount() == 1){
                        // 单独处理null 即便写入了null也无所谓
                        if (o == null){
                            writeMethod.invoke(dest, (Object) null);
                        }
                        else if (writeMethod.getParameterTypes()[0].isAssignableFrom(o.getClass())){
                            final Class<?> parameterType = writeMethod.getParameterTypes()[0];
                            final Object castParameter = parameterType.cast(o);
                            writeMethod.invoke(dest, castParameter);
                            count++;
                        }
                        // 处理基本类型
                        else if (isPrimitiveOrBoxingType(writeMethod.getParameterTypes()[0]) &&
                        isPrimitiveOrBoxingType(o.getClass())){
                            final Object o1 = primitiveChange(o, writeMethod.getParameterTypes()[0]);
                            // 即便写入了null也无所谓
                            final Class<?> parameterType = writeMethod.getParameterTypes()[0];
                            final Object castParameter = parameterType.cast(o1);
                            writeMethod.invoke(dest, castParameter);
                            count++;
                        }
                        // 处理数组、List、Set
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    // 写入失败，忽略这个属性
                }
            }
        }
        // 获取desc的
        return count;
    }
    private static boolean isPrimitiveOrBoxingType(Class<?> cl){
        if (cl == Number.class || Number.class.isAssignableFrom(cl)){
            return true;
        }
        if (cl == Character.class){
            return true;
        }
        if (cl == Boolean.class){
            return true;
        }
        return cl.isPrimitive();
    }
    private static <T1, T2> T2 primitiveChange(T1 primitive, Class<T2> t2Class){
        if (Number.class.isAssignableFrom(primitive.getClass())){
            Number number = (Number) primitive;
            if (t2Class == int.class || t2Class == Integer.class){
                return t2Class.cast(number.intValue());
            }
            if (t2Class == short.class || t2Class == Short.class){
                return t2Class.cast(number.shortValue());
            }
            if (t2Class == long.class || t2Class == Long.class){
                return t2Class.cast(number.longValue());
            }
            if (t2Class == byte.class || t2Class == Byte.class){
                return t2Class.cast(number.byteValue());
            }
            if (t2Class == double.class || t2Class == Double.class){
                return t2Class.cast(number.doubleValue());
            }
            if (t2Class == float.class || t2Class == Float.class){
                return t2Class.cast(number.floatValue());
            }
            if (t2Class == boolean.class || t2Class == Boolean.class){
                return t2Class.cast(number.intValue() != 0);
            }
            if (t2Class == char.class || t2Class == Character.class){
                final int l = number.intValue();
                if (l <= Character.MAX_VALUE && l >= Character.MIN_VALUE){
                    return t2Class.cast(l);
                }
                else{
                    final int i = Math.floorMod(l, Character.MAX_VALUE);
                    return t2Class.cast(i);
                }
            }
            // bigInteger、bigDecimal等
            return t2Class.cast(number);
        }
        else if (Boolean.TYPE == primitive.getClass()){
            if (Number.class.isAssignableFrom(t2Class)) {
                Boolean b = (Boolean) primitive;
                if (b)  return t2Class.cast(1);
                else    return t2Class.cast(0);
            }
            else if (Boolean.class.isAssignableFrom(t2Class)){
                return t2Class.cast(primitive);
            }
            else if(Character.class.isAssignableFrom(t2Class)){
                Boolean b = (Boolean) primitive;
                if (b){
                    return t2Class.cast('1');
                }
                else{
                    return t2Class.cast('0');
                }
            }
        }
        else if (Character.TYPE == primitive.getClass()){
            Character character = (Character) primitive;
            if (Number.class.isAssignableFrom(t2Class)) {
                Number number = Character.getNumericValue(character);
                return t2Class.cast(number);
            }
            else if (Boolean.TYPE.isAssignableFrom(t2Class)){
                return t2Class.cast((character == ' '));
            }
            else if (Character.TYPE.isAssignableFrom(t2Class)){
                return t2Class.cast(character);
            }
        }
        return t2Class.cast(primitive);
    }

    /**
     * 此方法会将一个类上的所有字段获取出来, 放在Map中
     *
     * @param javaBean
     * @return
     */
    public static Map<String ,Object> toMap(Object javaBean, boolean ignoreNull){
        final Class<?> javaBeanClass = javaBean.getClass();
        final Field[] declaredFields = javaBeanClass.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field f :
                declaredFields) {
            final String name = f.getName();
            if (!f.isAccessible()){
                f.setAccessible(true);
            }
            try {
                final Object o = f.get(javaBean);
                if (ignoreNull){
                    if (o != null){
                        map.put(name, o);
                    }
                }
                else{
                    map.put(name, o);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Map<String ,Object> toMap(Object javaBean){
        return toMap(javaBean, true);
    }


    public static Map<String, Field> toFieldMap(Object javaBean){
        final Class<?> javaBeanClass = javaBean.getClass();
        final Field[] declaredFields = javaBeanClass.getDeclaredFields();
        Map<String, Field> map = new HashMap<>();
        for (Field f :
                declaredFields) {
            final String name = f.getName();
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            map.put(name, f);
        }
        return map;
    }
}
