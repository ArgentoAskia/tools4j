package cn.argento.askia.utilities.text;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.lang.StringUtility;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * 格式化工具类.
 * <p>提供数据格式化(格式化、获取等)和格式化数据判别两大功能, 此类中:
 * <ul>
 *     <li>所有将数据按照某个格式进行格式化的方法均以format开头, 比如：formatTime代表格式化时间</li>
 *     <li>所有的判别方法均以match开头, 比如matchEmail代表提供的字符串是否符合Email格式</li>
 * </ul>
 * @author Askia
 */
@Utility("内容格式化工具类")
public class FormatUtility {
    private FormatUtility() {
        throw new IllegalAccessError("FormatUtility为工具类, 无法创建该类的对象");
    }
    // =============================== 正则表达式格式化部分 =====================================
    // ==================== 预先缓存好的Pattern ====================
    // 采用文本表达式+预先缓存的Pattern对象的方式
    // 邮件判别
    private static final String emailExp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern emailExpPattern = Pattern.compile(emailExp);
    /**
     * 判断字符串是否是一个合法的 {@code Email}
     * @param emailStr 邮箱字符串
     * @return 如果符合邮箱字符串的格式，则返回 {@code true}，否则返回{@code false}
     */
    public static boolean matchEmail(String emailStr){
        return emailExpPattern.matcher(emailStr).matches();
    }

    // 严格小驼峰
    private static final String camelCaseExp = "^[a-z]+([A-Z][a-z0-9]+)*$"; // 小驼峰
    private static final Pattern camelCaseExpPattern = Pattern.compile(camelCaseExp);
    // 非严格小驼峰
    private static final String camelCaseNotStrictExp = "^[a-z][a-zA-Z0-9]*$";
    private static final Pattern camelCaseNotStrictExpPattern = Pattern.compile(camelCaseNotStrictExp);
    /**
     * 判断字符是否符合严格小驼峰命名法.
     * @param str 字符串
     * @return 如果符合则输出{@code true}, 否则输出{@code false}
     */
    public static boolean matchCamelCasePattern(String str){
        // 检查驼峰命名法的正则表达式
        return camelCaseExpPattern.matcher(str).matches();
    }
    /**
     * 判断字符是否符合小驼峰命名法.
     * <p>此方法参数二可以指定是否使用严格小驼峰</p>
     * @param str 字符串
     * @param strictMode 是否开启严格模式
     * @return 如果符合则输出{@code true}, 否则输出{@code false}
     */
    public static boolean matchCamelCasePattern(String str, boolean strictMode){
        return strictMode?  matchCamelCasePattern(str):
                camelCaseNotStrictExpPattern.matcher(str).matches();
    }



    // 二进制文本
    private static final String binaryStringExp = "[0-1]+";
    private static final Pattern binaryStringExpPattern = Pattern.compile(binaryStringExp);
    /**
     * 判断一个字符串是否是二进制字符串, 比如：010101010101
     * @param str 任意字符串
     * @return 如果是二进制字符串则返回true, 否则返回false
     */
    public static boolean matchBinaryString(String str){
        return binaryStringExpPattern.matcher(str).matches();
    }


    /**
     * 判断字符串是否是一个合法的手机号.
     *
     * @param phoneNumber
     * @return
     */
    public static boolean matchPhoneNumber(String phoneNumber){
        return false;
    }

    /**
     * 是否符合驼峰命名法.
     * @param str
     * @return
     */
    public static boolean matchFullCamelCasePattern(String str) {
        return matchPascalCasePattern(str) || matchCamelCasePattern(str);
    }

    // 全限定类名
    private static final String classNameExp = "([A-Za-z_]\\w*(\\.[A-Za-z_]\\w*)*)";
    private static final Pattern classNameExpPattern = Pattern.compile(classNameExp);
    /**
     * 是否符合全限定类名格式
     * @param str 字符串
     * @return 如果符合则输出{@code true}, 否则输出{@code false}
     */
    public static boolean matchClassName(String str) {
       return classNameExpPattern.matcher(str).matches();
    }

    // 严格大驼峰
    private static final String pascalCasePatternExp = "^[A-Z][a-z0-9]*([A-Z][a-z0-9]+)*$";
    private static final Pattern pascalCasePatternExpPattern = Pattern.compile(pascalCasePatternExp);
    // 非严格大驼峰
    private static final String pascalCaseNotStrictExp = "^[A-Z][a-zA-Z0-9]*$";
    private static final Pattern pascalCaseNotStrictExpPattern = Pattern.compile(pascalCaseNotStrictExp);
    /**
     * 符合严格的大驼峰命名法
     * @param str 字符串
     * @return 如果符合则输出{@code true}, 否则输出{@code false}
     */
    public static boolean matchPascalCasePattern(String str){
       return pascalCasePatternExpPattern.matcher(str).matches();
    }

    /**
     * 判断字符是否符合大驼峰命名法.
     * <p>此方法参数二可以指定是否使用严格大驼峰</p>
     * @param str 字符串
     * @param strictMode 是否开启严格模式
     * @return 如果符合则输出{@code true}, 否则输出{@code false}
     */
    public static boolean matchPascalCasePattern(String str, boolean strictMode){
        return strictMode? matchPascalCasePattern(str):
                pascalCaseNotStrictExpPattern.matcher(str).matches();
    }
    // =============================== 正则表达式格式化部分 =====================================

    /**
     * 货币数字格式化.
     *
     * @param locale locale本地化
     * @param amount 金额, {@code  double}类型
     * @return 带格式的金额字符串
     */
    public static String formatToCurrency(double amount, Locale locale){
        // 指定货币格式化
        NumberFormat usCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return usCurrencyFormat.format(amount);
    }

    /**
     * 货币数字格式化.
     *
     * @param amount 金额, {@link BigDecimal}类型
     * @param locale locale本地化
     * @return 带格式的金额字符串
     */
    public static String formatToCurrency(BigDecimal amount, Locale locale){
        final NumberFormat usCurrencyFormat = NumberFormat.getCurrencyInstance(locale);
        return usCurrencyFormat.format(amount);
    }

    public static String formatDelimiterStringToPackageName(String string, char delimiter){
        return string.replace(delimiter, '.');
    }

    /**
     * 将任何一个格式化字符串转为驼峰命名法格式
     * @param string
     * @param delimiter
     * @return
     */
    public static String formatDelimiterStringToCamelCase(String string, String delimiter){
        StringTokenizer stringTokenizer = new StringTokenizer(string, delimiter);
        int countTokens = stringTokenizer.countTokens();
        StringBuilder cameCaseBuilder = new StringBuilder();
        for (int i = 0; i < countTokens; i++){
            String word = stringTokenizer.nextToken();
            // 第一个Token无需大写
            if (i == 0){
                cameCaseBuilder.append(word);
                continue;
            }
            String upperFirstLetterWord = StringUtility.upperFirstLetter(word);
            cameCaseBuilder.append(upperFirstLetterWord);
        }
        return cameCaseBuilder.toString();
    }

    public static String formatCamelCaseToSnakeCase(String camelCaseStr, boolean resultToUpperCase){
        if (!matchFullCamelCasePattern(camelCaseStr)){
            throw new IllegalArgumentException("string: " + camelCaseStr + " 不是一个合法的大小驼峰命名格式");
        }
        final String snakeCaseStr = formatCamelCaseTo(camelCaseStr, "_");
        if (resultToUpperCase){
            return snakeCaseStr.toUpperCase();
        }
        else{
            return snakeCaseStr.toLowerCase();
        }
    }

    public static String formatCamelCaseToPackageName(String camelCaseStr, boolean packageNameLowerCase, boolean lastPartLowerFirstLetter) {
        if(!matchFullCamelCasePattern(camelCaseStr)){
            throw new IllegalArgumentException("string: " + camelCaseStr + " 不是一个合法的大小驼峰命名格式");
        }
        String s = formatCamelCaseTo(camelCaseStr, " ");
        String[] camelCaseWords = s.split(" ");
        StringBuilder packageNameBuilder = new StringBuilder();
        for (int i = 0; i < camelCaseWords.length - 1; i++) {
            if (packageNameLowerCase){
                packageNameBuilder.append(camelCaseWords[i].toLowerCase()).append(".");
            }
            else{
                packageNameBuilder.append(camelCaseWords[i]).append(".");
            }
        }
        if (lastPartLowerFirstLetter){
            packageNameBuilder.append(camelCaseWords[camelCaseWords.length - 1].toLowerCase());
        }
        else{
            packageNameBuilder.append(camelCaseWords[camelCaseWords.length - 1]);
        }
        return packageNameBuilder.toString();
    }

    /**
     * 将一个驼峰命名的名称，比如FormatCamelCaseToPackageName转为包名字符串，比如Format.Camel.Case.To.Package.Name
     * <p> 由于我们判断一个字符串是否符合驼峰命名是靠大写字母来实现的, 也就是说他必须是{@code xxxUxxxUxxx}或者{@code UxxxUxxxUxxx}形式,
     * 其中的U代表大写字母, x代表小写字母, 下面的这些格式都是非法的驼峰命名法：
     * <ul>
     *     <li>AbSSe</li>
     *     <li>ffffffE</li>
     *     <li>MMMMMMMeeeeeee</li>
     * </ul>
     * <p> 当切分包名的时候, 首字母一定是大写的（除了第一个节之外）, 也就是说可能得到的包名首字母全是大写,  所以我们提供了第三个参数实现首字母小写
     * <p> 同理, 如果您希望将最后一节的名字当作包名而不是类名, 你可以指定第四个参数为true, 返回的最后一节则是包名.
     *
     * @param camelCaseStr 一个符合驼峰命名的字符串, 比如FormatCamelCaseToPackageName或者formatCamelCaseToPackageName
     * @param limit 返回的包名的节限制, 比如指定3则代表返回的节有三个, 剩余的所有单词会被整合在一起，比如Format.Camel.CaseToPackageName刚好满足三个节
     * @param packageNameLowerCase 包名部分首字母是否要转为小写
     * @param lastPartLowerFirstLetter 最后部分首字母是否要转为小写
     * @return
     */
    public static String formatCamelCaseToPackageName(String camelCaseStr, int limit, boolean packageNameLowerCase, boolean lastPartLowerFirstLetter){
        if(!matchFullCamelCasePattern(camelCaseStr)){
            throw new IllegalArgumentException("string: " + camelCaseStr + " 不是一个合法的大小驼峰命名格式");
        }
        String s = formatCamelCaseTo(camelCaseStr, " ");
        String[] camelCaseWords = s.split(" ");
        if (limit >= camelCaseWords.length){
            return formatCamelCaseToPackageName(camelCaseStr, packageNameLowerCase, lastPartLowerFirstLetter);
        }
        StringBuilder packageNameBuilder = new StringBuilder();
        for (int i = 1; i < limit; i++){
            if (packageNameLowerCase){
                packageNameBuilder.append(camelCaseWords[i - 1].toLowerCase()).append(".");
                continue;
            }
            packageNameBuilder.append(camelCaseWords[i - 1]).append(".");
        }
        StringBuilder lastPartBuilder = new StringBuilder();
        for (int i = limit; i < (camelCaseWords.length + 1); i++){
            lastPartBuilder.append(camelCaseWords[i - 1]);
        }
        if (lastPartLowerFirstLetter){
            packageNameBuilder.append(StringUtility.lowerFirstLetter(lastPartBuilder.toString()));
        }
        else{
            packageNameBuilder.append(lastPartBuilder);
        }
        return packageNameBuilder.toString();
    }

    //  在每个大写字母前插入一个replacement字符
    private static String formatCamelCaseTo(String camelCaseStr, String replacement){
        // 在每个大写字母前插入一个replacement字符
        return camelCaseStr.replaceAll("(?<=[a-z])(?=[A-Z])", replacement);
    }

    //                    ============== 时间格式化 ===========
    /**
     * 格式化输出{@link LocalDateTime}本地日期时间对象
     *
     * @param pattern 要格式化的格式
     * @param dateTime {@link LocalDateTime}对象
     * @return 格式化后的{@link LocalDateTime}字符串
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(dateTime);
    }

    /**
     * 格式化输出{@link java.util.Date}日期时间对象
     * @param pattern 要格式化的格式
     * @param date {@link java.util.Date}对象
     * @return 格式化后的{@link java.util.Date}字符串
     */
    public static String formatUtilDate(Date date, String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
    //                    ============== 时间格式化 ===========





    public static void main(String[] args) {
        System.out.println(matchBinaryString("00000001"));
        System.out.println(matchCamelCasePattern("checkAbout2"));
        System.out.println(FormatUtility.formatCamelCaseToSnakeCase("connectedAccountId", false));
        System.out.println(Double.class.isPrimitive());
        System.out.println(FormatUtility.formatDelimiterStringToCamelCase("sys.USER.initPassword", "."));
        System.out.println(matchCamelCasePattern("caseLittle"));
        System.out.println();
        System.out.println(formatCamelCaseToSnakeCase("camelableAb", false));
//        System.out.println(formatToCurrency(Locale.US, 1234));
        System.out.println(formatCamelCaseToPackageName( "camelCaseFormatToPackageName", 5, true, true));
        System.out.println(formatCamelCaseToPackageName( "CamelCaseFormatToPackageName", false, false));
    }
}
