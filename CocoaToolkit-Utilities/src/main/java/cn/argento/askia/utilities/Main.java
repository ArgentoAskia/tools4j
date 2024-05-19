package cn.argento.askia.utilities;

import cn.argento.askia.utilities.windows.reg.RegUtility;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println(RegUtility.query().keyName().fullKey(RegUtility.RootKeyConstants.HKLM, "/Software/Microsoft")
                .s().reg32().z().v("123").endOptionalArgs().toString());
        Scanner in = new Scanner(System.in);
        String str1 = "";
        String str2 = "";
        if (in.hasNextLine()){
            str1 = in.nextLine();
        }
        if (in.hasNextLine()){
            str2 = in.nextLine();
        }
        System.out.println(minStr(str1, str2));
    }

    // 辗转相除
    private static int gcd(int str1Length, int str2Length){
        int div1 = Math.max(str1Length, str2Length);
        int div2 = Math.min(str1Length, str2Length);

        while(div2 != 0){
            int res = div1 % div2;
            div1 = div2;
            div2 = res;
        }
        return div1;
    }
    private static String minStr(String str1, String str2){
        if (!str1.concat(str2).equals(str2.concat(str1))){
            return "No exist";
        }
        int g = gcd(str1.length(), str2.length());
        String str1Sub = str1.substring(0, g);
        String str2Sub = str2.substring(0, g);
        if (!str1Sub.equalsIgnoreCase(str2Sub)){
            return "No exist";
        }
        // str1 make by str1Sub
        for (int i = 0; i < str1.length(); i = i + g) {
            final String strRef = str1.substring(i, i + g);
            if (!strRef.equalsIgnoreCase(str1Sub)){
                return "No exist";
            }
        }
        // str2 make by str2sub
        for (int i = 0; i < str2.length(); i = i + g) {
            final String strRef = str2.substring(i, i + g);
            if (!strRef.equalsIgnoreCase(str2Sub)){
                return "No exist";
            }
        }
        return str1Sub;
    }
}
