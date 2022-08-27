package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Cart;
import com.imooc.malldevv1.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//Repository，语义注解，说明当前类用于业务持久层，通常描述对应Dao类
@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);


    //dev_v1 增加
    //添加商品到购物车
    //2022-08-26 编写
    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    //购物车列表模块
    //2022-08-26 编写
    List<CartVO> selectList(@Param("userId") Integer userId);
}