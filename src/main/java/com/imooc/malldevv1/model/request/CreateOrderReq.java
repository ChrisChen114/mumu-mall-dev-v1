package com.imooc.malldevv1.model.request;

import javax.validation.constraints.NotNull;

/**
 * 创建订单的一个请求类
 * 接收请求参数的类，用于在创建订单的Order类中
 * 来自视频8-2
 * 步骤s0
 * 2022-08-29 增加
 *
 * 另外：关于@Valid注解
 * 注解                      说明
 * @Valid                   需要验证
 * @NotNull                 非空
 * @Max(value)              最大值
 * @Size(max=5,min=2)       字符串长度范围限制
 */

public class CreateOrderReq {

    @NotNull(message = "receiverName不能为null")
    private String receiverName;

    @NotNull(message = "receiverMobile不能为null")
    private String receiverMobile;

    @NotNull(message = "receiverAddress不能为null")
    private String receiverAddress;

    private Integer postage = 0;//无邮费，包邮模式

    private Integer paymentType = 1;//代表在线支付




    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
}
