package com.imooc.malldevv1.model.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 前台商品列表的一个请求类
 * 接收请求参数的类，用于在前台商品列表的Product类中
 * 用途：用于前台商品列表模块
 * 来自视频6-9 前台商品列表接口
 *
 */
public class ProductListReq {

    private String keyword;

    private Integer categoryId;

    //前端页面支持排序的，需增加orderBy属性
    private String orderBy;

    //分页信息
    //设置默认值
    private Integer pageNum = 1;

    //设置默认值
    private Integer pageSize = 10;


    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * toString方法
     * 因为在filter包中WebLogAspect类，doBefore方法的log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));，需要传入String类型内容，调试时候更加方便
     * 2022-08-31 增加
     * 来自视频9-1 准备工作
     * @return
     */
    @Override
    public String toString() {
        return "ProductListReq{" +
                "keyword='" + keyword + '\'' +
                ", categoryId=" + categoryId +
                ", orderBy='" + orderBy + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}