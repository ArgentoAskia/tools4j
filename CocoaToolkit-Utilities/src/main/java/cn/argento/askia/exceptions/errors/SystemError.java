package cn.argento.askia.exceptions.errors;

import java.io.FileNotFoundException;
import java.util.Locale;

/**
 * 系统错误.
 * <p>
 *     当你的系统在遇到需要完全停止运行的错误的时候, 可以考虑抛出此异常, 包含了四个信息 (并非全都需要！):
 *     <ul>
 *         <li>系统错误码: 该码一般由开发者自行规定</li>
 *         <li>错误信息: 用于描述该系统错误发生的原因</li>
 *         <li>错误对象: 哪个混蛋引起的系统错误</li>
 *         <li>错误异常: 错误由异常引起的时候可以保存该异常</li>
 *     </ul>
 * <hr>
 *     使用方法：
 *     <blockquote style="background-color:rgb(232,232,232)"><pre>
 *         FileNotFoundException fnfe = ...;
 *
 *         // 抛出包含三个或者四个参数的默认格式的信息
 *         throw new SystemError(-1, fnfe);
 *
 *         // 还可以带自己的提示信息：
 *         throw new SystemError(-1, "exitcode:" + -1 + "原因：" + fnfe ,fnfe);
 *
 *         // 未知原因可以使用默认构造器
 *         throw new SystemError();
 *
 *         // 因异常而引起的系统错误 (即无引发异常的错误对象时)可以使用 SystemError(int exitCode, Object reasonObj, Throwable cause)
 *         // 其中 reasonObj 传递 SystemError.NO_REASON_OBJECT！
 *         throw new SystemError(-1, SystemError.NO_REASON_OBJECT ,fnfe);
 *     </pre></blockquote>
 *
 * @author Askia
 * @version 1.0.20240520
 * @see java.lang.Error Error class
 *
 */
public class SystemError extends Error{


    static final long serialVersionUID = 4603556048193261013L;

    public static final Object NO_REASON_OBJECT = new Object(){
        @Override
        public String toString() {
            return "no reason Object!";
        }

        @Override
        public int hashCode() {
            return 0;
        }
    };

    public SystemError(int exitCode, String message, Throwable cause){
        super(message, cause);
        this.exitCode = exitCode;
    }

    public SystemError(){
        super(Locale.getDefault() == Locale.CHINA || Locale.getDefault() == Locale.CHINESE ?
                SYSTEM_ERROR_WITH_UNKNOWN_REASON_CN : SYSTEM_ERROR_WITH_UNKNOWN_REASON_EN);
    }

    public SystemError(String message){
        super(message);
        this.exitCode = -1;
        this.reasonObj = null;
    }

    public SystemError(int exitCode, String message, Object reasonObj){
        super(message);
        this.exitCode = exitCode;
        this.reasonObj = reasonObj;
    }

    public SystemError(int exitCode, String message, Object reasonObj, Throwable cause){
        this(exitCode, message, cause);
        this.reasonObj = reasonObj;
    }

    public SystemError(int exitCode, Object reasonObj){
        super((Locale.getDefault() == Locale.CHINA? COMMON_SYSTEM_ERROR_MSG_NO_CAUSE_TEMPLATE_CN: COMMON_SYSTEM_ERROR_MSG_NO_CAUSE_TEMPLATE_EN)
                .replace("{{reasonObj}}", reasonObj != null ? reasonObj.toString() : "null")
                .replace("{{hashCode}}", String.valueOf(reasonObj == null ? "0" : reasonObj.hashCode()))
                .replace("{{exitCode}}", String.valueOf(exitCode)));
        this.reasonObj = reasonObj;
        this.exitCode = exitCode;
    }

    private static final String COMMON_SYSTEM_ERROR_MSG_NO_CAUSE_TEMPLATE_EN = "Oop! System Error occur！！！because of THE FUCKING OBJECT: " + System.lineSeparator() +
            "{" + System.lineSeparator() +
            "       the crash reason object: {{reasonObj}}" + System.lineSeparator() +
            "       it's hashCode:           {{hashCode}}" + System.lineSeparator() +
            "       exitCode:                {{exitCode}}" + System.lineSeparator() +
            "}";
    private static final String COMMON_SYSTEM_ERROR_MSG_NO_CAUSE_TEMPLATE_CN = "很抱歉通知您，您的系统已崩溃！！崩溃信息如下：" + System.lineSeparator() +
            "{" + System.lineSeparator() +
            "       引发崩溃的对象:      {{reasonObj}}" + System.lineSeparator() +
            "       哈希地址:           {{hashCode}}" + System.lineSeparator() +
            "       系统退出状态码:      {{exitCode}}" + System.lineSeparator() +
            "}";


    public SystemError(int exitCode, Object reasonObj, Throwable cause){
        super((Locale.getDefault() == Locale.CHINA? COMMON_SYSTEM_ERROR_MSG_TEMPLATE_CN: COMMON_SYSTEM_ERROR_MSG_TEMPLATE_EN)
                .replace("{{reasonObj}}", reasonObj == null? "null" : reasonObj.toString())
                .replace("{{hashCode}}", String.valueOf(reasonObj == null? "0" : reasonObj.hashCode()))
                .replace("{{exitCode}}", String.valueOf(exitCode))
                .replace("{{cause}}", cause.toString())
                , cause);
        this.exitCode = exitCode;
        this.reasonObj = reasonObj;
    }

    private static final String COMMON_SYSTEM_ERROR_MSG_TEMPLATE_EN = "Oop! System Error occur！！！because of THE FUCKING OBJECT: " + System.lineSeparator() +
            "{" + System.lineSeparator() +
            "       the crash reason object: {{reasonObj}}" + System.lineSeparator() +
            "       it's hashCode:           {{hashCode}}" + System.lineSeparator() +
            "       exitCode:                {{exitCode}}" + System.lineSeparator() +
            "       exception cause:         {{cause}}" + System.lineSeparator() +
            "}";

    private static final String COMMON_SYSTEM_ERROR_MSG_TEMPLATE_CN = "很抱歉通知您，您的系统已崩溃！！崩溃信息如下：" + System.lineSeparator() +
            "{" + System.lineSeparator() +
            "       引发崩溃的对象:      {{reasonObj}}" + System.lineSeparator() +
            "       哈希地址:           {{hashCode}}" + System.lineSeparator() +
            "       系统退出状态码:      {{exitCode}}" + System.lineSeparator() +
            "       引发崩溃的异常:      {{cause}}" + System.lineSeparator() +
            "}";

    private int exitCode;

    private Object reasonObj;

    public int getExitCode() {
        return exitCode;
    }

    public void setReasonObj(Object reasonObj) {
        this.reasonObj = reasonObj;
    }

    public Object getReasonObj() {
        return reasonObj;
    }

    private static final String SYSTEM_ERROR_WITH_UNKNOWN_REASON_EN = "Oop! System Error occur！！！UNKNOWN REASON AND NO EXCEPTION CAUSE！Just exit the system！";

    private static final String SYSTEM_ERROR_WITH_UNKNOWN_REASON_CN = "很抱歉通知您，您的系统由于未知原因崩溃了！佛主都保佑不了的那种，可以开席了！！";


    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
