package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.model.request.CreateOrderReq;
import com.imooc.malldevv1.model.vo.OrderVO;
import com.imooc.malldevv1.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 描述：      前台订单OrderController控制器
 * 2022-08-29 创建
 * 前台显示：
 * 创建订单
 * 订单详情
 * 订单列表
 * 取消订单
 * 生成支付二维码
 * 支付订单
 *
 * 前后台通用：订单完结
 */

@RestController      //@RestController = @Controller + @ResponseBody，两者相等
@RequestMapping("/order")  //增加映射地址前缀
public class OrderController {
    @Autowired
    OrderService orderService;

    //前台创建订单
    //视频8-2 创建订单接口：整个逻辑要点包括，
    //入参；从购物车中查找已经勾选的商品；判断购物车已勾选的商品是否为空；判断库存，保证不超卖，扣库存；数据库事务；删除购物车中对应的商品；生成订单；订单号生成规则；循环保存每个商品到order_item表；
    //技术点：1）需要用到order表和order_item表才能完整处理订单模块所有内容；2）创建订单完整逻辑关系；
    //2022-08-29 创建
    @PostMapping("/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@Valid @RequestBody CreateOrderReq createOrderReq){
        //s0，新建CreateOrderReq请求参数类，含receiverName，receiverMobile，和receiverAddress，以及带默认参数的postage=0和paymentType=1

        //s1，传入createOrderReq，进入Service层
        String orderNo = orderService.create(createOrderReq);

        //返回
        return ApiRestResponse.success(orderNo);
    }

    //前台创建订单
    //视频8-2 创建订单接口：整个逻辑要点包括，
    //入参；从购物车中查找已经勾选的商品；判断购物车已勾选的商品是否为空；判断库存，保证不超卖，扣库存；数据库事务；删除购物车中对应的商品；生成订单；订单号生成规则；循环保存每个商品到order_item表；
    //技术点：1）需要用到order表和order_item表才能完整处理订单模块所有内容；2）创建订单完整逻辑关系；
    //2022-08-29 创建
    @GetMapping("/detail")
    @ApiOperation("创建订单")
    public ApiRestResponse detail( @RequestParam("orderNo") String orderNo){

        //s0，传入orderNo，进入Service层
        OrderVO orderVO = orderService.detail(orderNo);

        //返回
        return ApiRestResponse.success(orderVO);
    }


}
