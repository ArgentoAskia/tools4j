package cn.argento.askia.networks.miniFeign.annotations;

import java.lang.annotation.*;

/**
 * 代表该参数是一个QueryParam
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface HttpQueryParam {
    /** 查询参数, 如果标记在Bean上, 则获取Bean上的属性名和属性值进行拼接 */
    String value() default "";
}
