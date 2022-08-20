package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    //2022-08-19
    //Controller层中，返回与最终结果有关的success或error；而Service层中，抛出异常
    //注意：post请求在浏览器中是不方便模拟的，此时可以使用Postman软件进行测试.
    //单纯的查询，用get；而涉及到插入、更新、删除等操作，要使用post请求
    //@GetMapping,浏览器可以支持，但不推荐
    //@RequestMapping是get和post都支持，但不推荐
    //@RequestParam,主要用于将请求参数区域的数据映射到控制层方法的参数上
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
    //技术点：登录状态需要保持；
    //session的实现方案，登录后，会保存用户信息到session
    //之后的访问，会先从session中获取用户信息，然后再执行业务逻辑
    //2022-08-19 创建
    //2022-08-20 编写
    //@RequestParam,主要用于将请求参数区域的数据映射到控制层方法的参数上
    @PostMapping("/login")
    public ApiRestResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ImoocMallException, NoSuchAlgorithmException {
        //判断用户名和密码是否为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        //到Service层中，进行登录的查询校验工作
        User user = userService.login(userName, password);
        //在Service层中，已经对user进行了check，此处如果在判断，多此一举
//        if (user == null){
//            //用户不存在，抛出异常
//            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_EXISTED_USER);
//        }

        //保存用户信息到session时，不保存密码；否则别人会分析这个密码的加密方式
        user.setPassword("");
        //设置session属性，名称和值（key,value）写入session中
        session.setAttribute(Constant.IMOOC_MALL_DEV_V1_USER, user);
        //success这里可以返回user信息
        return ApiRestResponse.success(user);
    }


    //前台：更新个性签名
    //2022-08-20 创建
    @PostMapping("/user/update")
    public ApiRestResponse updateUserInfo(@RequestParam("signature")  String signature,HttpSession session) throws ImoocMallException {
        //从session中获取用户信息；需要传入Constant中定义的用户常量名
        User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_DEV_V1_USER);

        //创建一个新的user，主要是在Service层中，进行有选择性的更新
        //updateByPrimaryKeySelective方法是有选择性的更新
        //如果将currentUser作为入参，会将所有字段更新一遍，重复操作，而且有被改错的风险.
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);//只更新签名，其他字段为null
        userService.updateInformation(user);
        return ApiRestResponse.success();

    }


    //前后台：退出登录
    //后台：管理员登录


}
