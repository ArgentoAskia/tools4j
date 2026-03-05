package cn.argento.askia.utilities.windows.reg.commands;


import cn.argento.askia.utilities.windows.reg.RegUtility;

public enum RegSubCommandManager {
    ALL,
    ADD,
    COMPARE,
    COPY,
    DELETE,
    EXPORT,
    FLAGS,
    IMPORT,
    LOAD,
    QUERY,
    RESTORE,
    SAVE,
    UNLOAD;



    public static <T extends AbstractRegCommand> T newRegSubCommandInstance(Class<T> regSubCommandClass){
        try {
            return regSubCommandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new InstantiationError("class = " + regSubCommandClass);
    }

    // 方便方法，方便将MachineRootKeyConstants --> RootKeyConstant
    static RegUtility.RootKeyConstants toRootKeyConstant(RegUtility.MachineRootKeyConstants machineRootKeyConstants){
        final String machineRootKey = machineRootKeyConstants.name();
        return RegUtility.RootKeyConstants.valueOf(machineRootKey);
    }


}
