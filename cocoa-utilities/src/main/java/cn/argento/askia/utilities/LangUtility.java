package cn.argento.askia.utilities;

import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

/**
 * 此工具类对标Objects
 */
public class LangUtility {




    /**
     * 运行时动态修改枚举常量演示.
     * 目前能实现动态添加某个常量，动态修改已有常量值
     * 底层技术支持：Java反射
     * 原理：
     *      1. Java中所有的枚举类使用一个数组常量$VALUES来存储所有的枚举常量，通过修改$VALUES
     *         可以实现动态增加枚举量、动态修改枚举常量
     *      2. 动态添加的枚举量只能通过Enum类的valueOf()静态方法来获取，无法直接通过[枚举类.枚举常量]的形式来获取
     *         如果希望直接使用枚举类.枚举常量的方式获取，则需要修改字节码
     *      3. 枚举类的Class对象会缓存枚举类的所有枚举常量，由Class类的两个字段来存放：
     *          private volatile transient Map<String, T> enumConstantDirectory = null;
     *          private volatile transient T[] enumConstants = null;
     *          Enum类的valueOf()方法会调用Class类的包级私有方法：
     *              Map<String, T> enumConstantDirectory()方法
     *          这个方法会初始化enumConstantDirectory字段，并通过调用：
     *              T[] getEnumConstantsShared()方法
     *          同时初始化enumConstants字段
     *      4. getEnumConstantsShared()初始化enumConstants字段的原理是靠调用枚举类的
     *         静态合成方法values()获取枚举数组常量$VALUES实现的
     *      总结：
     *      因此要想实现动态增加枚举常量，需要 1.修改$VALUES变量，
     *      2.清空Class类中enumConstantDirectory字段和enumConstants字段，让系统重新调用静态合成方法values()
     *      触发更新
     *
     *      5.如何创建枚举类实例？Enum方法有一个protected的构造器，参数是String，int
     *      其中String代表枚举常量的常量名，如Size枚举类型有一个SMALL的枚举常量，则会传入”SMALL“字符串
     *      第二个int代表该枚举常量在数组$VALUES的index
     *      因此可以使用这个构造器，实际上自定义的枚举类型所有的构造器在编译成字节码之后都将会添加上这两个参数
     *      虽然这两个参数在Java代码中不可见，但在字节码中可见，如枚举类Size的构造器被定义为；
     *      private Size() 则编译成字节码时反编译结果会是：
     *      private <init>(Ljava/lang/String;I) ==> private Size(String, int);
     *      Java类型中使用I代表int
     *
     *
     *  参考：
     *  https://blog.51cto.com/u_16175447/11520817
     *  https://blog.csdn.net/u013813491/article/details/126511277
     *
     * @author Askia
     */
    public static <T extends Enum<T>> T addEnumConstant(Class<T> enumClass,
                                                        String enumName,
                                                        Class<?>[] paramsTypes,
                                                        Object[] params){
        Objects.requireNonNull(params);
        Objects.requireNonNull(paramsTypes);

        Object[] args = new Object[2 + params.length];
        // 1.get new Ordinal
        final int newOrdinal = getNewOrdinal(enumClass);
        // 2.设置前面的两个固定参数，枚举常量名和其对应的index
        args[0] = enumName;
        args[1] = newOrdinal;
        // 3.剩余的全部复制到args数组
        System.arraycopy(params, 0, args, 2, params.length);

        // 4.获取枚举类型的private构造器
        final Constructor<T> enumConstructor = getEnumConstructor(enumClass, paramsTypes);
        if (enumConstructor == null){
            // 无法获取构造器，失败
            return null;
        }

        // 5.创建新的枚举常量
        final T newEnumConstantObject = newEnumConstantObject(enumConstructor, args);
        if (newEnumConstantObject == null){
            // 无法创建枚举常量，失败
            return null;
        }
        System.out.println("新的枚举常量：" + newEnumConstantObject);

        // 6.添加到$VALUES内部
        addNewEnumConstantToValuesArray(enumClass, newEnumConstantObject);

        // 7.清除缓存
        clearEnumClassCache(enumClass);

        return newEnumConstantObject;
    }

    // 获取枚举构造器
    private static <T extends Enum<T>> Constructor<T> getEnumConstructor(Class<T> enumClass,
                                                                         Class<?>[] paramsTypes){
        // 1.判断构造器参数是否为空？
        Class<?>[] realParamsTypes = null;
        if (paramsTypes == null || paramsTypes.length == 0){
            realParamsTypes = new Class[2];
        }
        else{
            realParamsTypes = new Class[2 + paramsTypes.length];
            // 复制剩余参数到realParamsTypes
            System.arraycopy(paramsTypes, 0, realParamsTypes, 2, paramsTypes.length);
        }

        // 2.组合成真实的构造器，枚举类型的所有构造器（无论有参还是无参），默认都需要加上一个String、一个int参数，这些参数会
        // 传递给Enum类的构造器protected Enum(String name, int ordinal)，见doc的第五条
        realParamsTypes[0] = String.class;
        realParamsTypes[1] = int.class;

        // 3.获取枚举类型private构造器
        try {
            return enumClass.getDeclaredConstructor(realParamsTypes);
        } catch (NoSuchMethodException e) {
            // 找不到该构造器
            e.printStackTrace();
            return null;
        }
    }


    // 创建枚举常量对象
    private static <T extends Enum<T>> T newEnumConstantObject(Constructor<T> enumPrivateConstructor,
                                                               Object[] params){
        System.out.println("enum private constructor = [" + enumPrivateConstructor + "], accessible = " + enumPrivateConstructor.isAccessible());
        enumPrivateConstructor.setAccessible(true);
        try {
            // 由于SecurityManager，直接使用newInstance()可能会
            // 抛出IllegalArgumentException: Cannot reflectively create enum objects
            // 使用原始的ReflectionFactory来创建即可
            // return enumPrivateConstructor.newInstance(params);
            final Object newEnumConstant = ReflectionFactory.getReflectionFactory()
                    .newConstructorAccessor(enumPrivateConstructor).newInstance(params);
            return enumPrivateConstructor.getDeclaringClass().cast(newEnumConstant);
        } catch (InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 修改static final的字段，去除final，使其可修改
    private static void makeFieldAccessibleAndSetValue(Field field, Object belong, Object value){
        // 1.设置可访问
        field.setAccessible(true);
        // 2.去除Field的final修饰符，实现访问static final 的 $VALUES
        try {
            // 2.1 获取Field类的modifiers字段，该字段代表一个字段的修饰符
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            // 2.2 获取修饰符
            int modifiers = modifiersField.getInt(field);
            if (Modifier.isFinal(modifiers)) {
                // 2.3 去除final修饰符
                modifiers = modifiers & (~Modifier.FINAL);
                modifiersField.setInt(field, modifiers);
            }


            // 3. 设置属性值
            // 由于安全管理器（SecurityManager）的权限管理，部分实现直接使用下面的set()会抛出IllegalArgumentException异常，
            // 无法设置值，因此决定采用原始的ReflectionFactory来设置值
            // field.set(belong, value);
            final FieldAccessor fieldAccessor = ReflectionFactory.getReflectionFactory().newFieldAccessor(field, false);
            fieldAccessor.set(belong, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取新Enum constant的Ordinal
    private static <T extends Enum<T>> int getNewOrdinal(Class<T> enumClass){
        final T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants == null? 0: enumConstants.length;
    }

    // 添加新的枚举常量到$VALUES数组
    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> void addNewEnumConstantToValuesArray(Class<T> enumClass, T newEnumConstant){
        try {
            // 1. get static enum array $VALUES and make it Accessible
            final Field valuesField = enumClass.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);

            // 2. change to array and add new Enum Constant
            final T[] values = (T[])valuesField.get(null);
            System.out.println("values before: " + Arrays.toString(values));
            List<T> valuesList = new ArrayList<>(Arrays.asList(values));
            valuesList.add(newEnumConstant);
            // 引用类型的强制类型转换必须存在继承关系才行，也就是夫类型强制转换为子类型（要求父类型必须实际上是子类型）
            // 强制类型转换还能发张基本类型上，基本类型必须是同一类型才行！如整数的byte、int等进行强转，但无法将boolean强转为int
            // java的强制类型转换和C++的稍有区别！
            // 这也解释了为什么这里(T[]) valuesList.toArray();会报错，因为Object[]和T[]没有关系，而Object[]、T[]都继承自Object
            // 正确的做法应该是使用另一个toArray()重载体
//            final T[] newValues = (T[]) valuesList.toArray();
            final T[] newValues = valuesList.toArray((T[]) Array.newInstance(enumClass, 0));
            System.out.println("values after: " + Arrays.toString(newValues));

            // 3. modify new $VALUES, static value belong arg set null
            makeFieldAccessibleAndSetValue(valuesField, null, newValues);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // 清除枚举类的Class对象中相关枚举常量的缓存！
    private static void clearEnumClassCache(Class<? extends Enum<?>> enumClass){
        // jdk class enumConstantDirectory Field and enumConstants Field checked
        try {
            final Field enumConstantDirectoryMapField = Class.class.getDeclaredField("enumConstantDirectory");
            makeFieldAccessibleAndSetValue(enumConstantDirectoryMapField, enumClass, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            final Field enumConstantsField = Class.class.getDeclaredField("enumConstants");
            makeFieldAccessibleAndSetValue(enumConstantsField, enumClass, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }



    public static String bytesToHexString(byte[] bytes){
        StringBuilder hexString = new StringBuilder("");
        for (byte b: bytes) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1){
                hexString.append('0');
            }
            hexString.append(s);
        }
        return hexString.toString();
    }


    public static String byteToBinaryString(byte b) {
        // 将字节转换为整数
        int intVal = b & 0xFF;
        // 将整数转换为二进制字符串，并补全为 8 位
        return String.format("%8s", Integer.toBinaryString(intVal)).replace(' ', '0');
    }

    // 各类型转为字节数组
    public static byte[] hexStringToBytes(String hexStr){
        int len = hexStr.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i+=2) {
            // 获取前4位的二进制，然后右移动4位，再补低4位，最后强转即可得到byte
            data[i / 2] = (byte) (((Character.digit(hexStr.charAt(i), 16)) << 4) + ((Character.digit(hexStr.charAt(i + 1), 16))));
        }
        return data;
    }

    public static boolean isBinaryString(String binaryString){
        return FormatUtility.matchBinaryString(binaryString);
    }

    // binaryString 转为各种数值
    static <T extends Number> T binaryStringExchange(String binaryString, Class<T> tClass, boolean checkBinaryString){
        if (checkBinaryString){
            if (!FormatUtility.matchBinaryString(binaryString)){
                throw new IllegalArgumentException("参数：" + binaryString + "不是二进制字符串");
            }
        }
        if (tClass == byte.class || tClass == Byte.class){
            //  转byte
        }
        if (tClass == Short.class || tClass == short.class){

        }

        if (tClass == Integer.class || tClass == int.class){

        }

        if (tClass == Long.class || tClass == long.class){

        }
        if (tClass == Double.class || tClass == double.class){

        }
        if (tClass == Float.class || tClass == float.class){

        }
        throw new UnsupportedOperationException("暂未支持非Java核心库下的Number类子类实现，您的需要的实现：" + tClass + "代码无法处理");
    }
    public static byte binaryStringToByte(String binaryString){
        if (FormatUtility.matchBinaryString(binaryString)){
            return binaryStringExchange(binaryString, byte.class, false);
        }
        throw new IllegalArgumentException("参数：" + binaryString + "不是二进制字符串");
    }

    public static void main(String[] args) {
        int a = 2;
        System.out.println();
        byte[] bytes = { (byte) 0x9a, (byte) 0xBB, (byte) 0x45, (byte) 0x77 };
        for (byte b : bytes) {
            System.out.println(byteToBinaryString(b));
        }
    }

    public static boolean isNumber(Object obj){
        final Class<?> objClass = obj.getClass();
        return Number.class.isAssignableFrom(objClass);
    }

    // 判断是否是基础类型(基本类型+String+基本类型的包装器类+Number类的所有子类)

    /**
     * 广泛地判断一个对象是否是基础类型对象.
     * <p><b>习惯上, 我们认为 基础类型 = 8大基本类型 + 基本类型的包装器类 + 字符串 + void, 原因是这些类型即是基础有常用！</b>
     * <p>而广泛地进行判断的意思是: 在基础类型的基础上添加 Number类的所有实现类, 比如：BigInteger、BigDecimal等, String类及其实现类
     *
     * @param obj 任何对象
     * @return 如果该对象对应的类是一个广泛的基础类型对象, 则返回true, 否则返回false
     * @see LangUtility#isBaseTypeObject(Object)
     */
    public static boolean isBaseTypeObjectWidely(Object obj){
        if (obj.getClass().isPrimitive() || obj instanceof String || LangUtility.isNumber(obj)){
            return true;
        }
        if (Boolean.TYPE == obj.getClass() || Boolean.class == obj.getClass()){
            // boolean包装
            return true;
        }
        return Character.TYPE == obj.getClass() || Character.class == obj.getClass();
    }

    /**
     * 判断一个对象是否是基础类型对象
     * <p><b>习惯上, 我们认为 基础类型 = 7大基本类型 + 基本类型的包装器类 + 字符串, 原因是这些类型即是基础有常用！</b>
     * @param obj 任何对象
     * @return 如果该对象对应的类是一个基础类型对象, 则返回true, 否则返回false
     * @see LangUtility#isBaseTypeObjectWidely(Object)
     * @see LangUtility#isBaseType(Class)
     */
    public static boolean isBaseTypeObject(Object obj){
        Class<?> objClass = obj.getClass();
        return isBaseType(objClass);
    }

    /**
     * 判断一个对象是否是基本类型及其包装器类
     * @param obj 任何对象
     * @return 如果该对象是基本类型数据或者其包装器类，则返回true, 否则返回false
     * @since 2026.2.9
     */
    public static boolean isPrimitiveObjectWidely(Object obj){
        return isPrimitiveObject(obj) || isBoxingObject(obj);
    }

    /**
     * 判断一个对象是否是基本类型
     * @param obj 任何对象
     * @return  如果该对象是基本类型数据，则返回true, 否则返回false
     * @since 2026.2.9
     */
    public static boolean isPrimitiveObject(Object obj){
        return obj.getClass().isPrimitive();
    }

    /**
     * 判断一个对象是否是包装器类型
     * @param obj 任何对象
     * @return 如果该对象是包装器类型数据，则返回true, 否则返回false
     * @since 2026.2.9
     */
    public static boolean isBoxingObject(Object obj){
        return obj.getClass() == Integer.class || obj.getClass() == Byte.class || obj.getClass() == Short.class || obj.getClass() == Long.class ||
                obj.getClass() == Double.class || obj.getClass() == Float.class ||
                obj.getClass() == Boolean.class || obj.getClass() == Character.class;
    }

    /**
     * 判断一个对象是否是基础类型对象
     * <p><b>习惯上, 我们认为 基础类型 = 7大基本类型 + 基本类型的包装器类 + 字符串, 原因是这些类型即是基础有常用！</b>
     * @param cls class对象
     * @return 如果该对象对应的类是一个基础类型对象, 则返回true, 否则返回false
     * @see LangUtility#isBaseTypeObjectWidely(Object)
     * @see LangUtility#isBaseTypeObject(Object)
     */
    public static boolean isBaseType(Class<?> cls){
        if (cls.isPrimitive()){
            return true;
        }
        // 再次排除Void
        if (cls == Void.TYPE){
            return true;
        }
        // 包装器整数类型
        if (cls == Integer.class || cls == Byte.class || cls == Short.class || cls == Long.class){
            return true;
        }
        // 包装器浮点类型
        if (cls == Double.class || cls == Float.class){
            return true;
        }
        // 包装器字符类型和Boolean类型
        if (cls == Boolean.class || cls == Character.class){
            return true;
        }
        // 字符串类型
        return cls == String.class;
    }
    private static boolean isPrimitiveType(Class<?> cls){
        return cls.isPrimitive();
    }

    /**
     * 判断一个类型是否是包装器类型
     * @param cls 任何类型
     * @return 如果该类型是包装器类型数据，则返回true, 否则返回false
     * @since 2026.2.9
     */
    public static boolean isBoxingType(Class<?> cls){
        return cls == Integer.class || cls == Byte.class || cls == Short.class || cls == Long.class ||
                cls == Double.class || cls == Float.class ||
                cls == Boolean.class || cls== Character.class;
    }

    /**
     * 判别一种基本类型能否被包装成包装器类型, 或者包装器类型能否被解包成基本类型
     * @param type1 类型1，要转化的类型，可以是包装器类型或者基本类型，但一旦指定为基本类型则type2必须是包装器类型，反之亦然
     * @param type2 类型2，被转化成的类型，可以是包装器类型或者基本类型，但一旦指定为基本类型则type1必须是包装器类型，反之亦然
     * @param strictMode 是否遵守严格模式，即对应的类型必须只能是对应的包装器类，提供false时则只要对应的类型能够转化为对应的包装器或者对应的包装器能够转化为对应的基本类型即可, 也就是说, 所有可能产生精度损失的转换都不被允许
     * @return
     */
    public static boolean isAble2BoxingOrUnboxing(Class<?> type1, Class<?> type2, boolean strictMode){
        if (isPrimitiveType(type1) && isBoxingType(type2) || isPrimitiveType(type2) && isBoxingType(type1)){
            // 严格模式判别，如果严格模式都通过则非严格模式也肯定没有问题，能转
            boolean result = primitiveTypeBoxingTypeMatchStrict(type1, type2);
            if (!strictMode && !result){
                // 进行非严格模式判断
                // 单独处理处理boolean类型
                if (type1 == boolean.class || type1 == Boolean.class || type2 == boolean.class || type2 == Boolean.class){
                    // 我们遵守boolean类型0=false，1=true的原则，boolean类型兼容所有其他类型
                    // 其他类型非0就是true，0就是false，所以转到Boolean也兼容
                    return true;
                }
                // 正常的判断方法：类型1的字节量要小于类型2，相当于将小盒子放大盒子
                int primitiveSize1 = getPrimitiveSize(type1);
                int primitiveSize2 = getPrimitiveSize(type2);
                result = (primitiveSize1 <= primitiveSize2);
            }
            return result;
        }
        else {
            throw new IllegalArgumentException("提供的类型参数必须有一个是基本类型，另外一个是包装器类型，提供的第一个类型是：" + type1 + "第二个类型是：" + type2);
        }
    }
    private static boolean primitiveTypeBoxingTypeMatchStrict(Class<?> type1, Class<?> type2){
        if (type1 == int.class && type2 == Integer.class || type2 == int.class && type1 == Integer.class){
            return true;
        }
        if (type1 == long.class && type2 == Long.class || type2 == long.class && type1 == Long.class){
            return true;
        }
        if (type1 == short.class && type2 == Short.class || type2 == short.class && type1 == Short.class){
            return true;
        }
        if (type1 == byte.class && type2 == Byte.class || type2 == byte.class && type1 == Byte.class){
            return true;
        }
        // 浮点
        if (type1 == float.class && type2 == Float.class || type2 == float.class && type1 == Float.class){
            return true;
        }
        if (type1 == double.class && type2 == Double.class || type2 == double.class && type1 == Double.class){
            return true;
        }
        // Boolean Character
        if (type1 == char.class && type2 == Character.class || type2 == char.class && type1 == Character.class){
            return true;
        }
        if (type1 == boolean.class && type2 == Boolean.class || type2 == boolean.class && type1 == Boolean.class){
            return true;
        }
        // 其他情况全部失败
        return false;
    }

    private static int getPrimitiveSize(Class<?> type) {
        if (type == byte.class || type == Byte.class) return Byte.BYTES;
        if (type == short.class || type == Short.class) return Short.BYTES;
        if (type == char.class || type == Character.class) return Character.BYTES;
        if (type == int.class || type == Integer.class) return Integer.BYTES;
        if (type == long.class || type == Long.class) return Long.BYTES;
        if (type == float.class || type == Float.class) return Float.BYTES;
        if (type == double.class || type == Double.class) return Double.BYTES;
        throw new IllegalArgumentException("type参数不是基本类型或者是Boolean类型，由于平台限制，我们暂时无法统计Boolean的大小");
    }

    public static boolean isNull(Object object){
        return object == null;
    }


    public static boolean safetyUnboxing(Boolean booleanBoxingType, boolean value){
        Optional<Boolean> optionalBoolean = Optional.ofNullable(booleanBoxingType);
        return optionalBoolean.orElse(value);
    }

    public static boolean safetyUnboxing(Boolean booleanBoxingType){
        return safetyUnboxing(booleanBoxingType, Boolean.FALSE);
    }


    public static char safetyUnboxing(Character characterBoxingType, char value){
        Optional<Character> optionalBoolean = Optional.ofNullable(characterBoxingType);
        return optionalBoolean.orElse(value);
    }

    public static char safetyUnboxing(Character characterBoxingType){
        return safetyUnboxing(characterBoxingType, Character.MIN_VALUE);
    }

    public static int safetyUnboxing(Integer integerBoxingType, int value){
        return Optional.ofNullable(integerBoxingType).orElse(value);
    }

    public static int safetyUnboxing(Integer integerBoxingType){
        return safetyUnboxing(integerBoxingType, 0);
    }

    public static byte safetyUnboxing(Byte byteBoxingType, byte value){
        return Optional.ofNullable(byteBoxingType).orElse(value);
    }

    public static byte safetyUnboxing(Byte byteBoxingType){
        return safetyUnboxing(byteBoxingType, (byte) 0);
    }

    public static short safetyUnboxing(Short shortBoxingType, short value){
        return Optional.ofNullable(shortBoxingType).orElse(value);
    }

    public static short safetyUnboxing(Short shortBoxingType){
        return safetyUnboxing(shortBoxingType, (short) 0);
    }

    public static long safetyUnboxing(Long longBoxingType, long value){
        return Optional.ofNullable(longBoxingType).orElse(value);
    }

    public static long safetyUnboxing(Long longBoxingType){
        return safetyUnboxing(longBoxingType, (short) 0);
    }

    public static float safetyUnboxing(Float floatBoxingType, float value){
        return Optional.ofNullable(floatBoxingType).orElse(value);
    }
    public static float safetyUnboxing(Float floatBoxingType){
        return safetyUnboxing(floatBoxingType, 0.0F);
    }

    /**
     * 安全的基本类型拆箱方法.
     * <h3>Java的自动拆箱机制</h3>
     * <p>
     * @param floatBoxingType 浮点类型包装器类
     * @param value 如果无法拆箱时返回的默认值
     * @return 浮点类型值
     */
    public static double safetyUnboxing(Double floatBoxingType, double value){
        return Optional.ofNullable(floatBoxingType).orElse(value);
    }
    public static double safetyUnboxing(Double floatBoxingType){
        return safetyUnboxing(floatBoxingType, 0.0);
    }


    // 各类基本类型，包装器类，以及Number类的字符串转化方法
    private static final Map<Class<?>, Function<String, ?>> STRING_PARSER = new HashMap<>();
    static {
        STRING_PARSER.put(int.class, Integer::parseInt);
        STRING_PARSER.put(Integer.class, Integer::valueOf);
        STRING_PARSER.put(Byte.class, Byte::valueOf);
        STRING_PARSER.put(byte.class, Byte::parseByte);
        STRING_PARSER.put(short.class, Short::parseShort);
        STRING_PARSER.put(Short.class, Short::valueOf);
        STRING_PARSER.put(long.class, Long::parseLong);
        STRING_PARSER.put(Long.class, Long::valueOf);

        STRING_PARSER.put(Float.class, Float::valueOf);
        STRING_PARSER.put(float.class, Float::parseFloat);

        STRING_PARSER.put(Double.class, Double::valueOf);
        STRING_PARSER.put(double.class, Double::parseDouble);

        STRING_PARSER.put(Character.class, s -> {
            if (s.isEmpty()){
                return Character.valueOf(Character.MIN_VALUE);
            }
            else{
                return s.charAt(0);
            }
        });
        STRING_PARSER.put(char.class, s -> {
            if (s.isEmpty()){
                return Character.MIN_VALUE;
            }
            else{
                return s.charAt(0);
            }
        });
        STRING_PARSER.put(Boolean.class, Boolean::valueOf);
        STRING_PARSER.put(boolean.class, Boolean::parseBoolean);

        STRING_PARSER.put(String.class, String::valueOf);

    }

    /**
     * 提供所有基础类型，包装器类型，Number类的所有子类的字符串parse方法.
     *
     * <h3>字符串值类型转化问题</h3>
     * <p>我们都知道几乎所有的包装器类型除了{@link Character}都有对应的parseXXX()方法, 比如{@link  Integer#parseInt(String)}、{@link Double#parseDouble(String)},而开发中有一个很困难的点在于, 你已经得到一个{@link Class}对象了，你也确定他基本是一个基础类型或者是一个具体的值类型，并且该类型的对象的字符串表示形式你也有了，而此时你在纠结这个{@link Class}到底具体是什么类型，你应该使用什么parse()方法来转化这个字符串，正常的做法是你基本要把所有的值类型都判断一边，而本方法将这段枯燥的代码封装起来了</p>
     *
     * <h3>实现细节</h3>
     * <p>本方法使用一个map将所有字符串到值类型(包括字符串本身)的parse()缓存起来，调用本方法时, 将会从这个map中查询相应的parse(), 然后调用此parse()方法并返回转化后的结果，如果查找不到相应的parse(), 则抛出异常{@link UnsupportedOperationException}</p>
     * <p>目前方法支持的parse有：
     * <ol>
     *     <li>所有基础类型：int、byte、long、short、double、float、char、boolean，这些基础类型采用其对应的包装器类的{@code parse()}静态方法, 唯一特殊的char则判断提供的字符串是否是空串，如果是，则返回{@link Character#MIN_VALUE}, 否则返回<b>字符串的第一个字符</b></li>
     *     <li>对于包装器类型：{@link Character}的处理方式同上, 其他则调用对应包装器类型的{@code valueOf()}</li>
     *     <li>字符串类型：返回本身</li>
     * </ol>
     * </p>
     *
     *
     * @param str 字符串形式的值，可以为null
     * @param type 要转化为的类型
     * @return 实际转化对象, 如果str对象为null时, 则返回null
     * @param <T> 具体的可转化类型，比如包装器类型，基本类型，Number类等
     * @throws UnsupportedOperationException 当提供的值类型当前方法不支持时，抛出此异常
     * @since 2026.3.19
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String str, Class<T> type){
        if (str == null) return null;
        Function<String, ?> stringFunction = STRING_PARSER.get(type);
        if (stringFunction == null){
            throw new UnsupportedOperationException("系统不支持当前类型" + type + "的parse操作, 更多转换方式敬请期待");
        }
        Object apply = stringFunction.apply(str);
        System.out.println(apply.getClass());
        if (type.isPrimitive()){
            // 基本类型直接转即可
            return (T)apply;
        }
        else{
            return type.cast(apply);
        }
    }

}
