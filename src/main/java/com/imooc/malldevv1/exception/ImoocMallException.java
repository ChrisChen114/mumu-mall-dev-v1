package com.imooc.malldevv1.exception;

//继承自Exception
public class ImoocMallException extends Exception{
    //状态码
    private final Integer code;
    //异常信息
    private final String msg;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ImoocMallException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ImoocMallException(ImoocMallExceptionEnum exceptionEnum) {
        this(exceptionEnum.getCode(),exceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
