package cn.argento.askia.langs;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TypeReference类型引用
 *
 * <p>由于类型擦除, 我们无法获取一个对象的泛型信息, 比如对于对象 {@code List<String> list} 当我们使用 {@code list.getClass()} 只能获取到 {@linkplain List java.util.List} , 而不知道它的具体类型, 一种解决方法就是在创建对象的时候主动维护该对象的泛型信息, 因此有了此类。
 *
 * <p> {@link Type} 类型接口支持获取父类的具体泛型类型, 比如 {@code public class A extends B<String>} 可以获取到 {@code B}的泛型类型 {@link String}, 而多亏了匿名类, 我们可以创建一个抽象的类的对象来屏蔽类的继承关系, 只需要 {@link TypeReference} 就是利用这种方式来存储泛型信息。
 *
 * <p>此类在很多 {@code Json} 框架如 {@code Gson} 等中都有定义, 包作者认为这些特定应该作为 {@code lang feature} 而被支持, 所以将此类提取出来并进行了扩展.
 *
 * <p>Java中的泛型类型支持一般有5种(泛型括号内能写的内容), 参考 {@link GenericType}：
 * <ul>
 *     <li>明确的类型：List&lt;String&gt; 、 List&lt;String[]&gt; 对应 {@link Type} 类型系统中的 {@link Class}</li>
 *     <li>参数化类型(嵌套泛型)：List&lt;Map&lt;String, Object&gt;&gt; 对应 {@link Type} 类型系统中的 {@link ParameterizedType} 接口</li>
 *     <li>泛型数组：List&lt;List&lt;String&gt;[]&gt; 对应 {@link Type} 类型系统中的 {@link GenericArrayType} 接口</li>
 *     <li>类型参数：List&lt;T&gt; 、 List&lt;T extends String&gt; 对应 {@link Type} 类型系统中的 {@link TypeVariable} 接口</li>
 *     <li>通配符类型：List&lt;?&gt; 、 List&lt;? extends String&gt; 、 List&lt;? super String&gt; 对应 {@link Type} 类型系统中的 {@link WildcardType} 接口</li>
 * </ul>
 *
 * <p>此类提供了两个核心方法来进行判别：{@link #match(GenericType)} 和 {@link #as(Class)} , 在明确是什么类型的情况下使用 {@link #as(Class)} 将类型转为特定的类型接口, 而在不明确的情况下则需要调用方考虑上面的所有 {@code 5} 种情况.
 * @param <T> 泛型类型
 */
public abstract class TypeReference<T> {

    private final ParameterizedType type;

    public enum GenericType{
        PARAMETERIZED_TYPE(ParameterizedType.class),
        GENERIC_ARRAY_TYPE(GenericArrayType.class),
        TYPE_VARIABLE(TypeVariable.class),
        WILDCARD_TYPE(WildcardType.class),
        BASE_TYPE(Class.class);


        public final Class<? extends Type> genericTypeClass;

        GenericType(Class<? extends Type> g){
            this.genericTypeClass = g;
        }
    }

    public TypeReference(){
        Type t = getClass().getGenericSuperclass();
        // 一般都是ParameterizedType
        if (t instanceof ParameterizedType){
            type = (ParameterizedType) t;
        }
        else{
            throw new RuntimeException("缺少泛型信息");
        }
    }

    public ParameterizedType getType(){
        return type;
    }

    public boolean match(GenericType genericType){
        final Type[] actualTypeArguments = type.getActualTypeArguments();
        // 因为TypeReference只有一个泛型参数，所以actualTypeArguments只有一个成员
        Type actualTypeArgument = actualTypeArguments[0];
        // 然后我们对这个actualTypeArgument进行判断
        return genericType.genericTypeClass.isAssignableFrom(actualTypeArgument.getClass());
    }

    public <GT extends Type> GT as(Class<GT> genericTypeClass){
        final Type[] actualTypeArguments = type.getActualTypeArguments();
        final Type actualTypeArgument = actualTypeArguments[0];
        return genericTypeClass.cast(actualTypeArgument);
    }

    public static boolean match(Type type, GenericType genericType){
        return genericType.genericTypeClass.isAssignableFrom(type.getClass());
    }

    public static <GT extends Type> GT as(Type type, Class<GT> genericTypeClass){
        return genericTypeClass.cast(type);
    }



    public static void main(String[] args) {
        TypeReference<List<Map<? extends String, Map<? super String, List<Object>[]>>>> typeReference = new TypeReference<List<Map<? extends String, Map<? super String, List<Object>[]>>>>() {};
        final ParameterizedType as = typeReference.as(ParameterizedType.class);
        System.out.println(as + ", " + as.getClass());
        System.out.println();
        final Type[] actualTypeArguments = as.getActualTypeArguments();
        System.out.println(Arrays.toString(actualTypeArguments) + ", " + actualTypeArguments[0].getClass());
        System.out.println();
        final ParameterizedType actualTypeArgument = (ParameterizedType) actualTypeArguments[0];
        final Type[] actualTypeArguments1 = actualTypeArgument.getActualTypeArguments();
        System.out.println();
        for (Type ac : actualTypeArguments1){
            System.out.println(ac + ", " + ac.getClass());
            if (ac instanceof WildcardType){
                WildcardType wildcardType = (WildcardType) ac;
                final Type[] lowerBounds = wildcardType.getLowerBounds();
                final Type[] upperBounds = wildcardType.getUpperBounds();
                System.out.println(Arrays.toString(lowerBounds));
                System.out.println(Arrays.toString(upperBounds));
            }
            if (ac instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) ac;
                final Type[] actualTypeArguments2 = parameterizedType.getActualTypeArguments();
                for (Type ad : actualTypeArguments2){
                    if (ad instanceof WildcardType){
                        WildcardType wildcardType2 = (WildcardType) ad;
                        final Type[] lowerBounds = wildcardType2.getLowerBounds();
                        final Type[] upperBounds = wildcardType2.getUpperBounds();
                        System.out.println(Arrays.toString(lowerBounds));
                        System.out.println(Arrays.toString(upperBounds));
                    }
                    if (ad instanceof GenericArrayType){
                        GenericArrayType genericArrayType = (GenericArrayType) ad;
                        final Type genericComponentType = genericArrayType.getGenericComponentType();
                        System.out.println(genericComponentType);
                    }
                }
            }
            System.out.println();
        }
    }
}

