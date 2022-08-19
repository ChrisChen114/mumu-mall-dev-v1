package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Order;
import org.springframework.stereotype.Repository;

@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);


    //dev_v1
    //




}