package cn.argento.askia.annotations;

import java.lang.annotation.*;

/**
 * 标记方法参数为一个"引用型参数"
 * <p>
 * 表示方法内部对该参数所指对象的修改(或对包装类型的重新赋值), 应当能够影响调用方的实参。
 * 在 {@code Java} 中，方法参数默认是"按值传递"的，但对于对象类型，修改参数内部的属性会反映到原对象上，而重新为参数赋值则不会影响实参。
 * 要明确标识某个参数具有"引用传递"的语义（即方法内部对该参数的修改应反映到调用方）。
 * <p>
 * <b>注意：</b>该注解本身不改变参数传递机制，需要配合编译器检查或运行时框架使用。
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefParam {
    // 2024/6/15 attributes？
    /**
     * 可选属性：描述该参数的用途，例如 "输出参数" 或 "输入输出参数"
     * @return 描述该参数的用途
     */
    String value() default "";

    /**
     * 参考型参数类型
     * @return 参考型参数类型, 具体查看 {@link Type}
     * @see Type
     */
    Type type() default Type.IN_OUT;

    /**
     * 参考型参数类型, 在传统的引用类型参数传递时, 参数的类型一般有三种: 输入参数, 输出参数, 输入输出参数
     *
     * @author Askia
     */
    enum Type{
        OUT, IN_OUT
    }
}
