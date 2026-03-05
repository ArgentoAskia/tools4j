package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.commands.RegSubCommandManager;

@RegCommandArg
public interface ArgOutput<A extends Arg> extends Arg {

    @RegCommandArg(argName = "oa")
    A oa();
    @RegCommandArg(argName = "od")
    A od();
    @RegCommandArg(argName = "os")
    A os();
    @RegCommandArg(argName = "on")
    A on();
}
