package cn.argento.askia.utilities.windows.reg;

/**
 * 抽象 {@code Reg} 命令接口.
 * <p>
 * 该接口主要有三个接口方法：<br>
 * <ul>
 *     <li>Object exec(): 调用该方法将会执行指令并得到执行结果！</li>
 *     <li>String toString(): 得到拼接出来的字符串形式的指令, 一般用于调试输出查看命令格式</li>
 *     <li>RegCommand build(): 调用此命令则构建出 {@code RegCommand}对象！</li>
 * </ul>
 *
 * @implNote
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


    /**
     * Build the {@linkplain RegCommand RegCommand} Object.
     *
     * @apiNote
     *  All Implementations use private Constructor and build {@linkplain RegCommand RegCommand} Object with this method.
     *  All Implementations should NOT Be instantiated by NEW!
     *
     * @implNote
     *  if the command need to do something before execute, such as checked the format, rebuild the command String and so on..
     *  you can write out the extra use with this method! This Method need to return {@code this} for going on calling!
     *
     * @return RegCommand
     */
    RegCommand build();

    // TODO: 2024/5/29 加入判断命令能否运行的接口, 静态方法
    static boolean isValid(RegCommand regCommand){
        return false;
    }

}
