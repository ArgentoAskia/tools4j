package cn.argento.askia.supports.matcher;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 通配符模式匹配器，用于匹配包名或带点分隔的字符串。
 * 模式示例：
 *   - "com.*.A"       匹配 "com.example.A"，不匹配 "com.example.sub.A"
 *   - "com.**.A"      匹配 "com.example.A" 和 "com.example.sub.A"
 *   - "java.lang.*"   匹配 "java.lang.String" 等
 */
public class WildcardMatcher implements PathMatcher{
    private final Pattern pattern;

    /**
     * 使用单级通配符 '*'（不匹配点号）构造匹配器。
     * @param pattern 通配符模式，例如 "com.*.A"
     */
    public WildcardMatcher(String pattern) {
        this(pattern, '.', true);
    }

    public WildcardMatcher(String pattern, char delimiter){
        this(pattern, delimiter, true);
    }

    /**
     * 构造匹配器，可指定是否支持多级通配符 '**'。
     * @param pattern     通配符模式
     * @param multiLevel  若为 true，则 "**" 匹配零个或多个任意字符（包括点号）；
     *                    若为 false，则 "*" 只匹配一个非点号段。
     */
    public WildcardMatcher(String pattern, char delimiter, boolean multiLevel) {
        this.pattern = compile(pattern, delimiter, multiLevel);
    }

    private Pattern compile(String pattern, char delimiter, boolean multiLevel) {
        StringBuilder regex = new StringBuilder("^");
        int len = pattern.length();
        for (int i = 0; i < len; i++) {
            char ch = pattern.charAt(i);
            if (ch == '*') {
                // 检查是否为连续两个 '*'，即多级通配符 '**'
                if (multiLevel && i + 1 < len && pattern.charAt(i + 1) == '*') {
                    regex.append("[^").append(Pattern.quote("" + delimiter));
                    regex.append("]+");   // 匹配至少一个目录结构
                    // 接下来匹配多个 \\abc
                    regex.append("(").append(Pattern.quote("" + delimiter));
                    regex.append("[^").append(Pattern.quote("" + delimiter));
                    regex.append("]+").append(")*");
                    i++;                  // 跳过下一个 '*'
                } else {
                    // 单级通配符，匹配非点号的一个或多个字符
                    regex.append("[^").append(Pattern.quote("" + delimiter)).append("]+");
                }
            } else if (ch == delimiter) {
                // 判断ch是否是表达式的关键符号
                regex.append(Pattern.quote("" + delimiter));
            } else if (ch == '?') {
                regex.append("[^").append(delimiter).append("]"); // 匹配一个非点号字符（可选）
            } else {
                regex.append(Pattern.quote(String.valueOf(ch)));
            }
        }
        regex.append("$");
        System.out.println(regex);
        return Pattern.compile(regex.toString());
    }

    /**
     * 判断输入字符串是否匹配模式。
     * @param input 待匹配的字符串，例如 "com.example.A"
     * @return true 匹配，false 不匹配
     */
    @Override
    public boolean matches(String input) {
        return input != null && pattern.matcher(input).matches();
    }


    // 简单测试
    public static void main(String[] args) {
        // 单级匹配
//        WildcardMatcher m1 = new WildcardMatcher("com.?.A");
//        System.out.println(m1.matches("com.example.A"));      // true
//        System.out.println(m1.matches("com.example.sub.A"));  // false
//        System.out.println(m1.matches("com.A.A"));             // false（空段不允许）

        // 多级匹配
        WildcardMatcher m2 = new WildcardMatcher("com\\**\\A", '\\');
        System.out.println(m2.matches("com\\example\\A"));       // true
        System.out.println(m2.matches("com/example/sub/sub2/A"));   // true
        System.out.println(m2.matches("com//A"));              // true（** 允许空段）

        // 通配符在末尾
//        WildcardMatcher m3 = new WildcardMatcher("java.lang.*");
//        System.out.println(m3.matches("java.lang.String"));    // true
//        System.out.println(m3.matches("java.lang"));           // false（缺少一级）
    }
}
