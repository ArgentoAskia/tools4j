package cn.argento.askia.context;

public class MoreThenOneBeanException extends Exception {
    public MoreThenOneBeanException() {
    }

    public MoreThenOneBeanException(String message) {
        super(message);
    }

    public MoreThenOneBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoreThenOneBeanException(Throwable cause) {
        super(cause);
    }

    protected MoreThenOneBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
