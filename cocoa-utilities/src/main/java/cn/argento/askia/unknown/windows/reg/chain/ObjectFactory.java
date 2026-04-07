package cn.argento.askia.unknown.windows.reg.chain;

import cn.argento.askia.exceptions.runtime.lang.InstantiationRuntimeException;
import cn.argento.askia.exceptions.runtime.reflect.ConstructorNotFoundRuntimeException;
import cn.argento.askia.exceptions.runtime.reflect.InvocationTargetRuntimeException;
import cn.argento.askia.exceptions.runtime.reflect.MethodNotFoundRuntimeException;
import cn.argento.askia.exceptions.runtime.reflect.MethodNotMatchesRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 对象工厂工具类.用于辅助三大对象创建方式：构造器创建、静态工厂创建、对象工厂创建.
 *
 * @author Askia
 * @since 1.0.2024.7.29
 */public class ObjectFactory {

    private ObjectFactory(){}

    public static <T> T createObjectByConstructor(Class<T> targetObjectClass,
                                                  Class<?>[] argType, Object[] args){
        try {

            final Constructor<T> publicConstructor = targetObjectClass.getConstructor(argType);
            return publicConstructor.newInstance(args);

        } catch (NoSuchMethodException e) {
            try {

                final Constructor<T> declaredConstructor = targetObjectClass.getDeclaredConstructor(argType);
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance(args);

            } catch (NoSuchMethodException noSuchMethodException) {

                throw new ConstructorNotFoundRuntimeException(targetObjectClass, argType);

            } catch (IllegalAccessException | InstantiationException | InvocationTargetException exception) {

                final InstantiationRuntimeException instantiationRuntimeException = new InstantiationRuntimeException("Can't not create Object with Constructor:" + targetObjectClass);
                instantiationRuntimeException.addSuppressed(exception);
                throw instantiationRuntimeException;

            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {

            final InstantiationRuntimeException instantiationRuntimeException = new InstantiationRuntimeException("Can't not create Object with Constructor:" + targetObjectClass);
            instantiationRuntimeException.addSuppressed(e);
            throw instantiationRuntimeException;

        }
    }
    public static <T> T createObjectByStaticFactory(Class<?> staticFactoryClass,
                                                     String factoryMethodName,
                                                     Class<?>[] argType, Object[] args,
                                                     Class<T> objType){
        final Object newTObject = createObjectByStaticFactory(staticFactoryClass, factoryMethodName, argType, args);
        return objType.cast(newTObject);
    }

    public static Object createObjectByStaticFactory(Class<?> staticFactoryClass,
                                                    String factoryMethodName,
                                                    Class<?>[] argType, Object[] args){
        try {

            final Method publicMethod = staticFactoryClass.getMethod(factoryMethodName, argType);
            if (Modifier.isStatic(publicMethod.getModifiers())){
                final Object newTObject = publicMethod.invoke(null, args);
                return newTObject;
            }
            throw new MethodNotMatchesRuntimeException();

        } catch (NoSuchMethodException e) {

            // if private factory method exist?
            try {

                final Method privateMethod = staticFactoryClass.getDeclaredMethod(factoryMethodName, argType);
                privateMethod.setAccessible(true);
                if (Modifier.isStatic(privateMethod.getModifiers())){
                    final Object newTObject = privateMethod.invoke(null, args);
                    return newTObject;
                }
                throw new MethodNotMatchesRuntimeException();

            } catch (NoSuchMethodException noSuchMethodException) {

                throw new MethodNotFoundRuntimeException();

            } catch (InvocationTargetException | IllegalAccessException exception) {

                throw new InvocationTargetRuntimeException();

            }

        } catch (IllegalAccessException | InvocationTargetException e) {

            // 传递e参数？
            throw new InvocationTargetRuntimeException();

        }

    }

    public static Object createObjectByObjectFactory(Object objectFactoryObj,
                                                    String factoryMethodName,
                                                    Class<?>[] argType, Object[] args){
        try {

            final Method method = objectFactoryObj.getClass().getMethod(factoryMethodName, argType);
            return method.invoke(objectFactoryObj, args);

        } catch (NoSuchMethodException e) {

            try {
                final Method declaredMethod = objectFactoryObj.getClass().getDeclaredMethod(factoryMethodName, argType);
                declaredMethod.setAccessible(true);
                return declaredMethod.invoke(objectFactoryObj, args);
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new MethodNotFoundRuntimeException();
            } catch (InvocationTargetException |IllegalAccessException exception) {
                throw new InvocationTargetRuntimeException();
            }

        } catch (IllegalAccessException | InvocationTargetException e) {

            throw new InvocationTargetRuntimeException();

        }

    }


    public static <T> T createObjectByObjectFactory(Object objectFactoryObj,
                                                    String factoryMethodName,
                                                    Class<?>[] argType, Object[] args,
                                                    Class<T> objType){
        final Object newTObject = createObjectByObjectFactory(objectFactoryObj, factoryMethodName, argType, args);
        return objType.cast(newTObject);
    }
}
