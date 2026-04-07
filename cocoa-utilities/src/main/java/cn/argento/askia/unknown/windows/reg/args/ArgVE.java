package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgVE<A extends Arg> extends Arg{
    @RegCommandArg(argName = "ve")
    A ve();
}
