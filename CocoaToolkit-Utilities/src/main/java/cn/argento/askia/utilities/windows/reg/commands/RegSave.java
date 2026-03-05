//package cn.argento.askia.utilities.windows.reg.commands;
//
//import cn.argento.askia.utilities.windows.reg.RegSubCommands;
//import cn.argento.askia.utilities.windows.reg.operators.FileName;
//import cn.argento.askia.utilities.windows.reg.operators.KeyName;
//
//public final class RegSave extends AbstractRegCommand {
//    protected RegSave() {
//        super(RegSubCommands.REG_SAVE);
//    }
//
//    @Override
//    protected void registerArgBits() {
//
//    }
//
//    public interface RegSaveOptionalArgs{}
//
//    private KeyName<FileName<RegSaveOptionalArgs>> keyName;
//    private FileName<RegSaveOptionalArgs> fileName;
//
//    @Override
//    protected String buildCommandPrefix() {
//        return null;
//    }
//}
