package cn.argento.askia.annotations;

import cn.argento.askia.supports.LifeCyclePhase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数注入注解.
 * 默认按照类型进行注入, 当类型发生冲突时按照名称进行注入, 如果没有指定名称则抛出异常
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 上下文参数名
     * @return 参数名
     */
    String value() default "";

    /**
     * 获取阶段结束的返回值
     * @return 阶段结束的返回值
     */
    LifeCyclePhase phaseRet() default LifeCyclePhase.LAST_PHASE;


    /**
     * 参数是否一定存在
     * @return
     */
    boolean must() default true;
}
