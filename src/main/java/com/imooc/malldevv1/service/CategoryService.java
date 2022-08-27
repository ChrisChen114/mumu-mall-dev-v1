package com.imooc.malldevv1.service;

import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.model.pojo.Category;
import com.imooc.malldevv1.model.request.AddCategoryReq;
import com.imooc.malldevv1.model.vo.CategoryVO;

import java.util.List;

/**
 * 描述：      商品分类Service
 */

public interface CategoryService {
    //后台管理：增加目录分类
    void add(AddCategoryReq addCategoryReq) throws ImoocMallException;

    //后台管理：更新目录分类
    void update(Category category);

    //后台管理：删除目录分类
    void delete(Integer id);

    //后台管理：目录列表（平铺）
    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    //前台管理：目录列表（递归）
    List<CategoryVO> listCategoryForCustomer(Integer parentId);

    //前台管理：目录列表（递归）


}
