package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgV<A extends Arg> extends Arg{

    @RegCommandArg(argName = "v", formatStr = "/v [valueName]")
    A v(String ValueName);
}