package cn.argento.askia.unknown.windows.reg.chain.impl;

public class CallerChainImplsHelper {


    public static <V, A> InTimeCallerChainImpl<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Class<?>[] argsType, Object[] args){
        return new InTimeCallerChainImpl<V, A>(object, nextObject, argsType, args);
    }

    public static <V, A> InTimeCallerChainImpl<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Class<?> factoryClass,
                                                                              String factoryMethodName,
                                                                              Class<?>[] argsType, Object[] args){
        return new InTimeCallerChainImpl<V, A>(object, nextObject, argsType, args, factoryClass, factoryMethodName);
    }


    public static <V, A> InTimeCallerChainImpl<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Object objectFactory,
                                                                              String factoryMethodName,
                                                                              Class<?>[] argsType, Object[] args){
        return new InTimeCallerChainImpl<V, A>(object, nextObject, argsType, args, objectFactory, factoryMethodName);
    }
}
