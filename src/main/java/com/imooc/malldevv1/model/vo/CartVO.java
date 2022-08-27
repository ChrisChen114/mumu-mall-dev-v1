package com.imooc.malldevv1.model.vo;


/**
 * CartVO  是经过一定的转换之后，将购物车列表信息返回给前端内容的类，给前端展示用
 * VO:视图对象，一般用于返回到前端的数据.
 * vo是经过一定的转换之后，返回给前端内容的类
 * 暂时未在CartVO类后面添加implements Serializable？？
 * 2022-08-26 创建
 */


public class CartVO   {

    private Integer id;

    private Integer productId;

    private Integer userId;

    private Integer quantity;

    private Integer selected;//是否选中

    private Integer price;//单价

    private Integer totalPrice;//总价

    private String productName;

    private String productImage;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

}