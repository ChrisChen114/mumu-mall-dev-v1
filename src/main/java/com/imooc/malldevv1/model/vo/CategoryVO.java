package com.imooc.malldevv1.model.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CategoryVO
 * VO:视图对象，一般用于返回到前端的数据.
 * 2022-08-21 创建
 *
 */
public class CategoryVO {

    @NotNull(message = "商品名称不能为null")
    private String name;

    @NotNull(message = "商品类型不能为null")
    private Integer type;

    @Min(1)
    private Integer parentId;

    @Max(10000)
    private Integer orderNum;



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

}