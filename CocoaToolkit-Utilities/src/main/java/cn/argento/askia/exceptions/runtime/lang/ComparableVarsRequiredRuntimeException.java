package cn.argento.askia.exceptions.runtime.lang;

public class ComparableVarsRequiredRuntimeException
extends RuntimeException{

    public ComparableVarsRequiredRuntimeException(Object obj){
        super("Object:[" + obj + "]'s type = [" + obj.getClass().getCanonicalName() + "], it's not a COMPARABLE Type!" );
    }

    public ComparableVarsRequiredRuntimeException(String message){
        super(message);
    }

    public ComparableVarsRequiredRuntimeException(String message, Throwable cause){
        super(message, cause);
    }
}
