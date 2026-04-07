//package cn.argento.askia.unknown.windows.reg.commands;
//
//import cn.argento.askia.unknown.windows.reg.RegSubCommands;
//import cn.argento.askia.unknown.windows.reg.RegUtility;
//import cn.argento.askia.unknown.windows.reg.args.ArgManager;
//import cn.argento.askia.unknown.windows.reg.operators.*;
//
//public final class RegCompare extends AbstractRegCommand implements
//        IRemoteKeyName<RemoteKeyName<RegCompare.RegCompareOptionalArgs>> {
//
//    protected RegCompare() {
//        super(RegSubCommands.REG_COMPARE);
//    }
//
//    @Override
//    protected void registerArgBits() {
//
//    }
//
//    public interface RegCompareOptionalArgs{
//
//    }
//
//
//    private KeyName<RemoteKeyName<RegCompareOptionalArgs>> keyName1;
//    private RemoteKeyName<RegCompareOptionalArgs> keyName2;
//
//    public RemoteKeyName<RegCompareOptionalArgs> keyName(RegUtility.RootKeyConstants root, String subKey){
//        RegCompareOptionalArgs regCompareOptionalArgs = ArgManager.createOptionArgsProxy(RegCompareOptionalArgs.class);
//        keyName2 = new RemoteKeyName<>(regCompareOptionalArgs);
//        keyName1 = new KeyName<>(keyName2);
//        return keyName1.keyName(root, subKey);
//    }
//
//    public RemoteKeyName<RegCompareOptionalArgs> keyName(Machine remoteMachine,
//                                                RegUtility.MachineRootKeyConstants root,
//                                                String subKey){
//        RegCompareOptionalArgs regCompareOptionalArgs = ArgManager.createOptionArgsProxy(RegCompareOptionalArgs.class);
//        keyName2 = new RemoteKeyName<>(regCompareOptionalArgs);
//        keyName1 = new RemoteKeyName<>(keyName2);
//        return ((RemoteKeyName<RemoteKeyName<RegCompareOptionalArgs>>)keyName1)
//                .keyName(remoteMachine, root, subKey);
//    }
//
//    @Override
//    protected String buildCommandPrefix() {
//        return super.buildCommandPrefix() + keyName1.toString() + keyName2.toString();
//    }
//}
