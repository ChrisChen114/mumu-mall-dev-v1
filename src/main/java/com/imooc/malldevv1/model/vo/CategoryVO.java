package com.imooc.malldevv1.model.vo;


import com.imooc.malldevv1.model.pojo.Category;

import java.util.List;

/**
 * CategoryVO
 * VO:视图对象，一般用于返回到前端的数据.
 * 2022-08-21 创建
 */
public class CategoryVO {
    private Integer total;
    private List<Category> list;


    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }
}