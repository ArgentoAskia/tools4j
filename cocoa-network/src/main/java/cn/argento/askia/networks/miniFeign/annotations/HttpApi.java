package cn.argento.askia.networks.miniFeign.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 代表该类是一个Http Api接口!
 *
 */
@Target({ TYPE })
@Retention(RUNTIME)
@Documented
public @interface HttpApi {

    // Api 名(记录是属于哪里的API)
    String value() default "";

    /** Api的状态, 默认是开发状态 */
    Status status() default Status.MAINTAINED;

    /**
     * Api的版本, 建议使用日期类进行维护
     */
    String since() default "";

    // 更新记录(仅显示)
    update[] updates() default {};

    @Target(ANNOTATION_TYPE)
    @Retention(SOURCE)
    @interface update{
        // 更新描述
        String value() default "";
    }

    enum Status {

        /**
         * Must not be used by any external code. Might be removed without prior
         * notice.
         */
        INTERNAL,

        /**
         * Should no longer be used. Might disappear in the next minor release.
         *
         * <p>This status is usually used in combination with the standard annotation
         * {@link Deprecated @Deprecated} because that annotation is recognized by
         * IDEs and the compiler. However, there are also cases where this status
         * can be used on its own, for example when transitioning a {@link #MAINTAINED}
         * feature to an {@link #INTERNAL} one.
         */
        DEPRECATED,

        /**
         * Intended for new, experimental features where the publisher of the
         * API is looking for feedback.
         *
         * <p>Use with caution. Might be promoted to {@link #MAINTAINED} or
         * {@link #STABLE} in the future, but might also be removed without
         * prior notice.
         */
        EXPERIMENTAL,

        /**
         * Intended for features that will not be changed in a backwards-incompatible
         * way for at least the next minor release of the current major version.
         * If scheduled for removal, such a feature will be demoted to
         * {@link #DEPRECATED} first.
         */
        MAINTAINED,

        /**
         * Intended for features that will not be changed in a backwards-incompatible
         * way in the current major version.
         */
        STABLE;

    }

    // FixME 下面字段暂时未实现
    // 功能字段
    // 具体实现类(默认没有), 如果指定了具体实现类, 则代表该@HttpApi接口已被某个类实现了
    // 此时我们可以尝试读取该类上是否带有Spring框架的@Controller或者@RestController + @RequestMapping来实现URL的自动获取而无需额外编写配置
    Class<?> implClass() default Void.class;
    // 指定类名而非Class对象(兼容非classpath的情况)
    String implClassName() default "java.lang.Void";
    // 如果你的实现类并不在当前classpath中, 则可以指定此属性, 我们会优先在此属性中寻找你的实现类
    String[] extClassPaths() default {};


}
