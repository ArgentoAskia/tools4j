package cn.argento.askia.utilities.windows.reg;

import org.junit.Test;

public class RegUtilityTest {

    @Test
    public void testCMD(){
        final Command build = RegUtility.query()
                .keyName().fullKey(RegUtility.RootKeyConstants.HKLM, "\\SOFTWARE\\Microsoft\\Clipboard")
                .noOptionalArgs()
                .build();
        System.out.println(build);
        System.out.println(build.exec());
    }
}
