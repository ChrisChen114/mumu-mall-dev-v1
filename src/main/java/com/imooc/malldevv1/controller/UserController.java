package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

/**
 * 用户控制器
 * <p>
 * 用户模块：
 * 前台：注册新用户
 * 前台：登录
 * 前台：更新个性签名
 * 前后台：退出登录
 * 后台：管理员登录
 * <p>
 * 2022-08-19 创建
 * 2022-08-19 编写register()方法
 */

//@Controller，语义注解，说明当前类是MVC应用中的控制器类

@RestController   //@RestController = @Controller + @ResponseBody，两者相等
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/test1")
    //@ResponseBody   //JSON通讯方式，需要引入这个注解
    public User personalPage() {
        return userService.getUser();
    }


    //前台：注册新用户
    //Controller层中，返回与最终结果有关的success或error；而Service层中，抛出异常
    //注意：post请求在浏览器中是不方便模拟的，此时可以使用Postman软件进行测试.
    //单纯的查询，用get；而涉及到插入、更新、删除等操作，要使用post请求
    //@GetMapping,浏览器可以支持，但不推荐
    //@RequestMapping是get和post都支持，但不推荐
    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws ImoocMallException, NoSuchAlgorithmException {
        //下面这三重判断，放在controller层中比较合适；通过三重验证后，可以进入service层，然后进行数据库的操作
        //判断用户名和密码是否为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能小于8位
        if (password.length() < 8) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }

        //到service层中进行数据库的操作
        userService.register(userName, password);
        return ApiRestResponse.success();
    }


    //前台：登录
    //前台：更新个性签名
    //前后台：退出登录
    //后台：管理员登录


}
