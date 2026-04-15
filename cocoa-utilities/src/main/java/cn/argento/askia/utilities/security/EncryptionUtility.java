package cn.argento.askia.utilities.security;

import cn.argento.askia.annotations.Utility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 *
 */
@Utility("加密工具类")
public class EncryptionUtility {

    private EncryptionUtility() {
        throw new IllegalAccessError("DigestUtility为工具类, 无法创建该类的对象");
    }

    public enum AsymmetricAlgorithm{
        RSA;
    }

    // RSA 公私密钥
    private static volatile KeyPair keyPair = null;
    private static volatile Cipher cipher = null;
    public static KeyPair generatorKeyPair(){
        return generatorKeyPair(AsymmetricAlgorithm.RSA);
    }
    public static KeyPair generatorKeyPair(AsymmetricAlgorithm algorithm){
        return generatorKeyPair(algorithm.name());
    }
    public static KeyPair generatorKeyPair(String algorithm){
        try {
            KeyPairGenerator rsa = KeyPairGenerator.getInstance(algorithm);
            rsa.initialize(2048);
            keyPair = rsa.generateKeyPair();
            return keyPair;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Cipher generatorCipher(){
        return generatorCipher("RSA");
    }
    public static Cipher generatorCipher(AsymmetricAlgorithm algorithm){
        return generatorCipher(algorithm.name());
    }
    public static Cipher generatorCipher(String algorithm){
        try {
            cipher = Cipher.getInstance(algorithm);
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void doubleCheckLockForInstance(){
        if (keyPair == null || cipher == null){
            synchronized (EncryptionUtility.class){
                if (keyPair == null || cipher == null){
                    generatorKeyPair();
                    generatorCipher();
                }
            }
        }
    }
    public static byte[] encrypt(byte[] context){
        doubleCheckLockForInstance();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            return cipher.doFinal(context);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] encrypt(byte[] context, Cipher cipher, KeyPair keyPair){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            return cipher.doFinal(context);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] context){
        doubleCheckLockForInstance();
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            return cipher.doFinal(context);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] decrypt(byte[] context, Cipher cipher, KeyPair keyPair){
        try {
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            return cipher.doFinal(context);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String context, Charset charset){
        byte[] encrypt = encrypt(context.getBytes(charset));
        return Base64.getEncoder().encodeToString(encrypt);
    }
    public static String encrypt(String context, Charset charset, Cipher cipher, KeyPair keyPair){
        byte[] encrypt = encrypt(context.getBytes(charset), cipher, keyPair);
        return Base64.getEncoder().encodeToString(encrypt);
    }
    public static String encrypt(String context){
        return encrypt(context, StandardCharsets.UTF_8);
    }
    public static String encrypt(String context, Cipher cipher, KeyPair keyPair){
        return encrypt(context, StandardCharsets.UTF_8, cipher, keyPair);
    }

    public static String decrypt(String context, Charset charset){
        byte[] encrypt = Base64.getDecoder().decode(context);
        byte[] decrypt = decrypt(encrypt);
        String s = new String(decrypt, charset);
        return s;
    }
    public static String decrypt(String context, Charset charset, Cipher cipher, KeyPair keyPair){
        byte[] encrypt = Base64.getDecoder().decode(context);
        byte[] decrypt = decrypt(encrypt, cipher, keyPair);
        String s = new String(decrypt, charset);
        return s;
    }
    public static String decrypt(String context){
        return decrypt(context, StandardCharsets.UTF_8);
    }
    public static String decrypt(String context , Cipher cipher, KeyPair keyPair){
        return decrypt(context, StandardCharsets.UTF_8, cipher, keyPair);
    }

    public static PrivateKey pemToPrivateKey(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 去除 PEM 格式中的头部和尾部
        String privateKeyBase64 = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        // 将 Base64 编码的字符串转换为字节数组
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);

        // 将字节数组转换为 X509EncodedKeySpec
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        // 使用 KeyFactory 生成 PublicKey 对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    public static PublicKey pemToPublicKey(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 去除 PEM 格式中的头部和尾部
        String publicKeyBase64 = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        // 将 Base64 编码的字符串转换为字节数组
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // 将字节数组转换为 X509EncodedKeySpec
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // 使用 KeyFactory 生成 PublicKey 对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
    public static String getPEMPublicKey(){
        PublicKey publicKey = keyPair.getPublic();
        byte[] publicKeyEncoded = publicKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyEncoded);
        // 构建 PEM 格式字符串
        return "-----BEGIN PUBLIC KEY-----\n" +
                // 通常 PEM 内容每行 64 个字符，这里简单处理，实际应用中可以更精细地控制换行
                publicKeyBase64.replaceAll("(.{64})", "$1\n") +
                "\n-----END PUBLIC KEY-----";
    }
    public static String getPEMPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] publicKeyEncoded = publicKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyEncoded);
        // 构建 PEM 格式字符串
        return "-----BEGIN PUBLIC KEY-----" +
                // 通常 PEM 内容每行 64 个字符，这里简单处理，实际应用中可以更精细地控制换行
                publicKeyBase64 +
                "-----END PUBLIC KEY-----";
    }
    public static String getPEMPrivateKey(){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] publicKeyEncoded = privateKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyEncoded);
        // 构建 PEM 格式字符串
        return "-----BEGIN PRIVATE KEY-----" +
                // 通常 PEM 内容每行 64 个字符，这里简单处理，实际应用中可以更精细地控制换行
                publicKeyBase64 +
                "-----END PRIVATE KEY-----";
    }
    public static String getPEMPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] publicKeyEncoded = privateKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyEncoded);
        // 构建 PEM 格式字符串
        return "-----BEGIN PRIVATE KEY-----" +
                // 通常 PEM 内容每行 64 个字符，这里简单处理，实际应用中可以更精细地控制换行
                publicKeyBase64 +
                "-----END PRIVATE KEY-----";
    }
    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        clearKeyPair();
        KeyPair keyPair = generatorKeyPair();
        Cipher cipher = generatorCipher();

        String publicKey = getPEMPublicKey(keyPair);
        String privateKey = getPEMPrivateKey(keyPair);
        PrivateKey privateKey1 = pemToPrivateKey(privateKey);
        PublicKey publicKey1 = pemToPublicKey(publicKey);
        System.out.println(publicKey);
        System.out.println(privateKey);
        System.out.println(keyPair.getPrivate().equals(privateKey1));
        System.out.println(keyPair.getPublic().equals(publicKey1));
        System.out.println();

        KeyPair keyPair1 = new KeyPair(publicKey1, privateKey1);
        String helloText = "Hello World!";
        String encrypt = encrypt(helloText, cipher, keyPair1);
        System.out.println("after encrypt: " + encrypt);
        System.out.println();
        String decrypt = decrypt(encrypt, cipher, keyPair1);
        System.out.println("after decrypt: " + decrypt);


        System.out.println();
        Cipher cipher1 = generatorCipher();
        String pv = "-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKfGfeG81H9Zd6wWmiC9U4LoS0BbHi87O+lhWVfuutXML5sMueG8HvrVIPI+skzWutFZg/sSgdWxVuhfc3pjot6DftbHc8xctk/+fzKAC2FTOTuA3ROqrd7G0qXJCJMwH+3Mr9ebz6QiSkb+WySXv3R8u/hWcNSLxymPnx8l4VZdF6Oae78qeFlCl+V2CRgIzdfRYs2IdIGRg1/3H3aH+KWHW66ksw+xmueYMhWvzM9cKTnl7xvvFlvZedbQr9fD835XMJ3ZW6MK7Z4lMKLEzQ5n8elp7+YBVUVsbG2c185GP/5RkP3hMIWNnzB++CJE3juGAK2Zx6Aq27W5uKCz5NAgMBAAECggEASYO9B7UuhjKjVTDzSGiLqaVYINCdG+NO2a+GibO0nHUPs8jcbnRBwV/6nbrtir3ZmSpLhhQfPWYqvF81Wz9Mxy3s8oaPzZ0aWeVXVkc4CXC7zoD4JMfGCMUHNmOo/owrVQYUY/aKETenh/s2rixAOIbObD7+85hXIWZ/2KZWt3hqT0jtRURKAf6/CQVnQ7lxkcTJfki1o0/McbHLvBMrLrizYEU2kvseebGtI87C52qfmTw3NjlIPxxNHkhIovtvHIx6kwqXjLcOVSaGa+yyK95s/Khcz50RCts5OYWGdqCoH4TMVtFXLG2oJBnDgYOovuBPZfUi+2M9oGQB1wzVaQKBgQDSnz+PWYR9tExZbz7frUb3mBkLlklgtJHC/wSF0wyrfb152G9i+Qj/D2O2hD+dE/zE2xkUpFalrHN5kOAiYZBTf7pe2TXUbEJNfZ3W/Adrz/mRkxVIkCsNU/+d1olOOYr/F1Ntwx7exK3j+GxmNwpk3RTnsULDYp0BUJjE5sOF9wKBgQCoUocX8QpBfq8SzTwHJS4UZrUIozqaUwdbU3Se1dKkod1iv5yLLVKIbsiDzGSZjv0mBEFLhUii51WnWmg2c7bstJ/g4DgQfH2ruSdzi6u21eschydgldcxD91kVJ5/JOvST/pK3OptfDf5Rv0kOSNXhCpr/F5fWX0vAKe2qjx82wKBgHYenTS1jVo6DT9y5h3nPY+JmzImXIyBd+WVw2GR1N8wK1z0BrPI2kTAyrkHPe3H0dBM16n96CINrs0UdL6TXK+XSFv9EPVKqXxv4F87BKZwApSjmiIRyDM3UnmgYWyfVNZurHk4Hk3sm2DoZ1KDt66NVjYB8WFGIqskGikDCdBNAoGAY6uSsTua/hNc6f47eGOsuInk0vuM3QsjRRHS8abdt3zdkfayZLturrki5OBoBYHlJtjILUb8LvJbBuEC6pN0kw+VyFwc9q4w4DmgQcxH3tknAlqJl6jCpxuk1h9BRzp+paeUJzZPXot6vZIqnYN0CQOK9yyWWVGtTeLObNy7OpcCgYEAmspOGlXb3XOsVPB3aX5YexbRweQaVqcGpNmzoc3NCJcxBxc5OMScpWlG5m2qm7XJuOYqd93QQV2jioByWoocOv6M3FFaVX7fuTx/waiu9tXu5A/Q2CIxTO9gcI+IHXW97FqyEdYh/Cj9sZU7CVwrumz9ff4hFgs2murixRJjPsg=-----END PRIVATE KEY-----";
        String pu = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAinxn3hvNR/WXesFpogvVOC6EtAWx4vOzvpYVlX7rrVzC+bDLnhvB761SDyPrJM1rrRWYP7EoHVsVboX3N6Y6Leg37Wx3PMXLZP/n8ygAthUzk7gN0Tqq3extKlyQiTMB/tzK/Xm8+kIkpG/lskl790fLv4VnDUi8cpj58fJeFWXRejmnu/KnhZQpfldgkYCM3X0WLNiHSBkYNf9x92h/ilh1uupLMPsZrnmDIVr8zPXCk55e8b7xZb2XnW0K/Xw/N+VzCd2VujCu2eJTCixM0OZ/Hpae/mAVVFbGxtnNfORj/+UZD94TCFjZ8wfvgiRN47hgCtmcegKtu1ubigs+TQIDAQAB-----END PUBLIC KEY-----";
        PublicKey publicKey2 = pemToPublicKey(pu);
        PrivateKey privateKey2 = pemToPrivateKey(pv);
        KeyPair keyPair2 = new KeyPair(publicKey2, privateKey2);
//        String encrypt1 = encrypt(helloText, cipher1, keyPair2);
//        System.out.println(encrypt1);
        System.out.println(cipher1.getProvider());
        String encrypt1 = "NpCr3DXI1Kk9s8JAb5o6YN//IgqKdvttrBxue5OHZsKrcgxcXgp05ROdCV+ZyppurEbt1WDILU+AzSLW4aiIYyUBb40Pt6cFRqa4uPWNKzvSpMTwupdJ9A314UFH/rzePri15gd0fVFm89v4xU4/TQ//ZcTuujJe11Z2/Fugv5lxJmw6GhX3MRGnY+AK/UJAHiup2yAHCgG2m+QHkzUYmEA1cP34OMWQtBC9LjMtEwv97yeRLkyPSDo1K1OFKNj9ih8pmyX56ptdlb5ou3o8rzMWRgqatYLqnrGg4kzyhbbPMRIqPDeCUJQYBHSrQbsqcQIGBu9Z+ls+WgMOgVC8Zg==";
        String decrypt1 = decrypt(encrypt1, cipher1, keyPair2);
        System.out.println(decrypt1);



    }
    public static void clearKeyPair(){
        if (keyPair != null || cipher != null){
            synchronized (EncryptionUtility.class){
                if (keyPair != null || cipher != null){
                    keyPair = null;
                    cipher = null;
                }
            }
        }
    }


}
