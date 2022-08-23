package com.imooc.malldevv1.model.dao;

import com.imooc.malldevv1.model.pojo.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

//Repository，语义注解，说明当前类用于业务持久层，通常描述对应Dao类
@Repository  //告诉IDE（此处是IDEA），这是一个资源
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);


    //dev_v1 增加
    //2022-08-21 编写，用于新增目录等查询
    Category selectByName(String name);

    //2022-08-23 后台目录列表；利用分页技术实现
    //查询所有目录
    List<Category> selectList();

    //2022-08-23 前台目录列表：查询parentId=0的所有目录记录
    List<Category>  selectCategoriesByParentId(Integer parentId);

}