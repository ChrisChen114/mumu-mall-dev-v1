package com.imooc.malldevv1.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 更新目录的一个请求类
 * 接收请求参数的类，用于在添加目录的Category类中
 * 2022-08-22 增加
 *
 * 与实体类pojo的Category非常像：如何解释？
 * 每个类的职责应该要明确，不应把一个类用于两种用途；
 * 在查询的时候只需要这4个字段，就定义这4个字段的一个请求类
 * 另外：关于@Valid注解
 * 注解                      说明
 * @Valid                   需要验证
 * @NotNull                 非空
 * @Max(value)              最大值
 * @Size(max=5,min=2)       字符串长度范围限制
 */
public class UpdateCategoryReq {
    @NotNull(message = "商品id不能为null") //不能不传；其他4个可以为null，即选择性的更新
    private Integer id;

    @Size(min=2,max = 10)//不能太短太长
    private String name;

    @Max(3)// 不超过3
    private Integer type;

    private Integer parentId;

    private Integer orderNum;

    //记得一定要生成getter和setter方法
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

    /**
     * toString方法
     * 因为在filter包中WebLogAspect类，doBefore方法的log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));，需要传入String类型内容，调试时候更加方便
     * 2022-08-31 增加
     * 来自视频9-1 准备工作
     * @return
     */
    @Override
    public String toString() {
        return "UpdateCategoryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}