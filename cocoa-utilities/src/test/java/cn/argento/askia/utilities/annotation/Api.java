package cn.argento.askia.utilities.annotation;


import java.lang.annotation.*;

@Target({ElementType.CONSTRUCTOR, ElementType.FIELD,
        ElementType.LOCAL_VARIABLE, ElementType.METHOD,
        ElementType.PARAMETER, ElementType.TYPE, ElementType.TYPE_PARAMETER,
        ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {
    String id() default "";
    String name() default "";
    String version() default "";
}
