//package cn.argento.askia.utilities.windows.reg.commands;
//
//import cn.argento.askia.utilities.windows.reg.RegSubCommands;
//import cn.argento.askia.utilities.windows.reg.RegUtility;
//import cn.argento.askia.utilities.windows.reg.args.Arg;
//import cn.argento.askia.utilities.windows.reg.args.ArgManager;
//import cn.argento.askia.utilities.windows.reg.args.ArgV;
//import cn.argento.askia.utilities.windows.reg.args.ArgVE;
//import cn.argento.askia.utilities.windows.reg.operators.IKeyName;
//import cn.argento.askia.utilities.windows.reg.operators.KeyName;
//import cn.argento.askia.utilities.windows.reg.operators.Machine;
//import cn.argento.askia.utilities.windows.reg.operators.RemoteKeyName;
//
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//
//public final class RegDelete extends AbstractRegCommand implements IKeyName<RegDelete.RegDeleteOptionalArgs> {
//
//
//
//    protected RegDelete(){
//        super(RegSubCommands.REG_DELETE);
//    }
//
//
//    @Override
//    protected void build() {
//
//    }
//
//    @Override
//    protected void registerSameArgs(BiConsumer<String, String> registerSame, Function<String, String> checkedArg) {
//
//    }
//
//    public interface RegDeleteOptionalArgs extends Arg {
//
//    }
//
//
//
//
//    private KeyName<RegDeleteOptionalArgs> keyName;
//    public RegDeleteOptionalArgs keyName(RegUtility.RootKeyConstants root, String subKey){
//        RegDeleteOptionalArgs regDeleteOptionalArgs = ArgManager.createOptionArgsProxy(RegDeleteOptionalArgs.class);
//        keyName = new KeyName<>(regDeleteOptionalArgs);
//        return keyName.keyName(root, subKey);
//    }
//
//    public RegDeleteOptionalArgs keyName(Machine remoteMachine,
//                                      RegUtility.MachineRootKeyConstants root,
//                                      String subKey){
//        RegDeleteOptionalArgs regDeleteOptionalArgs = ArgManager.createOptionArgsProxy(RegDeleteOptionalArgs.class);
//        keyName = new RemoteKeyName<>(regDeleteOptionalArgs);
//        return ((RemoteKeyName<RegDeleteOptionalArgs>)keyName).keyName(remoteMachine, root, subKey);
//    }
//
//}
