package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cloneable {

    String methodName() default "clone";

    Class<?>[] params() default {};

    // 处理深拷贝的处理器
    Class<?> handlerClass() default Void.class;
}
