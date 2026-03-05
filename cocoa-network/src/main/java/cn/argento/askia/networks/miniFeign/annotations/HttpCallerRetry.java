package cn.argento.askia.networks.miniFeign.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface HttpCallerRetry {

    /** 重试机制, 默认不重试 */
    int retry() default 3;

    /** 默认隔多少秒重试  */
    long seconds() default 10;
}
