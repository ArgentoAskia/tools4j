package cn.argento.askia.utilities.windows.reg.args;

@RegCommandArg
public interface ArgReg64<A extends Arg> extends Arg{

    @RegCommandArg(argName = "reg:64")
    A reg64();
}
