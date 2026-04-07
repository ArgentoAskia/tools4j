package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgOutput<A extends Arg> extends Arg {

    @RegCommandArg(argName = "oa")
    A oa();
    @RegCommandArg(argName = "od")
    A od();
    @RegCommandArg(argName = "os")
    A os();
    @RegCommandArg(argName = "on")
    A on();
}
