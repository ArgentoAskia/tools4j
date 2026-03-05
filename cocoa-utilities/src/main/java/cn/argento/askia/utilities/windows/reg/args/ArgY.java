package cn.argento.askia.utilities.windows.reg.args;


@RegCommandArg
public interface ArgY<A extends Arg> extends Arg {
    @RegCommandArg(argName = "y", formatStr = "/y")
    A y();
}
