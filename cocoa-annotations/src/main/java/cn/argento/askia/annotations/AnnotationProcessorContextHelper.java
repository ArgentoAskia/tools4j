package cn.argento.askia.annotations;


import cn.argento.askia.annotations.ApiStatus;
import cn.argento.askia.annotations.annotation.Destroy;
import cn.argento.askia.annotations.annotation.Initialization;
import cn.argento.askia.annotations.annotation.Param;
import cn.argento.askia.annotations.context.AnnotationProcessorContext;
import cn.argento.askia.annotations.context.BeanNotFoundException;
import cn.argento.askia.annotations.context.MoreThenOneBeanException;
import cn.argento.askia.annotations.context.MutableAnnotationProcessorContext;
import cn.argento.askia.utilities.annotation.AnnotationUtility;
import cn.argento.askia.utilities.lang.StringUtility;
import cn.argento.askia.utilities.reflect.ReflectUtility;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;


@ApiStatus.Internal
class AnnotationProcessorContextHelper {

    /**
     * 将 {@link AnnotationProcessorContext} 转为 {@link MutableAnnotationProcessorContext}
     * @param context 实现了 {@link MutableAnnotationProcessorContext} 的子实现
     * @return {@link MutableAnnotationProcessorContext} 对象, 转换失败将返回 {@code null}
     */
    static MutableAnnotationProcessorContext getMutableContext(AnnotationProcessorContext context){
        if (context instanceof MutableAnnotationProcessorContext){
            return (MutableAnnotationProcessorContext) context;
        }
        return null;
    }

    /**
     * 先按照类型，再按照值来找Bean对象
     * @param tClass
     * @param beanName
     * @param context
     * @return
     * @throws BeanNotFoundException
     */
    static Object findBean(Class<?> tClass, String beanName, AnnotationProcessorContext context) throws BeanNotFoundException {
        try {
            Object beanByType = context.getBeanByType(tClass);
            if (beanByType == null){
                throw new BeanNotFoundException("找不到类型为" + tClass + "的Bean, 开启按照名字查找");
            }
            return beanByType;
        }
        catch (MoreThenOneBeanException | BeanNotFoundException e) {
            Object beanByName = null;
            if (StringUtility.isBlank(beanName)){
                throw new IllegalArgumentException("beanName不能为空, 因为容器按照" + tClass + "无法找到");
            }
            else{
                beanByName = context.getBeanByName(beanName);
                if (beanByName == null){
                    // 必须要有但是找不到
                    throw new BeanNotFoundException("找不到名字为" + beanName + "的Bean, 因此抛出异常", e);
                }
                return beanByName;
            }
        }
    }


    /**
     * 初始化上下文容器
     * @param context context
     */
    static void initContext(MutableAnnotationProcessorContext context, Object annotationProcessObj) throws InvocationTargetException, IllegalAccessException {
        final Class<?> annotationProcessObjClass = annotationProcessObj.getClass();
        final Set<Method> declaredMethods = ReflectUtility.getAllMethods(annotationProcessObjClass);
        Method initMethod = null;
        String beanName = null;
        for (Method m : declaredMethods){
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, Initialization.class)) {
                // 标记了@Initialization
                initMethod = m;
                beanName = Initialization.class.getSimpleName();
                break;
            }
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, PostConstruct.class)){
                // 标记了@PostConstruct
                initMethod = m;
                beanName = PostConstruct.class.getSimpleName();
                break;
            }
        }
        if (initMethod != null){
            // 允许访问私有方法和继承性方法
            if (!initMethod.isAccessible()){
                initMethod.setAccessible(true);
            }
            final Class<?>[] parameterTypes = initMethod.getParameterTypes();
            if (parameterTypes.length == 0){
                // 直接调用
                final Object invoke = initMethod.invoke(annotationProcessObj);
                context.registerBean(beanName, invoke);
                return;
            }
            if (parameterTypes.length == 1 && (AnnotationProcessorContext.class == parameterTypes[0] || MutableAnnotationProcessorContext.class == parameterTypes[0])){
                final Object invoke = initMethod.invoke(annotationProcessObj, context);
                context.registerBean(beanName, invoke);
                return;
            }
            // 两个以上的参数
            if (parameterTypes.length > 1){
                final Parameter[] parameters = initMethod.getParameters();
                Object[] args = new Object[parameters.length];
                int index = 0;
                for (Parameter p : parameters){
                    if (p.getType() == AnnotationProcessorContext.class || p.getType() == MutableAnnotationProcessorContext.class){
                        args[index++] = context;
                        continue;
                    }
                    String paramName = "";
                    if (p.isAnnotationPresent(Param.class)){
                        final Param declaredAnnotation = p.getDeclaredAnnotation(Param.class);
                        paramName = declaredAnnotation.value();
                    }
                    try{
                        final Object bean = AnnotationProcessorContextHelper.findBean(p.getType(), paramName, context);
                        args[index++] = bean;
                    }
                    catch (BeanNotFoundException e){
                        //  找不到填null
                        args[index++] = null;
                    }
                }
                final Object invoke = initMethod.invoke(annotationProcessObj, args);
                // 如果方法返回值不等于void或者Void, 则我们尝试将返回值放入容器中, 哪怕返回值本身就是null
                if (initMethod.getReturnType() != void.class && initMethod.getReturnType() != Void.class){
                    context.registerBean(beanName, invoke);
                }
            }
        }
    }
    // 只读容器不能操作，因此不需要关闭容器
    static void initReadOnlyContext(AnnotationProcessorContext context, Object annotationProcessObj){

    }

    static void closeContext(MutableAnnotationProcessorContext context, Object annotationProcessObj) throws InvocationTargetException, IllegalAccessException {
        final Class<?> annotationProcessObjClass = annotationProcessObj.getClass();
        final Set<Method> declaredMethods = ReflectUtility.getAllMethods(annotationProcessObjClass);
        Method destroyMethod = null;
        String beanName = null;
        for (Method m : declaredMethods){
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, Destroy.class)) {
                // 标记了@Initialization
                destroyMethod = m;
                beanName = Destroy.class.getSimpleName();
                break;
            }
            if (AnnotationUtility.isDeclaredAnnotationPresent(m, PreDestroy.class)){
                // 标记了@PostConstruct
                destroyMethod = m;
                beanName = PreDestroy.class.getSimpleName();
                break;
            }
        }
        if (destroyMethod != null){
            // 允许访问私有方法和继承性方法
            if (!destroyMethod.isAccessible()){
                destroyMethod.setAccessible(true);
            }
            final Class<?>[] parameterTypes = destroyMethod.getParameterTypes();
            if (parameterTypes.length == 0){
                // 直接调用
                final Object invoke = destroyMethod.invoke(annotationProcessObj);
                context.registerBean(beanName, invoke);
                return;
            }
            if (parameterTypes.length == 1 && (AnnotationProcessorContext.class == parameterTypes[0] || MutableAnnotationProcessorContext.class == parameterTypes[0])){
                final Object invoke = destroyMethod.invoke(annotationProcessObj, context);
                context.registerBean(beanName, invoke);
                return;
            }
            // 两个以上的参数
            if (parameterTypes.length > 1){
                final Parameter[] parameters = destroyMethod.getParameters();
                Object[] args = new Object[parameters.length];
                int index = 0;
                for (Parameter p : parameters){
                    if (p.getType() == AnnotationProcessorContext.class || p.getType() == MutableAnnotationProcessorContext.class){
                        args[index++] = context;
                        continue;
                    }
                    args[index++] = null;
                }
                final Object invoke = destroyMethod.invoke(annotationProcessObj, args);
                if (destroyMethod.getReturnType() != void.class && destroyMethod.getReturnType() != Void.class){
                    context.registerBean(beanName, invoke);
                }
            }
        }
    }
}
