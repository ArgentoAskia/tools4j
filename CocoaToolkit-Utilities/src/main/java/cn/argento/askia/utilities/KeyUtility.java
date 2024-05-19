package cn.argento.askia.utilities;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * UUID、CDKey生成类
 *
 * @author Askia
 * @version 1.3
 * @since 1.0
 */
public class KeyUtility {
    public static void main(String[] args) {
        System.out.println(newUUID());
        System.out.println(KeyUtility.newUUID(false, true));
        String s = newUUID();
        System.out.println(s);
        // fromString
        System.out.println(toUUID("12345678-abaaaaaaaaa-123-312311222"));
        System.out.println(toUUID("12345678-abcdaaaaaaaa"));
        System.out.println(toUUID("1234567899-abcaaaaaaaaa"));
        System.out.println(toUUID(0xaaaaaaaaL, 0, 0, 0, 0x0L));
    }
    private static final String INVALID_KEY_LENGTH_PARAMETER = "invalid key length parameter, key length parameter must >= 2";
    private static final String INVALID_SECTION_LENGTH_PARAMETER = "invalid section length parameter, section length parameter must >= 3";
    private static final String INVALID_SECTIONS_COUNT_PARAMETER = "节过多，最多只有5个-！";

    /**
     * 通过提供的字符串来格式化成UUID形式.
     * <p>
     *     理论上可以提供任意的字符串,方法都会将字符串转换成 {@code UUID} .但需要注意, {@code UUID} 本身是16进制内转换的,因此提供的
     *     内容必须是包含16进制数内容的字符串,如下面这个 {@code UUID:12345678-yyyy-kkkk-aaaa-555544442222} 就是非法的,
     *     因为超过最大的16进制数 {@code F}
     * <p>
     *     你可以使用带 {@code -} 的字符串, 方法将会以 {@code -} 作为分隔符，将字符串分割成多个节，这些节可能不够5个，也有可能超过5个，具体的处理方式：
     *     <ul>
     *         <li>刚好等于5个,则直接转化成 {@code UUID}, 至于每个节是否符合 {@code UUID} 的要求, 这个由真正负责生成 {@code UUID} 的方法{@link UUID#fromString(String)}决定</li>
     *         <li>小于5个,则会把文本不符合 {@code UUID:8-4-4-4-12} 排列的部分按照字符顺序拆成两部分处理,如:
     *         <pre>12345678-abcaaaaa1234-123-312311222</pre>
     *         其中第二部分不符合长度的abcaaaaaaaaa拆成两部分: abca-aaaa1234,至于后面的aaaa1234, {@link UUID#fromString(String)} 会决定保留低位的1234
     *         </li>
     *         <li>大于5个,则会抛出{@link InvalidParameterException}异常</li>
     *     </ul>
     *<p>
     *     特别注意,拆分不符合的 {@code UUID} 节的时候, 因为只采用了拆成两部分的算法,拆开的部分如果仍不符合节的大小规定，则由
     *     {@link UUID#fromString(String)} 来进行最小为取舍,见上面的abca-aaaa1234的拆法！<br>
     *     因此,对于一些比较极端的字符串参数,如:12345678-abcaaaaa1234,
     *     则方法只会拆成:12345678-abca-aaaa1234,
     *     然后剩余的缺省节会补0：12345678-abca-aaaa1234-0-0
     *     通过 {@link UUID#fromString(String)} 格式化之后就变成：12345678-abca-1234-0000-000000000000<br>
     *<p>
     *     <strong>但并不是每次都是按照开发者意愿来的,在测试 {@link UUID#fromString(String)} 时发现如果第二个节存在字母 {@code cd}, 生成的 {@code UUID} 会往下挪一个字符变成 {@code ef} </strong>
     *
     * @param fromString 一个待转换的字符串!
     * @return UUID字符串
     * @since 1.3
     */
    public static String toUUID(String fromString){
        // 1.先分割string,把节分开
        String[] split = fromString.split("-");
        if (split.length > 5){
            throw new InvalidParameterException(INVALID_SECTIONS_COUNT_PARAMETER);
        }
        String[] sessions = new String[]{"0", "0", "0", "0", "0"};
        // UUID每个节的长度
        int[] sessionLength = {8, 4, 4, 4, 12};
        int i = 0;
        // 没有-的情况,则split返回原本的字符串，这个时候split只有一个成员
        if (split.length == 1){
            // 初始话字符长度开头
            int subStrBegin = 0;
            // 初始化长度字符串长度
           int strLen = fromString.length();
           // fromString长度不满第一个节,剩余全部补零
           do{
               // 如果剩余字符长度小于节长度的，取最小值
               int min = Math.min(sessionLength[i], strLen);
               // 扣掉节长度！
               strLen -= sessionLength[i];
               // 取字串，转整数
               String sessionStr = fromString.substring(subStrBegin, subStrBegin + min);
               // 增长字串开始取值未知
               subStrBegin = subStrBegin + min;
               // 填入数组
               sessions[i++] = sessionStr;
           }while (strLen > 0);
        }else if (split.length == 5){
            for (String part :
                    split) {
                sessions[i++] = part;
            }
        }else {
            // 开始拆词
            // 需要拆的次数
            int splitTimes = sessions.length - split.length;
            int splited = 0;
            for (int j = 0; j < sessions.length; j++) {
                // 当分割的次数和遍历的次数超过的时候，这是split的文本已经全部被分割完毕了
                // 这个时候就要直接结束循环
                // 无论session是否被遍历满5次(填充满)
                // 未满的直接补0
                if (i >= split.length){
                    break;
                }
                // 拆完了,直接将剩下的全部加入即可
                if (splited >= splitTimes){
                    sessions[j] = split[i++];
                    continue;
                }
                // 开始拆
                if (split[i].length() > sessionLength[j]){
                    String sessionPart1 = split[i].substring(0, sessionLength[j]);
                    String sessionPart2 = split[i++].substring(sessionLength[j]);
                    sessions[j++] =  sessionPart1;
                    sessions[j] =  sessionPart2;
                    splited++;
                }else{
                    // 长度小的直接小于等于的节直接当作一个独立的节！
                    sessions[j] = split[i++];
                }
            }
        }
        System.out.println("拼接的节：" + sessions[0] + "-" + sessions[1] + "-" + sessions[2] + "-" + sessions[3] + "-" +sessions[4]);
        return UUID.fromString(sessions[0] + "-" + sessions[1] + "-" + sessions[2] + "-" + sessions[3] + "-" +sessions[4]).toString();
    }
    /**
     * 将五个整数转换为UUID.
     * <p>
     *     <strong>建议在输入参数的时候，以十六进制0x的方式输入，这样形成的UUID更加直观</strong>
     * <p>
     *     一个 {@code UUID} 的5个节长度如下：
     * <pre>{@code 1245b211-01f2-4a7f-9bec-91c925076476}<br>{@code    8    - 4  - 4  - 4  -    12}</pre>
     * 刚好凑成32位.
     * <p>
     *     - 如果输入的位数超过节长度，则保留整数的低位，如：
     * <pre>{@code 1245b211-01f22-4a7f2-9bec-91c925076476}<br>则会生成：<br>{@code 1245b211-1f22-a7f2-9bec-91c925076476}</pre>
     * <p>
     *     - 如果输入的位数不够,则会高位补0,如：
     * <pre>{@code 1245b211-01-7f2-ec-91c925076476}<br>则生成为:<br>{@code 1245b211-0001-07f2-00ec-91c925076476}</pre>
     *
     * @param hexNumberSession1 第一个节,为了可读性建议写类似于 {@code 0x123} 这样的16进制形式
     * @param hexNumberSession2 第二个节,为了可读性建议写类似于 {@code 0x123} 这样的16进制形式
     * @param hexNumberSession3 第三个节,为了可读性建议写类似于 {@code 0x123} 这样的16进制形式
     * @param hexNumberSession4 第四个节,为了可读性建议写类似于 {@code 0x123} 这样的16进制形式
     * @param hexNumberSession5 第五个节,为了可读性建议写类似于 {@code 0x123} 这样的16进制形式
     * @return 一个完整的 {@code UUID}, 如: {@code 1245b211-0001-07f2-00ec-91c925076476}
     * @since 1.3
     */
    public static String toUUID(long hexNumberSession1,
                                int hexNumberSession2,
                                int hexNumberSession3,
                                int hexNumberSession4,
                                Long hexNumberSession5){
        String hexSession1Str = Long.toHexString(hexNumberSession1);
        String hexSession2Str = Integer.toHexString(hexNumberSession2);
        String hexSession3Str = Integer.toHexString(hexNumberSession3);
        String hexSession4Str = Integer.toHexString(hexNumberSession4);
        String hexSession5Str = Long.toHexString(hexNumberSession5);
        return UUID.fromString(
                hexSession1Str + "-" + hexSession2Str + "-" + hexSession3Str + "-" + hexSession4Str + "-" +hexSession5Str
        ).toString();
    }

    /**
     * 生成带{@code -}的随机小写的{@code UUID}。
     *
     * @since 1.0
     * @return  UUID,一个32位的唯一序列，比如这种：{@code acd7e584-0970-41e9-a26b-538f95b053ae}
     */
    public static String newUUID(){
        return newUUID(true);
    }

    /**
     * 生成随机的小写的{@code UUID}。
     *
     * @param withHyphen 指定这个{@code UUID}是否带{@code -},{@code true}代表带，{@code false}代表不带
     * @return 32位的{@code UUID}
     * @since 1.0
     */
    public static String newUUID(boolean withHyphen){
        return newUUID(withHyphen, false);
    }

    /**
     * 生成随机UUID。
     *
     * @param withHyphen 生成的UUID是否带{@code -}符号
     * @param isUpperCase 生成的UUID是否大写
     * @return 32位的{@code UUID}
     * @since 1.0
     */
    public static String newUUID(boolean withHyphen, boolean isUpperCase){
        String uuid = UUID.randomUUID().toString();
        if (withHyphen && !isUpperCase){
            return uuid;
        }else if(withHyphen){
            return uuid.toUpperCase();
        }else {
            String[] split = uuid.split("-");
            String uuidWithoutHyphen = split[0] +
                    split[1] + split[2] +
                    split[3] + split[4];
            if (!isUpperCase){
                return uuidWithoutHyphen;
            }else{
                return uuidWithoutHyphen.toUpperCase();
            }
        }
    }

    private static final char[] charGaps =
            {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G',
                    'H', 'I', 'J', 'K', 'L', 'M', 'N',
                    'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'
            };
    private static final char[] numGaps =
            {
                    '0', '1', '2', '3',
                    '4', '5', '6',
                    '7', '8', '9',
            };

    /**
     * 生成随机的{@code CD-KEY}. 比如说：{@code 05K56D-46T753-7SG172-E16M5J-O2MWVA}。该方法随机生成4-6节，每节4-6的字符的{@code CD-KEY}（因此有9种组合可能），如下面的都是这个方法可能生成的：
     * <pre>
     *     <code>05K56D-46T753-7SG172-E16M5J-O2MWVA</code>
     *     <code>05K6D-46T53-7G172-E165J-O2MVA</code>
     *     <code>5K6D-4653-7G12-E15J-O2MA-FF72D</code>
     *     <code>HJ633-SS45S-BJCEF-GR456</code>
     * </pre>
     *
     * <p>
     *     对于下面这个{@code 05K56D-46T753-7SG172-E16M5J-O2MWVA},我们一般称这个{@code CD-KEY}有5个节({@code session}),每个节有6个字符<br/>
     *     每个节使用{@code -}分割
     * @return {@code CD-KEY}
     * @since 1.1
     */
    public static String newCDKey(){
        Random random = new Random();
        // randomKeyLength: 4-6
        int randomKeyLength = 4 + random.nextInt(3);
        // randomSectionLength: 4-6
        int randomSectionLength = 4 + random.nextInt(3);
        return newCDKey(randomKeyLength, randomSectionLength);
    }
    /**
     * 随机{@code CD-KEY},可以指定有多少个节和每个节的长度.
     *
     * @param keyLength CD-KEY有多少个节,长度必须大于{@code 1}
     * @param sectionLength 每个节有多长,长度必须大于{@code 2}
     * @return {@code CD-KEY}
     * @since 1.1
     */
    public static String newCDKey(int keyLength, int sectionLength){
        if (keyLength <= 1){
            throw new InvalidParameterException(INVALID_KEY_LENGTH_PARAMETER);
        }
        if (sectionLength <= 2){
            throw new InvalidParameterException(INVALID_SECTION_LENGTH_PARAMETER);
        }
        return newCDKey(keyLength, sectionLength, true);
    }

    /**
     * 随机{@code CD-KEY},可以指定有多少个节、每个节的长度和是否带{@code -}.
     *
     * @param keyLength 有多少个节
     * @param sectionLength 每个节长度
     * @param withHyphen 是否带{@code -}
     * @return 指定节数，长度的{@code CD-KEY}
     * @since 1.1
     */
    public static String newCDKey(int keyLength, int sectionLength, boolean withHyphen){
        Random random = new Random();
        StringBuilder cdk = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            for (int j = 0; j < sectionLength; j++) {
                if (random.nextBoolean()) {
                    int charGapsIndex = random.nextInt(charGaps.length);
                    char randomChar = charGaps[charGapsIndex];
                    cdk.append(randomChar);
                }else{
                    int numGapsIndex = random.nextInt(numGaps.length);
                    char randomNum = numGaps[numGapsIndex];
                    cdk.append(randomNum);
                }
            }
            if (withHyphen) {
                cdk.append("-");
            }
        }
        return cdk.substring(0, cdk.length() - 1);
    }

    /**
     * @since 1.3
     */
    private static final Random random = new Random();

    /**
     * @since 1.2
     */
    private static final char[] upperCharGaps = charGaps;
    private static final char[] lowerCharGaps =
            {
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                    'w', 'x', 'y', 'z'
            };
    private static final char[] simpleCharGaps =
            {
                    '!', '~', '@', '#', '$', '%',
                    '^', '&', '*', '?', '/', '\\',
                    '+', '-'
            };
    // 随机验证码
    /**
     * 验证码类型常量值！
     */
    public static final int VALID_CODE_NUMBER = 1;
    public static final int VALID_CODE_LOWER_ALPHABET = 2;
    public static final int VALID_CODE_UPPER_ALPHABET = 4;
    public static final int VALID_CODE_SIMPLE_CHARACTERS = 8;

    /**
     * 生成一个验证码.
     * <p>
     *     调用者可以使用
     * @param length 指定生成的验证码长度
     * @param codeType 指定生成的验证码类型, 参考 {@code VALID_CODE_*} 常量
     * @return 字符串验证码
     * @throws IllegalArgumentException 当输入的 {@code codeType}超过常量值范围 {@code 1- 15} 时，抛出该异常
     * @since 1.2
     */
    public static String newValidCode(int length, int codeType){
        if (codeType < 1 || codeType > 15 || length < 0){
            throw new IllegalArgumentException("codeType参数请使用 VALID_CODE_* 常量!多个值则需进行相加或者位或叠加！");
        }
        StringBuilder validCode = new StringBuilder();
        List<char[]> gaps = new ArrayList<>();
        if ((codeType & VALID_CODE_NUMBER) == VALID_CODE_NUMBER){
            gaps.add(numGaps);
        }
        if ((codeType & VALID_CODE_LOWER_ALPHABET) == VALID_CODE_LOWER_ALPHABET){
            gaps.add(lowerCharGaps);
        }
        if ((codeType & VALID_CODE_UPPER_ALPHABET) == VALID_CODE_UPPER_ALPHABET){
            gaps.add(upperCharGaps);
        }
        if ((codeType & VALID_CODE_SIMPLE_CHARACTERS) == VALID_CODE_SIMPLE_CHARACTERS){
            gaps.add(simpleCharGaps);
        }
        for (int i = 0; i < length; i++) {
            char[] typeGaps = gaps.get(random.nextInt(gaps.size()));
            validCode.append(typeGaps[random.nextInt(typeGaps.length)]);
        }
        return validCode.toString();
    }

    /**
     * 生成一个4-6位的简单验证码.
     *
     * @param codeType 指定生成的类型, 参考 {@code VALID_CODE_*}常量
     * @return 字符串验证码
     * @since 1.2
     */
    public static String newSimpleValidCode(int codeType){
        return newValidCode(4 + random.nextInt(3), codeType);
    }
    /**
     * 生成一个长度在4-6个数字的随机验证码.
     *
     * @return 字符串验证码
     * @since 1.2
     */
    public static String newNumberOnlyValidCode(){
       return newSimpleValidCode(VALID_CODE_NUMBER);
    }

    /**
     * 生成一个长度在4-6个字母的随机验证码.
     *
     * @return 字符串验证码
     * @since 1.2
     */
    public static String newAlphabetOnlyValidCode(){
        return newSimpleValidCode(VALID_CODE_LOWER_ALPHABET | VALID_CODE_UPPER_ALPHABET);
    }
}
