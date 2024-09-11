package cn.argento.askia.exceptions.runtime.lang;


import java.util.function.Consumer;

/**
 * 当一个理论上必定能够实例化成功的对象, 在代码运行的时候由于各种各样的原因导致实例化失败的时候, 抛出此异常.<br>
 *
 * 通常在创建对象时, 会抛出该异常则证明要创建的对象只准实例化成功, 即为关键对象, 宁可让整个系统停止运行也不愿意承担该对象实例化失败导致产生的后果.
 * 因此该异常包括了很多非常详尽的异常信息, 如：诊断信息、起源调查信息、解决方案参考等
 *
 * @author Askia
 * @since 1.0
 */
public class InstantiationRuntimeException extends RuntimeException {
    private Class<?> classUsingForInstantiation;
    private Exception whyCauseThisException;

    public InstantiationRuntimeException(String message){
        super(message);
    }

    public InstantiationRuntimeException(String message, Throwable cause){
        super(message, cause);
    }

    public InstantiationRuntimeException(Class<?> classUsingForInstantiation){
        super("Can't not create Object by Class: " + classUsingForInstantiation.getName());
        this.classUsingForInstantiation = classUsingForInstantiation;
    }

    public InstantiationRuntimeException(Class<?> classUsingForInstantiation,
                                         Exception whyCauseThisException) {
        super("Can't not create Object with Class: " + classUsingForInstantiation.getName()
                + ", because another exception causes this exception:\n\t" + whyCauseThisException.toString(), whyCauseThisException);
        this.classUsingForInstantiation = classUsingForInstantiation;
        this.whyCauseThisException = whyCauseThisException;
    }


    public Class<?> getClassUsingForInstantiation() {
        return classUsingForInstantiation;
    }

    public Exception getWhyCauseThisException() {
        return whyCauseThisException;
    }
}
