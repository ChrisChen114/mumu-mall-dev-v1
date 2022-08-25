package com.imooc.malldevv1.controller;


import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.model.vo.ProductVO;
import com.imooc.malldevv1.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用于前台管理的商品 ProductCustomerController
 * Controller层区分前台和后台，但Service层不做区分，因为Service层的背后逻辑很多是一致的
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
    public ApiRestResponse listProduct(@Valid @RequestBody AddProductReq addProductReq) {
        //前置自动处理：
        // @Valid注解，是对AddProductReq类中的属性进行校验
        //@RequestBody,这样body中的参数与AddProductReq进行绑定
        //检查有没有登录和校验是不是管理员，已通过过滤器包filter中的AdminFilter拦截并校验

        //S1，进入Service层中，执行增加商品操作
        productService.listProductForCustomer();

        //返回："data": null
        return ApiRestResponse.success();
    }


    //商品详情
    //2022-08-25 创建
    //技术点：1）
    @ApiOperation("前台商品详情")   //增加这个Swagger注解，即可生成swagger API文档
    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam("id") Integer id) {
        //s1，传入id，返回一个pojo类型的Product
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }


}
