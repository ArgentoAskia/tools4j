package cn.argento.askia.unknown.windows.reg.args;

import cn.argento.askia.unknown.windows.reg.args.formatters.Formatter;

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
