//package cn.argento.askia.unknown.windows.reg.commands;
//
//import cn.argento.askia.unknown.windows.reg.RegSubCommands;
//import cn.argento.askia.unknown.windows.reg.RegUtility;
//import cn.argento.askia.unknown.windows.reg.args.ArgManager;
//import cn.argento.askia.unknown.windows.reg.operators.FileName;
//import cn.argento.askia.unknown.windows.reg.operators.IKeyName;
//import cn.argento.askia.unknown.windows.reg.operators.KeyName;
//import cn.argento.askia.unknown.windows.reg.operators.Machine;
//
//public final class RegLoad extends AbstractRegCommand {
//
//    protected RegLoad() {
//        super(RegSubCommands.REG_LOAD);
//    }
//
//    private KeyName<FileName<RegLoadOptionalArgs>> keyName;
//    private FileName<RegLoadOptionalArgs> fileName;
//
//    @Override
//    protected String buildCommandPrefix() {
//        return super.buildCommandPrefix() + keyName.toString() + fileName.toString();
//    }
//
//    public FileName<RegLoadOptionalArgs> keyName(RegUtility.MachineRootKeyConstants root, String subKey) {
//        final RegLoadOptionalArgs regLoadOptionalArgs = ArgManager.createOptionArgsProxy(RegLoadOptionalArgs.class);
//        fileName = new FileName<>(regLoadOptionalArgs);
//        keyName = new KeyName<>(fileName);
//        return keyName.keyName(RegSubCommandManager.toRootKeyConstant(root), subKey);
//    }
//
//    public interface RegLoadOptionalArgs{}
//    @Override
//    protected void registerArgBits() {
//
//    }
//}
