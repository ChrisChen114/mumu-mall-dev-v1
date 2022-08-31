package com.imooc.malldevv1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.filter.UserFilter;
import com.imooc.malldevv1.model.dao.CartMapper;
import com.imooc.malldevv1.model.dao.OrderItemMapper;
import com.imooc.malldevv1.model.dao.OrderMapper;
import com.imooc.malldevv1.model.dao.ProductMapper;
import com.imooc.malldevv1.model.pojo.Order;
import com.imooc.malldevv1.model.pojo.OrderItem;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.request.CreateOrderReq;
import com.imooc.malldevv1.model.vo.CartVO;
import com.imooc.malldevv1.model.vo.OrderItemVO;
import com.imooc.malldevv1.model.vo.OrderVO;
import com.imooc.malldevv1.service.CartService;
import com.imooc.malldevv1.service.OrderService;
import com.imooc.malldevv1.util.OrderCodeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：      订单OrderService实现类模块
 * 2022-08-29 创建
 */

@Service

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
    @Transactional(rollbackFor = Exception.class)  //遇到任何异常都会回滚
    @Override
    public String create(CreateOrderReq createOrderReq) {
        //s2，在create方法外面开启数据库事务：@Transactional(rollbackFor = Exception.class)  //遇到任何异常都会回滚

        //s3，拿到用户ID. 从UserFilter拿到
        Integer userId = UserFilter.currentUser.getId();

        //s4,从购物车查找已经勾选的商品
        //List<Order> orderList = orderMapper.selectCheckedItem(userId);
        //int countChecked = 0;

        //s4-1，调用购物车列表方法
        List<CartVO> cartVOList = cartService.list(userId);
        //s4-2，将已经勾选的，临时存储到cartVOListTemp中
        List<CartVO> cartVOListTemp = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
                cartVOListTemp.add(cartVO);
            }
        }
        //s4-3，更新cartVOList，只包含选中的
        cartVOList = cartVOListTemp;

        //s5,如果购物车已勾选的商品为空，则报错
        //自己写成cartVOListTemp.size() == 0，可能不太好
        //视频使用的是CollectionUtils的isEmpty
        if (CollectionUtils.isEmpty(cartVOList)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CART_EMPTY);
        }

        //s6,判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);

        //s7，把购物车对象转为订单Order_Item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);

        //s8，扣库存
        deductStock(cartVOList);

        //s9，把购物车中的已勾选商品删除
        cleanCart(cartVOList);

        //***
        //s10，生成订单，
        //这块也能抽出去形成方法，但是要传递的参数很多
        Order order = new Order();
        //s10-1，生成订单号，有独立的规则，（根据id进行加密+加随机数组成固定长度编码）
        //具体查看util包下的OrderCodeFactory类；代码是视频里提供的
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        //s10-2，计算所有已勾选的商品总价，调用totalPrice
        order.setTotalPrice(totalPrice(orderItemList));//所有已勾选的商品总价，而不是每个商品的总价
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        //s10-3，状态;在Constant包中定义枚举类OrderStatusEnum
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

    //前台创建详情
    //2022-08-30 创建
    //返回值是OrderVO
    @Override
    public OrderVO detail(String orderNo){
        //为了安全起见，不允许暴露主键，所以oderMapper.selectByPrimaryKey方法不能用.

        //s1-s2,在vo包中新建OrderVO和OrderItemVO

        //s3,根据订单号orderNo，在orderMapper中查询订单
        //s3中，需要在orderMapper中创建selectByOrderNo
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        //s4,订单存在，则判断所属；即判断这个订单是不是属于当前这个用户的
        //s4-1，拿到用户ID. 从UserFilter拿到
        Integer userId = UserFilter.currentUser.getId();
        //s4-2，order里的userId与session中的userId进行比较
        if (!order.getUserId().equals(userId)){
            //不相等，抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }
        //s5，获取订单详情信息，经组装后返回给前端
        OrderVO orderVO = getOrderVO(order);

        //s6，返回
        return orderVO;
    }

    //s5，获取订单详情信息，经组装后返回给前端
    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        //s5-1，将order的一部分属性拷贝到orderVO
        BeanUtils.copyProperties(order,orderVO);
        //s5-2到5-4，获取订单对应的orderItemVOList
        //s5-2，在orderItemMapper中根据订单号orderNo，查询订单商品
        //s5-2中，需要在orderItemMapper中创建selectByOrderNo
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        //s5-3,判断orderItemList是不是为空；视频里面未判断
        //!用错....
//        if(CollectionUtils.isEmpty(orderItemList)){
//            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
//        }

        //s5-4，循环拷贝orderItemList到orderItemVOList中
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItemVO orderItemVO = new OrderItemVO();
            OrderItem orderItem = orderItemList.get(i);
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOList.add(orderItemVO);
        }

        //s5-5，设置orderVO中OrderItemVOList属性
        orderVO.setOrderItemVOList(orderItemVOList);

        //s5-6,获取状态码对应的中文，比如购物车、下单、付款、出库、交易成功
        //orderVO.setOrderStatusName(Constant.OrderStatusEnum.NOT_PAID.getValue());//其实是有问题的，不同的订单阶段，状态是不一样的
        //视频8-6中的写法
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    //前台订单列表
    //2022-08-30 创建
    //返回值是PageInfo
    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize){
        //s1,利用PageHelper创建分页
        PageHelper.startPage(pageNum, pageSize);
        //s2,从UserFilter中拿到userId
        Integer userId = UserFilter.currentUser.getId();
        //s3,到orderMapper中查询所有属于userId的订单order
        List<Order> orderList = orderMapper.selectForCustomerByUserId(userId);
        //s4,要不要判空？；视频里没提
        //s5,orderList转为orderVOList
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);

        //s6，PageInfo里面传入的是orderList，然后pageInfo的setList接收orderVOList
        //自己写的
        //PageInfo<OrderVO> orderVOPageInfo = new PageInfo<>(orderVOList);
        //PageInfo在构造的时候，传入的一定是查出来的内容，即orderList的内容。不可以是orderVOList，但是看着列表显示也正常，如何解释？
        PageInfo pageInfo = new PageInfo<>(orderList);
        //s6-1，pageInfo有一个setList方法，可以把orderVOList赋值
        //PageInfo<Order> pageInfo = new PageInfo<>(orderList);
        //pageInfo.setList(orderVOList);//这样写会报错，把PageInfo<Order>的泛型去掉即可
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    //s5,orderList转为orderVOList
    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderVO orderVO = this.getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }


    //前台取消订单
    //2022-08-31 创建
    @Override
    public void cancel(String orderNo){
        //s1,根据订单号orderNo，在orderMapper中查询订单
        //s1中，需要在orderMapper中创建selectByOrderNo
        Order order = orderMapper.selectByOrderNo(orderNo);
        //s1-1，查不到订单，报错
        if(order == null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        //s2,验证用户身份
        //s2,订单存在，则判断所属；即判断这个订单是不是属于当前这个用户的
        //s2-1，拿到用户ID. 从UserFilter拿到
        Integer userId = UserFilter.currentUser.getId();
        //s2-2，order里的userId与session中的userId进行比较
        if (!order.getUserId().equals(userId)){
            //不相等，抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.NOT_YOUR_ORDER);
        }

        //s3，没有付款的时候，可以取消；此步骤仅供理解，现实情况是付没付款都可以取消的
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            Order orderNew = new Order();
            orderNew.setId(order.getId());
            //s3-1，订单状态要改:0-用户已取消
            orderNew.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            //s3-2，设置结束时间;忘记书写了
            orderNew.setEndTime(new Date());
            int count = orderMapper.updateByPrimaryKeySelective(orderNew);
            if (count == 0){
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        }else{
            //订单状态不符
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    //前台生成支付二维码
    //2022-08-31 创建
    @Override
    public String qrcode(String orderNo){
        //s1,获取到请求属性
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //拿到http请求
        HttpServletRequest request = requestAttributes.getRequest();

        //使用QRCodeGenerator组件
        return null;
    }


}
