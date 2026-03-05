package cn.argento.askia;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.processors.AnnotationProcessor;
import cn.argento.askia.supports.AnnotationKeyValue;
import cn.argento.askia.supports.proxy.AnnotationInvocationHandler;
import cn.argento.askia.utilities.AnnotationUtility;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiFunction;

@Utility(name = "AnnotationProcessingHelper", version = "1.0")
public class AnnotationProcessingHelper {

    public static <A extends Annotation> AnnotationProcessor<A> findProcessor(Class<A> annotationClass, AnnotatedElement annotatedElement){
        return null;
    }




    public static <A extends Annotation, B extends Annotation> A annotationAliasFor(A source, B target){
        return null;
    }

    // String = JDK publisher
    // BiConsumer = how to deal with it annotation mechanism
    private static Map<String, BiFunction<Annotation, AnnotationKeyValue<?>[], Annotation>> annotationInvocationHandlersCache;
    // init cache handler for different JDK
    static {
        annotationInvocationHandlersCache = new HashMap<>();
        // TODO: 2024/3/10  init jdk annotationInvocationHandler here

    }

    // TODO: 2024/3/10 how to get the real publisher, it is a question!
    // publisher str is a short string!
    private static String getJDKPublisher(){
        return "Oracle";
    }

    // check "sun.reflect.annotation.AnnotationInvocationHandler" exist
    private static boolean isSunAnnotationInvocationHandlerExist(){
        try {
            // we set classloader null to ensure security manager using bootstrap classloader
            // no need to initialize class, we just want to know whether is is exist or not !
            Class.forName("sun.reflect.annotation.AnnotationInvocationHandler", false, null);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    public static <A extends Annotation> Annotation modifyAnnotation(A targetAnnotation, AnnotationKeyValue<?>... annotationKeyValues){
        // oracle JDK openJDK
        // if sun.reflect.annotation.AnnotationInvocationHandler exist, we use it!
        if (isSunAnnotationInvocationHandlerExist()){
            for (AnnotationKeyValue<?> kv :
                    annotationKeyValues) {
                AnnotationUtility.modifyAnnotation(targetAnnotation, kv.getAttributeName(), kv.getValue());
            }
            return targetAnnotation;
        }
        // 非OracleJDK
        final BiFunction<Annotation, AnnotationKeyValue<?>[], Annotation> handlerCode = annotationInvocationHandlersCache.get(getJDKPublisher());
        if (handlerCode != null){
            // 使用各自发行商的JDK版本注解机制来实现修改注解
            return handlerCode.apply(targetAnnotation, annotationKeyValues);
        }
        else {
            // 作为最后，我们使用自定义的AnnotationInvocationHandler作为兜底！
            // TODO: 2024/2/19 DIY handler!
            final Object o = Proxy.newProxyInstance(targetAnnotation.getClass().getClassLoader(),
                    new Class[]{targetAnnotation.annotationType()},
                    new AnnotationInvocationHandler(targetAnnotation));
            final Annotation newRefAnnotation = targetAnnotation.annotationType().cast(o);
            return newRefAnnotation;
        }
    }

    private static Map<String, Object> keyValueAsMap(Class<? extends Annotation> annotationClass, AnnotationKeyValue<?>... annotationKeyValues){
        Map<String, Object> memberValues = new HashMap<>();
        final Method[] attributes = annotationClass.getDeclaredMethods();
        Set<String> attributeRequireValueSet = new HashSet<>();
        for (Method attribute :
                attributes) {
            final Object defaultValue = attribute.getDefaultValue();
            if (defaultValue != null){
                memberValues.put(attribute.getName(), defaultValue);
            }
            else{
                attributeRequireValueSet.add(attribute.getName());
            }
        }
        // update default value
        for (AnnotationKeyValue<?> keyValue:
             annotationKeyValues) {
            // 此时我们要添加需要值的属性(非覆盖！)，所以从Set中移除掉！
            attributeRequireValueSet.remove(keyValue.getAttributeName());
            memberValues.put(keyValue.getAttributeName(), keyValue.getValue());
        }

        // 如果还有剩余的需要值的属性，则选择抛异常!
        if (attributeRequireValueSet.size() != 0)
            throw new AnnotationFormatError("注解" + annotationClass.getName() + "的属性集：" + attributeRequireValueSet + "本身不具备默认值，并且注解实例化过程中没有提供值！");
        return memberValues;
    }

    public static <A extends Annotation> A newAnnotation(Class<A> annotationClass, AnnotationKeyValue<?>... annotationKeyValues){
        final Map<String, Object> memberValueMap = keyValueAsMap(annotationClass, annotationKeyValues);
        InvocationHandler annotationInvocationHandler = null;
        try {
            // some impl may put ”sun.reflect.annotation.AnnotationInvocationHandler“ in /lib/ext?
            // With the Parent Delegation Mechanism, we can proxy it from extClassLoader and end with bootstrap classloader
            final Class<?> annotationInvocationHandlerClass = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler", false, ClassLoader.getSystemClassLoader());
            final Constructor<?> constructor = annotationInvocationHandlerClass.getDeclaredConstructor(Class.class, Map.class);
            // make constructor accessible！
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            annotationInvocationHandler = (InvocationHandler) constructor.newInstance(annotationClass, memberValueMap);
        } catch (NoSuchMethodException e) {
            // AnnotationInvocationHandler exist but no match constructor!
            // so we use fields? i don't know
            // so we decide to do the same with ClassNotFoundException
            // but we may change it in the future version!
            annotationInvocationHandler = new AnnotationInvocationHandler(annotationClass, memberValueMap);
        } catch (ClassNotFoundException e) {
            // AnnotationInvocationHandler class not found!
            // we use our AnnotationInvocationHandler:cn.argento.askia.support.proxy.AnnotationInvocationHandler
            annotationInvocationHandler = new AnnotationInvocationHandler(annotationClass, memberValueMap);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            annotationInvocationHandler = new AnnotationInvocationHandler(annotationClass, memberValueMap);
        }

        final Object o = Proxy.newProxyInstance(AnnotationProcessingHelper.class.getClassLoader(),
                new Class[]{annotationClass},
                annotationInvocationHandler);
        return annotationClass.cast(o);
    }

    public static <S, T extends S> S cloneObject(T obj){
        return null;
    }


    public static void main(String[] args) {

        final Utility utility = newAnnotation(Utility.class, AnnotationKeyValue.newAnnotationKeyValue("version", "1.0"));
        System.out.println(utility);
    }
}
