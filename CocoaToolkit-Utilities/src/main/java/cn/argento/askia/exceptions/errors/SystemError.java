package cn.argento.askia.exceptions.errors;

public class SystemError extends Error{

    static final long serialVersionUID = 4603556048193261013L;

    public SystemError(String message, Throwable cause){
        super(message, cause, true, true);
    }


    public SystemError(){
        super();
    }

    public SystemError(String message){
        super(message);
    }

    public SystemError(Throwable cause){
        super(cause);
    }

}
