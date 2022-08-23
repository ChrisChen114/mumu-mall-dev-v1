package com.imooc.malldevv1.config;

import com.imooc.malldevv1.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Admin过滤器的配置
 * 2022-08-23 创建
 * 来自视频5-8 统一校验管理员身份
 *
 */


@Configuration  //增加注解，代表这是一个配置类
public class AdminFilterConfig {

    //s1,给filter定义出来
    @Bean //Spring就能识别到了；@Bean作用在方法上，一般表明返回的对象直接被Spring管理起来
    public AdminFilter adminFilter(){
        //会new一个Admin过滤器，在配合拦截的URL
        return new AdminFilter();
    }

    //s2,把filter放到整个filter过滤器的链路上
    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        //对Bean的描述和包括各个参数的配置
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //设置上这个过滤器
        filterRegistrationBean.setFilter(adminFilter());
        //给过滤器要拦截的URL进行设置
        //要拦截与管理员操作有关的方法
        filterRegistrationBean.addUrlPatterns("/admin/category/*");//目录
        filterRegistrationBean.addUrlPatterns("/admin/product/*");//商品
        filterRegistrationBean.addUrlPatterns("/admin/order/*");//订单
        //给过滤器配置设置名称
        filterRegistrationBean.setName("adminFilterConf");//名字不能和类名一样
        return filterRegistrationBean;
    }
}
