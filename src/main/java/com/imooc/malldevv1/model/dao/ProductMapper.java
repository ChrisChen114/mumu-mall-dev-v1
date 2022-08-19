package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Product;
import org.springframework.stereotype.Repository;

@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);


    //dev_v1
    //




}