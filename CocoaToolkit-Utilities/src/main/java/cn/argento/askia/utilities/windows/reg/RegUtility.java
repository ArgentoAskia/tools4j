package cn.argento.askia.utilities.windows.reg;


/**
 * 注册表工具类！
 */
public class RegUtility {

    public enum RootKeyConstants{
        HKLM,
        HKCU,
        HKCR,
        HKU,
        HKCC;
    }
    // 在远程机器上只有 HKLM 和 HKU 可用。
    public enum MachineRootKeyConstants{
        HKLM,
        HKU
    }

    public enum Type{
        REG_SZ,
        REG_MULTI_SZ,
        REG_EXPAND_SZ,
        REG_DWORD,
        REG_QWORD,
        REG_BINARY,
        REG_NONE
    }

    public static void main(String[] args) {

    }

    private static boolean systemSupported(){
        if (System.getenv("OS").equalsIgnoreCase("Windows_NT")){
            return true;
        }
        return System.getProperty("os.name").contains("windows");
    }


    public static boolean isSystemSupported(){
        return systemSupported();
    }

    private static void checkSystemSupported(){
        if (!isSystemSupported())
            throw new UnsupportedOperationException("非 Windows NT 系统, 不支持注册表操作！");
    }
    public static RegQuery query(){
        checkSystemSupported();
        return new RegQuery();
    }
    public static RegAdd add(){
        checkSystemSupported();
        return new RegAdd();
    }
    public static RegDelete delete(){
        checkSystemSupported();
        return new RegDelete();
    }

    public static RegCompare compare(){
        checkSystemSupported();
        return new RegCompare();
    }

    public static RegCopy copy(){
        checkSystemSupported();
        return new RegCopy();
    }

    public static RegExport export(){
        checkSystemSupported();
        return new RegExport();
    }

    public static RegFlags flags(){
        checkSystemSupported();
        return new RegFlags();
    }

    public static RegImport imports(){
        checkSystemSupported();
        return new RegImport();
    }

    public static RegLoad load(){
        checkSystemSupported();
        return new RegLoad();
    }

    public static RegRestore restore(){
        checkSystemSupported();
        return new RegRestore();
    }

    public static RegSave save(){
        checkSystemSupported();
        return new RegSave();
    }

    public static RegUnload unload(){
        checkSystemSupported();
        return new RegUnload();
    }
}
