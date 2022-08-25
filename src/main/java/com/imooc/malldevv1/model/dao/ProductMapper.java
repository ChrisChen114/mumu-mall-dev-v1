package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


//Repository，语义注解，说明当前类用于业务持久层，通常描述对应Dao类
@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);


    //dev_v1 增加
    //按名字查询商品
    Product selectByName(String name);

    //批量上下架商品
    //2022-08-24 创建
    //注意点：是@Param注解，不是@RequestParam注解
    //第一种更新方案：在service层中，逐个调用更新上下架状态的update语句
    int updateSellStatus(@Param("id") Integer id, @Param("status") Integer status);
    //第二种更新方案：传入ids和sellStatus到productmapper中，在update语句中遍历更新上下架状态
    int updateSellStatus1(@Param("ids") Integer[] ids, @RequestParam("status") Integer status);

    //后台商品列表
    //2022-08-24 创建
    List<Product> selectListProductForAdmin();



}