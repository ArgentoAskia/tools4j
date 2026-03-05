package cn.argento.askia.utilities.windows.reg.commands;

import cn.argento.askia.utilities.windows.reg.RegSubCommands;

import java.util.function.BiConsumer;
import java.util.function.Function;

// this is a wrapper for regCommand
public final class RegCommandBuilder extends AbstractRegCommand{
    // clone all properties to
    private AbstractRegCommand regCommand;
    public RegCommandBuilder(AbstractRegCommand regCommand) {
        super(regCommand.regSubCommands);
        this.regCommand = regCommand;
    }

    private static boolean buildCommandBeforeExec = false;

    public static void setBuildCommandBeforeExec(boolean buildCommandBeforeExec) {
        RegCommandBuilder.buildCommandBeforeExec = buildCommandBeforeExec;
    }

    // build command！
    @Override
    public void build() {
        if (buildCommandBeforeExec){
            regCommand.build();
        }
    }

    @Override
    public void registerSameArgs(BiConsumer<String, String> registerSame) {
        regCommand.registerSameArgs(registerSame);
    }

    public RegSubCommands getRegSubCommands(){
        return regSubCommands;
    }
}
