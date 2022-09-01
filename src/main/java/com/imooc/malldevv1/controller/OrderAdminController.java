package com.imooc.malldevv1.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述：      后台订单管理OrderAdminController模块
 * 2022-08-29 创建
 * 后台管理：
 * 订单列表
 * 订单发货
 *
 * 前后台通用：订单完结
 */

@RestController      //@RestController = @Controller + @ResponseBody，两者相等
//@RequestMapping("/admin")  //增加映射地址前缀
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    //管理员订单列表
    //视频8-10 后台订单列表
    //功能上，与listForCustomer基本类似
    //技术点：1）利用PageHelper和PageInfo实现；2)admin会在AdminFilter中进行拦截，此处无需多写代码；
    //2022-08-31 创建
    @GetMapping("/admin/order/list")
    @ApiOperation("管理员订单列表")
    public ApiRestResponse listForAdmin(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize")  Integer pageSize){
        //s0，传入pageNum和pageSize，进入Service层
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);

        //返回pageInfo
        return ApiRestResponse.success(pageInfo);
    }

    //管理员订单发货
    //订单状态流程：0用户已取消；10未付款；20已付款；30已发货；40订单完结
    //视频8-12 后台管理订单接口开发
    //技术点：1）
    //2022-08-31 创建
    @PostMapping("/admin/order/delivered")
    @ApiOperation("管理员订单发货")
    public ApiRestResponse delivered(@RequestParam("orderNo") String orderNo){
        //s0，传入pageNum和pageSize，进入Service层
        orderService.deliver(orderNo);

        //返回pageInfo
        return ApiRestResponse.success();
    }

    //前后台通用：订单发货
    //视频8-12 管理订单接口开发
    //技术点：1）
    //2022-08-31 创建
    @PostMapping("/order/finish")
    @ApiOperation("管理员订单列表")
    public ApiRestResponse finish(@RequestParam("orderNo") String orderNo){
        //s0，传入pageNum和pageSize，进入Service层
        orderService.finish(orderNo);

        //返回pageInfo
        return ApiRestResponse.success();
    }

}
