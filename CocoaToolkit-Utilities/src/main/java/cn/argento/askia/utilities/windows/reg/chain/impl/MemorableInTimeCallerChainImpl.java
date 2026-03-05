package cn.argento.askia.utilities.windows.reg.chain.impl;

import cn.argento.askia.utilities.windows.reg.chain.MemorableInTimeCallerChain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


class MemorableInTimeCallerChainImpl<C, N> extends InTimeCallerChainImpl<C, N>
        implements MemorableInTimeCallerChain<C, N> {

    private Map<Class<?>, Object> cache;


    MemorableInTimeCallerChainImpl(C object){
        super(object);
        cache = new HashMap<>();
    }


    @Override
    public N newNext(boolean memorable) {
        return null;
    }

    @Override
    public N poll(boolean creatWhenFail) {
        return null;
    }

    //  refresh will refresh the cache
    @Override
    public MemorableInTimeCallerChain<C, N> refresh() {
        return null;
    }

    @Override
    public C recycle() {
        return null;
    }

    @Override
    public C cloneRecycle(Function<C, C> cloneFunction) {
        return null;
    }
}
