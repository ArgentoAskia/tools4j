package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.RegUtility;

@RegCommandArg
public interface ArgT<A extends Arg> extends Arg {

    @RegCommandArg(argName = "t", formatStr = "/t [type] ")
    A t(RegUtility.Type type);
}
