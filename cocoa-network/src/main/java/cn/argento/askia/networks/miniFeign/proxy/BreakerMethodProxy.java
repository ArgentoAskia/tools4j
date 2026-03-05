package cn.argento.askia.networks.miniFeign.proxy;

import java.lang.reflect.InvocationHandler;

public interface BreakerMethodProxy<T> extends InvocationHandler {
    T getBreaker();
}
