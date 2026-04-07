//package cn.argento.askia.unknown.windows.reg.commands;
//
//import cn.argento.askia.unknown.windows.reg.RegSubCommands;
//import cn.argento.askia.unknown.windows.reg.RegUtility;
//import cn.argento.askia.unknown.windows.reg.args.Arg;
//import cn.argento.askia.unknown.windows.reg.args.ArgManager;
//import cn.argento.askia.unknown.windows.reg.operators.FileName;
//import cn.argento.askia.unknown.windows.reg.operators.IKeyName;
//import cn.argento.askia.unknown.windows.reg.operators.KeyName;
//import cn.argento.askia.unknown.windows.reg.operators.Machine;
//
//public final class RegExport extends AbstractRegCommand
//implements IKeyName<FileName<RegExport.RegExportOptionalArgs>> {
//
//
//    protected RegExport() {
//        super(RegSubCommands.REG_EXPORT);
//    }
//
//    public interface RegExportOptionalArgs{
//
//    }
//    @Override
//    protected void registerArgBits() {
//
//    }
//
//
//
//    private KeyName<FileName<RegExportOptionalArgs>> keyName;
//    private FileName<RegExportOptionalArgs> fileName;
//    @Override
//    public FileName<RegExportOptionalArgs> keyName(RegUtility.RootKeyConstants root, String subKey) {
//        fileName = new FileName<>(ArgManager.createOptionArgsProxy(RegExportOptionalArgs.class));
//        keyName = new KeyName<>(fileName);
//        return keyName.keyName(root, subKey);
//    }
//
//    @Override
//    protected String buildCommandPrefix() {
//        return super.buildCommandPrefix() + keyName.toString() + fileName.toString();
//    }
//}
