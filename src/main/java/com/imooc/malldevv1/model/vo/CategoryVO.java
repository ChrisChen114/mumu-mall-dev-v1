package com.imooc.malldevv1.model.vo;


import com.imooc.malldevv1.model.pojo.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CategoryVO
 * VO:视图对象，一般用于返回到前端的数据.
 * vo是经过一定的转换之后，返回给前端内容的类
 * CategoryVO只比Category多一个属性，private List<CategoryVO> childCategory = new ArrayList<>();
 * CategoryVO和Category两者很像，但又不一样，Category属于pojo下，是一个实体类；
 * CategoryVO是经过一定的转换之后，返回给前端内容的类
 *
 * CategoryVO还需要实现implements序列化Serializable
 *
 * 利用Redis缓存加速响应-S7：CategoryVO implements实现 Serializable，目的将CategoryVO序列化
 *
 * 2022-08-21 创建
 * 2022-08-23 编写
 */

public class CategoryVO implements Serializable {
    private Integer id;

    private String name;

    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    private Date createTime;

    private Date updateTime;

    //比pojo下Category类多这一个属性
    private List<CategoryVO> childCategory = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<CategoryVO> getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(List<CategoryVO> childCategory) {
        this.childCategory = childCategory;
    }
}