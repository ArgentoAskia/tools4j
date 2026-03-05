package cn.argento.askia.utilities.windows.reg.args;

import cn.argento.askia.utilities.windows.reg.commands.RegSubCommandManager;

@RegCommandArg
public interface ArgReg32<A extends Arg> extends Arg{

    @RegCommandArg(argName = "reg:32")
    A reg32();

}
