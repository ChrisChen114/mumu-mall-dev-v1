package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


//Repository，语义注解，说明当前类用于业务持久层，通常描述对应Dao类
@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    //dev_v1 增加
    // 2022-08-19 新增；用户模块中注册新用户用到
    //在String userName前面，不用加@RequestParam注解
    //对于MyBatis而言，入参只有一个的话，可以不写@RequestParam注解
    //而如果有多个，则需要使用@RequestParam注解，注解里面与后面的属性名一致
    User selectByName( String userName);

    // 2022-08-20 新增；用户模块中前台用户登录用到
    //对于MyBatis而言，入参只有一个的话，可以不写@RequestParam注解
    //而如果有多个，则需要使用@RequestParam注解，注解里面与后面的属性名一致
    User selectLogin(@RequestParam("userName") String userName,@RequestParam("password") String password);




}