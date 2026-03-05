package cn.argento.askia.utilities.windows.reg.args;

@RegCommandArg
public interface ArgVA<A extends Arg> extends Arg{

    @RegCommandArg(argName = "va")
    A va();
}
