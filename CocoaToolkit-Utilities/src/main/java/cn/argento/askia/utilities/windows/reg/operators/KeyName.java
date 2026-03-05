package cn.argento.askia.utilities.windows.reg.operators;

import cn.argento.askia.utilities.windows.reg.RegUtility;
/**
 *
 * @param <N> 调用完KeyName之后，返回的下一个类型对象，如：FileName或者RegAddOptionalArgs
 */
public class KeyName<N> implements IKeyName<N>{

    private RegUtility.RootKeyConstants rootKey;

    protected String subKey;

    protected N nextJump;

    // default-private constructor
    public KeyName(N nextJump){
        this.nextJump = nextJump;
    }

    // functional lib？
    public N keyName(RegUtility.RootKeyConstants root, String subKey){
        this.rootKey = root;
        this.subKey = subKey;
        return nextJump;
    }



    protected void formatSubKeyString(){
        // 去除开头和结尾的空白
        subKey = subKey.trim();
        // 将subKey中的/换成\\
        subKey = subKey.replace("/", "\\");
        // 这个判断绝对满足，但建议还是判断！
        if (!subKey.endsWith(" ")){
            subKey = subKey + " ";
        }
        if (!subKey.startsWith("\\")){
            subKey = subKey + "\\";
        }
    }
    @Override
    public String toString() {
        formatSubKeyString();
        return rootKey.toString() + subKey;
    }
}
