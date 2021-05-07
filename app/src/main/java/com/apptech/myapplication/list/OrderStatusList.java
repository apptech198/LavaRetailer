package com.apptech.myapplication.list;

public class OrderStatusList {

    String id;
    String product_id;
    String model_name;
    String product_name;
    String product_ivt_id;
    String dis_id;
    String ret_id;
    String dis_name;
    String address;
    String ret_name;
    String ret_mobile;
    String time;
    String qty;
    String discount_price;
    String actual_price;
    String order_no;
    String pretotal;
    String discount;
    String order_total;
    String item_total;
    String imei;
    String p_type;
    String status;

    public OrderStatusList(String id, String product_id, String model_name, String product_name, String product_ivt_id, String dis_id, String ret_id, String dis_name, String address, String ret_name, String ret_mobile, String time, String qty, String discount_price, String actual_price, String order_no, String pretotal, String discount, String order_total, String item_total, String imei, String p_type, String status) {
        this.id = id;
        this.product_id = product_id;
        this.model_name = model_name;
        this.product_name = product_name;
        this.product_ivt_id = product_ivt_id;
        this.dis_id = dis_id;
        this.ret_id = ret_id;
        this.dis_name = dis_name;
        this.address = address;
        this.ret_name = ret_name;
        this.ret_mobile = ret_mobile;
        this.time = time;
        this.qty = qty;
        this.discount_price = discount_price;
        this.actual_price = actual_price;
        this.order_no = order_no;
        this.pretotal = pretotal;
        this.discount = discount;
        this.order_total = order_total;
        this.item_total = item_total;
        this.imei = imei;
        this.p_type = p_type;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_ivt_id() {
        return product_ivt_id;
    }

    public String getDis_id() {
        return dis_id;
    }

    public String getRet_id() {
        return ret_id;
    }

    public String getDis_name() {
        return dis_name;
    }

    public String getAddress() {
        return address;
    }

    public String getRet_name() {
        return ret_name;
    }

    public String getRet_mobile() {
        return ret_mobile;
    }

    public String getTime() {
        return time;
    }

    public String getQty() {
        return qty;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getPretotal() {
        return pretotal;
    }

    public String getDiscount() {
        return discount;
    }

    public String getOrder_total() {
        return order_total;
    }

    public String getItem_total() {
        return item_total;
    }

    public String getImei() {
        return imei;
    }

    public String getP_type() {
        return p_type;
    }

    public String getStatus() {
        return status;
    }
}
