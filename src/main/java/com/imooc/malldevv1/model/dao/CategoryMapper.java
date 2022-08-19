package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Category;
import org.springframework.stereotype.Repository;

@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);


    //dev_v1
    //


}