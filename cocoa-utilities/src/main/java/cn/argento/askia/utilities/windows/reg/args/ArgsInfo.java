package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.args.formatters.Formatter;

import java.util.Map;

// 参数变动性很大
// 同一参数不同指令具有不同表现
// Bean类
public final class ArgsInfo {

    private String argName;
    private String argFormatString;
    private Object[] args;
    private Class<? extends Formatter> formatterClass;



    @Override
    public String toString() {
        return null;
    }
}
