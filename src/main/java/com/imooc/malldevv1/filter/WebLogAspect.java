package com.imooc.malldevv1.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * AOP统一处理Web请求日志
 * 2022-08-19
 */

/**
 *
 * @Aspect注解 SPring AOP 面向切面编程 Aspect Oriented Programming；
 * AOP的做法是将通用、与业务无关的功能抽象封装为切面类；
 * 切面可配置在目标方法的执行前、后运行，真正做到即插即用。
 * @Aspect: 切面，具体的可插拔组件功能类，通常一个切面只实现一个通用功能
 *
 */

/**
 * @Component注解 组件注解，通用注解，被该注解描述的类将被IoC容器管理并实例化
 */
@Aspect  //引入AOP注解
@Component
public class WebLogAspect {
    public final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * @Pointcut,说明切面作用在com.imooc.malldevv1.controller包下的所有类的所有方法上
     * @Pointcut，切入点，使用execution表达式说明切面要作用在系统的哪些类上
     */
    @Pointcut("execution(public * com.imooc.malldevv1.controller.*.*(..))")
    public void webLog() {

    }

    /**
     * 在webLog()方法执行前，对请求内容进行记录
     * JoinPoint，连接点，切面运行过程中是包含了目标类/方法元数据的对象
     *
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //收到请求，记录（打印）请求内容

        //获取request请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //URL : http://127.0.0.1:8083/test1
        log.info("URL : " + request.getRequestURL());
        //HTTP_METHOD : GET
        log.info("HTTP_METHOD : " + request.getMethod());
        //IP : 127.0.0.1
        log.info("IP : " + request.getRemoteAddr());
        //CLASS_METHOD : com.imooc.malldevv1.controller.UserController.personalPage
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //ARGS : []             将数组转为String
        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 在webLog()方法执行后，
     *
     * @param res
     */
    @AfterReturning(returning = "res", pointcut = "webLog()")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        //处理完请求，返回内容
        //RESPONSE : {"id":1,"username":"1","password":"1","personalizedSignature":"3","role":1,"createTime":1576435053000,"updateTime":1581244872000}
        //需要将res转成JSON格式的对象，使用ObjectMapper，它是fastjson提供的，把对象转为json的工具
        log.info("RESPONSE : " + new ObjectMapper().writeValueAsString(res));
    }
}
