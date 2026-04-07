package cn.argento.askia.unknown.windows.reg.chain;


class CallerChainNode<T> {
    T object;
    Class<T> objClass;

    @SuppressWarnings("unchecked")
    CallerChainNode(T object){
        this.object = object;
        this.objClass = (Class<T>) object.getClass();
    }

    public CallerChainNode(Class<T> nextClass, T o) {
        this.objClass = nextClass;
        this.object = o;
    }

    T getObject() {
        return object;
    }

    Class<T> getObjClass() {
        return objClass;
    }
}
