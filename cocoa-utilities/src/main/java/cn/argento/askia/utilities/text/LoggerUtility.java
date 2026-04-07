package cn.argento.askia.utilities.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用的Logger工具类封装.
 *
 * 提供一套自适应的日志级别配置, 该工具类会自动加载相关日志实现并打印日志, 我们提供了一套这样的机制：
 */
public class LoggerUtility {

//    private static final boolean HAS_SLF4J;
//    private static final Object LOGGER;
//    private
//    static {
//        boolean hasSlf4j = false;
//    }

    // 我们按照默认的实现来检测使用
    public static void main(String[] args) {
        detect("SLF4J", "org.slf4j.LoggerFactory");
        detect("Logback", "ch.qos.logback.classic.LoggerContext");
        detect("Log4j2", "org.apache.logging.log4j.LogManager");
        detect("Log4j1", "org.apache.log4j.Logger");
        detect("JUL", "java.util.logging.Logger");

        String out = log("User {} logged in at {} {} {}", "Alice", "2025-06-18", 1);
        System.out.println(out);
    }

    private static void detect(String name, String className) {
        try {
            Class.forName(className);
            System.out.println("✅ 检测到：" + name);
        } catch (ClassNotFoundException e) {
            System.out.println("❌ 未检测到：" + name);
        }
    }

    /**
     * 生成一条 Log 信息体。
     * @param logMsg
     * @param params 如果参数个数和{}不匹配时，{}多则保留多的{}不替换, 参数多则忽略多余的参数
     * @return
     */
    public static String log(String logMsg, Object... params){
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\{\\}").matcher(logMsg);
        int idx = 0;
        while (m.find()) {
            if (idx < params.length) m.appendReplacement(sb, String.valueOf(params[idx++]));
            else m.appendReplacement(sb, "{}"); // 参数不够，保留原样
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
