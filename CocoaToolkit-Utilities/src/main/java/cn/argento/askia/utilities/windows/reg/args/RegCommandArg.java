package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.args.formatters.Formatter;

import java.lang.annotation.*;

/**
 * annotation for marking down what arg it is and how to append to the string command!
 *
 *
 * @author Askia
 * @since 1.0
 * @see Arg         top-level interface for args
 * @see ArgD        arg "d"
 * @see ArgF        arg "f"
 * @see ArgOutput   arg "output"
 * @see ArgQuerySet arg "queryset"
 * @see ArgReg64    arg "reg:64"
 * @see ArgReg32    arg "reg:32"
 * @see ArgS    arg "s"
 * @see ArgT    arg "t"
 * @see ArgV    arg "v"
 * @see ArgVA   arg "va"
 * @see ArgVE   arg "ve"
 * @see Converter the converter for args
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@interface RegCommandArg {

    // 格式化方式, 默认格式化是没有参数的,并引用argName属性
    // {}引用属性，[]可选参数！ ...可变参数
    // 例如： /d [data] ==> /d 123
    //       /c [data...] ==> /c 123 456 678 890
    String formatStr() default "/{argName}";

    // 属性名
    String argName() default "REG-COMMAND-ARG";


    // 如果是Java类型，则需要提供一种Java类型转换到String的转换器！
    // 这是为了兼容Java类型的拼接参数,可选
    @Deprecated
    Class<? extends Converter> converter() default Converter.EmptyConverter.class;

    @Deprecated
    Class<? extends Formatter> formatter() default Formatter.class;

}
