package cn.argento.askia.utilities.windows.reg.sub;

import cn.argento.askia.utilities.windows.reg.AbstractRegCommand;

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
}
