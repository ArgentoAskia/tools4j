package cn.argento.askia.exceptions.runtime.reflect;

public class ReflectiveOperationRuntimeException extends RuntimeException{


    private static final long serialVersionUID = -2045131651397192761L;

    public ReflectiveOperationRuntimeException() {
    }

    public ReflectiveOperationRuntimeException(String message) {
        super(message);
    }

    public ReflectiveOperationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectiveOperationRuntimeException(Throwable cause) {
        super(cause);
    }

    protected ReflectiveOperationRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
