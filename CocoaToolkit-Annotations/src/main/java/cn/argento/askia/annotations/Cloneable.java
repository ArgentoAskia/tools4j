package cn.argento.askia.annotations;


public @interface Cloneable {

    String methodName() default "clone";


    Class<?>[] params() default {};
}
