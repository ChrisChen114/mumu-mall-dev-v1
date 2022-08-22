package com.imooc.malldevv1.service.impl;

import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.dao.CategoryMapper;
import com.imooc.malldevv1.model.pojo.Category;
import com.imooc.malldevv1.model.request.AddCategoryReq;
import com.imooc.malldevv1.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 描述：      商品分类目录Service实现类
 * 2022-08-21 创建
 * 2022-08-21 编写后台管理：增加目录分类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    //注入CategoryMapper映射器，说明当前类用于业务持久层
    @Autowired
    CategoryMapper categoryMapper;

    //后台管理：增加目录分类
    //从Controller中，将请求中Body的参数（已映射或注入到AddCategoryReq）传入Service层
    //如body: {"name":"食品","type":1,"parentId":0,"orderNum":1}
    @Override
    public void add(AddCategoryReq addCategoryReq) throws ImoocMallException {
        //S1, 设置属性
        //方案1，传统方式使用set
        //设置属性，有没有更好的方法实现快速赋值呢
        Category category = new Category();
//        category.setName(addCategoryReq.getName());
//        category.setType(addCategoryReq.getType());
//        category.setParentId(addCategoryReq.getParentId());
//        category.setOrderNum(addCategoryReq.getOrderNum());
//        category.setCreateTime(new Date());
        //方案2，使用BeanUtils.copyProperties()方法，
        // 这种方法一下子拷贝字段类型相同的所有字段
        // 字段类型和字段名一样的进行拷贝
        BeanUtils.copyProperties(addCategoryReq, category);

        //S2，获取并判断新增目录是否存在
        //此步在构思时遗漏了
        Category CategoryOld = categoryMapper.selectByName(addCategoryReq.getName());//原来有没有
        if (CategoryOld != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        //S3，不存在目录，则插入目录名，完成新增
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            //没有插入成功
            //增加目录分类失败
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATED_FAILED);
        }

    }


    //后台管理：更新目录分类
    //后台管理：删除目录分类
    //后台管理：目录列表（平铺）
    //前台管理：目录列表（递归）

}
