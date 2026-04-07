package cn.argento.askia.unknown.windows.reg.chain;

public interface MemorableInTimeCallerChain<C, N> extends InTimeCallerChain<C, N>{

    @Override
    default N newNext(){
        return newNext(false);
    }

    N  newNext(boolean memorable);


    default N poll(){
        return poll(true);
    }

    N poll(boolean creatWhenFail);

    MemorableInTimeCallerChain<C, N> refresh();

}
