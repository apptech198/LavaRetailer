
package com.apptech.lava_retailer.modal.order_statusList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("model_name")
    @Expose
    private String modelName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_ivt_id")
    @Expose
    private String productIvtId;
    @SerializedName("dis_id")
    @Expose
    private String disId;
    @SerializedName("ret_id")
    @Expose
    private String retId;
    @SerializedName("dis_name")
    @Expose
    private String disName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("ret_name")
    @Expose
    private String retName;
    @SerializedName("ret_mobile")
    @Expose
    private String retMobile;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("pretotal")
    @Expose
    private String pretotal;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("item_total")
    @Expose
    private String itemTotal;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("p_type")
    @Expose
    private String pType;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductIvtId() {
        return productIvtId;
    }

    public void setProductIvtId(String productIvtId) {
        this.productIvtId = productIvtId;
    }

    public String getDisId() {
        return disId;
    }

    public void setDisId(String disId) {
        this.disId = disId;
    }

    public String getRetId() {
        return retId;
    }

    public void setRetId(String retId) {
        this.retId = retId;
    }

    public String getDisName() {
        return disName;
    }

    public void setDisName(String disName) {
        this.disName = disName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRetName() {
        return retName;
    }

    public void setRetName(String retName) {
        this.retName = retName;
    }

    public String getRetMobile() {
        return retMobile;
    }

    public void setRetMobile(String retMobile) {
        this.retMobile = retMobile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPretotal() {
        return pretotal;
    }

    public void setPretotal(String pretotal) {
        this.pretotal = pretotal;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.pType = status;
    }

}
