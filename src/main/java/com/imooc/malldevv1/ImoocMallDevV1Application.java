package com.imooc.malldevv1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@MapperScan注解
//作用：指定要编程实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
//添加位置：是在Springboot启动类上面添加

@SpringBootApplication
@MapperScan(basePackages = "com.imooc.malldevv1.model.dao")     //告诉spring，dao的mapper对象都放在了哪里
@EnableSwagger2   //开启swagger能力 5-6视频里
@EnableCaching    //利用Redis缓存加速响应-S4：打开缓存功能
public class ImoocMallDevV1Application {

    public static void main(String[] args) {
        SpringApplication.run(ImoocMallDevV1Application.class, args);
    }

}
