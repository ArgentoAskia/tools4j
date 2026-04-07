package cn.argento.askia.unknown.windows.reg.chain;

public interface ScheduledCallerChain {

    // 多条路选择的时候
    <T> T next(Class<T> nextObjectClass);
    // 检查想要的tClass是否在下一跳中
    <T> Class<T> checkNextClass(Class<T> tClass);
    Class<?>[] getChainNextAllClasses();

    // 单路选择，setNextClass()进行调用,需要自行指定类型
    <T> T next();




    String toString();
}
