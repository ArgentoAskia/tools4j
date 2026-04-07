package cn.argento.askia.exceptions.catchable.files;

public class UnsupportedMimeTypeException extends Exception {
    public UnsupportedMimeTypeException() {
        super();
    }

    public UnsupportedMimeTypeException(String message) {
        super(message);
    }

    public UnsupportedMimeTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedMimeTypeException(Throwable cause) {
        super(cause);
    }

    protected UnsupportedMimeTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
