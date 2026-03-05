package cn.argento.askia.networks.miniFeign.annotations;

import java.lang.annotation.*;

/**
 * 代表该参数是一个PathVariable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface HttpPathVariable {
    /** URL中PathVariable的名字 */
    String value();
}
