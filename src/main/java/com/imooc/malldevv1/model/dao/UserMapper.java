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


    //dev_v1
    //
    User selectByName( String userName);




}