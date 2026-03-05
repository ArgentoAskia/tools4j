package cn.argento.askia.exceptions.runtime.lang;

/**
 * 当你要求接收的参数或者变量必须是“可比较”的, 但是实际拿到的参数缺不能进行比较的时候，可以尝试抛出此运行时异常.
 * - 基本类型除了boolean, 其他都是“可比较”的
 * - 引用类型必须是实现了 {@linkplain Comparable Comparable} 接口的类型才符合“可比较”
 *
 * @author Askia
 * @since 1.0
 * @see java.lang.RuntimeException
 * @see cn.argento.askia.utilities.AssertionUtility
 */
public class ComparableVarsRequiredRuntimeException
extends RuntimeException{

    public ComparableVarsRequiredRuntimeException(Object obj){
        super("Object:[" + obj + "]'s type = [" + obj.getClass().getCanonicalName() + "], it's not a COMPARABLE Type!");
    }

    public ComparableVarsRequiredRuntimeException(String message){
        super(message);
    }

    public ComparableVarsRequiredRuntimeException(String message, Throwable cause){
        super(message, cause);
    }

    public ComparableVarsRequiredRuntimeException(Class<?> objType){
        super("Object's type = [" + objType.getCanonicalName() + "], it's not a COMPARABLE Type! " +
                "If it is a Primitive type, it may be boolean!! , " +
                "If it is a Reference type, it may not implement java.lang.Comparable interface or java.util.Comparator interface!!");
    }
}
