//package cn.argento.askia.utilities.windows.reg.commands;
//
//import cn.argento.askia.utilities.windows.reg.RegSubCommands;
//import cn.argento.askia.utilities.windows.reg.RegUtility;
//import cn.argento.askia.utilities.windows.reg.args.Arg;
//import cn.argento.askia.utilities.windows.reg.args.ArgManager;
//import cn.argento.askia.utilities.windows.reg.operators.IKeyName;
//import cn.argento.askia.utilities.windows.reg.operators.KeyName;
//import cn.argento.askia.utilities.windows.reg.operators.Machine;
//
//public final class RegFlags extends AbstractRegCommand {
//
//    protected RegFlags() {
//        super(RegSubCommands.REG_FLAGS);
//    }
//
//    @Override
//    protected void registerArgBits() {
//
//    }
//
//
//
//    public interface RegFlagsOptionalArgs{
//
//    }
//
//    private KeyName<RegFlagsOptionalArgs> keyName;
//
//    public RegFlagsOptionalArgs keyName(String subKey) {
//        final RegFlagsOptionalArgs regFlagsOptionalArgs = ArgManager.createOptionArgsProxy(RegFlagsOptionalArgs.class);
//        keyName = new KeyName<>(regFlagsOptionalArgs);
//        return keyName.keyName(RegUtility.RootKeyConstants.HKLM, "Software\\" + subKey);
//    }
//
//
//
//
//    @Override
//    protected String buildCommandPrefix() {
//        return super.buildCommandPrefix() + keyName.toString();
//    }
//}
