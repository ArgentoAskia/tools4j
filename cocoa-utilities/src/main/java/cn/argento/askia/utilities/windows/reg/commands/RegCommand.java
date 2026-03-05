package cn.argento.askia.utilities.windows.reg.commands;


/**
 * 抽象 {@code Reg} 命令接口.
 * <p>
 * 该接口提供了修改Windows系统注册表的能力, 该接口主要有三个接口方法：<br>
 * <ul>
 *     <li>Object exec(): 调用该方法将会执行指令并得到执行结果！</li>
 *     <li>String toString(): 得到拼接出来的字符串形式的指令, 一般用于调试输出查看命令格式</li>
 *     <li>String trace(): 调用该方法，你将会得到一个具体的文本报告，其中包括了当前指令字符串, 能否运行, 是否存在问题等等，仅用作调试使用</li>
 * </ul>
 *
 * @apiNote
 *  核心原理是拼接Windows系统下的Reg指令，然后通过{@link ProcessBuilder}创建CMD进程并执行Reg指令, 然后对拿到的返回值结果进行分析并返回！
 *  需要注意，该接口的所有实现只能在Windows平台下运行, 因此并不具备跨平台能力, 所有的子命令实现无需检测当前平台.
 *
 * @author Askia
 */
public interface RegCommand {

    /**
     * execute cmd and get result.
     *
     * @return result for command executed！
     * @implNote impls should change the return type
     */
    Object exec();


    /**
     * get the String type of RegCommand!
     *
     * @return String type Command！
     * @apiNote
     *  define this interface method for debug using, we can call this API to ensure the command is right!
     *  so this method is needful！
     *
     * @implNote
     *  because of {@linkplain Object Object Class}, Your implementation does not necessarily need to implement this method！
     *  when your implementation doesn't override this method, it will use {@linkplain Object#toString() Object's toString()}.
     *  In general, The Abstract Implementation Meets the basic requirements of the method, so if you use {@link AbstractRegCommand},
     *  you don't need to Override the method!
     *
     * @implSpec
     *  The Abstract Implementation will return {@code null} if you don‘t put any command string in the StringBuilder, or return the String type Command!
     *  WARNING！！！ the returned String type Command may be an invalid Command! use {@link #isValid(RegCommand)} to judge a RegCommand object can run or not!
     *
     */
    String toString();


    // 这个方法能实现指令检查，比如检查文件是否存在等
    // 需要根据具体的指令来编写出错的位置，然后通过参数的形式告诉开发哪些错误
    // 开发者需要预定定义好相关的错误问题的解决方案！
    // 比如冲突的参数（ve和v），导出位置不存在等等...
    // 经过该方法之后，RegCommand应该能直接运行且毫无错误
    RegCommand trace();
}
