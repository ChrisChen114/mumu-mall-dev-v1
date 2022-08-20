package com.imooc.malldevv1.exception;

import com.imooc.malldevv1.common.ApiRestResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 2022-08-19 创建
 * 2022-08-19 编写
 */

/**
 * 为什么要新建GlobalExceptionHandler
 * <p>
 * 为什么要对异常进行统一处理？
 * 是对前端结构统一以及安全的考虑。
 * <p>
 * 效果：抛出异常，直接转化为Json的APIResponse
 *
 * @ControllerAdvice 是什么意思？作用原理是？
 * 使用@ControllerAdvice来声明一些全局性的东西，最常见的是结合@ExceptionHandler注解用于全局异常的处理。
 * 这里@RestControllerAdvice等同于@ControllerAdvice + @ResponseBody
 */

/**
 * @ControllerAdvice 是什么意思？作用原理是？
 * 使用@ControllerAdvice来声明一些全局性的东西，最常见的是结合@ExceptionHandler注解用于全局异常的处理。
 *
 * 这里@RestControllerAdvice等同于@ControllerAdvice + @ResponseBody
 */


/**
 * GlobalExceptionHandler：处理统一异常的handler
 */
@ControllerAdvice   //这个注解的作用是用于拦截异常的
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * @ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的；
     * @ExceptionHandler(Exception.class)，处理的是Exception.class类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception: ", e);
        //返回的是Json格式的ApiRestResponse，需要增加@ResponseBody注解.
        return ApiRestResponse.error(ImoocMallExceptionEnum.SYSTEM_ERROR);
    }


    /**
     * @ExceptionHandler(ImoocMallException.class),处理的是ImoocMallException.class类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(ImoocMallException.class)
    @ResponseBody
    public Object handleImoocMallException(ImoocMallException e) {
        log.error("ImoocMall Exception: ", e);
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }



}
