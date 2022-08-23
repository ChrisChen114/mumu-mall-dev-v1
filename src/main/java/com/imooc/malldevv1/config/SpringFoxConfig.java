package com.imooc.malldevv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 使用Swagger生成API文档
 * 2022-08-22 创建（拷贝）
 * 模板化的文档
 * pom里引入依赖和Application开启EnableSwagger2注解后，
 * 还需进行配置，因此新建config包，专门用于存储配置文件的。
 * 在此新建SpringFoxConfig类
 */
@Configuration   //增加注解，代表这是一个配置类
public class SpringFoxConfig {

    //访问http://localhost:8083/swagger-ui.html可以看到API文档
    @Bean
    public Docket api() {
        //固定格式
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

//    @Bean  //增加@Bean注解
//    public Docket api1(){
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
//
//    }

    //可以改title
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("慕慕生鲜")
                .description("")
                .termsOfServiceUrl("")
                .build();
    }

//    private ApiInfo apiInfo1(){
//        return new ApiInfoBuilder().title("生鲜").description("").termsOfServiceUrl("").build();
//    }
}