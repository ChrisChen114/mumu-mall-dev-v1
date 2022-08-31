package com.imooc.malldevv1.model.vo;

/**
 * OrderItemVO  是经过一定的转换之后，将订单列表信息返回给前端内容的类，给前端展示用
 * 作为一个属性，在OrderVO中使用
 * VO:视图对象，一般用于返回到前端的数据.
 * vo是经过一定的转换之后，返回给前端内容的类
 * 暂时未在OrderVO类后面添加implements Serializable？？
 * 2022-08-30 创建
 */
public class OrderItemVO {

    private String orderNo;

    private Integer productId;//首次创建OrderItemVO，被遗漏

    private String productName;

    private String productImg;

    private Integer unitPrice;

    private Integer quantity;

    private Integer totalPrice;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg == null ? null : productImg.trim();
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}