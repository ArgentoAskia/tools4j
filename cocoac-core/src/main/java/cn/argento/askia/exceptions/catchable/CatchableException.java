package cn.argento.askia.exceptions.catchable;

/**
 * 捕获型异常父类
 */
public class CatchableException extends Exception{

    public CatchableException() {
    }

    public CatchableException(String message) {
        super(message);
    }

    public CatchableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CatchableException(Throwable cause) {
        super(cause);
    }

    protected CatchableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
