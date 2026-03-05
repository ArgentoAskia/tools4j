package cn.argento.askia.utilities.windows.reg;

/**
 * Reg子指令枚举类, 列出所有Reg子指令, 如: add、copy、load等
 *
 * @author Askia
 * @since 1.0
 */
public enum RegSubCommands {
    REG_ADD("REG ADD"),
    REG_COMPARE("REG COMPARE"),
    REG_COPY("REG COPY"),
    REG_DELETE("REG DELETE"),
    REG_EXPORT("REG EXPORT"),
    REG_FLAGS("REG FLAGS"),
    REG_IMPORT("REG IMPORT"),
    REG_LOAD("REG LOAD"),
    REG_QUERY("REG QUERY"),
    REG_RESTORE("REG RESTORE"),
    REG_SAVE("REG SAVE"),
    REG_UNLOAD("REG UNLOAD");


    private String subCommandPre;


    RegSubCommands(String subCommandPre) {
        this.subCommandPre = subCommandPre;
    }

    public String getSubCommandPre() {
        return subCommandPre;
    }

    /**
     * add " " to the end!
     * @return
     */
    @Override
    public String toString() {
        return subCommandPre.endsWith(" ")? subCommandPre : subCommandPre + " ";
    }
}
