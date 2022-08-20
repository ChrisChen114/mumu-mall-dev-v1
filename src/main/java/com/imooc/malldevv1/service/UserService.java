package com.imooc.malldevv1.service;

import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.model.pojo.User;

import java.security.NoSuchAlgorithmException;

/**
 * UserService,抽象类
 *  2022-08-19 创建
 *  2022-08-19 编写register()方法
 */
public interface UserService {

    User getUser();

    //前台：注册新用户
    void register(String userName, String password) throws ImoocMallException, NoSuchAlgorithmException;



}
