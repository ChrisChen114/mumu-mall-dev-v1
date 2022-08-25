package com.imooc.malldevv1.model.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 更新商品的一个请求类
 * 接收请求参数的类，用于在更新商品的Product类中
 * <p>
 * 另外：关于@Valid注解
 * 注解                      说明
 *
 * @Valid 需要验证
 * @NotNull 非空
 * @Max(value) 最大值
 * @Size(max=5,min=2) 字符串长度范围限制
 */
public class UpdateProductReq {

    @NotNull(message = "商品name不能为null")
    private Integer id;//需要传入id，之前遗漏了，而且是必传的

    //其他属性上的@NotNull要去掉，因为不一定更新
    private String name;

    private String image;

    private String detail;

    private Integer categoryId;

    @Min(value = 1,message = "价格不能小于1分钱")  //最小不能小于1分钱
    private Integer price;

    @Max(value=10000,message = "库存不能大于10000")
    private Integer stock;

    private Integer status;


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}