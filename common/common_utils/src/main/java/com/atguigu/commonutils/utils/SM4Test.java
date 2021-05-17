package com.atguigu.commonutils.utils;

import org.junit.Test;

import java.io.IOException;

public class SM4Test {

    @Test
    public void testSM4() throws IOException
    {
        String plainText = "abcdef";

        SM4Utils sm4 = new SM4Utils();
        sm4.setSecretKey("1234567890abcdef");
        sm4.setHexString(false);

//        System.out.println("ECB模式");
//        String cipherText = sm4.encryptData_ECB(plainText);
//        System.out.println("密文: " + cipherText);
//        System.out.println("");
//
//        plainText = sm4.decryptData_ECB(cipherText);
//        System.out.println("明文: " + plainText);
//        System.out.println("");

        System.out.println("CBC模式");
        String cipherText = sm4.encryptData_CBC(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptData_CBC(cipherText);
        System.out.println("明文: " + plainText);
    }
}
