package com.imooc.malldevv1.service.impl;

import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.dao.CartMapper;
import com.imooc.malldevv1.model.dao.ProductMapper;
import com.imooc.malldevv1.model.pojo.Cart;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.vo.CartVO;
import com.imooc.malldevv1.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 描述：      购物车CartService实现类模块
 * 2022-08-26 创建
 * 购物车列表
 * 添加商品到购物车
 * 更新购物车某个商品的数量
 * 删除购物车的某个商品
 * 选中/不选中购物车的某个商品
 * 全选/全不选购物车的某个商品
 *
 * CartServiceImpl要记得添加implements CartService
 */

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;


    //购物车列表
    //2022-08-26 创建
    @Override
    public List<CartVO> list(int userId){
        //s2,根据userId，查询该用户下的购物车列表
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        //s3,计算totalPrice
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO =  cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getQuantity()*cartVO.getPrice());
            //经过测试说明，他们的地址是同一个地址，所以设置cartVO的setTotalPrice，会改变cartVOS里的内容。
//            System.out.println(cartVO);
//            System.out.println(cartVOS.get(i));
//            com.imooc.malldevv1.model.vo.CartVO@411328f8
//            com.imooc.malldevv1.model.vo.CartVO@411328f8
        }
        return cartVOS;
    }


    //添加商品到购物车
    //为什么要返回一个List呢？因为添加完成后，购物车肯定会变化，要及时返回
    //2022-08-26 创建
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        //s2, 兜底的异常处理，对于id商品是否存在和上下架和库存数量的校验
        //本质目的是验证本次添加是不是合法的
        validProduct(productId,count);
        //s3,根据用户id和商品id，查询商品
        Cart cartOld = cartMapper.selectByUserIdAndProductId(userId,productId);
        //s4,判断这个商品在不在购物车
        if (cartOld == null){
            //s4-1,这个商品之前不在购物车，需要新建一个记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            //cart.setCreateTime(new Date());
            //插入商品到购物车
            int countCart = cartMapper.insertSelective(cart);
            if (countCart == 0){
                throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
            }
        }else{
            //s4-2,这个商品已经在购物车，原有的订单量加上新增的数量
            count = count+ cartOld.getQuantity();//原有的订单量加上新增的数量
            Cart cartNew = new Cart();
            //BeanUtils.copyProperties(cartOld, cartNew);//用这个不是太好，更新的会多（虽然理论上部分字段值一样，但还是谨慎）
            cartNew.setQuantity(count);
            cartNew.setUserId(cartOld.getUserId());
            cartNew.setId(cartOld.getId());//更新到已有的id上，一定要写
            cartNew.setProductId(cartOld.getProductId());
            cartNew.setSelected(Constant.Cart.CHECKED);
            //cartNew.setCreateTime(new Date());
            //更新购物车
            int countCart = cartMapper.updateByPrimaryKeySelective(cartNew);
            if (countCart == 0){
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }

        //s5，调用list方法，返回的是“购物车列表”
        return this.list(userId);
    }

    //s2, 异常处理，对于商品id和数量的校验
    private void validProduct(Integer productId, Integer count) {
        //s2-0,根据productId，查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        //s2-1,判断商品是否存在；商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        //s2-2,判断库存够不够
        if (count>product.getStock()){
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }


    //更新购物车某个商品的数量
    //2022-08-26 创建
    @Override
    public void update(Integer productId, Integer count){

    }


    //删除购物车的某个商品
    //2022-08-26 创建
    @Override
    public void delete(Integer productId){

    }


    //选中/不选中购物车的某个商品
    //2022-08-26 创建
    @Override
    public void select(Integer productId, Integer selected){

    }


    //全选/全不选购物车的某个商品
    //2022-08-26 创建
    @Override
    public void selectAll(Integer selected){

    }

}
