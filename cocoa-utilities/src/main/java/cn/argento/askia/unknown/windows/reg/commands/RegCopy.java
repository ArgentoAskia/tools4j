//package cn.argento.askia.unknown.windows.reg.commands;
//
//import cn.argento.askia.unknown.windows.reg.RegSubCommands;
//import cn.argento.askia.unknown.windows.reg.RegUtility;
//import cn.argento.askia.unknown.windows.reg.args.ArgManager;
//import cn.argento.askia.unknown.windows.reg.operators.*;
//
//public final class RegCopy extends AbstractRegCommand
//        implements IRemoteKeyName<RemoteKeyName<RegCopy.RegCopyOptionalArgs>> {
//
//    protected RegCopy() {
//        super(RegSubCommands.REG_COPY);
//    }
//
//    @Override
//    protected void registerArgBits() {
//
//    }
//    public interface RegCopyOptionalArgs {
//    }
//
//
//    private KeyName<RemoteKeyName<RegCopyOptionalArgs>> keyName1;
//    private RemoteKeyName<RegCopyOptionalArgs> keyName2;
//    public RemoteKeyName<RegCopyOptionalArgs> keyName(RegUtility.RootKeyConstants root, String subKey){
//        RegCopyOptionalArgs regCopyOptionalArgs = ArgManager.createOptionArgsProxy(RegCopyOptionalArgs.class);
//        keyName2 = new RemoteKeyName<>(regCopyOptionalArgs);
//        keyName1 = new RemoteKeyName<>(keyName2);
//        return keyName1.keyName(root, subKey);
//    }
//
//    public RemoteKeyName<RegCopyOptionalArgs> keyName(Machine remoteMachine,
//                                      RegUtility.MachineRootKeyConstants root,
//                                      String subKey){
//        RegCopyOptionalArgs regCopyOptionalArgs = ArgManager.createOptionArgsProxy(RegCopyOptionalArgs.class);
//        keyName2 = new RemoteKeyName<>(regCopyOptionalArgs);
//        keyName1 = new RemoteKeyName<>(keyName2);
//        return ((RemoteKeyName<RemoteKeyName<RegCopyOptionalArgs>>)keyName1)
//                .keyName(remoteMachine, root, subKey);
//    }
//
//    @Override
//    protected String buildCommandPrefix() {
//        return super.buildCommandPrefix() + keyName1.toString() + keyName2.toString();
//    }
//}
