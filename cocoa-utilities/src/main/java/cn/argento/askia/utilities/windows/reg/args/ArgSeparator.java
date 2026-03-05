package cn.argento.askia.utilities.windows.reg.args;

@RegCommandArg
public interface ArgSeparator<A extends Arg> extends Arg {

    @RegCommandArg(argName = "s", formatStr = "/s [separator]")
    A s(String Separator);
}
