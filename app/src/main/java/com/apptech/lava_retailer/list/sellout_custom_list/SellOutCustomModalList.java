package com.apptech.lava_retailer.list.sellout_custom_list;

import com.google.gson.annotations.SerializedName;

public class SellOutCustomModalList {


    private String model;
    private String modelAr;
    private String qty;
    private String value;
    private String count;
    private boolean Line = false;


    public SellOutCustomModalList(String model, String modelAr, String qty, String value, String count, boolean line) {
        this.model = model;
        this.modelAr = modelAr;
        this.qty = qty;
        this.value = value;
        this.count = count;
        Line = line;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelAr() {
        return modelAr;
    }

    public void setModelAr(String modelAr) {
        this.modelAr = modelAr;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isLine() {
        return Line;
    }

    public void setLine(boolean line) {
        Line = line;
    }

}
