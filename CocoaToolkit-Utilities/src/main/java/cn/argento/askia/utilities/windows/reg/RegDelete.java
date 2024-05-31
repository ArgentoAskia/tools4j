package cn.argento.askia.utilities.windows.reg;

public final class RegDelete extends AbstractRegCommand {
    @Override
    protected void registerArgBits() {
        register("keyName", "v", "f", "reg:32");
        registerSame("ve", "v");
        registerSame("va", "v");
        registerSame("reg:64", "reg:32");
    }

    @Override
    protected Object analyzeResult(byte[] result) {
        return null;
    }

    @Override
    public RegCommand build() {
        return RegDelete.this;
    }

    RegDelete(){
        super("REG DELETE ");
    }

    public interface KeyName{
        interface Machine{
            OptionalArgs machineFullKey(RegUtility.MachineRootKeyConstants rootKey,
                                               String subKey);
        }

        Machine machine(String machineName);
        OptionalArgs fullKey(RegUtility.RootKeyConstants root, String sub);
    }

    private class KeyNameImpl implements KeyName {
        Machine machine;
        MachineFullKey machineFullKey;
        FullKey fullKey;

        class MachineImpl implements Machine {
            String machine;

            MachineImpl(String machine){
                this.machine = machine;
            }

            // \\ABC\HKLM
            @Override
            public String toString() {
                return "\\\\" + machine + "\\";
            }

            public OptionalArgs machineFullKey(RegUtility.MachineRootKeyConstants rootKey,
                                                      String subKey){
                machineFullKey = new MachineFullKey(rootKey, subKey);
                // set call bit to avoid call method again
                // use or means add
                addBit("keyName");
                appendCMD(machineFullKey.toString(), false);
                return new OptionalArgsImpl();
            }

        }
        class MachineFullKey{
            RegUtility.MachineRootKeyConstants rootKey;
            String subKey;

            MachineFullKey(RegUtility.MachineRootKeyConstants rootKey, String subKey) {
                this.rootKey = rootKey;
                this.subKey = subKey;
            }

            @Override
            public String toString() {
                final String rootKeyStr = rootKey.toString();
                if (!subKey.startsWith("\\")){
                    subKey = "\\" + subKey;
                }
                if (subKey.endsWith("\\")){
                    subKey = subKey.substring(0, subKey.length() - 1);
                }
                return rootKeyStr + subKey;
            }
        }

        class FullKey{
            RegUtility.RootKeyConstants rootKey;
            String subKey;

            FullKey(RegUtility.RootKeyConstants root, String sub){
                subKey = sub;
                rootKey = root;
            }

            @Override
            public String toString() {
                final String rootKeyStr = rootKey.toString();
                // 把锁有的 / 替换成 \\
                subKey = subKey.replace('/', '\\');
                if (!subKey.startsWith("\\")){
                    subKey = "\\" + subKey;
                }
                if (subKey.endsWith("\\")){
                    subKey = subKey.substring(0, subKey.length() - 1);
                }
                return rootKeyStr + subKey;
            }
        }

        @Override
        public Machine machine(String machineName) {
            this.machine = new MachineImpl(machineName);
            appendCMD(this.machine.toString(), false);
            return machine;
        }

        @Override
        public OptionalArgs fullKey(RegUtility.RootKeyConstants root, String sub) {
            this.fullKey = new FullKey(root, sub);
            addBit("keyName");
            appendCMD(this.fullKey.toString(), false);
            return new OptionalArgsImpl();
        }
    }

    public KeyName keyName(){
        checkArgUniqueAndAddBit("keyName");
        return new KeyNameImpl();
    }

    private void checkArgUniqueAndAddBit(String argName){
        if (cmpBit(argName)){
            throw new IllegalArgumentException(argName + " arg not allow Duplicated");
        }
        addBit(argName);
        needAppendWhiteSpace();
    }

    public interface OptionalArgs{
        OptionalArgs v(String value);
        OptionalArgs ve();
        OptionalArgs va();
        OptionalArgs f();
        OptionalArgs reg32();
        OptionalArgs reg64();

        //end to build optionalArgs
        RegCommand endOptionalArgs();
        RegCommand noOptionalArgs();
    }
    private class OptionalArgsImpl implements OptionalArgs{

        private OptionalArgs argsAdd(String arg, String data, boolean appendSpace){
            checkArgUniqueAndAddBit(arg);
            appendCMD("/" + arg + (data == null? "" : data), appendSpace);
            return this;
        }

        @Override
        public OptionalArgs v(String value) {
            return argsAdd("v", value, false);
        }

        @Override
        public OptionalArgs ve() {
            return argsAdd("ve", null, false);
        }

        @Override
        public OptionalArgs va() {
            return argsAdd("va", null, false);
        }

        @Override
        public OptionalArgs f() {
            return argsAdd("f", null, false);
        }

        @Override
        public OptionalArgs reg32() {
            return argsAdd("reg:32", null, false);
        }

        @Override
        public OptionalArgs reg64() {
            return argsAdd("reg:64", null, false);
        }

        @Override
        public RegCommand endOptionalArgs() {
            return RegDelete.this;
        }

        @Override
        public RegCommand noOptionalArgs() {
            return endOptionalArgs();
        }
    }
}
