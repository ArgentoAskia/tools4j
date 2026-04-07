package cn.argento.askia.unknown.windows.reg.args;

@RegCommandArg
public interface ArgVA<A extends Arg> extends Arg{

    @RegCommandArg(argName = "va")
    A va();
}
