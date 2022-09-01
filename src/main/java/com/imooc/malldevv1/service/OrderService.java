package com.imooc.malldevv1.service;

import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.model.request.CreateOrderReq;
import com.imooc.malldevv1.model.vo.OrderVO;

/**
 * OrderService接口类
 * 2022-08-29 创建
 */
public interface OrderService {

    //前台创建订单
    //2022-08-29 创建
    String create(CreateOrderReq createOrderReq);


    //前台创建详情
    //2022-08-30 创建
    //返回值是OrderVO
    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);


    //前台取消订单
    //2022-08-31 创建
    void cancel(String orderNo);

    //前台取消订单
    //2022-08-31 创建
    String qrcode(String orderNo);

    //前台支付订单
    //2022-08-31 创建
    void pay(String orderNo);

    //后台订单列表
    //2022-08-31 创建
    //返回值是PageInfo
    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    //后台订单列表
    //2022-08-31 创建
    //返回值是PageInfo
    void deliver(String orderNo);

    //前后台通用：订单完结
    //2022-08-31 创建
    void finish(String orderNo);
}
