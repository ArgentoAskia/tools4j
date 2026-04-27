package cn.argento.askia.web.servlet.interceptors;

import org.springframework.core.Ordered;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.lang.annotation.*;
import java.util.function.Supplier;


/**
 * 标记一个类是一个拦截器.
 *
 * <p>提供注解声明式拦截器注册, 原本的注册中你需要在 {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer#addInterceptors(InterceptorRegistry)} 中调用参数方法进行注册, 而如今, 使用此注解你可以随意在特定的目录下进行扫描注册</p>
 *
 * @see org.springframework.web.servlet.config.annotation.InterceptorRegistration
 * @see org.springframework.web.servlet.config.annotation.InterceptorRegistry
 * @see org.springframework.web.servlet.HandlerInterceptor
 * @see InterceptorAnnotationProcessor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR})
@Documented
@Repeatable(Interceptor.InterceptorContainer.class)
public @interface Interceptor {

    /**
     * <p>注意如果同时配置了{@link Interceptor#addPathPatterns()}和{@link Interceptor#value()}, 则结果会进行交集融合</p>
     *
     * @see Interceptor#addPathPatterns()
     * @return addPathPatterns
     */
    String[] value();

    /**
     * @see org.springframework.web.servlet.config.annotation.InterceptorRegistration#order(int)
     * @return order
     */
    int order() default 0;

    /**
     * @see org.springframework.web.servlet.config.annotation.InterceptorRegistration#excludePathPatterns(String...)
     * @return excludePathPatterns
     */
    String[] excludePathPatterns() default {};

    /**
     * @see org.springframework.web.servlet.config.annotation.InterceptorRegistration#addPathPatterns(String...)
     * @return addPathPatterns
     */
    String[] addPathPatterns() default {};

    /**
     * 提供PathMatcher的子实现类的Supplier提供类.
     *
     * @see org.springframework.web.servlet.config.annotation.InterceptorRegistration#pathMatcher(PathMatcher)
     * @see DefaultPathMatcherSupplier
     * @return pathMatcherSupplierClass
     */
    Class<? extends Supplier<? extends PathMatcher>> pathMatcherSupplierClass() default DefaultPathMatcherSupplier.class;


    /**
     * 注解容器
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Documented
    @interface InterceptorContainer{
        Interceptor[] value();
    }
}
