package cn.argento.askia.utilities.windows.reg;

import java.io.Console;

public final class RegAdd extends AbstractCommand{



    RegAdd(){
        super("REG ADD ");
    }

    @Override
    protected void registerArgBits() {
        register("keyName", "v", "s", "f", "t", "d", "reg:64");
        registerSame("ve", "v");
        registerSame("reg:32", "reg:64");
    }

    // TODO: 2024/3/28 how to solve with the result for REG Add?
    @Override
    protected Object analyzeResult(byte[] result) {

        return null;
    }

    @Override
    public Command build() {
        return RegAdd.this;
    }


    // 借助接口来暴露方法
    private void checkArgUniqueAndAddBit(String argName){
        if (cmpBit(argName)){
            throw new IllegalArgumentException(argName + " arg not allow Duplicated");
        }
        addBit(argName);
        needAppendWhiteSpace();
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

            public RegAdd.OptionalArgs machineFullKey(RegUtility.MachineRootKeyConstants rootKey,
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
        public KeyName.Machine machine(String machineName) {
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
        OptionalArgs s(char c);
        OptionalArgs t(RegUtility.Type type);


        OptionalArgs f();
        OptionalArgs reg64();

        OptionalArgs reg32();


        OptionalArgs separator(char c);

        OptionalArgs d(String data);




        //end to build optionalArgs
        Command endOptionalArgs();

        Command noOptionalArgs();
    }

    private class OptionalArgsImpl implements OptionalArgs {

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
        public OptionalArgs s(char c) {
            return separator(c);
        }


        @Override
        public OptionalArgs t(RegUtility.Type type) {
            checkArgUniqueAndAddBit("t");
            appendCMD("/t " + type.toString(), false);
            return this;
        }

        @Override
        public OptionalArgs f() {
            checkArgUniqueAndAddBit("f");
            appendCMD("/f", false);
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
            checkArgUniqueAndAddBit("s");
            appendCMD("/s " + c, false);
            return this;
        }

        // 插入变量时请使用 ^ 引用！
        @Override
        public OptionalArgs d(String data) {
            // variable ，such as %systemroot%
            if (data.startsWith("%") && data.endsWith("%")){
                data = data.replace("%", "^%");
            }
            checkArgUniqueAndAddBit("d");
            appendCMD("/d " + data, false);
            return this;
        }


        @Override
        public Command endOptionalArgs() {
            return RegAdd.this;
        }

        @Override
        public Command noOptionalArgs() {
            return endOptionalArgs();
        }
    }
}
