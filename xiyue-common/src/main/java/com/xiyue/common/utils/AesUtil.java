package com.xiyue.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.symmetric.AES;

public class AesUtil {

    //这个不要轻易更改
    private static final String secretKey = "lykj-hcl-aes-key";

    public static String encryptHex(String content, String salt){

        AES aes = new AES("CBC", "PKCS7Padding",
                secretKey.getBytes(),
                // iv加盐，按照实际需求添加
                salt.getBytes());

        return aes.encryptHex(content);
    }

    public static String decryptStr(String encryptHex, String salt){

        AES aes = new AES("CBC", "PKCS7Padding",
                secretKey.getBytes(),
                // iv加盐，按照实际需求添加
                salt.getBytes());

        return aes.decryptStr(encryptHex);

    }

    public static void main(String[] args) {
        String salt = RandomUtil.randomString(16);

        String encryptHex = AesUtil.encryptHex("123456",salt);
        String decryptStr = AesUtil.decryptStr(encryptHex, salt);
        //
       // log.info();
    }

}
