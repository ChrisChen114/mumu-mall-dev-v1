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
 * <p>
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
    public List<CartVO> list(int userId) {
        //s2,根据userId，查询该用户下的购物车列表
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        //s3,计算totalPrice
        //敲 itli
//        for (int i = 0; i < cartVOS.size(); i++) {
//            CartVO cartVO =  cartVOS.get(i);
//
//        }
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getQuantity() * cartVO.getPrice());

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
        validProduct(productId, count);

        //s3,根据用户id和商品id，查询商品
        Cart cartOld = cartMapper.selectByUserIdAndProductId(userId, productId);

        //s4,判断这个商品在不在购物车
        if (cartOld == null) {
            //s4-1,这个商品之前不在购物车，需要新建一个记录
            Cart cart = new Cart();
            //setId不用设置，会自动生成
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            //cart.setCreateTime(new Date());//为啥不用设置
            //插入商品到购物车
            int countCart = cartMapper.insertSelective(cart);
            if (countCart == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
            }
        } else {
            //s4-2,这个商品已经在购物车，则原有的订单量加上新增的数量
            count = count + cartOld.getQuantity();//原有的订单量加上新增的数量
            Cart cartNew = new Cart();
            //BeanUtils.copyProperties(cartOld, cartNew);//用这个不是太好，更新的会多（虽然理论上部分字段值一样，但还是谨慎）
            cartNew.setQuantity(count);
            cartNew.setId(cartOld.getId());//更新到已有的id上，一定要写
            cartNew.setUserId(cartOld.getUserId());
            cartNew.setProductId(cartOld.getProductId());
            cartNew.setSelected(Constant.Cart.CHECKED);//可设置可不设置。不同的电商平台理解都不一样
            //cartNew.setCreateTime(new Date());
            //更新购物车
            int countCart = cartMapper.updateByPrimaryKeySelective(cartNew);
            if (countCart == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }

        //s5，调用list方法，返回的是“购物车列表”
        return this.list(userId);
    }

    //s2, 异常处理，对于商品id和数量的校验
    //count代表要买的数量
    private void validProduct(Integer productId, Integer count) {
        //s2-0,根据productId，查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        //s2-1,判断商品是否存在，商品是否上架
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
        }
        //s2-2,判断库存够不够
        if (count > product.getStock()) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
        }
    }


    //更新购物车某个商品的数量
    //2022-08-26 创建
    //2022-08-28 编写
    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        //s2，兜底的异常处理
        validProduct(productId, count);
        //s3,根据userId和productId，查询到该商品；
        //需新建查询selectByProductId
        //写selectByProductId方法，是因为忘记传入userId，而另写的查询sql方法。虽然功能看似没问题，但会有横向越权的问题，必须要加上userId进行查询
        //××××××××××××××××××××××××××××××××××××
        //Cart cart = cartMapper.selectByProductId(productId);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        //s4,判断这个商品在不在购物车
        if (cart == null) {
            //s4-1，这个商品之前不在购物车里，无法更新
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        } else {
            //s4-2，在购物车里，则更新商品数量
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());//更新到这个id
            cartNew.setQuantity(count);
            cartNew.setUserId(cart.getUserId());
            cartNew.setProductId(cart.getProductId());//也可以不设置
            cartNew.setSelected(Constant.Cart.CHECKED);//可设置可不设置。不同的电商平台理解都不一样
            //选择性更新购物车
            int countCart = cartMapper.updateByPrimaryKeySelective(cartNew);
            if (countCart == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }

        //s5，调用list方法，返回的是“购物车列表”
        return this.list(userId);
    }


    //删除购物车的某个商品
    //2022-08-26 创建
    //2022-08-28 编写
    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        //s3,根据userId和productId，查询到该商品
        //Cart cart = cartMapper.selectByProductId(productId);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);

        //s4,判断这个商品在不在购物车
        if (cart == null) {
            //s4-1，这个商品之前不在购物车里，无法删除
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        } else {
            //s4-2，商品已经在购物车里了，则可以删除
            int countCart = cartMapper.deleteByPrimaryKey(cart.getId());
            if (countCart == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
            }
        }

        //s5，调用list方法，返回的是“购物车列表”
        return this.list(userId);
    }


    //选中/不选中购物车的某个商品
    //2022-08-26 创建
    //2022-08-28 编写
    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) {
        //s3,根据userId和productId，查询到该商品；
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        //s4,判断这个商品在不在购物车
        if (cart == null) {
            //s4-1，这个商品之前不在购物车里，无法选择
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        } else {
            //s4-2，在购物车里，则可以选中/不选中
            //方案一：new一个cart，然后设置selected，最后调用updateByPrimaryKeySelective方法
            //目前不确定有没有潜在风险
//            Cart cartNew = new Cart();
//            cartNew.setId(cart.getId());//更新到这个id
//            cartNew.setQuantity(cart.getQuantity());
//            cartNew.setUserId(cart.getUserId());
//            cartNew.setProductId(cart.getProductId());//也可以不设置
//            cartNew.setSelected(selected);//可设置可不设置。不同的电商平台理解都不一样
//            //选择性更新购物车
//            int countCart = cartMapper.updateByPrimaryKeySelective(cartNew);
//            if (countCart == 0) {
//                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
//            }

            //方案二：在mapper.xml中进行更新，此种方法配合“全选/全不选购物车的某个商品”功能，效率更高.
            //s4-2，在购物车里，则可以选中/不选中
            int countCart = cartMapper.selectOrNot(userId, productId, selected);
            if (countCart == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }

        //s5，调用list方法，返回的是“购物车列表”
        return this.list(userId);

    }


    //全选/全不选购物车的某个商品
    //2022-08-26 创建
    //2022-08-28 编写
    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected) {
        //s2，调用list方法里面的selectList
        //方案一：调用service层里的selectOrNot方法，
//        List<CartVO> cartVOList = cartMapper.selectList(userId);
//        //itli
//        //s3，循环更新选中状态，调用选中/不选中购物车的某个商品的select方法
//        for (int i = 0; i < cartVOList.size(); i++) {
//            CartVO cartVO =  cartVOList.get(i);
//            selectOrNot(userId, cartVO.getProductId(), selected);
//            if (countCart == 0) {
//                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
//            }
//        }
        //方案二：调用mapper里的selectOrNot方法，在sql中更新
        //s2，改变选中状态
        //productId传入null，就会把userId下的所有商品进行选中/不选中
        int countCart = cartMapper.selectOrNot(userId, null, selected);
        //s3，调用list方法，返回的是“购物车列表”
        return this.list(userId);
    }

}
