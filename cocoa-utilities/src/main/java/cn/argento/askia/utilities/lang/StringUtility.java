package cn.argento.askia.utilities.lang;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.classes.ResourceUtility;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Utility("字符串工具类")
public class StringUtility {

    private StringUtility() {
        throw new IllegalAccessError("StringUtility为工具类, 无法创建该类的对象");
    }

    public static boolean isEnglishStringLoop(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个字符串是否是纯英文字符串.
     * <p>此方法通过正则表达式实现判断字符串中是否仅包含英文字母.
     *
     * @param str 字符串
     * @return 如果仅包含英文字母则返回 {@code true} , 否则返回 {@code false}
     */
    public static boolean isEnglishString(String str) {
        return str != null && !str.isEmpty() && str.matches("^[A-Za-z]+$");
    }

    /**
     * 将一段纯英文的字符串的首字母大写.
     * <p>纯英文的字符串会被当作一个大字符串来处理, 只会将字符串的首字母变成大写(哪怕提供字符串包含空格或者并非是单词, 或者是一句英文), 例如：
     * <ul>
     *     <li>{@code abs} 将会被设置为 {@code Abs}</li>
     *     <li>{@code djlajlkwljljdlawl} 将会被处理为 {@code Djlajlkwljljdlawl}</li>
     *     <li>{@code hello my name Is Alice} 会被处理为 {@code Hello my name Is Alice}</li>
     * </ul>
     * <p>如果大字符串首字母已经是大写, 则会忽略, 比如提供 {@code Hello} 时将会原样返回 {@code Hello}
     * <p>如果大字符串不是一个只包含英文字母的字符串, 则同样会忽略返回自身, 比如提供 {@code 中华} 时将会原样返回 {@code 中华}
     *
     * <hr>使用方法：
     * <blockquote style="background-color:rgb(232,232,232)"><pre>
     *
     *
     *
     * </pre></blockquote>
     *
     * @param s  纯英文字符串(建议提供单词)
     * @return 经过首字母大写处理之后的单词
     */
    public static String upperFirstLetter(String s){
        Objects.requireNonNull(s);
        AssertionUtility.requireNotEquals(s.length(), 0);
        if (isEnglishString(s)){
            final char[] chars = s.toCharArray();
            if (Character.isLowerCase(chars[0])){
                final char upperCase = Character.toUpperCase(chars[0]);
                chars[0] = upperCase;
                return String.valueOf(chars);
            }
        }
        return s;
    }

    public static String lowerFirstLetter(String s){
        Objects.requireNonNull(s);
        AssertionUtility.requireNotEquals(s.length(), 0);
        final char[] chars = s.toCharArray();
        if (Character.isUpperCase(chars[0])){
            final char lowerCase = Character.toLowerCase(chars[0]);
            chars[0] = lowerCase;
        }
        return String.valueOf(chars);

    }

    /**
     *
     * @param s
     * @param locale 传递 {@code null} 将使用默认的 {@linkplain Locale#}
     * @return
     */
    public static String upper(String s, Locale locale){
        if (locale == null) {
            return s.toLowerCase();
        }
        else{
            return s.toLowerCase(locale);
        }
    }

    public static List<String> getWords(String caseString, List<Character> splits){
        Objects.requireNonNull(caseString);
        Queue<Character> queue = new LinkedList<>();
        List<String> retString = new ArrayList<>();
        StringBuilder stringBuilder = null;
        char[] chars = caseString.toCharArray();
        // 空字符串则返回空列表
        if (chars.length == 0){
            return Collections.emptyList();
        }
        for (char c : chars) {
            if (splits.contains(c)) {
                // 匹配则拿出来
                stringBuilder = new StringBuilder();
                while (!queue.isEmpty()) {
                    Character poll = queue.poll();
                    stringBuilder.append(poll);
                }
                retString.add(stringBuilder.toString());
                continue;
            }
            queue.offer(c);
        }
        return retString;
    }

    public static void main(String[] args) {
//        System.out.println(Character.isLetter('中'));
        final String s = simpleChinese2HuoXingChinese("购买商品前，请先仔细阅读商品订购须知。订单支付确认将被视為您已理解并接受了定购须知的内容，定金/尾款一经支付，概不退还。");
        final String s1 = simpleChinese2TraditionalChinese("购买商品前，请先仔细阅读商品订购须知。订单支付确认将被视為您已理解并接受了定购须知的内容，定金/尾款一经支付，概不退还。");
        final String s2 = traditionalChinese2HuoXingChinese(s1);
        System.out.println(s2);
        final Character[] a = traditionalChineseWordMapping.get("壹");
        System.out.println(Arrays.toString(a));
        System.out.println(s);
        System.out.println(s1);
    }


    /**
     * 超级trim(), 提供将多个空白保留成一个的参数而非完全去除, 和 {@link String#trim()}方法不同, 默认会去掉所有的空白而不仅仅是字符串左右两边的
     *
     * @param str
     * @param keepOnlyOneBlank
     * @param keepHeadTailOnlyOneBlank
     * @return
     */
    public static String trimSuperbly(String str, boolean keepOnlyOneBlank, boolean keepHeadTailOnlyOneBlank){
        if (!keepOnlyOneBlank && !keepHeadTailOnlyOneBlank){
            return str.trim().replaceAll("\\s+", "");
        }
        if (keepOnlyOneBlank && keepHeadTailOnlyOneBlank){
            return str.replaceAll("\\s+", " ");
        }
        if (keepOnlyOneBlank){
            return str.replaceAll("\\s+", " ").trim();
        }
        return " " + str.trim() + " ";
    }


    public static boolean isBlank(String s){
        return s == null || s.length() == 0;
    }


    // 简体转繁体0和火星文1
    private static Map<Character, Character[]> simpleChineseWordMapping;
    // 繁体转简体0和火星文1
    private static Map<Character, Character[]> traditionalChineseWordMapping;
    // 火星文转简体和繁体
    private static Map<Character, Character[]> huoxingChineseWordMapping;

    // todo 词库有重复, 需要修正(√)
    private static void initDictionary(){
        huoxingChineseWordMapping = new HashMap<>();
        traditionalChineseWordMapping = new HashMap<>();
        simpleChineseWordMapping = new HashMap<>();
        try {
            final List<String> huoxingStringList = ResourceUtility.readAllLinesFromClassPath("com.yumitoy.utilities", "huoxingChineseDictionary.txt");
            final List<String> traditionalStringList = ResourceUtility.readAllLinesFromClassPath("com.yumitoy.utilities", "TraditionalChineseDictionary.txt");
            final List<String> simpleStringList = ResourceUtility.readAllLinesFromClassPath("com.yumitoy.utilities", "SimpleChineseDictionary.txt");
            // 一定要有值
            assert huoxingStringList.size() > 0;
            assert traditionalStringList.size() > 0;
            assert simpleStringList.size() > 0;

            final String huoxingString = huoxingStringList.get(0);
            final String traditionalString = traditionalStringList.get(0);
            final String simpleString = simpleStringList.get(0);
            final int minSize = Math.min(huoxingString.length(), Math.min(traditionalString.length(), simpleString.length()));
            for (int i = 0; i < minSize; i++) {
                final char huoxingWord = huoxingString.charAt(i);
                final char traditionalWord = traditionalString.charAt(i);
                final char simpleWord = simpleString.charAt(i);
                huoxingChineseWordMapping.put(huoxingWord, new Character[]{simpleWord, traditionalWord});
                traditionalChineseWordMapping.put(traditionalWord, new Character[]{simpleWord, huoxingWord});
                simpleChineseWordMapping.put(simpleWord, new Character[]{traditionalWord, huoxingWord});
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        initDictionary();
    }

    /**
     * 中文转繁体。
     *
     * @param chinese 一段中文文本, 可以包含非简体中文字符, 不能转为繁体的字符将会被原样保留。
     * @return 转译之后的繁体中文
     * @apiNote 实现参考jf.homefont.cn/
     */
    public static String simpleChinese2TraditionalChinese(String chinese){
        return getTraditionalWord(chinese, simpleChineseWordMapping, 0);
    }
    /**
     * 简体中文转火星文.
     *
     * @param chinese
     * @return
     *
     * @apiNote 实现参考jf.homefont.cn/
     */
    public static String simpleChinese2HuoXingChinese(String chinese){
        return getTraditionalWord(chinese, simpleChineseWordMapping, 1);
    }
    private static String traditionalChinese2SimpleChinese(String chinese){
        return getTraditionalWord(chinese, traditionalChineseWordMapping, 0);
    }
    private static String traditionalChinese2HuoXingChinese(String chinese){
        return getTraditionalWord(chinese, traditionalChineseWordMapping, 1);
    }

    private static String getTraditionalWord(String chinese, Map<Character, Character[]> wordMapping, int mappingIndex){
        StringBuilder stringBuilder = new StringBuilder();
        final int length = chinese.length();
        for (int i = 0; i < length; i++){
            final char c = chinese.charAt(i);
            final Character[] traditionalWord = wordMapping.get(c);
            if (traditionalWord != null && traditionalWord.length == 2){
                stringBuilder.append(traditionalWord[mappingIndex]);
            }
            else{
                // 否则代表无法转为繁体, 则我们转会给自己即可
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 格式化字符串拼接.
     * <p>此方法会将列表中的内容格式化拼接在一起返回, 其中你需要提供字符串列表, 拼接前缀，拼接后缀，分割符号，方法会将字符串按照 {@code 拼接前缀 成员1 分割符号 成员2 ... 拼接后缀} 的形式进行拼接
     * <p>例如，你有一个字符串列表: {@code [A, B, C, D, E, F, G, H, I, J]}，拼接前缀为 {@code |} ，拼接后缀为 {@code |} ，分割符号为 {@code -}，则拼接结果是 {@code |A-B-C-D-E-F-G-H-I-J|}
     *
     * @param joinObjs 对象列表, 会自动调用 {@link Object#toString()} 来获取字符串
     * @param prefix 拼接前缀, 可提供空字符串
     * @param suffix 拼接后缀, 可提供空字符串
     * @param delimiter 分隔符号, 提供空字符串将代表没有分割符号
     * @return 格式化拼接完成的字符串
     * @apiNote 此方法的功能包装自 {@linkplain StringJoiner StringJoiner类} , 后期可能需要考虑字符串拼接性能问题和并行拼接支持
     */
    public static <T> String joinStrings(List<T> joinObjs, String prefix, String suffix, String delimiter){
        if (prefix == null){
            prefix = "";
        }
        if (suffix == null){
            suffix = "";
        }
        StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);
        for (T obj :
                joinObjs) {
            if (obj != null){
                stringJoiner.add(obj.toString());
            }
        }
        return stringJoiner.toString();
    }


    /**
     * 制作带缩略符号的字符串.
     * <p>此方法用于将一个很长的字符串缩略显示。比如有一个字符串：{@code AAAAAAABBBBBBBCCCCCCC...(此处省略326个字符)}, 你的需求是显示其中的 {@code AAAAAAA}作为缩略显示时就可以考虑此方法
     * <p>你需要提供源字符串（或者是字符串的片段），指定所使用的缩略符号，以及指定缩略限制数，例如上面的例子中，我们使用{@code (truncated)...}作为缩略符号，限制显示 {@code AAAAAAA} 有7个字符，则会返回结果：{@code AAAAAAA(truncated)...}
     * @param src 字符串源
     * @param ellipsis 省略标识
     * @param limits 限制显示的数量
     * @return 带缩略符号的字符串
     */
    public static String makeEllipsisString(String src, String ellipsis, Integer limits){
        final String substring = src.substring(0, limits);
        return substring + ellipsis;
    }

    /**
     * 字符串分割式拼接.
     * <p>此方法用于复杂的字符串【切割-合并】操作，它会先将字符串按照特定的字符进行切割，然后将切割得到的两个分组，两两进行合并.
     * <p>例如：您有下面两个字符串：
     * <ol>
     *     <li>{@code 85b302f9-0a86-4e00-ba0c-6c3c70fb40f3}</li>
     *     <li>{@code e3164096-9467-4849-a74d-d19e0b8279b9}</li>
     * </ol>
     * 你需要将这两个字符串按照-进行分割, 分割成功之后，将其中的 {@code 85b302f9} 和 {@code e3164096} 进行合并, 你将会得到一个列表：
     * <p>{@code [85b302f9e3164096, 0a869467, 4e004849, ba0ca74d, 6c3c70fb40f3d19e0b8279b9]}
     *
     * @param str1  字符串1
     * @param separator1 分割字符串1所需的分隔符
     * @param str2 字符串2
     * @param separator2 分割字符串2所需的分割符
     * @param concatStr 合并时使用的合并符号
     * @return 合并结束之后的结果
     * @since 2026.2.2
     */
    public static List<String> concat(String str1, String separator1, String str2, String separator2, String concatStr){
        String[] splitStr1 = str1.split(separator1);
        String[] splitStr2 = str2.split(separator2);
        ArrayList<String> stringsList1 = new ArrayList<>(Arrays.asList(splitStr1));
        ArrayList<String> stringsList2 = new ArrayList<>(Arrays.asList(splitStr2));
        return concat(stringsList1, stringsList2, concatStr);
    }
    /**
     * 拼接两个列表中的文本.
     * <p>规则如下，如我有两个列表：
     * <ol>
     *     <li>[Apple, Blue, Just, do ,have, now]</li>
     *     <li>[what, means, happen, after]</li>
     * </ol>
     * 使用-进行拼接，则拼接过后返回的列表：[Apple-what, Blue-means, Just-happen, do-after, have, now]
     *
     * <p><b>注意，此方法为内部API, 仅限 {@code com.yumitoy.utilities} 包使用</b>
     *
     * @param list1 列表1
     * @param list2 列表2
     * @param concatStr 拼接字符串
     * @return 拼接完成的列表
     */
    static List<String> concat(List<String> list1, List<String> list2, String concatStr){
        return IntStream.range(0, Math.max(list1.size(), list2.size()))
                .mapToObj(i -> {
                    String s1 = i < list1.size() ? list1.get(i) : "";
                    String s2 = i < list2.size() ? list2.get(i) : "";
                    if ("".equals(s1)){
                        return s2;
                    }
                    else if ("".equals(s2)){
                        return s1;
                    }
                    else{
                        return s1 + concatStr +s2;
                    }
                })
                .collect(Collectors.toList());
    }

    // find out what means AST?

    /**
     * 找出两个有序的字符串数组中的共同部分、第一个字符串数组有而第二个字符串数组没有的和第一个字符串数组没有而第二个字符串数组有的.
     * <p>此方法是Linux系统comm指令的个人实现, 作者某天为了搞定comm的原理而摸出来的代码、
     * <p>
     * @param str1Array 字符串数组1
     * @param str2Array 字符串数组2
     * @param result1 第一个字符串数组有而第二个字符串数组没有的字符串结果集
     * @param result2 第一个字符串数组没有而第二个字符串数组有的字符串结果集
     * @param commonResult 公有的部分字符串结果集合
     * @since 2026.2.2
     */
    public static void compareCommonLines(final String[] str1Array, final String[] str2Array,
                               List<String> result1,  List<String> result2,  List<String> commonResult){
        // 判断是否有序，无序返回false;
        int strArray1Pointer = 0;
        int strArray2Pointer = 0;
        Set<String> commonResultList = new HashSet<>();
        Set<String> str1ArrayAlongList = new HashSet<>();
        Set<String> str2ArrayAlongList = new HashSet<>();
        while (strArray1Pointer < str1Array.length && strArray2Pointer < str2Array.length){
            if (str1Array[strArray1Pointer].equals(str2Array[strArray2Pointer])){
                commonResultList.add(str1Array[strArray1Pointer]);
                // 跳过相同的部分
                // strArray1Pointer + 1可能会引发ArrayIndexOfOutBoundException，所以需要判断
                if (strArray1Pointer + 1 < str1Array.length){
                    while(str1Array[strArray1Pointer].equals(str1Array[strArray1Pointer + 1])){
                        strArray1Pointer++;
                    }
                }
                if (strArray2Pointer + 1 < str2Array.length){
                    while(str2Array[strArray2Pointer].equals(str2Array[strArray2Pointer + 1])){
                        strArray2Pointer++;
                    }
                }
                strArray2Pointer++;
                strArray1Pointer++;
            }
            else if (str1Array[strArray1Pointer].compareTo(str2Array[strArray2Pointer]) < 0){
                str1ArrayAlongList.add(str1Array[strArray1Pointer]);
                strArray1Pointer++;
            }
            else if (str1Array[strArray1Pointer].compareTo(str2Array[strArray2Pointer]) > 0){
                str2ArrayAlongList.add(str2Array[strArray2Pointer]);
                strArray2Pointer++;
            }
        }
        // 下面的if满足其一
        if (strArray1Pointer < str1Array.length){
            str1ArrayAlongList.addAll(Arrays.asList(str1Array).subList(strArray1Pointer, str1Array.length));
        }
        if (strArray2Pointer < str2Array.length){
            str2ArrayAlongList.addAll(Arrays.asList(str2Array).subList(strArray2Pointer, str2Array.length));
        }
        // 结果返回
        commonResult.addAll(commonResultList);
        result2.addAll(str2ArrayAlongList);
        result1.addAll(str1ArrayAlongList);
    }
}
