package cn.argento.askia.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cloneable {

    String methodName() default "clone";

    Class<?>[] params() default {};
}
