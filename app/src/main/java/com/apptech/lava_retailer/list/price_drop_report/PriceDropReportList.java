package com.apptech.lava_retailer.list.price_drop_report;

public class PriceDropReportList {
    private String id;
    private String type;
    private String imei;
    private String date;
    private String retailer_id;
    private String retailer_name;
    private String time;
    private String product_id;
    private String model;
    private String commodity_id;
    private String commodity;
    private String check_status;
    private String status;
    private String price_drop_id;
    private String price_drop_name;
    private String qty;
    private String price;
    private String drop_amount;
    private String locality_id;
    private String locality_name;

    public PriceDropReportList(String id, String type, String imei, String date, String retailer_id, String retailer_name, String time, String product_id, String model, String commodity_id, String commodity, String check_status, String status, String price_drop_id, String price_drop_name, String qty, String price, String drop_amount, String locality_id, String locality_name) {
        this.id = id;
        this.type = type;
        this.imei = imei;
        this.date = date;
        this.retailer_id = retailer_id;
        this.retailer_name = retailer_name;
        this.time = time;
        this.product_id = product_id;
        this.model = model;
        this.commodity_id = commodity_id;
        this.commodity = commodity;
        this.check_status = check_status;
        this.status = status;
        this.price_drop_id = price_drop_id;
        this.price_drop_name = price_drop_name;
        this.qty = qty;
        this.price = price;
        this.drop_amount = drop_amount;
        this.locality_id = locality_id;
        this.locality_name = locality_name;
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

    public String getRetailer_name() {
        return retailer_name;
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

    public String getCommodity_id() {
        return commodity_id;
    }

    public String getCommodity() {
        return commodity;
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

    public String getDrop_amount() {
        return drop_amount;
    }

    public String getLocality_id() {
        return locality_id;
    }

    public String getLocality_name() {
        return locality_name;
    }
}
