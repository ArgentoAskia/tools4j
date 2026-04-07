package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgReg32<A extends Arg> extends Arg{

    @RegCommandArg(argName = "reg:32")
    A reg32();

}
