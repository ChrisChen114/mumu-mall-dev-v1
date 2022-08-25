package com.imooc.malldevv1.controller;


import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用于前台管理的商品 ProductCustomerController
 * 2022-08-24 创建
 * <p>
 * 商品模块：
 * 后台管理：
 * 增加商品
 * 上传图片
 * 更新商品
 * 删除商品
 * 批量上下架商品
 * 商品列表（后台）
 * 前台管理：***************
 * 商品列表
 * 商品详情
 */

@RestController
public class ProductCustomerController {
    //注入商品ProductService
    @Autowired
    ProductService productService;


    //
    //前台管理：
    //
    //前台商品列表
    //2022-08-24 创建
    //技术点：1）
    @ApiOperation("前台商品列表")   //增加这个Swagger注解，即可生成swagger API文档
    @GetMapping("/product/list")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        //前置自动处理：
        // @Valid注解，是对AddProductReq类中的属性进行校验
        //@RequestBody,这样body中的参数与AddProductReq进行绑定
        //检查有没有登录和校验是不是管理员，已通过过滤器包filter中的AdminFilter拦截并校验

        //S1，进入Service层中，执行增加商品操作
        productService.addProduct(addProductReq);

        //返回："data": null
        return ApiRestResponse.success();
    }


    //商品详情



}
