package cn.argento.askia.unknown.windows.reg.args;


@RegCommandArg
public interface ArgY<A extends Arg> extends Arg {
    @RegCommandArg(argName = "y", formatStr = "/y")
    A y();
}
