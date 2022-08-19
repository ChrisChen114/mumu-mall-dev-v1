package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Cart;
import org.springframework.stereotype.Repository;

@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);


    //dev_v1
    //



}