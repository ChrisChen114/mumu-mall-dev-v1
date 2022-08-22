package com.imooc.malldevv1.exception;

import com.imooc.malldevv1.common.ApiRestResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AliasFor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * 2022-08-19 创建
 * 2022-08-19 编写
 *
 * @ControllerAdvice 是什么意思？作用原理是？
 * 使用@ControllerAdvice来声明一些全局性的东西，最常见的是结合@ExceptionHandler注解用于全局异常的处理。
 * 这里@RestControllerAdvice等同于@ControllerAdvice + @ResponseBody
 * <p>
 * 为什么要新建GlobalExceptionHandler
 * 为什么要对异常进行统一处理？
 * 是对前端结构统一以及安全的考虑。
 * 效果：抛出异常，直接转化为Json的APIResponse
 */


/**
 * GlobalExceptionHandler：处理统一异常的handler

 */
@ControllerAdvice   //这个注解的作用是用于拦截全局异常的
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * @ExceptionHandler注解标注的方法：用于捕获Controller中抛出的不同类型的异常，从而达到异常全局处理的目的；
     * @ExceptionHandler(Exception.class)，处理的是Exception.class类型的异常
     *  * 视频4-6 统一异常处理
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
     *  * 视频4-6 统一异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ImoocMallException.class)
    @ResponseBody
    public Object handleImoocMallException(ImoocMallException e) {
        log.error("ImoocMall Exception: ", e);
        return ApiRestResponse.error(e.getCode(), e.getMessage());
    }


    /**
     * 设置全局入参异常捕获，主要解决校验参数异常
     * 2022-08-21 增加
     * MethodArgumentNotValidException是系统自带的异常类,
     *     定义：Exception to be thrown when validation on an argument annotated with {@code @Valid} fails.
     *
     * @param e
     * @return 如下：
     * {
     *     "status": 10012,
     *     "msg": "[size must be between 2 and 5]",或"msg": "[size must be between 2 and 5, must be less than or equal to 3]",或其他...
     *     "data": null
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: ", e);
        //getBindingResult():   Return the results of the failed validation.
        return handleBindingResult(e.getBindingResult());
    }

    /**
     * 主要解决校验参数异常
     * 视频5-5 @Valid注解优雅校验入参
     * 2022-08-21 增加
     * @BindingResult result
     */
    private ApiRestResponse handleBindingResult(BindingResult result) {
        //把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            /**
             * getAllErrors():
             * Get all errors, both global and field ones.
             * @return a list of {@link ObjectError} instances
             */
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }

}
