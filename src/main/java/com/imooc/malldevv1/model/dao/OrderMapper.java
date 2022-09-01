package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


//Repository，语义注解，说明当前类用于业务持久层，通常描述对应Dao类
@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);


    //dev_v1 增加
    //前台创建订单
    //2022-08-29 创建
    //List<Order> selectCheckedItem(@Param("userId") Integer userId);

    //前台订单详情
    //2022-08-30
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    //前台订单列表
    //2022-08-30 创建
    List<Order> selectForCustomerByUserId(@Param("userId") Integer userId);

    //后台订单列表
    //2022-08-31 创建
    List<Order> selectAllForAdmin();

}