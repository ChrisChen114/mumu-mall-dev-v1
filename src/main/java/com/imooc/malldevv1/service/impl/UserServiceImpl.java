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
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }


    //前台：注册新用户
    //2022-08-19
    //在Service层中不直接返回success或error，不直接触碰与最终结果有关的东西；Controller层中，直接返回与最终结果有关的success或error
    //因此Service层中，需要用到抛出异常类（自定义异常类）
    @Override
    public void register(String userName, String password) throws ImoocMallException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            //需要用到自定义异常类
            //用户已存在，不允许重名
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        User user = new User();
        user.setUsername(userName);
        try {
            user.setPassword(MD5Utils.getMd5Str(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //此处不能使用insert插入，因为insert插入语句是插入所有字段，要保证所有字段都存在。
        //而insertSelective，是选择性插入，有值的插入，没有值的字段则用默认值。
        //通常使用insertSelective
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            //需要用到自定义异常类
            //注册新用户失败
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    //前台：登录
    //2022-08-19 编写
    @Override
    public User login(String userName, String password) throws ImoocMallException {
        //*****************************
        // 方案1，视频思路
        // 思路：在库中一块校验用户名和密码，
        // 把符合的用户查出来
        //*****************************
        //将密码进行MD5加密，然后与数据库里保存的进行比对
//        String md5Password = null;
//        try {
//            md5Password = MD5Utils.getMd5Str(password);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        User result = userMapper.selectLogin(userName, md5Password);
//        if (result == null) {
//            //用户不存在
//            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
//        }
//        //能找到用户，则返回用户
//        return result;


        //*****************************
        // 方案2，个人撰写
        // 与方案1的区别：根据用户名先查库获取用户，
        // 再判断password是否相等
        //*****************************
        User result = userMapper.selectByName(userName);
        if (result == null){
            //用户不存在
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        //判断密码是否相等
        try {
            //将密码进行MD5加密，然后与数据库里保存的进行比对
            String passwd = MD5Utils.getMd5Str(password);
            if(!passwd.equals(result.getPassword())){
                //密码不相等，抛出异常
                throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    //前台：更新个性签名
    //2022-08-20 编写
    @Override
    public void updateInformation(User user) throws ImoocMallException {
        //选择性的更新
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        //只更新一条数据，主键是不能重复的；=1是正确的；超过1条，肯定存在错误.
        if (updateCount > 1) {
            //更新失败
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }


    //后台：管理员登录
    //2022-08-20 编写
//    @Override
//    public User adminLogin(String userName, String password) throws ImoocMallException {
//        //将密码进行MD5加密，然后与数据库里保存的进行比对
//        String md5Password = null;
//        try {
//            md5Password = MD5Utils.getMd5Str(password);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        User user = userMapper.selectLogin(userName, md5Password);
//        if (user == null) {
//            //用户不存在
//            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
//        }
//        //检查角色role是否等于2
//        if(!checkAdminRole(user)){
//            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
//        }
//        //能找到管理员，则返回
//        return user;
//    }


    //后台：管理员登录
    //2022-08-20 编写
    @Override
    public boolean checkAdminRole(User user) {
        //根据字段角色role，1-普通用户，2-管理员
        return user.getRole().equals(2);
    }


}
