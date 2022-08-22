package com.imooc.malldevv1.service;

import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.model.request.AddCategoryReq;

/**
 * 描述：      商品分类Service
 */

public interface CategoryService {
    //后台管理：增加目录分类
    void add(AddCategoryReq addCategoryReq) throws ImoocMallException;

    //后台管理：增加目录分类

    //后台管理：更新目录分类
    //后台管理：删除目录分类
    //后台管理：目录列表（平铺）
    //前台管理：目录列表（递归）


}
