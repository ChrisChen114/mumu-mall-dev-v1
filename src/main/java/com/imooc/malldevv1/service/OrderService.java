package com.imooc.malldevv1.service;

import com.imooc.malldevv1.model.request.CreateOrderReq;

/**
 * OrderService接口类
 * 2022-08-29 创建
 */
public interface OrderService {

    //前台创建订单
    //2022-08-29 创建
    String create(CreateOrderReq createOrderReq);


}
