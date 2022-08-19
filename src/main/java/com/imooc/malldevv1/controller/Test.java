package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {
    @Autowired
    TestService testService;

    @GetMapping("/test")
    @ResponseBody
    public void test(){
        testService.test1();
    }

}
