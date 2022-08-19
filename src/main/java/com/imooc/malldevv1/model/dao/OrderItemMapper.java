package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.OrderItem;
import org.springframework.stereotype.Repository;

@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);


    //dev_v1
    //



}