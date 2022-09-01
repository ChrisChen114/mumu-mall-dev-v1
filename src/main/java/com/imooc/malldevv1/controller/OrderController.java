package com.imooc.malldevv1.controller;

import com.github.pagehelper.PageInfo;
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
//@RequestMapping("/order")  //增加映射地址前缀
public class OrderController {
    @Autowired
    OrderService orderService;

    //前台创建订单
    //视频8-2 创建订单接口：整个逻辑要点包括，
    //入参；从购物车中查找已经勾选的商品；判断购物车已勾选的商品是否为空；判断库存，保证不超卖，扣库存；数据库事务；删除购物车中对应的商品；生成订单；订单号生成规则；循环保存每个商品到order_item表；
    //技术点：1）需要用到order表和order_item表才能完整处理订单模块所有内容；2）创建订单完整逻辑关系；
    //2022-08-29 创建
    @PostMapping("/order/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@Valid @RequestBody CreateOrderReq createOrderReq){
        //s0，新建CreateOrderReq请求参数类，含receiverName，receiverMobile，和receiverAddress，以及带默认参数的postage=0和paymentType=1

        //s1，传入createOrderReq，进入Service层
        String orderNo = orderService.create(createOrderReq);

        //返回
        return ApiRestResponse.success(orderNo);
    }

    //前台订单详情
    //视频8-6 订单详情
    //技术点：1）返回的数据中，需要经过拼装才能使用；2）基于1，需要在vo包中新建OrderVO和OrderItemVO；3）为了安全起见，不允许暴露主键，所以oderMapper.selectByPrimaryKey方法不能用.
    //2022-08-30 创建
    @GetMapping("/order/detail")
    @ApiOperation("前台创建订单")
    public ApiRestResponse detail( @RequestParam("orderNo") String orderNo){
        //s0，传入orderNo，进入Service层
        OrderVO orderVO = orderService.detail(orderNo);

        //返回
        return ApiRestResponse.success(orderVO);
    }


    //前台订单列表
    //视频8-7 订单列表
    //技术点：1）利用PageHelper和PageInfo实现；2）PageInfo创建的时候，要传入查询出来的OrderList，而不是OrderVOList；3）调用getOrderVO方法
    //疑问点：PageInfo在构造的时候，一定是查出来的内容，即orderList的内容。不可以是orderVOList，但是传入orderVOList后测试结果看着列表显示也正常，如何解释？
    //2022-08-30 创建
    @GetMapping("/order/list")
    @ApiOperation("前台订单列表")
    public ApiRestResponse list( @RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize")  Integer pageSize){
        //s0，传入pageNum和pageSize，进入Service层
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);

        //返回pageInfo
        return ApiRestResponse.success(pageInfo);
    }

    //前台取消订单
    //视频8-8 取消订单
    //技术点：1）验证用户身份；2）没有付款的时候（前提条件），可以取消
    //2022-08-31 创建
    @PostMapping("/order/cancel")
    @ApiOperation("前台取消订单")
    public ApiRestResponse cancel( @RequestParam("orderNo") String orderNo){
        //s0，传入orderNo，进入Service层
        orderService.cancel( orderNo);

        return ApiRestResponse.success();
    }

    //前台生成支付二维码
    //视频8-9 二维码接口开发
    //技术点：1）获取到请求属性(ServletRequestAttributes) RequestContextHolder.getRequestAttributes()，拿到http请求HttpServletRequest request = requestAttributes.getRequest()；2）使用QRCodeGenerator组件；3)在ImoocMallWebMvcConfig类，用于“配置地址映射”,(之前已在图片上传模块设置)
    //2022-08-31 创建
    //需复习
    @GetMapping("/order/qrcode")
    @ApiOperation("前台生成支付二维码")
    public ApiRestResponse qrcode( @RequestParam("orderNo") String orderNo){
        //s0，传入orderNo，进入Service层
        String pngAddress = orderService.qrcode(orderNo);
        //返回图片地址
        return ApiRestResponse.success(pngAddress);
    }


    //前台支付订单
    //视频8-11 支付订单接口开发
    //技术点：1）
    //2022-08-31 创建
    @GetMapping("/pay")
    @ApiOperation("前台支付订单")
    public ApiRestResponse pay( @RequestParam("orderNo") String orderNo){
        //s0，传入orderNo，进入Service层
        orderService.pay( orderNo);

        return ApiRestResponse.success();
    }

}
