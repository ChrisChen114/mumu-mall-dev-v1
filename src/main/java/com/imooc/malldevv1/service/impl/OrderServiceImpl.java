package com.imooc.malldevv1.service.impl;

import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.filter.UserFilter;
import com.imooc.malldevv1.model.dao.CartMapper;
import com.imooc.malldevv1.model.dao.OrderItemMapper;
import com.imooc.malldevv1.model.dao.OrderMapper;
import com.imooc.malldevv1.model.dao.ProductMapper;
import com.imooc.malldevv1.model.pojo.Cart;
import com.imooc.malldevv1.model.pojo.Order;
import com.imooc.malldevv1.model.pojo.OrderItem;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.request.CreateOrderReq;
import com.imooc.malldevv1.model.vo.CartVO;
import com.imooc.malldevv1.service.CartService;
import com.imooc.malldevv1.service.OrderService;
import com.imooc.malldevv1.util.OrderCodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：      订单OrderService实现类模块
 * 2022-08-29 创建
 */

@Service
@Transactional(rollbackFor = Exception.class)  //遇到任何异常都会回滚
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartService cartService;

    @Autowired
    ProductMapper productMapper;

    //前台创建订单
    //2022-08-29 创建
    //返回值是orderNo
    @Override
    public String create(CreateOrderReq createOrderReq) {
        //s2，在类外面开启数据库事务：@Transactional(rollbackFor = Exception.class)  //遇到任何异常都会回滚

        //s3，拿到用户ID. 从UserFilter拿到
        Integer userId = UserFilter.currentUser.getId();

        //s4,从购物车查找已经勾选的商品
        //List<Order> orderList = orderMapper.selectCheckedItem(userId);
        //int countChecked = 0;

        //购物车列表方法
        List<CartVO> cartVOList = cartService.list(userId);
        List<CartVO> cartVOListTemp = new ArrayList<>();//临时存储用的
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;//更新cartVOList，只包含选中的

        //s5,如果购物车已勾选的商品为空，则报错
        //自己写成cartVOListTemp.size() == 0，可能不太好
        //视频使用的是CollectionUtils的isEmpty
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }

        //s6,判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);

        //s7，把购物车对象转为订单Item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);

        //s8，扣库存
        deductStock(cartVOList);

        //s9，把购物车中的已勾选商品删除
        cleanCart(cartVOList);

        //***
        //s10，生成订单，
        Order order = new Order();
        //s10-1，生成订单号，有独立的规则，（根据id进行加密+加随机数组成固定长度编码）
        //具体查看util包下的OrderCodeFactory类
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        //s10-2，计算所有已勾选的商品总价，调用totalPrice
        order.setTotalPrice(totalPrice(orderItemList));//所有已勾选的商品总价，而不是每个商品的总价
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        //s10-3，状态
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());//定义一个枚举
        order.setPostage(0);
        order.setPaymentType(1);

        //s11，插入到order表
        orderMapper.insertSelective(order);

        //s12，循环保存每个商品到order_item表
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            //要把setOrderNo赋值
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }

        //s13，把结果返回
        return orderNo;
    }

    //s6，判断商品是否存在、上下架状态、库存
    //2022-08-29 创建
    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            //根据productId，查询商品
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            //判断商品是否存在，商品是否上架
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_SALE);
            }
            //判断库存够不够
            if (cartVO.getQuantity() > product.getStock()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
            }
        }

    }

    //s7，把购物车对象转为订单Item对象,cartVOList To orderItemList
    //2022-08-29 创建
    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            //Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());//无需用到product
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    //s8，扣库存
    //2022-08-29 创建
    private void deductStock(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            Product productOld = productMapper.selectByPrimaryKey(cartVO.getProductId());
            Product productNew = new Product();
            productNew.setId(productOld.getId());
            //得到新的库存，还需判断库存不能小于0
            int stock = productOld.getStock() - cartVO.getQuantity();
            if (stock < 0) {
                //因为加入到购物车时有库存，但是下单时候已经售卖完了
                throw new ImoocMallException(ImoocMallExceptionEnum.NOT_ENOUGH);
            }
            productNew.setStock(stock);
            int count = productMapper.updateByPrimaryKeySelective(productNew);
            if (count == 0) {
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }
    }

    //s9，把购物车中的已勾选商品删除
    //2022-08-29 创建
    private void cleanCart(List<CartVO> cartVOList) {
        //cartVOList其实只包含已经勾选的，前面已经做过筛选，s4里
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            //if (cartVO.getSelected().equals(Constant.Cart.CHECKED))
            {
                int count = cartMapper.deleteByPrimaryKey(cartVO.getId());
                if (count == 0) {
                    throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
                }
            }
        }
    }

    //s10，生成订单，
    //2022-08-29 创建
    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem =  orderItemList.get(i);
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


}