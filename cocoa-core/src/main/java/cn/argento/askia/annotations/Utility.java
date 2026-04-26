package cn.argento.askia.annotations;


import java.lang.annotation.*;

/**
 * 工具类声明注解.
 *
 * <p>此注解用于标记一个类是工具类, 如果您定义的类是一个工具类, 则可以在类上标记此注解</p>
 *
 * <p><b>此注解是否只起标记作用完全取决去实现端是否有做此注解的注解处理器, 默认情况下{@code cocoa-core}包不提供此注解的注解处理器.</b></p>
 *
 * <p>此注解之所以被声明为{@link RetentionPolicy#RUNTIME}是为了保证注解能被APT, 字节码处理工具以及反射API进行获取, 以方便相关类型的注解处理器在不同阶段做不同的事情</p>
 *
 * @author Askia
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface Utility {

    /**
     * 描述此工具类的用途的文本(名称)
     * @return 描述文本
     */
    String value();


    /**
     * 归档分类, 使用/进行分割, 默认应该使用简易包名作为归档类型
     * @return 归档名，默认包名
     */
    String archive() default "{{ packageName }}";

    /**
     * 当前版本
     * @return 版本信息
     */
    String version() default "1.0.0";

    /**
     * 作者信息
     * @return 作者信息
     */
    String author() default "Unknown";


//    /**
//     * 工具类的描述！
//     *
//     * @return 工具类的描述
//     */
//    String description() default "";
//
//    /**
//     * 描述文件的具体位置，该选项主要防止{@link Utility#description()}过长导致注解过于臃肿而将具体的描述信息外移！
//     * 也可以将{@link Utility#description()}作为简短说明而本属性作为长说明使用！
//     *
//     * 文件位置支持："classpath:"选项,因此可以这样路径：
//     * <code>
//     *      @Utility(descriptionFileURL = "classpath:statement.txt")
//     * </code>
//     *
//     * @return 描述文件的路径！
//     */
//    String descriptionFileURL() default "";
}
