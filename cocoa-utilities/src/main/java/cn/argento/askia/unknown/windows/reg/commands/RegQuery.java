//package cn.argento.askia.unknown.windows.reg.commands;
//
//import cn.argento.askia.unknown.windows.reg.RegSubCommands;
//import cn.argento.askia.unknown.windows.reg.RegUtility;
//import cn.argento.askia.unknown.windows.reg.args.ArgManager;
//import cn.argento.askia.unknown.windows.reg.operators.*;
//
//public final class RegQuery extends AbstractRegCommand
//        implements IRemoteKeyName<RegQuery.RegQueryOptionalArgs> {
//    protected RegQuery() {
//        super(RegSubCommands.REG_QUERY);
//    }
//
//    @Override
//    protected void registerArgBits() {
//
//    }
//
//    private KeyName<RegQueryOptionalArgs> keyName;
//    @Override
//    public RegQueryOptionalArgs keyName(RegUtility.RootKeyConstants root, String subKey) {
//        final RegQueryOptionalArgs regQueryOptionalArgs = ArgManager.createOptionArgsProxy(RegQueryOptionalArgs.class);
//        keyName = new KeyName<>(regQueryOptionalArgs);
//        return keyName.keyName(root, subKey);
//    }
//
//    @Override
//    public RegQueryOptionalArgs keyName(Machine remoteMachine, RegUtility.MachineRootKeyConstants root, String subKey) {
//        final RegQueryOptionalArgs regQueryOptionalArgs = ArgManager.createOptionArgsProxy(RegQueryOptionalArgs.class);
//        keyName = new RemoteKeyName<>(regQueryOptionalArgs);
//        return ((RemoteKeyName<RegQueryOptionalArgs>)keyName).keyName(remoteMachine, root, subKey);
//    }
//
//
//    public interface RegQueryOptionalArgs{}
//
//}
