package com.imooc.malldevv1.service;


import com.imooc.malldevv1.model.pojo.Cart;
import com.imooc.malldevv1.model.vo.CartVO;

import java.util.List;

/**
 * 描述：      购物车CartService模块
 * 2022-08-26 创建
 * 购物车列表
 * 添加商品到购物车
 * 更新购物车某个商品的数量
 * 删除购物车的某个商品
 * 选中/不选中购物车的某个商品
 * 全选/全不选购物车的某个商品
 *
 */

public interface CartService {


    //购物车列表
    //2022-08-26 创建
    List<CartVO> list(int userId);


    //添加商品到购物车
    //2022-08-26 创建
    List<CartVO> add(Integer userId, Integer productId, Integer count);

    //更新购物车某个商品的数量
    //2022-08-26 创建
    void update(Integer productId, Integer count);

    //删除购物车的某个商品
    //2022-08-26 创建
    void delete(Integer productId);

    //选中/不选中购物车的某个商品
    //2022-08-26 创建
    void select(Integer productId, Integer selected);

    //全选/全不选购物车的某个商品
    //2022-08-26 创建
    void selectAll(Integer selected);
}
