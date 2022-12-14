package com.imooc.malldevv1.util;

import com.imooc.malldevv1.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具（hash算法）
 * 来自视频4-8 对密码进行MD5保护
 * 2022-08-19 创建
 * 2022-08-19 编写
 */

@Component
public class MD5Utils {


    //由于它是一个工具类，使用static修饰，方便其他类调用.
    //带密匙加密
    public static String getMd5Str(String strValue) throws NoSuchAlgorithmException {
        //getInstance，需要传入算法，此处传入 MD5 算法名称
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        //digest方法的接收类型是字节数据byte[]
        //Base64的转码，方便存储。String containing Base64 characters.
        //加盐值，使得密码强度更强;将盐值这个常量定义在common包下的Constant类下
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));

        //自己写的 （：
//        String encodeStr = DigestUtils.md5Hex(password + salt);
//        return encodeStr;
    }

}
