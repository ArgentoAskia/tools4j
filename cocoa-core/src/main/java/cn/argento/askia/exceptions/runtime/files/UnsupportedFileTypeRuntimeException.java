package cn.argento.askia.exceptions.runtime.files;

public class UnsupportedFileTypeRuntimeException extends RuntimeException{
    public UnsupportedFileTypeRuntimeException() {
    }

    public UnsupportedFileTypeRuntimeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFileTypeRuntimeException(Throwable cause) {
        super(cause);
    }

    protected UnsupportedFileTypeRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
