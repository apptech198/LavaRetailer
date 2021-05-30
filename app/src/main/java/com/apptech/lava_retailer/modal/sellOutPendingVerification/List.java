
package com.apptech.lava_retailer.modal.sellOutPendingVerification;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class List {

    String id;
    String type;
    String imei;
    String date;
    String retailer_id;
    String time;
    String product_id;
    String model;
    String check_status;
    String status;
    String price_drop_id;
    String price_drop_name;
    String qty;
    String price;


    public List(String id, String type, String imei, String date, String retailer_id, String time, String product_id, String model, String check_status, String status, String price_drop_id, String price_drop_name, String qty, String price) {
        this.id = id;
        this.type = type;
        this.imei = imei;
        this.date = date;
        this.retailer_id = retailer_id;
        this.time = time;
        this.product_id = product_id;
        this.model = model;
        this.check_status = check_status;
        this.status = status;
        this.price_drop_id = price_drop_id;
        this.price_drop_name = price_drop_name;
        this.qty = qty;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getImei() {
        return imei;
    }

    public String getDate() {
        return date;
    }

    public String getRetailer_id() {
        return retailer_id;
    }

    public String getTime() {
        return time;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getModel() {
        return model;
    }

    public String getCheck_status() {
        return check_status;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice_drop_id() {
        return price_drop_id;
    }

    public String getPrice_drop_name() {
        return price_drop_name;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }
}
