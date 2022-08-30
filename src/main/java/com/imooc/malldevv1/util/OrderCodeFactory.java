package com.imooc.malldevv1.util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成订单号No的工具类
 * 涉及到加密、随机数等
 * 2022-08-29 创建
 * 来自视频8-3
 * 对应步骤s10-1
 * 增加@Component 注解
 */

public class OrderCodeFactory {

    //订单类别头    //都是1打头的
    private static final String ORDER_CODE = "1";//

    //随机码
    private static final int[] r = new int[]{7, 9, 6, 2, 8, 1, 3, 0, 5, 4};

    //用户id和随机数总长度
    private static final int maxLength = 5;

    //根据id进行加密+加随机数组成固定长度编码
    private static String toCode(Long id) {
        String idStr = id.toString();
        StringBuilder idSb = new StringBuilder();
        for (int i = idStr.length() - 1; i >= 0; i--) {
            idSb.append(r[idStr.charAt(i) - '0']);
        }
        //生成随机的内容，起到加密保护的作用
        //return idSb.append(getRandom(Long.valueOf(maxLength - idStr.length()))).toString();
        return idSb.append(getRandom(maxLength - idStr.length())).toString();
    }

    //生成时间戳
    private static String getDateTime() {
        DateFormat sdf = new SimpleDateFormat("HHmmss");
        return sdf.format(new Date());
    }

    //生成固定长度随机码
    //@param n 长度
    private static long getRandom(long n) {
        long min = 1, max = 9;
        //i从1开始，不是从0开始
        //之前写错了.
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min;
        return rangeLong;
    }

    //生成不带类别标头的编码
/*
1.修饰一个代码块,被修饰的代码块称为同步语句块,其作用的范围是大括号{}括起来的代码,作用的对象是调用这个代码块的对象;
2.修饰一个方法,被修饰的方法称为同步方法,其作用的范围是整个方法,作用的对象是调用这个方法的对象;
3.修改一个静态的方法,其作用的范围是整个静态方法,作用的对象是这个类的所有对象;------------->>>>>>
4.修改一个类,其作用的范围是synchronized后面括号括起来的部分,作用主的对象是这个类的所有对象。
*/
    private static synchronized String getCode(Long userId) {
        userId = userId == null ? 10000 : userId;
        return getDateTime() + toCode(userId);
    }

    //生成订单单号编码
    public static String getOrderCode(Long userId) {
        return ORDER_CODE + getCode(userId);
    }
}



