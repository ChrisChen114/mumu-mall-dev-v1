package com.imooc.malldevv1.config;

/**
 *
 * pom里引入依赖和Application开启EnableSwagger2注解后，
 * 还需进行配置，因此新建config包，专门用于存储配置文件的。
 * 在此新建SpringFoxConfig类，
 * 还要在新建ImoocMallWebMvcConfig类，用于“配置地址映射”
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置地址映射
 * 2022-08-22 创建
 * 5-6视频：Swagger自动生成API文档
 */
@Configuration  //增加注解，代表这是一个配置类
public class ImoocMallWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //把swagger-ui.html这个地址，对应到"classpath:/META-INF/resources/"和"classpath:/META-INF/resources/webjars/"目录下
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");//最后一个"/"不能少 （：
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");//最后一个"/"不能少   （：
    }
}
