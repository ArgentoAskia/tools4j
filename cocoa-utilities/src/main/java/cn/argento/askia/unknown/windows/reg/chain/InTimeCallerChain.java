package cn.argento.askia.unknown.windows.reg.chain;

import java.util.function.Function;

// 画图
public interface InTimeCallerChain<V, T> {

    T newNext();

    V recycle();

    V cloneRecycle(Function<V, V> cloneFunction);

}
