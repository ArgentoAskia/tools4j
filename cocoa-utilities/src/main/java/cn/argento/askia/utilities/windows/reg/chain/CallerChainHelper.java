package cn.argento.askia.utilities.windows.reg.chain;


import cn.argento.askia.utilities.windows.reg.chain.impl.CallerChainImplsHelper;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Queue;

/**
 * 链式调用栈辅助类.
 */
// 目的：业务类，JavaBean等模型类无需负责下一跳的位置，不要耦合吓一跳给业务代码
// 实现：默认情况下，开发者可以实现决定好下一跳，只要预先设置好链即可,对于不确定的链则怎么办？考虑泛型和插队机制？
// 调用ChainNext 得到吓一跳,使用 Class<T>参数来决定下一跳的类型？
// 调用ChainJump 插队
// 调用hasChainNext() 还有吓一跳？
// 预先定义好的吓一跳和只有在调用了对应的链对象之后才确定的吓一跳
// 吓一跳是同类型？有没有可能？（不可能！)
// 这里定义具体的RegAdd调用链！
// 创建CallerChain对象
public class CallerChainHelper {
    public static void main(String[] args) {
        wrap(null);
    }


    //wrap() ==> {}
    @SafeVarargs
    public static <T> T[] wrap(T... arrays){
        Objects.requireNonNull(arrays);
        return arrays;
    }

    public static <V, A> InTimeCallerChain<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Class<?>[] argsType, Object[] args){
        return CallerChainImplsHelper.currentInTimeCallerChain(object, nextObject, argsType, args);
    }

    public static <V, A> InTimeCallerChain<V, A> currentInTimeCallerChain(V object, Class<A> nextObject){
        return CallerChainImplsHelper.currentInTimeCallerChain(object, nextObject, new Class[0], new Object[0]);
    }


    public static <V, A> InTimeCallerChain<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Class<?> factoryClass,
                                                                              String factoryMethodName,
                                                                              Class<?>[] argsType, Object[] args){
        return CallerChainImplsHelper.currentInTimeCallerChain(object, nextObject, factoryClass, factoryMethodName, argsType, args);
    }

    public static <V, A> InTimeCallerChain<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Class<?> factoryClass,
                                                                              String factoryMethodName){
        return CallerChainImplsHelper.currentInTimeCallerChain(object, nextObject, factoryClass, factoryMethodName, new Class[0], new Object[0]);
    }

    public static <V, A> InTimeCallerChain<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Object objectFactory,
                                                                              String factoryMethodName){
        return CallerChainImplsHelper.currentInTimeCallerChain(object, nextObject, objectFactory, factoryMethodName, new Class[0], new Object[0]);
    }

    public static <V, A> InTimeCallerChain<V, A> currentInTimeCallerChain(V object, Class<A> nextObject,
                                                                              Object objectFactory,
                                                                              String factoryMethodName,
                                                                              Class<?>[] argsType, Object[] args){
        return CallerChainImplsHelper.currentInTimeCallerChain(object, nextObject, objectFactory, factoryMethodName, argsType, args);
    }




}
