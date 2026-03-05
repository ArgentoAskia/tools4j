package cn.argento.askia.supports.proxy;

import cn.argento.askia.supports.AnnotationKeyValue;
import cn.argento.askia.utilities.AnnotationUtility;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * 手动实现注解的InvocationHandler
 * remake
 */
public class AnnotationInvocationHandler implements InvocationHandler, Serializable {
    private static final long serialVersionUID = -6755470513258282082L;

    protected Class<? extends Annotation> annotationClass;
    protected Map<String, Object> memberValues;
    protected Annotation refAnnotation;

    protected transient volatile Method[] memberMethods;

    private transient final String[] attributeNameCache;

    private void checkAttributeMatchedAndAddDefaultAttributes(){
        final Set<String> set = memberValues.keySet();
        for (String attribute :
                attributeNameCache) {
            try {
                final Object defaultValue = AnnotationUtility.getDefaultValue(annotationClass, attribute);
                // 该属性没有默认值，必须存在于 memberValues
                if (defaultValue == null){
                    if (!set.contains(attribute)){
                        throw new NoSuchMethodError("注解 " + annotationClass.getSimpleName() + "没有" + attribute + "属性");
                    }
                }
                else{
                    // 有默认值,但memberValues中不包含该属性，则需要将该默认值添加到memberValues中
                    if (!set.contains(attribute)){
                        memberValues.put(attribute, defaultValue);
                    }
                }
            } catch (NoSuchMethodException e) {
                // this no happen
            }
        }
    }

    public AnnotationInvocationHandler(Class<? extends Annotation> annotationClass,
                                       Map<String, Object> memberValues){
        this.annotationClass = annotationClass;
        this.memberValues = memberValues;
        memberMethods = annotationClass.getDeclaredMethods();
        attributeNameCache = new String[memberMethods.length];
        // Attribute Cache
        Arrays.stream(memberMethods).forEach(new Consumer<Method>() {
            private int index = 0;
            @Override
            public void accept(Method method) {
                attributeNameCache[index++] = method.getName();
            }
        });
        checkAttributeMatchedAndAddDefaultAttributes();
        // 和 annotationClass 互斥
        refAnnotation = null;
    }
    public AnnotationInvocationHandler(Annotation ref){
        memberValues = new HashMap<>();
        refAnnotation = ref;
        memberMethods = refAnnotation.getClass().getDeclaredMethods();
        attributeNameCache = new String[memberMethods.length];
        // Attribute Cahce
        Arrays.stream(memberMethods).forEach(new Consumer<Method>() {
            private int index = 0;
            @Override
            public void accept(Method method) {
                attributeNameCache[index++] = method.getName();
            }
        });
        annotationClass = null;
    }
    // 判断是否包含某个属性
    private boolean containAttribute(String attributeName){
        for (String attribute :
                attributeNameCache) {
            if (attribute.equals(attributeName)){
                return true;
            }
        }
        return false;
    }


    public AnnotationInvocationHandler add(AnnotationKeyValue<?> annotationKeyValue){
        if (containAttribute(annotationKeyValue.getAttributeName())){
            memberValues.put(annotationKeyValue.getAttributeName(), annotationKeyValue.getValue());
        }
        else{
            throw new IllegalArgumentException("注解：" + (annotationClass != null? annotationClass.getName() : refAnnotation) + ", 没有属性：" + annotationKeyValue.getAttributeName());
        }
        return this;
    }

    public AnnotationInvocationHandler remove(AnnotationKeyValue<?> annotationKeyValue){
        final boolean remove = memberValues.remove(annotationKeyValue.getAttributeName(), annotationKeyValue.getValue());
        if (!remove){
            memberValues.remove(annotationKeyValue.getAttributeName());
        }
        return this;
    }

    private Object getValue(String attribute) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (refAnnotation != null) return memberValues.getOrDefault(attribute, AnnotationUtility.getAnnotationAttributeValue(refAnnotation, attribute));
        else                       return memberValues.get(attribute);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String var4 = method.getName();
        Class<?>[] var5 = method.getParameterTypes();
        if (var4.equals("equals") && var5.length == 1 && var5[0] == Object.class){
            return equalsImpl(args[0]);
        }
        if (var4.equals("hashCode") && var5.length == 0 && method.getReturnType() == int.class){
            return hashCodeImpl();
        }
        // toStringImpl()
        if (var4.equals("toString") && var5.length == 0 && method.getReturnType() == String.class){
            return toStringImpl();
        }

        if (var4.equals("annotationType") && var5.length == 0 && method.getReturnType() == Class.class){
            return annotationTypeImpl();
        }
        beforeValue();
        final Object result = getValue(var4);
        callingValue();
        afterValue();
        return result;
    }


    protected void beforeValue(){

    }

    protected void callingValue(){

    }

    protected void afterValue(){

    }


    private String toStringImpl(){
        StringBuilder toStringBuilder = new StringBuilder(128);
        toStringBuilder.append('@');
        if (annotationClass != null){
            toStringBuilder.append(annotationClass.getName());
        }
        else{
            toStringBuilder.append(refAnnotation.annotationType().getName());
        }
        toStringBuilder.append('(');
        boolean flag = true;
        for (Method attribute :
                memberMethods) {
            if (flag) {
                flag = false;
            }
            else{
                toStringBuilder.append(", ");
            }

            toStringBuilder.append(attribute.getName());
            toStringBuilder.append('=');
            try {
                final Object attributeValue = getValue(attribute.getName());
                toStringBuilder.append(memberValueToString(attributeValue));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        toStringBuilder.append(')');
        return toStringBuilder.toString();
    }
    private static String memberValueToString(Object var){
        final Class<?> varClass = var.getClass();
        if (!varClass.isArray()){
            return var.toString();
        }
        else if (varClass == byte[].class) {
            return Arrays.toString((byte[])((byte[])var));
        } else if (varClass == char[].class) {
            return Arrays.toString((char[])((char[])var));
        } else if (varClass == double[].class) {
            return Arrays.toString((double[])((double[])var));
        } else if (varClass == float[].class) {
            return Arrays.toString((float[])((float[])var));
        } else if (varClass == int[].class) {
            return Arrays.toString((int[])((int[])var));
        } else if (varClass == long[].class) {
            return Arrays.toString((long[])((long[])var));
        } else if (varClass == short[].class) {
            return Arrays.toString((short[])((short[])var));
        } else {
            return varClass == boolean[].class ? Arrays.toString((boolean[])((boolean[])var)) : Arrays.toString((Object[])((Object[])var));
        }
    }

    private int hashCodeImpl(){
        int hashCodeResult = 0;

        for (String attribute :
                attributeNameCache) {
            try {
                final Object attributeValue = getValue(attribute);
                // create hash code
                hashCodeResult += 127 * attribute.hashCode() ^ memberValueHashCode(attributeValue);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return hashCodeResult;
    }
    private static int memberValueHashCode(Object var0) {
        Class<?> var1 = var0.getClass();
        if (!var1.isArray()) {
            return var0.hashCode();
        } else if (var1 == byte[].class) {
            return Arrays.hashCode((byte[]) var0);
        } else if (var1 == char[].class) {
            return Arrays.hashCode((char[]) var0);
        } else if (var1 == double[].class) {
            return Arrays.hashCode((double[]) var0);
        } else if (var1 == float[].class) {
            return Arrays.hashCode((float[]) var0);
        } else if (var1 == int[].class) {
            return Arrays.hashCode((int[]) var0);
        } else if (var1 == long[].class) {
            return Arrays.hashCode((long[]) var0);
        } else if (var1 == short[].class) {
            return Arrays.hashCode((short[]) var0);
        } else {
            return var1 == boolean[].class ? Arrays.hashCode((boolean[]) var0) : Arrays.hashCode((Object[]) var0);
        }
    }


    private boolean equalsImpl(Object o){
        if (o == this){
            return true;
        }
        else if (!(annotationClass != null? annotationClass:refAnnotation.annotationType()).isInstance(o)) {
            return false;
        }
        // 属性和值比较
        else {
            final Annotation castOfObj = (annotationClass != null? annotationClass:refAnnotation.annotationType()).cast(o);
            final String[] annotationAllAttributes = AnnotationUtility.getAnnotationAllAttributes(castOfObj.annotationType());
            if (annotationAllAttributes.length != attributeNameCache.length){
                return false;
            }
            // 进行排序
            Arrays.sort(annotationAllAttributes);
            Arrays.sort(attributeNameCache);
            // 逐一比较
            for (int i = 0; i < annotationAllAttributes.length; i++) {
                if (!annotationAllAttributes[i].equals(attributeNameCache[i])){
                    return false;
                }
                try {
                    final Object annotationAttributeValue = AnnotationUtility.getAnnotationAttributeValue(castOfObj, annotationAllAttributes[i]);
                    Object attributeValue = getValue(attributeNameCache[i]);
                    if (!memberValueEquals(annotationAttributeValue, attributeValue)){
                        return false;
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
    }
    private static boolean memberValueEquals(Object var0, Object var1) {
        Class<?> var2 = var0.getClass();
        if (!var2.isArray()) {
            return var0.equals(var1);
        } else if (var0 instanceof Object[] && var1 instanceof Object[]) {
            return Arrays.equals((Object[])var0, (Object[])var1);
        } else if (var1.getClass() != var2) {
            return false;
        } else if (var2 == byte[].class) {
            return Arrays.equals((byte[])var0, (byte[])var1);
        } else if (var2 == char[].class) {
            return Arrays.equals((char[])var0, (char[])var1);
        } else if (var2 == double[].class) {
            return Arrays.equals((double[])var0, (double[])var1);
        } else if (var2 == float[].class) {
            return Arrays.equals((float[])var0, (float[])var1);
        } else if (var2 == int[].class) {
            return Arrays.equals((int[])var0, (int[])var1);
        } else if (var2 == long[].class) {
            return Arrays.equals((long[])var0, (long[])var1);
        } else if (var2 == short[].class) {
            return Arrays.equals((short[])var0, (short[])var1);
        } else {
            assert var2 == boolean[].class;

            return Arrays.equals((boolean[])var0, (boolean[])var1);
        }
    }

    private Class<? extends Annotation> annotationTypeImpl(){
        return annotationClass != null? annotationClass:refAnnotation.annotationType();
    }
}