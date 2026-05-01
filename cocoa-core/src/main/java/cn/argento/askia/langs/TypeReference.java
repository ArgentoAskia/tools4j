package cn.argento.askia.langs;


import java.lang.reflect.*;
import java.util.ArrayList;
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

    // 储存 TypeReference<T>
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

    /**
     * 获取TypeReference包装内的T
     * @return
     */
    public Type getType(){
        final Type[] actualTypeArguments = type.getActualTypeArguments();
        return actualTypeArguments[0];
    }

    public boolean match(GenericType genericType){
        final Type[] actualTypeArguments = type.getActualTypeArguments();
        // 因为TypeReference只有一个泛型参数，所以actualTypeArguments只有一个成员
        Type actualTypeArgument = actualTypeArguments[0];
        System.out.println(actualTypeArgument.getClass());
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

    /**
     * 判断两个类型是否兼容, 即 {@code type} 的对象能否放入 {@code type2}
     * @param type type1
     * @param type2 type2
     * @return {@code true}如果类型满足 {@code type} 放入 {@code type2} 则返回 {@code true} ,否则返回 {@code false}
     */
    @SuppressWarnings("all")
    public static boolean typeMatch(Type type, Type type2, boolean matchExtends){
        // 基础类型匹配：String CharSequence
        if (TypeReference.match(type, GenericType.BASE_TYPE) && (TypeReference.match(type2, GenericType.BASE_TYPE))){
            // 两个都是基本类型，则判断是否是继承
            final Class<?> typeClass1 = TypeReference.as(type, Class.class);
            final Class<?> typeClass2 = TypeReference.as(type2, Class.class);
            if (matchExtends){
                return typeClass2.isAssignableFrom(typeClass1);
            }
            else{
                return typeClass1 == typeClass2;
            }
        }
        // 两个都是泛型参数类型
        if (TypeReference.match(type, GenericType.PARAMETERIZED_TYPE) && TypeReference.match(type2, GenericType.PARAMETERIZED_TYPE)){
            // 判断是否是简单的泛型参数类型, 比如：List<String> 、 List<CharSequence>
            final ParameterizedType parameterizedType1 = TypeReference.as(type, ParameterizedType.class);
            final ParameterizedType parameterizedType2 = TypeReference.as(type2, ParameterizedType.class);
            // 我们先判断Raw Type是否兼容, 比如 List和 ArrayList
            final Type rawType1 = parameterizedType1.getRawType();
            final Type rawType2 = parameterizedType2.getRawType();
            if (typeMatch(rawType1, rawType2, true)) {
                // ok, 类型符合, 则考虑泛型
                final Type[] actualTypeArguments1 = parameterizedType1.getActualTypeArguments();
                final Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
                if (actualTypeArguments1.length == actualTypeArguments2.length){
                    int argsCount = actualTypeArguments1.length;
                    boolean match = true;
                    for (int i = 0; i < argsCount; i++) {
                        match = typeMatch(actualTypeArguments1[i], actualTypeArguments2[i], false);
                        // 只要适配就break
                        if (!match){
                            break;
                        }
                    }
                    return match;
                }
                else{
                    // todo 类型参数数量不符合(需要尝试解析 public class A<T> extends B<? extends T, String>)这种情况
                    return false;
                }
            }
            else{
                // raw type不符合则不进行
                return false;
            }
        }
        // 一个是泛型参数类型，另外一个有可能是raw type
        if (TypeReference.match(type, GenericType.PARAMETERIZED_TYPE) && TypeReference.match(type2, GenericType.BASE_TYPE) ||
                TypeReference.match(type2, GenericType.PARAMETERIZED_TYPE) && TypeReference.match(type, GenericType.BASE_TYPE)){
            Class<?> rawType = null;
            ParameterizedType parameterizedType = null;
            if (TypeReference.match(type, GenericType.PARAMETERIZED_TYPE) && TypeReference.match(type2, GenericType.BASE_TYPE)){
                parameterizedType = TypeReference.as(type, ParameterizedType.class);
                rawType = TypeReference.as(type2, Class.class);
            }
            else{
                parameterizedType = TypeReference.as(type2, ParameterizedType.class);
                rawType = TypeReference.as(type, Class.class);
            }
            // 判断是否是rawType
            if (rawType.getTypeParameters().length > 0){
                // 再判断是否是子类
                final Type rawType1 = parameterizedType.getRawType();
                if (TypeReference.match(rawType1, GenericType.BASE_TYPE)){
                    //  rawType 是 parameterizedType的子类
                    return rawType.isAssignableFrom(TypeReference.as(rawType1, Class.class));
                }
            }
            // todo class A extends Comparable<A>的可能

            return false;
        }
        // 泛型数组匹配：List<String>[] List<CharSequence>[] 获取泛型数组类型：List<String> 、 List<CharSequence>
        if (TypeReference.match(type, GenericType.GENERIC_ARRAY_TYPE) && TypeReference.match(type2, GenericType.GENERIC_ARRAY_TYPE)){
            List<? extends Map<? extends String, Object>> maps1 = new ArrayList<>();
            List<? extends Map> maps = maps1;
            // 都是泛型数组类型
            final GenericArrayType genericArrayType1 = TypeReference.as(type, GenericArrayType.class);
            final GenericArrayType genericArrayType2 = TypeReference.as(type2, GenericArrayType.class);
            // 获取泛型类
            final Type genericComponentType = genericArrayType1.getGenericComponentType();
            final Type genericComponentType2 = genericArrayType2.getGenericComponentType();
            return typeMatch(genericComponentType, genericComponentType2, true);
        }
        // 泛型参数T：List<T> 、 List<D extends String>, 默认靠Equals来判断
        if (TypeReference.match(type, GenericType.TYPE_VARIABLE) && TypeReference.match(type2, GenericType.TYPE_VARIABLE)){
            final TypeVariable<?> typeVariable1 = TypeReference.as(type, TypeVariable.class);
            final TypeVariable<?> typeVariable2 = TypeReference.as(type2, TypeVariable.class);
            return typeVariable1.equals(typeVariable2);
        }
        // 通配符类型比如<? extends Map>、<? extends List<String>>
        if (TypeReference.match(type, GenericType.WILDCARD_TYPE) && TypeReference.match(type2, GenericType.WILDCARD_TYPE)){
            final WildcardType wildcardType1 = TypeReference.as(type, WildcardType.class);
            final WildcardType wildcardType2 = TypeReference.as(type2, WildcardType.class);
            if (wildcardType1.equals(wildcardType2)){
                // 如果 wildcardType1 和 wildcardType2相等，则类型肯定兼容
                return true;
            }
            else{
                // 规则表：
                //   L             R
                //  无界          任意通配符 | 类型             true
                //  无界          无界                        true
                // ? extends A  ? extends B   B extends A    true
                // ? super A    ? super B     A extends B    true
                // ? extends A  ? super B     只有A是Object才会返回true
                // ? super A    ? extends B                  false
                Type type1LowerBound = null;
                Type type1UpperBound = null;
                Type type2LowerBound = null;
                Type type2UpperBound = null;
                final Type[] lowerBounds1 = wildcardType1.getLowerBounds();
                final Type[] upperBounds1 = wildcardType1.getUpperBounds();
                if (lowerBounds1.length == 1){
                    type1LowerBound = lowerBounds1[0];
                }
                if (upperBounds1.length == 1){
                    type1UpperBound = upperBounds1[0];
                }
                final Type[] lowerBounds2 = wildcardType2.getLowerBounds();
                final Type[] upperBounds2 = wildcardType2.getUpperBounds();
                if (lowerBounds2.length == 1){
                    type2LowerBound = lowerBounds2[0];
                }
                if (upperBounds2.length == 1){
                    type2UpperBound = upperBounds2[0];
                }
                // 没有下界，上界是Object(无界)
                if (type1LowerBound == null && type1UpperBound == Object.class ||
                    type2LowerBound == null && type2UpperBound == Object.class){
                    // t1 无界，则不管t2的界限
                    return true;
                }
                // 两个都是只有上界没有下界
                if (type1LowerBound == null && type2LowerBound == null){
                    return typeMatch(type1UpperBound, type2UpperBound, true);
                }
                // 两个都是super
                if ((type1LowerBound != null && type1UpperBound == Object.class) &&
                        (type2LowerBound != null && type2UpperBound == Object.class)){
                    return typeMatch(type1UpperBound, type2UpperBound, true);
                }

            }
        }
        if (TypeReference.match(type, GenericType.BASE_TYPE) && TypeReference.match(type2, GenericType.WILDCARD_TYPE) ||
                TypeReference.match(type2, GenericType.BASE_TYPE) && TypeReference.match(type, GenericType.WILDCARD_TYPE)){
            Class<?> baseType = null;
            WildcardType wildcardType = null;
            if (TypeReference.match(type, GenericType.BASE_TYPE) && TypeReference.match(type2, GenericType.WILDCARD_TYPE)){
                baseType = TypeReference.as(type, Class.class);
                wildcardType = TypeReference.as(type2, WildcardType.class);
            }
            else{
                baseType = TypeReference.as(type2, Class.class);
                wildcardType = TypeReference.as(type, WildcardType.class);
            }
            // wildcardType 必须覆盖baseType
            final Type[] upperBounds = wildcardType.getUpperBounds();
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            Type upper = null;
            Type lower = null;
            if (upperBounds.length == 1){
                upper = upperBounds[0];
            }
            if (lowerBounds.length == 1){
                lower = lowerBounds[0];
            }
            if (lower == null && upper != null){
                // 只有上界, 上界是否覆盖
                if (TypeReference.match(upper, GenericType.BASE_TYPE)){
                    return TypeReference.as(upper, Class.class).isAssignableFrom(baseType);
                }
                else{
                    return typeMatch(type, type2, true);
                }
            }
            else if (lower != null && upper != null){
                // 有上界和下界
                // 只有上界, 上界是否覆盖
                if (TypeReference.match(upper, GenericType.BASE_TYPE) && TypeReference.match(lower, GenericType.BASE_TYPE)){
                    return TypeReference.as(upper, Class.class).isAssignableFrom(baseType) && baseType.isAssignableFrom(TypeReference.as(lower, Class.class));
                }
                else{
                    return typeMatch(type, type2, true);
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        TypeReference<List<?>> reference = new TypeReference<List<?>>() {};
        TypeReference<ArrayList<String>> reference1 = new TypeReference<ArrayList<String>>() {};
        System.out.println(TypeReference.typeMatch(reference1.getType(), reference.getType(), true));
        System.out.println();
//        final Class as1 = reference.as(Class.class);
//        System.out.println("123" + as1 + ", " + Arrays.toString(as1.getTypeParameters()) + ", " + as1.getClass());
//        System.out.println(CharSequence[].class.isAssignableFrom(String[].class));
//        TypeReference<List<Map<? extends String, Map<? super String, List<Object>[]>>>> typeReference = new TypeReference<List<Map<? extends String, Map<? super String, List<Object>[]>>>>() {};
//        final ParameterizedType as = typeReference.as(ParameterizedType.class);
//        System.out.println(as + ", " + as.getClass());
//        System.out.println();
//        final Type[] actualTypeArguments = as.getActualTypeArguments();
//        System.out.println(Arrays.toString(actualTypeArguments) + ", " + actualTypeArguments[0].getClass());
//        System.out.println();
//        final ParameterizedType actualTypeArgument = (ParameterizedType) actualTypeArguments[0];
//        final Type[] actualTypeArguments1 = actualTypeArgument.getActualTypeArguments();
//        System.out.println();
//        for (Type ac : actualTypeArguments1){
//            System.out.println(ac + ", " + ac.getClass());
//            if (ac instanceof WildcardType){
//                WildcardType wildcardType = (WildcardType) ac;
//                final Type[] lowerBounds = wildcardType.getLowerBounds();
//                final Type[] upperBounds = wildcardType.getUpperBounds();
//                System.out.println(Arrays.toString(lowerBounds));
//                System.out.println(Arrays.toString(upperBounds));
//            }
//            if (ac instanceof ParameterizedType){
//                ParameterizedType parameterizedType = (ParameterizedType) ac;
//                final Type[] actualTypeArguments2 = parameterizedType.getActualTypeArguments();
//                for (Type ad : actualTypeArguments2){
//                    if (ad instanceof WildcardType){
//                        WildcardType wildcardType2 = (WildcardType) ad;
//                        final Type[] lowerBounds = wildcardType2.getLowerBounds();
//                        final Type[] upperBounds = wildcardType2.getUpperBounds();
//                        System.out.println(Arrays.toString(lowerBounds));
//                        System.out.println(Arrays.toString(upperBounds));
//                    }
//                    if (ad instanceof GenericArrayType){
//                        GenericArrayType genericArrayType = (GenericArrayType) ad;
//                        final Type genericComponentType = genericArrayType.getGenericComponentType();
//                        System.out.println(genericComponentType + ", " + genericComponentType.getClass());
//                    }
//                }
//            }
//            System.out.println();
//        }
    }
}

