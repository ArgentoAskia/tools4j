package cn.argento.askia.exceptions.runtime.reflect;

/**
 * 当一个类的构造器理论上绝对存在, 但是使用反射Api获取不到的时候, 抛出此异常.<br>
 * <p>
 * 异常类型：Runtime, no catch <br>
 * <p>
 * 应用场景：
 * <ol>
 *     <li></li>
 *     <li>应用场景：</li>
 * </ol>
 */
public class ConstructorNotFoundRuntimeException extends ReflectiveOperationRuntimeException{
    private Class<?> clazz;
    private Class<?>[] argsType;

    public ConstructorNotFoundRuntimeException(Class<?> clazz, Class<?>[] argsType){
        super(clazz.getSimpleName() + "(" + argTypeString(argsType) + ")" + " not found!");
    }

    private static String argTypeString(Class<?>[] argsType){
        StringBuilder stringBuilder = new StringBuilder(argsType[0].getSimpleName());
        for (int i = 1; i < argsType.length; i++) {
            stringBuilder.append(", ").append(argsType[i].getSimpleName());
        }
        return stringBuilder.toString();
    }


}
