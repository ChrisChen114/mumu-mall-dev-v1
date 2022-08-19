package com.imooc.malldevv1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.imooc.malldevv1.model.dao")  //告诉spring，dao的mapper对象都放在了哪里
public class ImoocMallDevV1Application {

    public static void main(String[] args) {
        SpringApplication.run(ImoocMallDevV1Application.class, args);
    }

}
