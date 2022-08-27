package com.imooc.malldevv1.model.query;

import java.util.List;

/**
 * 描述：  查询商品列表的Query
 * 构建query类，用于完成前台商品列表功能
 * 用途：用于前台商品列表模块
 * 来自视频6-9 前台商品列表接口
 * 起因：复杂查询，通常是构建一个Query对象，专门用于查询的.
 * 2022-05-25 创建
 */

public class ProductListQuery {
    //关键字
    private String keyword;

    //目录信息，往往是一个列表
    private List<Integer> categoryIds;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
