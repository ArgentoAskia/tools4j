package cn.argento.askia.utilities.windows.reg;

/**
 * Reg Unload 子命令
 * <p>
 *
 */
public final class RegUnload extends AbstractRegCommand{
    @Override
    protected void registerArgBits() {
        register("keyName");

    }

    @Override
    protected Object analyzeResult(byte[] result) {
        return null;
    }

    @Override
    public RegCommand build() {
        return this;
    }

    RegUnload(){
        super("REG UNLOAD ");
    }

    /**
     * keyName args
     * @param rootKey
     * @param subKey
     * @return
     */
    public RegUnload keyName(RegUtility.MachineRootKeyConstants rootKey, String subKey){
        addBit("keyName");
        subKey = subKey.replace('/', '\\');
        if (!subKey.startsWith("\\")){
            subKey = "\\" + subKey;
        }
        if (subKey.endsWith("\\")){
            subKey = subKey.substring(0, subKey.length() - 1);
        }
        appendCMD(rootKey.name() + subKey, false);
        return this;
    }
}
