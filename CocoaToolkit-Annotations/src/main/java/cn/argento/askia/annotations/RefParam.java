package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 * 标记该参数是一个Reference类型的参数，用于返回数据
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefParam {
    // TODO: 2024/6/15 attributes？ 
}
