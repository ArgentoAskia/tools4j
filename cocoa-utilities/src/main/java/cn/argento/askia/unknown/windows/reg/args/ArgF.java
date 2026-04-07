package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgF<A extends Arg> extends Arg{
    @RegCommandArg(argName = "f")
    A f();
}
