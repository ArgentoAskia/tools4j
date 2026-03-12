package cn.argento.askia.networks.miniFeign.annotations;

import cn.argento.askia.networks.miniFeign.supports.http.HttpRequestMethod;

import java.lang.annotation.*;

/**
 * 1. 在方法上标记该注解会让该方法变成Http请求调用
 * 2. 标记在类型上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Documented
public @interface HttpCaller {

    /** 调用的URL */
    String value() default "";
    /** 调用方法 */
    HttpRequestMethod method() default HttpRequestMethod.POST;
}
