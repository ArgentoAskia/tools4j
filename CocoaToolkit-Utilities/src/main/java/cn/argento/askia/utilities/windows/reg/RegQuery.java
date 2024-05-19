package cn.argento.askia.utilities.windows.reg;


import java.io.UnsupportedEncodingException;

public final class RegQuery extends AbstractCommand{

//    public interface RequiredArgs{
//        OptionalArgs addOptionalArgs();
//    }
//
//    public interface OptionalArgs{
//        <T extends ArgOptionalSubArgs> T addArgOptionalSubArgs();
//    }
//
//    public interface ArgOptionalSubArgs{
//        OptionalArgs finish();
//    }

    @Override
    protected void registerArgBits() {
        register("keyName", "v", "s", "f", "t", "z", "se", "reg:64");
        registerSame("ve", "v");
        registerSame("reg:32", "reg:64");
    }

    @Override
    protected String analyzeResult(byte[] result) {
        try {
            return new String(result, 0, result.length, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 借助接口来暴露方法
    private void checkArgUniqueAndAddBit(String argName){
        if (cmpBit(argName)){
            throw new IllegalArgumentException(argName + " arg not allow Duplicated");
        }
        addBit(argName);
        needAppendWhiteSpace();
    }
    // default package make visible for RegUtility.java
    RegQuery(){
        super("REG QUERY ");
    }

    @Override
    public Command build() {
        return RegQuery.this;
    }

    public interface KeyName{
        interface Machine{
            OptionalArgs machineFullKey(RegUtility.MachineRootKeyConstants rootKey,
                                        String subKey);
        }

        KeyName.Machine machine(String machineName);
        OptionalArgs fullKey(RegUtility.RootKeyConstants root, String sub);
    }

    private class KeyNameImpl implements KeyName{
        Machine machine;
        MachineFullKey machineFullKey;
        FullKey fullKey;

        class Machine implements KeyName.Machine {
            String machine;

            Machine(String machine){
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
            this.machine = new Machine(machineName);
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


    public interface OptionalArgs{
        OptionalArgs v(String valueName);
        OptionalArgs ve();
        OptionalArgs s();
        OptionalArgs t(RegUtility.Type type);

        OptionalArgs z();

        OptionalArgs reg64();

        OptionalArgs reg32();


        OptionalArgs separator(char c);

        OptionalArgs se(char c);


        // has sub-args arg
        SubOptionalArgsF f(String data);



        //end to build optionalArgs
        Command endOptionalArgs();
        Command noOptionalArgs();
    }


    private class OptionalArgsImpl implements OptionalArgs{

        @Override
        public OptionalArgs v(String valueName) {
            checkArgUniqueAndAddBit("v");
            appendCMD("/v \"" + valueName + "\"", false);
            return this;
        }

        @Override
        public OptionalArgs ve() {
            checkArgUniqueAndAddBit("ve");
            appendCMD("/ve", false);
            return this;
        }

        @Override
        public OptionalArgs s() {
            checkArgUniqueAndAddBit("s");
            appendCMD("/s", false);
            return this;
        }

        @Override
        public OptionalArgs t(RegUtility.Type type) {
            checkArgUniqueAndAddBit("t");
            appendCMD("/t " + type.toString(), false);
            return this;
        }

        @Override
        public OptionalArgs z() {
            checkArgUniqueAndAddBit("z");
            appendCMD("/z", false);
            return this;
        }

        @Override
        public OptionalArgs reg64() {
            checkArgUniqueAndAddBit("reg:64");
            appendCMD("/reg:64", false);
            return this;
        }

        @Override
        public OptionalArgs reg32() {
            checkArgUniqueAndAddBit("reg:32");
            appendCMD("/reg:32", false);
            return this;
        }

        @Override
        public OptionalArgs separator(char c) {
            checkArgUniqueAndAddBit("se");
            appendCMD("/se " + c, false);
            return this;
        }

        @Override
        public OptionalArgs se(char c) {
            return separator(c);
        }

        @Override
        public SubOptionalArgsF f(String data) {
            checkArgUniqueAndAddBit("f");
            appendCMD("/f \"" + data + "\"", false);
            return new SubOptionalArgFImpl();
        }

        @Override
        public Command endOptionalArgs() {
            return RegQuery.this;
        }

        @Override
        public Command noOptionalArgs() {
            return endOptionalArgs();
        }
    }


    public interface SubOptionalArgsF{
        SubOptionalArgsF k();
        SubOptionalArgsF c();
        SubOptionalArgsF d();
        SubOptionalArgsF e();

        OptionalArgs endSubOptionalArg();
        OptionalArgs noSubOptionalArg();
    }


    private class SubOptionalArgFImpl implements SubOptionalArgsF{

        @Override
        public SubOptionalArgsF k() {
            checkArgUniqueAndAddBit("k");
            appendCMD("/k", false);
            return this;
        }

        @Override
        public SubOptionalArgsF c() {
            checkArgUniqueAndAddBit("c");
            appendCMD("/c", false);
            return this;
        }

        @Override
        public SubOptionalArgsF d() {
            checkArgUniqueAndAddBit("d");
            appendCMD("/d", false);
            return this;
        }

        @Override
        public SubOptionalArgsF e() {
            checkArgUniqueAndAddBit("e");
            appendCMD("/e", false);
            return this;
        }

        @Override
        public OptionalArgs endSubOptionalArg() {
            return null;
        }

        @Override
        public OptionalArgs noSubOptionalArg() {
            return null;
        }
    }


}
