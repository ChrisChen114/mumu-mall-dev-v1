package com.imooc.malldevv1.service;

import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.model.request.ProductListReq;
import com.imooc.malldevv1.model.request.UpdateProductReq;

/**
 * 商品ProductService
 * 2022-08-23 创建
 *
 */

public interface ProductService {

    //后台增加商品
    //2022-08-23 创建
    void addProduct(AddProductReq addProductReq);


    //更新商品
    //2022-08-24 创建
    void     updateProduct(UpdateProductReq updateProductReq);

    //删除商品
    //2022-08-23 创建
    void deleteProduct(Integer id);

    //删除商品
    //2022-08-24 创建
    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    //后台商品列表
    //2022-08-24 编写
    PageInfo listProductForAdmin(Integer pageNum, Integer pageSize);

    //前台相关模块
    //前台商品列表
    //2022-08-25 创建
    PageInfo listProductForCustomer(ProductListReq productListReq);

    //前台商品详情
    //2022-08-25 创建
    Product detail(Integer id);
}
