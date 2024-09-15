package cn.argento.askia.utilities;

import cn.argento.askia.exceptions.runtime.lang.IllegalStringFormatRuntimeException;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtility {
    public static void main(String[] args) {
        final String s = MBitToMByte("60MBit/ps", null);
        System.out.println(s);
    }

    private static final String DEFAULT_MEGA_BYTE_UNIT = "MB/s";
    private static final String DEFAULT_MEGA_BIT_UNIT = "Mbps";

    /**
     * 实现 Mega Bit per second(Mbps) 到 Mega Byte per second(MB/s)之间的换算
     *
     * @param MBitPerSecond 输入的Mega Bit per second(Mbps), 以下格式都可以：[15 MBit/ps] [15Mbps] [15MBit/s] [15Mbit/s] [15Mbit/ps]
     * @param format 提供的输出格式化, 提供扩展显示, 如不想指定, 置null即可
     * @return ega Byte per second(MB/s)
     * @see #MByteToMBit(String, MessageFormat)
     */
    public static String MBitToMByte(String MBitPerSecond, MessageFormat format){
        final String trimMBitPerSecond = MBitPerSecond.trim();
        float result = 0;
        // match [15 MBit/ps] [15Mbps] [15MBit/s] [15Mbit/s] [15Mbit/ps]
        final Pattern exp = Pattern.compile("(\\d+)(\\s?)(MBit/ps|Mbps|MBit/s|Mbit/s|Mbit/ps)");
        final Matcher expMatcher = exp.matcher(trimMBitPerSecond);
        if (expMatcher.matches()){
            result = (float) (Integer.parseInt(expMatcher.group(1)) / 8.0);
            return format == null?
                    result + " " + DEFAULT_MEGA_BYTE_UNIT:
                    format.format(result);
        }
        throw new IllegalStringFormatRuntimeException(exp, MBitPerSecond);
    }

    /**
     * 实现 Mega Byte per second(MB/s) 到 Mega Bit per second(Mbps) 之间的换算.
     *
     * @param MBytePerSecond
     * @param format
     * @return
     * @see #MBitToMByte(String, MessageFormat)
     */
    public static String MByteToMBit(String MBytePerSecond, MessageFormat format){
        // 去掉莫名其秒的空
        final String trimMBytePerSecond = MBytePerSecond.trim();
        float result = 0;
        // match [15 MBit/ps] [15Mbps] [15MBit/s] [15Mbit/s] [15Mbit/ps]
        final Pattern exp = Pattern.compile("(\\d+)(\\s?)(MB/s|MB/ps|MByte/s|MByte/ps|)");
        final Matcher expMatcher = exp.matcher(trimMBytePerSecond);
        if (expMatcher.matches()){
            result = (float) (Integer.parseInt(expMatcher.group(1)) / 8.0);
            return format == null?
                    result + " " + DEFAULT_MEGA_BIT_UNIT:
                    format.format(result);
        }
        throw new IllegalStringFormatRuntimeException(exp, MBytePerSecond);
    }
}
