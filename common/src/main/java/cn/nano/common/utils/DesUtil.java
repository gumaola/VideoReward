package cn.nano.common.utils;


import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class DesUtil {

    // 密钥 长度不得小于24
    public final static String DES_KEY = "perfect36501234567892017";
    // 向量 可有可无 终端后台也要约定
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    public final static String encoding = "UTF-8";


    public static byte[] encrypt(byte[] srcData, byte[] keyData) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keyData);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes(encoding));
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(srcData);
        return encryptData;
    }

    public static byte[] decrypt(byte[] byteMi, byte[] keyData) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(keyData);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes(encoding));
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(byteMi);
        return decryptData;
    }
}
