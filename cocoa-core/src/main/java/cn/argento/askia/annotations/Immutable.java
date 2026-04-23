package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 * 不可变对象声明注解.
 *
 * <p>当一个类上标记了此注解时, 则说明这个类的所有对象都是不可变的. 类似于{@link String}, 类编写者应该不要提供任何可以修改字段的方法, 常见的一个做法时, 将该类的所有的构造器全部标记为 {@code private}, 然后提供对应的 {@code valueOf}方法</p>
 *
 * <p>此接口暂时没有任何其他的内容, 仅仅作为标记使用</p>
 * @author Admin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Immutable {
}
