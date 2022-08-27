package com.imooc.malldevv1.controller;


import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.request.ProductListReq;
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
    //用户涉及到的操作有：比如点击某个商品目录、又比如输入关键字，再比如对列表进行排序等操作；
    //所以要根据用户操作进行功能实现
    //2022-08-24 创建
    //2022-08-25 开发
    //技术点：1）新建ProductListReq请求参数类；新建ProductListQuery 查询商品列表的Query类
    //2）搜索功能：入参判空（不使用@Valid判空，因为有的参数可传可不传）--->加%通配符--->like关键字（加%通配符和like关键字在SQL中拼接，实现查找功能）；
    //3）重要步骤：搜索处理、目录处理、排序处理、复杂查询语句（if、foreach等）
    //疑问：@RequestBody不用加吗? 值得注意的是，此处虽然用ProductListReq，但是请求参数不是从Body中传入，
    // 而是params传入，与request包中其他类从Body传入不一样。
    @ApiOperation("前台商品列表")   //增加这个Swagger注解，即可生成swagger API文档
    @GetMapping("/product/list")
    public ApiRestResponse listProduct(ProductListReq productListReq) {
        //note：@RequestBody不用加，并不是从postman的Body中入参，而是从Params入参，有同名的类型和name会绑定

        //S1，进入Service层中，执行商品列表操作
        PageInfo pageInfo = productService.listProductForCustomer(productListReq);

        //返回："data": {
        //        "total": 8,
        //        "list": [
        //            {
        //                "id": 2,...
        return ApiRestResponse.success(pageInfo);
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
