package cn.argento.askia.utilities.security;

import cn.argento.askia.annotations.Utility;
import cn.argento.askia.utilities.lang.LangUtility;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Utility("信息摘密工具类")
public class DigestUtility {

    private DigestUtility(){
        throw new IllegalAccessError("DigestUtility为工具类, 无法创建该类的对象");
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString("123".getBytes(StandardCharsets.UTF_8)));
        String s = sha1("123", StandardCharsets.UTF_8);
        System.out.println(s);
    }


    public static String md5(String str){
        byte[] bytes = md5(str.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b: bytes) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1){
                hexString.append('0');
            }
            hexString.append(s);
        }
        return hexString.toString();
    }
    public static String md5(String str, Charset c){
        byte[] bytes = md5(str.getBytes(c));
        StringBuilder hexString = new StringBuilder();
        for (byte b: bytes) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1){
                hexString.append('0');
            }
            hexString.append(s);
        }
        return hexString.toString();
    }
    private static byte[] md5(byte[] bytes){
        try {
            MessageDigest md5 = MessageDigest.getInstance("Md5");
            return md5.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
    private static String md5Unsigned(byte[] bytes){
        byte[] signedBytes = md5(bytes);
        StringBuilder stringBuilder = new StringBuilder();
        for (byte signedByte : signedBytes) {
            String s = Integer.toUnsignedString(signedByte);
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }



    private enum SHA{
        SHA_512("SHA-512"), SHA_1("SHA-1"), SHA_256("SHA-256");

        private final String algorithmName;


        SHA(String name){
            this.algorithmName = name;
        }

        public String getAlgorithmName() {
            return algorithmName;
        }
    }

    public static String sha1(String str){
        return sha1(str, Charset.defaultCharset());
    }
    public static String sha1(String str, Charset c){
        return shaHex(str, c, SHA.SHA_1);
    }
    public static String sha256(String str){
        return sha256(str, Charset.defaultCharset());
    }
    public static String sha256(String str, Charset c){
        return shaHex(str, c, SHA.SHA_256);
    }
    public static String sha512(String str){
        return sha512(str, Charset.defaultCharset());
    }
    public static String sha512(String str, Charset c){
        return shaHex(str, c, SHA.SHA_512);
    }
    private static String shaHex(String str, Charset c, SHA algo){
        byte[] contextBytes = str.getBytes(c);
        byte[] hashBytes = sha(contextBytes, algo);
        return LangUtility.bytesToHexString(hashBytes);
    }
    private static byte[] sha(byte[] bytes, SHA algo){
        try {
            MessageDigest sha = MessageDigest.getInstance(algo.getAlgorithmName());
            return sha.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            //  this will not happen!
            e.printStackTrace();
            return new byte[0];
        }
    }

}
