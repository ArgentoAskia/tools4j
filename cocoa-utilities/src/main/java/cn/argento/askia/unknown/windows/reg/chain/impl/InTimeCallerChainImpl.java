package cn.argento.askia.unknown.windows.reg.chain.impl;


import cn.argento.askia.exceptions.errors.lang.SystemError;
import cn.argento.askia.unknown.windows.reg.chain.InTimeCallerChain;
import cn.argento.askia.unknown.windows.reg.chain.ObjectFactory;

import java.util.function.Function;


class InTimeCallerChainImpl<V, A> implements InTimeCallerChain<V, A> {

    enum ObjectCreator{
        CONSTRUCTOR, STATIC_FACTORY, OBJECT_FACTORY
    }

    private ObjectCreator creator;
    private final V object;
    private Class<A> nextClass;
    private Class<?>[] argType;
    private Object[] args;

    private Class<?> factoryClass;
    private Object factoryObject;
    private String factoryMethodName;

    InTimeCallerChainImpl(V objectProxy) {
        object = objectProxy;
    }

    InTimeCallerChainImpl(V objectProxy,
                          Class<A> nextClass,
                          Class<?>[] argType,
                          Object[] args){
        this(objectProxy);
        this.nextClass = nextClass;
        this.argType = argType;
        this.args = args;
        this.creator = ObjectCreator.CONSTRUCTOR;
    }

    InTimeCallerChainImpl(V objectProxy,
                          Class<A> nextClass,
                          Class<?>[] argType,
                          Object[] args,
                          Class<?> factoryClass,
                          String factoryMethodName){
        this(objectProxy, nextClass, argType, args);
        this.factoryClass = factoryClass;
        this.factoryMethodName = factoryMethodName;
        this.creator = ObjectCreator.STATIC_FACTORY;
    }

    InTimeCallerChainImpl(V objectProxy,
                          Class<A> nextClass,
                          Class<?>[] argType,
                          Object[] args,
                          Object factoryObject,
                          String factoryMethodName){
        this(objectProxy, nextClass, argType, args);
        this.factoryObject = factoryObject;
        this.factoryMethodName = factoryMethodName;
        this.creator = ObjectCreator.OBJECT_FACTORY;
    }



    protected A newNextByConstructor() {
        return ObjectFactory.createObjectByConstructor(nextClass, argType, args);
    }

    protected A newNextByStaticFactory() {
        return ObjectFactory.createObjectByStaticFactory(factoryClass, factoryMethodName,
                argType, args, nextClass);
    }

    protected A newNextByObjectFactory() {
        return ObjectFactory.createObjectByObjectFactory(factoryObject, factoryMethodName,
                argType, args, nextClass);
    }


    @Override
    public A newNext() {
        switch (creator){
            case CONSTRUCTOR:
                return newNextByConstructor();
            case OBJECT_FACTORY:
                return newNextByObjectFactory();
            case STATIC_FACTORY:
                return newNextByStaticFactory();
        }
        // 除非使用特殊手段，否则该错误不会发生
        throw new SystemError();
    }

    @Override
    public V recycle() {
        return object;
    }

    @Override
    public V cloneRecycle(Function<V, V> cloneFunction) {
        return cloneFunction.apply(object);
    }

}
