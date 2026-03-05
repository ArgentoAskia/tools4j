package cn.argento.askia.utilities;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 日期和时间工具类.
 *
 * <p>这是一个纯处理日期时间的工具类, 主要包含三部分:
 * <ol>
 *     <li>各类时间类的对象创建: 统一各类时间对象的创建方式, 包括指定年月日创建, 获取当前时间, 字符串转换创建等</li>
 *     <li>时间计算、判别和表达: 时间加减、两个时间差、闰年判别、获取特定字段、时间对齐(月初、下周一问题)、时间段表示(XX秒, XX天, 1年2个月3天)</li>
 *     <li>时间对象转换: {@linkplain LocalDateTime LocalDateTime} 转 {@linkplain LocalDate LocalDate}、{@linkplain LocalDateTime LocalDateTime} 转 {@linkplain Date Date}等</li>
 * </ol>
 * <p><b>需要特别注意：此工具类不包含任何时间格式化API, 如果您需要格式化时间, 您应该使用 {@link FormatUtility} , 这个类包含了几乎所有文本格式相关的API</b>, 之所以将时间格式化的API摘除到 {@link FormatUtility} ,是因为能够进行符号格式化的内容实在是太多了, 作者更希望将所有格式化内容相关的API封装在一起.
 *
 * <p>工具类整合的时间类主要有三类：
 * <ol>
 *     <li>基于 {@code java.time.*} 下的类, 包括: {@linkplain LocalDateTime java.time.LocalDateTime}、{@linkplain LocalDate java.time.LocalDate}、{@linkplain LocalTime java.time.LocalTime}、{@linkplain Instant java.time.Instant}、{@linkplain OffsetDateTime java.time.OffsetDateTime}、{@linkplain OffsetTime java.time.OffsetTime}、{@linkplain ZonedDateTime java.time.ZonedDateTime}、{@linkplain Period java.time.Period}、{@linkplain Duration java.time.Duration}</li>
 *     <li>来自 {@code java.util.*} 下的：{@linkplain Date java.util.Date}、 {@linkplain Calendar java.util.Calendar}、 {@linkplain java.util.TimeZone java.util.TimeZone}</li>
 *     <li>来自 {@code java.time.chrono.*} 下的特殊日历</li>
 * </ol>
 *
 *
 * @author Askia
 * @version 2025.10.10
 * @since 1.0 初始化工具类
 * @see FormatUtility
 *
 */
public class DateTimeUtility {

    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
//        Date date = localDateTimeToDate(now);
//        System.out.println(now);
//        System.out.println(date);
//        System.out.println();
//        LocalDateTime localDateTime = dateToLocalDateTime(new Date());
//        System.out.println(localDateTime);
//
//        final long between = between(LocalDate.of(2000, Month.AUGUST, 1), LocalDate.now(), ChronoUnit.YEARS);
//        System.out.println(between);
//
//
//        LocalDateTime beijingTime = LocalDateTime.parse("2025-06-25T12:00:03"); // 实际是 UTC 时间
//        LocalDateTime londonTime  = convertUTC(beijingTime, 8, 0);
//        System.out.println(londonTime);   // 2025-06-25T04:00:00Z
//        Instant parse = Instant.parse("2025-06-25T12:00:25Z");
//        LocalDateTime localDateTime1 = convertUTC(parse, -8);
//        System.out.println(localDateTime1);
//
//        System.out.println(plus(LocalDateTime.now(), -1, ChronoUnit.HOURS));
        System.out.println(LocalDateTime.now());
    }

    private DateTimeUtility(){}

    // localdatetime本身不好含时区信息，所以需要先转为包含时区信息的ZonedDateTime
    // 不建议转为毫秒再到Date，会丢失nano time
    public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId){
        if (localDateTime == null){
            return null;
        }
        if (zoneId == null){
            zoneId = ZoneId.systemDefault();
        }
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        return localDateTimeToDate(localDateTime, ZoneId.systemDefault());
    }

    public static ZonedDateTime dateToZoneDateTime(Date date, ZoneId zoneId){
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        return zonedDateTime;
    }

    public static LocalDateTime dateToLocalDateTime(Date date){
        if (date == null){
            return null;
        }
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Timestamp localDateTimeToTimestamp(LocalDateTime dateTime){
        return dateTime != null? Timestamp.valueOf(dateTime):null;
    }

    public static Timestamp localDateTimeToTimestamp(LocalDateTime dateTime, ZoneId zoneId){
        if (dateTime == null){
            return null;
        }
        if (zoneId == null){
            zoneId = ZoneId.systemDefault();
        }
        ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        return Timestamp.from(instant);
    }

    public static Timestamp zoneDateTimeToTimestamp(ZonedDateTime zonedDateTime){
        if (zonedDateTime == null){
            return null;
        }
        Instant instant = zonedDateTime.toInstant();
        return Timestamp.from(instant);
    }

    public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp){
        if (timestamp == null){
            return null;
        }
        return timestamp.toLocalDateTime();
    }

    public static LocalDate dateToLocalDate(java.sql.Date date){
        if (date == null){
            return null;
        }
        return date.toLocalDate();
    }

    public static java.sql.Date localDateToDate(LocalDate localDate){
        if (localDate == null){
            return null;
        }
        return java.sql.Date.valueOf(localDate);
    }

    public static LocalTime sqlTimeToLocaleTime(Time time){
        if (time != null){
            return time.toLocalTime();
        }
        return null;
    }

    public static Time localTimeToSqlTime(LocalTime localTime){
        if (localTime == null){
            return null;
        }
        return Time.valueOf(localTime);
    }

    private static Class<?>[] dateTimeClasses = new Class[]{
            LocalDateTime.class, ZonedDateTime.class,
            OffsetDateTime.class, Date.class, Calendar.class, Timestamp.class, Instant.class
    };

    // ======================= 计算 API  ==============================
    /**
     * 判断两个本地时间的时间差
     * @param dateTime
     * @param datetime2
     * @param unit
     * @return
     */
    public static long between(LocalDateTime dateTime, LocalDateTime datetime2,
                               TemporalUnit unit){
        return unit.between(dateTime, datetime2);
    }

    /**
     * 判断两个日期的日期差
     * @param localDate
     * @param localDate2
     * @param unit
     * @return
     */
    public static long between(LocalDate localDate, LocalDate localDate2, TemporalUnit unit){
        return unit.between(localDate, localDate2);
    }

    /**
     * 判断参数1时间是否在参数2的时间之前.
     *
     * @param localDateTime1 第一个时间, 本地时间
     * @param localDateTime2 第二个时间, 本地时间
     * @return 如果参数1时间在参数2之前则返回true, 如果参数2时间在参数1之前或者相等则返回false
     * @since 2025.10.12
     */
    public static boolean isBefore(LocalDateTime localDateTime1, LocalDateTime localDateTime2){
        return between(localDateTime1, localDateTime2, ChronoUnit.NANOS) > 0;
    }
    /**
     * 将给定时间由 fromUtc 转换到 toUtc。
     * 例如：convertUtc(fromTime, 8, 2) 表示把 UTC+8 的 fromTime 转成 UTC+2 的同一时间戳。
     * <b>此API为了简便只允许了小时层面的转换, 如果希望更加细粒度的时间转换, 请使用convertUTCEx</b>
     *
     * @param fromTime 原始时间（物理瞬间）
     * @param fromUTC 原始偏移，单位小时, 代表原始时间实际是哪个UTC偏移的时间, 可以传递负数。例：北京时间传 8
     * @param toUTC 目标偏移，单位小时, 可以传递负数。例：伦敦冬季传 0
     * @return 转换后的时间本地时间
     * @since 2025.10.21
     * @see DateTimeUtility#convertUTCEx(LocalDateTime, String, String)
     */
    public static LocalDateTime convertUTC(LocalDateTime fromTime, int fromUTC, int toUTC){
        Objects.requireNonNull(fromTime, "fromTime must not be null");
        ZoneOffset fromOffset = ZoneOffset.ofHours(fromUTC);
        ZoneOffset toOffset = ZoneOffset.ofHours(toUTC);
        ZonedDateTime zdtFrom = fromTime.atOffset(fromOffset).toZonedDateTime();
        ZonedDateTime zdtTo = zdtFrom.withZoneSameInstant(toOffset);
        return zdtTo.toLocalDateTime();
    }

    /**
     * 将给定时间由 fromUtc 转换到 toUtc。
     * @param fromTime Instant对象，代表UTC+0('Z')上的点
     * @param toUTC 转为具体的UTC X时间，比如传递 -2代表UTC-2, 传递8代表UTC+8
     * @return 转换后的时间本地时间
     * @since 2025.10.21
     */
    public static LocalDateTime convertUTC(Instant fromTime, int toUTC){
        ZonedDateTime utc = fromTime.atOffset(ZoneOffset.UTC).toZonedDateTime();
        return utc.withZoneSameInstant(ZoneOffset.ofHours(toUTC)).toLocalDateTime();
    }

    /**
     * 将给定时间由 fromUtc 转换到 toUtc。
     * 提供了到秒的更加精细的时间转换.
     * @param fromTime 原始时间（物理瞬间）
     * @param fromUTC 原始偏移，需要提供类似于'+2:23:59'、'-2:23:59'这样的字符串
     * @param toUTC 目标偏移，需要提供类似于'+2:23:59'、'-2:23:59'这样的字符串
     * @return 转换后的时间本地时间
     * @since 2025.10.21
     * @see DateTimeUtility#convertUTC(LocalDateTime, int, int)
     * @see DateTimeUtility#convertUTC(Instant, int)
     */
    public static LocalDateTime convertUTCEx(LocalDateTime fromTime, String fromUTC, String toUTC){
        final String[] fromUTCTimes = fromUTC.split(":");
        final String[] toUTCTimes = toUTC.split(":");
        int fromUTCHour = 0;
        int toUTCHour = 0;
        int fromUTCMinute = 0;
        int toUTCMinute = 0;
        int fromUTCSecond = 0;
        int toUTCSecond = 0;
        if (fromUTCTimes.length > 3 || fromUTCTimes.length <= 0){
            throw new IllegalArgumentException("fromUTC的格式只能最多包含[时分秒], 比如: +2:20:02、+2:20或者、+2");
        }
        if (toUTCTimes.length > 3 || toUTCTimes.length <= 0){
            throw new IllegalArgumentException("toUTC的格式只能最多包含[时分秒], 比如: +2:20:02、+2:20或者、+2");
        }
        if (fromUTCTimes.length == 3){
            fromUTCHour = Integer.parseInt(fromUTCTimes[0]);
            fromUTCMinute = Integer.parseInt(fromUTCTimes[1]);
            fromUTCSecond = Integer.parseInt(fromUTCTimes[2]);
        }
        else if (fromUTCTimes.length == 2){
            fromUTCHour = Integer.parseInt(fromUTCTimes[0]);
            fromUTCMinute = Integer.parseInt(fromUTCTimes[1]);
        }
        else{
            fromUTCHour = Integer.parseInt(fromUTCTimes[0]);
        }
        if (toUTCTimes.length == 3){
            toUTCHour = Integer.parseInt(toUTCTimes[0]);
            toUTCMinute = Integer.parseInt(toUTCTimes[1]);
            toUTCSecond = Integer.parseInt(toUTCTimes[2]);
        }
        else if (fromUTCTimes.length == 2){
            toUTCHour = Integer.parseInt(toUTCTimes[0]);
            toUTCMinute = Integer.parseInt(toUTCTimes[1]);
        }
        else{
            toUTCHour = Integer.parseInt(fromUTCTimes[0]);
        }
        ZoneOffset zoFrom = ZoneOffset.ofHoursMinutesSeconds(fromUTCHour, fromUTCMinute, fromUTCSecond);
        ZoneOffset zoTo = ZoneOffset.ofHoursMinutesSeconds(toUTCHour, toUTCMinute, toUTCSecond);
        ZonedDateTime zdtFrom = fromTime.atOffset(zoFrom).toZonedDateTime();
        ZonedDateTime zdtTo = zdtFrom.withZoneSameInstant(zoTo);
        return zdtTo.toLocalDateTime();
    }

    // 加减乘除
    @SuppressWarnings("unchecked")
    public static <T extends Temporal> T plus(T temporal, long value, TemporalUnit unit){
        return (T) temporal.plus(value, unit);

    }
    @SuppressWarnings("unchecked")
    public static <T extends Temporal> T minus(T temporal, long value, TemporalUnit unit){
        return (T) temporal.minus(value, unit);
    }


    // ======================= 计算 API 结束 ==============================

    // ======================= 对象创建 API =============================
    @SuppressWarnings("all")
    public static <T> T now(Class<?> dateTimeClass){
        if (checkForDateTimeClass(dateTimeClass)){
            if (dateTimeClass == LocalDateTime.class){
                return (T) LocalDateTime.now();
            }
            else if (dateTimeClass == ZonedDateTime.class){
                return (T) ZonedDateTime.now();
            }
            else if (dateTimeClass == OffsetDateTime.class){
                return (T) OffsetDateTime.now();
            }
            else if (dateTimeClass == Date.class){
                return (T) new Date();
            }
            else if (dateTimeClass == Calendar.class){
                return (T) Calendar.getInstance();
            }
            else if (Timestamp.class == dateTimeClass){
                return (T) zoneDateTimeToTimestamp(ZonedDateTime.now());
            }
            else if (Instant.class == dateTimeClass){
                return (T) ZonedDateTime.now().toInstant();
            }
        }
        return (T) Long.valueOf(System.currentTimeMillis());
    }

    private static boolean checkForDateTimeClass(Class<?> tClass){
        return ArrayUtility.fastContain(dateTimeClasses, tClass);
    }

    // ======================= 对象创建 API 结束 =============================
    public static <T extends TemporalAccessor> T parse(String datetime, DateTimeFormatter formatter){
        return null;
    }


}
