package cn.argento.askia.utilities.windows.reg.args;

@RegCommandArg
public interface ArgVE<A extends Arg> extends Arg{
    @RegCommandArg(argName = "ve")
    A ve();
}
