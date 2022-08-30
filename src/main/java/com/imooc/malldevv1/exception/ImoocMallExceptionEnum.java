package com.imooc.malldevv1.exception;

/**
 * 异常枚举
 * 2022-08-19 创建
 * 2022-08-19 编写
 *
 * 起因：在ApiRestResponse类中，失败的常见错误，不想每次都写，
 *      需要有一个类定义各种常见的错误，枚举是一个不错的方式.
 *
 */
public enum ImoocMallExceptionEnum {
    //通过不同的状态码，前端同样可以知晓异常错误类型
    //10000打头的表示业务异常
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码不能少于8位"),
    NAME_EXISTED(10004,"不允许重名"),
    INSERT_FAILED(10005,"插入失败，请重试"),
    WRONG_PASSWORD(10006,"密码错误，请重试"),
    NEED_LOGIN(10007,"用户未登录"),
    UPDATE_FAILED(10008,"更新失败"),
    NEED_ADMIN(10009,"无管理员权限"),
    PARA_NOT_NULL(10010,"名字不能为空"),
    CREATED_FAILED(10011,"新增失败"),
    REQUEST_PARAM_ERROR(10012,"参数错误"),
    DELETE_FAILED(10013,"删除失败"),
    MAKE_DIR_FAILED(10014,"文件夹创建失败"),
    UPLOAD_FAILED(10015,"图片上传失败"),
    NOT_SALE(10016,"商品状态不可售"),
    NOT_ENOUGH(10017,"商品库存不足"),
    CART_EMPTY(10018,"购物车已勾选的商品为空"),
    NO_ENUM(10019,"未找到对应的枚举"),


    //20000打头的，表示系统类型的异常
    SYSTEM_ERROR(20000,"系统异常");

    //异常码
    Integer code;
    //异常信息
    String msg;

    ImoocMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }




}
