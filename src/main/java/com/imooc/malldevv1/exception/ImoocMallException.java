package com.imooc.malldevv1.exception;

/**
 * 统一异常
 * 2022-08-19 创建
 * 2022-08-19 编写
 */
//继承自Exception
//将Exception改为RuntimeException运行时异常
//public class ImoocMallException extends Exception{
public class ImoocMallException extends RuntimeException {
    //状态码
    private final Integer code;
    //异常信息
    //注意这里不能写成msg，否则在基类Exception中无法识别message。因为基类中定义的是message属性
    private final String message;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ImoocMallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ImoocMallException(ImoocMallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
