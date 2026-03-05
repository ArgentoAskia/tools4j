package cn.argento.askia.utilities.windows.reg.commands;

import cn.argento.askia.utilities.windows.reg.RegSubCommands;
import cn.argento.askia.utilities.windows.reg.args.OptionalArgsHandler;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *  Abstract RegCommand class. for sub commands!
 * 提供一种标记重复调用参数的能力
 *
 *
 * @author Askia
 */
public abstract class AbstractRegCommand implements RegCommand {

    // string type REG command!
    // use for build()?
    protected StringBuilder commandBuilder;


    // for sub command, like REG ADD、REG COMPARE
    protected RegSubCommands regSubCommands;

    // 参数
    protected OptionalArgsHandler<?> optionalArgs;

    protected AbstractRegCommand(RegSubCommands commands){
        this.regSubCommands = commands;
    }

    // build Command for commandBuilder and prepared exec
    // lazy build
    protected abstract void build();

    protected abstract void registerSameArgs(BiConsumer<String, String> registerSame);




    @Override
    public Object exec() {
        return null;
    }

    @Override
    public RegCommand trace() {
        return null;
    }
}
