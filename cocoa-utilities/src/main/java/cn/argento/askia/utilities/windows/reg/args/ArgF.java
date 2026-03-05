package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.commands.RegSubCommandManager;

@RegCommandArg
public interface ArgF<A extends Arg> extends Arg{
    @RegCommandArg(argName = "f")
    A f();
}
