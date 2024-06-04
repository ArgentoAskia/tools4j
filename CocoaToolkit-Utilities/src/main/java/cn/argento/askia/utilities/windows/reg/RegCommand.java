package cn.argento.askia.utilities.windows.reg;

/**
 * 抽象 {@code Reg} 命令接口.
 * <p>
 * 该接口提供了修改Windows系统注册表的能力, 该接口主要有三个接口方法：<br>
 * <ul>
 *     <li>Object exec(): 调用该方法将会执行指令并得到执行结果！</li>
 *     <li>String toString(): 得到拼接出来的字符串形式的指令, 一般用于调试输出查看命令格式</li>
 *     <li>RegCommand build(): 调用此命令则构建出 {@code RegCommand}对象！</li>
 * </ul>
 *
 * @apiNote
 *  核心原理是拼接Windows系统下的Reg指令，然后通过{@link ProcessBuilder}创建CMD进程并执行Reg指令, 然后对拿到的返回值结果进行分析并返回！
 *  需要注意，该接口的所有实现只能在Windows平台下运行, 因此并不具备跨平台能力, 所有的实现无需检测当前平台, 该工作交由{@link RegUtility}来做！
 *
 *
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

    // done-> 2024/5/29 加入判断命令能否运行的接口, 静态方法,考虑使用正则表达式？
    /**
     * 判别该{@code RegCommand}上的字符串指令是否是一个可运行的指令, 即合法指令！
     * <p>
     *
     * <hr>
     *   使用示例如下:
     *   <blockquote style="background-color:rgb(232,232,232)"><pre>
     *   final RegCommand regAddCmd = RegUtility.query()
     *           .keyName().fullKey(RegUtility.RootKeyConstants.HKLM, "subkey")
     *           .ve()
     *           .endOptionalArgs().build();
     *
     *   RegCommand.isValid(regAddCmd);     // true
     *   </pre></blockquote>
     *
     * @param regCommand regCommand对象, 使用{@link RegCommand#build()}即可得到实例！
     * @return boolean类型, 如果指令格式合法, 责返回true, 否则返回false
     */
    static boolean isValid(RegCommand regCommand){
        final String s = regCommand.toString();
        // because regCommand.toString() return null instance of ""
        if (s == null){
            // so we decide to throw an exception!
            throw new NullPointerException("reg command is null!");
        }
        // will microsoft update the reg command and add some new features in it???
        // this condition may not happen recently! so enum is enough!

        // start with ignore case?
        String sCopy = s.toUpperCase();
        if (sCopy.startsWith("REG ADD")){
            return AbstractRegCommand.regAddPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG COMPARE")){
            return AbstractRegCommand.regComparePattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG COPY")){
            return AbstractRegCommand.regCopyPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG DELETE")){
            return AbstractRegCommand.regDeletePattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG EXPORT")){
            return AbstractRegCommand.regExportPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG FLAGS")){
            return AbstractRegCommand.regFlagsPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG IMPORT")){
            return AbstractRegCommand.regImportPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG LOAD")){
            return AbstractRegCommand.regLoadPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG QUERY")){
            return AbstractRegCommand.regQueryPattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG RESTORE")){
            return AbstractRegCommand.regRestorePattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG SAVE")){
            return AbstractRegCommand.regSavePattern.matcher(s).matches();
        }
        if (sCopy.startsWith("REG UNLOAD")){
            return AbstractRegCommand.regUnloadPattern.matcher(s).matches();
        }
        // 默认情况下, 每个指令可能都会包含到子指令，这是因为子实现在构造器中就已经明确了指令类型
        // 但是可能有极端情况（虽然不常见）, 比如拿到了空字符串？遇到这种情况我们选择抛一个异常！
        throw new IllegalStateException("check if it is REG COMMAND?");
    }

}
