package com.imooc.malldevv1.common;

import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;

/**
 * common通用类：
 * API统一返回对象；通用返回对象
 *
 * 2022-08-19创建
 * 2022-08-19 更新
 *
 */



public class ApiRestResponse<T> {
    //定义哪些属性，请根据生鲜接口文档中的返回实例进行参考，如
    //{
    //"status": 10000,
    //"msg": "SUCCESS",
    //"data":null
    //}
    private Integer status;
    private String msg;
    //泛型对象，使用T表示；主要基于各个模块的返回值不同，使用泛型合适。需要在类后面增加"<T>"即可.
    private T data;

    //定义两个常量，用于默认情况下的消息返回
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    //三参构造函数
    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //两参构造函数
    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    //无参构造函数，用于返回默认情况的数据
    public ApiRestResponse() {
        //调用两参构造函数
        this(OK_CODE,OK_MSG);
    }

    //success方法
    public static <T> ApiRestResponse<T> success(){
        //调用无参构造函数
        return new ApiRestResponse<>();
    }

    //重载success方法
    //考虑到生鲜接口文档中，有的方法中data是带数据的，需要一个泛型T result，将data返回给前端
    public static <T> ApiRestResponse<T> success(T result){
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    //失败的方法
    public static <T> ApiRestResponse<T> error(Integer code, String msg){
        return new ApiRestResponse<>(code,msg);
        //起因：失败的常见错误，不想每次都写，需要有一个类定义各种常见的错误，枚举是一个不错的方式.
    }

    //重载失败的方法
    public static <T> ApiRestResponse<T> error(ImoocMallExceptionEnum ex){
        return new ApiRestResponse<>(ex.getCode(),ex.getMsg());
        //起因：失败的常见错误，不想每次都写，需要有一个类定义各种常见的错误，枚举是一个不错的方式.
    }

    //有的时候需要打印出来，生成toString方法
    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
