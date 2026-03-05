package cn.argento.askia.networks.miniFeign.annotations;

import java.lang.annotation.*;

/**
 * 代表该参数是一个RequestBody
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface HttpRequestBody {
    /** 代表Content-Type */
    String value() default "";
}
