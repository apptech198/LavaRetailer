package com.apptech.lava_retailer.list.sellout_custom_list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SellOutCustomCategoryList {



    private String commodity;
    private String commodity_ar;
    private String model;
    private String model_ar;
    private String qty;
    private String value;
    private String count;
    private List<SellOutCustomModalList> sellOutCustomModalLists;


//    public SellOutCustomCategoryList(String commodity, String commodity_ar, String model, String model_ar, String qty, String value, String count, List<SellOutCustomModalList> sellOutCustomModalLists) {
//        this.commodity = commodity;
//        this.commodity_ar = commodity_ar;
//        this.model = model;
//        this.model_ar = model_ar;
//        this.qty = qty;
//        this.value = value;
//        this.count = count;
//        this.sellOutCustomModalLists = sellOutCustomModalLists;
//    }

    public String getCommodity() {
        return commodity;
    }

    public String getCommodity_ar() {
        return commodity_ar;
    }

    public String getModel() {
        return model;
    }

    public String getModel_ar() {
        return model_ar;
    }

    public String getQty() {
        return qty;
    }

    public String getValue() {
        return value;
    }

    public String getCount() {
        return count;
    }

    public List<SellOutCustomModalList> getSellOutCustomModalLists() {
        return sellOutCustomModalLists;
    }
}