package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于顾客的商品ProductCustomerController
 * 2022-08-23 创建
 *
 * 商品模块：
 *  后台管理：
 *      增加商品
 *      上传图片
 *      更新商品
 *      删除商品
 *      批量上下架商品
 *      商品列表（后台）
 *  前台管理：**************
 *      商品列表
 *      商品详情
 */

@RestController
public class ProductCustomerController {
    //注入商品ProductService
    @Autowired
    ProductService productService;



    //前台管理：
    //商品列表
    @ApiOperation("前台商品列表")
    @PostMapping("/product/list")
    public ApiRestResponse list(){

        return ApiRestResponse.success();
    }


    //商品详情
    @ApiOperation("前台商品详情")
    @PostMapping("/product/detail")
    public ApiRestResponse detail(){

        return ApiRestResponse.success();
    }


}
