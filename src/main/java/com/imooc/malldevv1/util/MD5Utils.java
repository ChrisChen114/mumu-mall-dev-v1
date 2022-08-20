package com.imooc.malldevv1.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具（hash算法）
 * 2022-08-19 创建
 * 2022-08-19 编写
 */

@Component
public class MD5Utils {
    //盐值,此处是写的固定的。当然也可以把盐值写入数据库
    private static final String salt = "SNfha&ivAp,.@!";

    //由于它是一个工具类，使用static修饰，方便其他类调用.
    //带密匙加密
    public static String getMd5Str(String strValue) throws NoSuchAlgorithmException {
        //getInstance，需要传入算法，此处传入 MD5 算法名称
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //digest方法的接收类型
        //Base64的转码，方便存储。String containing Base64 characters.
        return Base64.encodeBase64String(md5.digest((strValue+salt).getBytes())) ;
//        String encodeStr = DigestUtils.md5Hex(password + salt);
//        return encodeStr;
    }

}
