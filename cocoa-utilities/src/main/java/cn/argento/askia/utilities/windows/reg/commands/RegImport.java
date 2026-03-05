//package cn.argento.askia.utilities.windows.reg.commands;
//
//import cn.argento.askia.utilities.windows.reg.RegSubCommands;
//import cn.argento.askia.utilities.windows.reg.args.ArgManager;
//import cn.argento.askia.utilities.windows.reg.operators.FileName;
//import cn.argento.askia.utilities.windows.reg.operators.IFileName;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.util.function.Supplier;
//
//public final class RegImport extends AbstractRegCommand implements IFileName<RegImport.RegImportOptionalArgs> {
//
//    protected RegImport() {
//        super(RegSubCommands.REG_IMPORT);
//    }
//
//
//    public interface RegImportOptionalArgs {}
//    @Override
//    protected void registerArgBits() {
//
//    }
//
//    private FileName<RegImportOptionalArgs> fileName;
//
//    @Override
//    public RegImportOptionalArgs fileName(Supplier<File> fileSupplier) {
//        fileName = new FileName<>(ArgManager.createOptionArgsProxy(RegImportOptionalArgs.class));
//        return fileName.fileName(fileSupplier);
//    }
//
//    @Override
//    public RegImportOptionalArgs fileName(String filePath) {
//        fileName = new FileName<>(ArgManager.createOptionArgsProxy(RegImportOptionalArgs.class));
//        return fileName.fileName(filePath);
//    }
//
//    @Override
//    public RegImportOptionalArgs fileName(Path filePath) {
//        fileName = new FileName<>(ArgManager.createOptionArgsProxy(RegImportOptionalArgs.class));
//        return fileName.fileName(filePath);
//    }
//
//    @Override
//    protected String buildCommandPrefix() {
//        return super.buildCommandPrefix() + fileName.toString();
//    }
//}
