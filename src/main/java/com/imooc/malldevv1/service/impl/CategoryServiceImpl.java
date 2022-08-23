package com.imooc.malldevv1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.dao.CategoryMapper;
import com.imooc.malldevv1.model.pojo.Category;
import com.imooc.malldevv1.model.request.AddCategoryReq;
import com.imooc.malldevv1.model.vo.CategoryVO;
import com.imooc.malldevv1.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.AssertFalse;
import java.util.List;


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
    @Override
    public void update(Category updateCategory) {
        //s5，更新时候，尤其名字不能与别人冲突，因此需要到库里先查询
        //查询的前提是name不能为空
        if (updateCategory.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            //这块的判断条件，没想到****************
            //名字不为空、ID不一样，此时需要给拒绝掉，不能更新，抛出异常
            //名字不为空、ID不一样，它的意思是库中已有一条目录记录，但是库里ID和传入不一样，根据传入ID进行更新时，就会有两条相同的目录名字，是不符合要求的.
            //查到的categoryOld的ID不能等于入参传进来的updateCategory的ID
            //究其根本，就是要保证目录不能重名，但是代码上需要复合判断
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
                //存在冲突，抛出异常
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }

        //s6, 根据主键进行选择性更新
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            //更新失败
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);

        }
    }


    //后台管理：删除目录分类
    @Override
    public void delete(Integer id){
        //s2,查询待删除的目录id是否为空
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        if (categoryOld == null){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
        //根据主键删除
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0){
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    //后台管理：目录列表（平铺）
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        //s2, 利用PageHelper设置分页参数
        //其中，"type:order_num"是指‘orderBy’
        PageHelper.startPage(pageNum, pageSize,"type,order_num");
        //s3, 查询所有目录信息
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo<>(categoryList);
        return pageInfo;
    }



    //前台管理：目录列表（递归）

}
