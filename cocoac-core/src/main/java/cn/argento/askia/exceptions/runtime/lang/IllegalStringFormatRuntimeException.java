package cn.argento.askia.exceptions.runtime.lang;

import java.util.regex.Pattern;

public class IllegalStringFormatRuntimeException extends IllegalArgumentException{

    public IllegalStringFormatRuntimeException(String correctFormat,
                                               String illegalFormat){
        super("Illegal String Format, expect: " + correctFormat + ", provide: " + illegalFormat);
    }

    public IllegalStringFormatRuntimeException(Pattern correctFormat,
                                               String illegalFormat){
        super("Illegal String Format, expect Pattern: " + correctFormat + ", provide: " + illegalFormat);
    }

    public IllegalStringFormatRuntimeException(String correctFormat,
                                               String illegalFormat,
                                               Throwable cause){
        super("Illegal String Format, expect: " + correctFormat + ", provide: " + illegalFormat, cause);
    }

    public IllegalStringFormatRuntimeException(Pattern correctFormat,
                                               String illegalFormat,
                                               Throwable cause){
        super("Illegal String Format, expect Pattern: " + correctFormat + ", provide: " + illegalFormat, cause);
    }


    public IllegalStringFormatRuntimeException(String msg){
        super(msg);
    }

    public IllegalStringFormatRuntimeException(String msg, Throwable cause){
        super(msg, cause);
    }
}
