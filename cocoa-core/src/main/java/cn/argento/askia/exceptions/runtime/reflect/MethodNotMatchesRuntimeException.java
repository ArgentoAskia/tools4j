package cn.argento.askia.exceptions.runtime.reflect;

/**
 * 当获取到的 {@linkplain java.lang.reflect.Method Method} 对象对应的方法不满足一些条件的时候, 抛出此异常.<br>
 *
 * <ul>
 *     <li>异常类型：Runtime, no catch</li>
 *     <li>应用场景：如原本需要public static的方法，但是方法实际上是private</li>
 * </ul>
 */
public class MethodNotMatchesRuntimeException extends ReflectiveOperationRuntimeException{
}
