package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 * 标记该类是一个工具类.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Utility {
    /**
     * 工具类的名称.
     *
     * 默认获取工具类的类名作为该属性的值！
     *
     * @return 工具类名称
     */
    String name() default "{default}";

    /**
     * 工具类的描述！
     *
     * @return 工具类的描述
     */
    String description() default "";

    /**
     * 描述文件的具体位置，该选项主要防止{@link Utility#description()}过长导致注解过于臃肿而将具体的描述信息外移！
     * 也可以将{@link Utility#description()}作为简短说明而本属性作为长说明使用！
     *
     * 文件位置支持："classpath:"选项,因此可以这样路径：
     * <code>
     *      @Utility(descriptionFileURL = "classpath:statement.txt")
     * </code>
     *
     * @return 描述文件的路径！
     */
    String descriptionFileURL() default "";

    /**
     * 工具类的版本!
     *
     * @return 版本号
     */
    String version() default "";
}
