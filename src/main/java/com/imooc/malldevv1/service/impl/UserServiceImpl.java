package com.imooc.malldevv1.service.impl;

import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.dao.UserMapper;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.service.UserService;
import com.imooc.malldevv1.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * UserService实现类
 * 2022-08-19 创建
 * 2022-08-19 编写register()方法
 */
//@Service，语义注解，说明当前类是Service业务服务类
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser(){
        return userMapper.selectByPrimaryKey(1);
    }


    //前台：注册新用户
    //在Service层中不直接返回success或error，不直接触碰与最终结果有关的东西；Controller层中，直接返回与最终结果有关的success或error
    //因此Service层中，需要用到抛出异常类（自定义异常类）
    @Override
    public void register(String userName, String password) throws ImoocMallException, NoSuchAlgorithmException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null){
            //需要用到自定义异常类
            //用户已存在，不允许重名
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        User user = new User();
        user.setUsername(userName);
        user.setPassword(MD5Utils.getMd5Str(password) );

        //此处不能使用insert插入，因为insert插入语句是插入所有字段，要保证所有字段都存在。
        //而insertSelective，是选择性插入，有值的插入，没有值的字段则用默认值。
        //通常使用insertSelective
        int count = userMapper.insertSelective(user);
        if(count == 0){
            //需要用到自定义异常类
            //注册新用户失败
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }



}
