package cn.argento.askia.utilities.windows.reg.commands;

import cn.argento.askia.utilities.windows.reg.*;
import cn.argento.askia.utilities.windows.reg.args.*;
import cn.argento.askia.utilities.windows.reg.operators.*;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class RegAdd extends AbstractRegCommand implements IRemoteKeyName<RegAdd.RegAddOptionalArgs> {

    protected RegAdd() {
        super(RegSubCommands.REG_ADD);
    }

    private KeyName<RegAddOptionalArgs> keyName;
    public RegAddOptionalArgs keyName(RegUtility.RootKeyConstants root, String subKey){
        optionalArgs = ArgManager.newOptionalArgsHandler(RegAddOptionalArgs.class, this);
        RegAddOptionalArgs regAddOptionalArgs = ArgManager.createOptionArgsProxy(RegAddOptionalArgs.class, optionalArgs);
        keyName = new KeyName<>(regAddOptionalArgs);
        return keyName.keyName(root, subKey);
    }
    public RegAddOptionalArgs keyName(Machine remoteMachine,
                     RegUtility.MachineRootKeyConstants root,
                     String subKey){
        optionalArgs = ArgManager.newOptionalArgsHandler(RegAddOptionalArgs.class, this);
        RegAddOptionalArgs regAddOptionalArgs = ArgManager.createOptionArgsProxy(RegAddOptionalArgs.class, optionalArgs);
        keyName = new RemoteKeyName<>(regAddOptionalArgs);
        return ((RemoteKeyName<RegAddOptionalArgs>)keyName).keyName(remoteMachine, root, subKey);
    }

    @Override
    protected void build() {

    }

    @Override
    protected void registerSameArgs(BiConsumer<String, String> registerSame) {
    }

    public interface RegAddOptionalArgs extends ArgV<RegAddOptionalArgs>, ArgVE<RegAddOptionalArgs>{
    }




}
