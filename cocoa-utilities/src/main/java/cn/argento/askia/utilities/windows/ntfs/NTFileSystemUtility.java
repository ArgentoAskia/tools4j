package cn.argento.askia.utilities.windows.ntfs;


public class NTFileSystemUtility {


    static boolean systemSupported(){
        if (System.getenv("OS").equalsIgnoreCase("Windows_NT")){
            return true;
        }
        return System.getProperty("os.name").contains("windows");
    }


}
