package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgS<A extends Arg> extends Arg{
    // for REG COPY
    @RegCommandArg(argName = "s")
    A s();
}
