package com.imooc.malldevv1.config;

/**
 *
 * pom里引入依赖和Application开启EnableSwagger2注解后，
 * 还需进行配置，因此新建config包，专门用于存储配置文件的。
 * 在此新建SpringFoxConfig类，
 * 还要在新建ImoocMallWebMvcConfig类，用于“配置地址映射”
 */

import com.imooc.malldevv1.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置地址映射
 * 2022-08-22 创建
 * 5-6视频：Swagger自动生成API文档
 * 来自视频6-5 资源映射开发
 *
 */
@Configuration  //增加注解，代表这是一个配置类
public class ImoocMallWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 视频9-2 上线前准备工作（带前端）
        //2022-08-31 添加
        //与admin相关的，带admin相关的文件，会路由到/static/admin/下面
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");

        //后台商品上传图片s3-3,
        //来自视频6-5 资源映射开发
        //addResourceHandler是源路径，将images下的图片都会转发到addResourceLocations下的地方
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);


        //把swagger-ui.html这个地址，对应到"classpath:/META-INF/resources/"和"classpath:/META-INF/resources/webjars/"目录下
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");//最后一个"/"不能少 （：
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");//最后一个"/"不能少   （：
    }
}
