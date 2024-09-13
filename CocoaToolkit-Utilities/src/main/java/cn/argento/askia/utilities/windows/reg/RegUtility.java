package cn.argento.askia.utilities.windows.reg;


import cn.argento.askia.utilities.windows.reg.args.RegCommandArg;
import cn.argento.askia.utilities.windows.reg.sub.RegAdd;
import cn.argento.askia.utilities.windows.reg.sub.RegCompare;
import cn.argento.askia.utilities.windows.reg.sub.RegCopy;
import cn.argento.askia.utilities.windows.reg.sub.RegSubCommandManager;

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

    private static void checkSystemSupportedAndThrow(){
        if (!isSystemSupported())
            throw new UnsupportedOperationException("非 Windows NT 系统, 不支持注册表操作！");
    }

    public enum Solver{
        USE_LATEST, THROW_EXCEPTION
    }
    private static Solver solver;
    public static void setCallingChainExceptionSolve(Solver solve){
        RegUtility.solver = solve;
    }
    public static Solver getCallingChainExceptionSolve(){
        return solver;
    }


    public RegAdd add(){
        return RegSubCommandManager.newRegSubCommandInstance(RegAdd.class);
    }

    public RegCopy copy(){
        return RegSubCommandManager.newRegSubCommandInstance(RegCopy.class);
    }

    public RegCompare compare(){
        return RegSubCommandManager.newRegSubCommandInstance(RegCompare.class);
    }


}
