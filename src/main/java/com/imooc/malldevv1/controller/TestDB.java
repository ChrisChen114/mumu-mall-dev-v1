package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//@Controller，语义注解，说明当前类是MVC应用中的控制器类
@Controller
public class TestDB {
    @Autowired
    TestService testService;

    @GetMapping("/test")
    @ResponseBody
    public void testDB(){
        testService.test1();
    }

}
