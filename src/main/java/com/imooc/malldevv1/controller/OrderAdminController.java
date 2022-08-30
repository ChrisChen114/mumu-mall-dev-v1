package com.imooc.malldevv1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：      后台订单OrderAdminController模块
 * 2022-08-29 创建
 * 后台管理：
 * 订单列表
 * 订单发货
 *
 * 前后台通用：订单完结
 */

@RestController      //@RestController = @Controller + @ResponseBody，两者相等
@RequestMapping("/admin")  //增加映射地址前缀
public class OrderAdminController {
}
