package cn.argento.askia.utilities.windows.reg.args;

@RegCommandArg
public interface ArgD<A extends Arg> extends Arg{
    @RegCommandArg(formatStr = "/d [Data]", argName = "d")
    A d(String data);
}
