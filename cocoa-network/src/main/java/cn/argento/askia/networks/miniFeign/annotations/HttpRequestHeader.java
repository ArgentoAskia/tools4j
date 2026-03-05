package cn.argento.askia.networks.miniFeign.annotations;

import java.lang.annotation.*;

/**
 * 代表该参数是一个Http header
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface HttpRequestHeader {
    /** header name */
    String value() default "";
}
