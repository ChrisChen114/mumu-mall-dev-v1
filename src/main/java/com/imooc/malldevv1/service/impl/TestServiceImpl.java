package com.imooc.malldevv1.service.impl;

import com.imooc.malldevv1.model.dao.UserMapper;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    UserMapper userMapper;

    @Override
    public void test1(){
        User user = userMapper.selectByPrimaryKey(1);
        System.out.println(user);
    }



}
