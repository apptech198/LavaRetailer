package com.apptech.lava_retailer.list.tradecatlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradingMenuList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_ar")
    @Expose
    private String name_ar;
    @SerializedName("name_fr")
    @Expose
    private String name_fr;
    @SerializedName("time")
    @Expose
    private String time;


    public TradingMenuList(String id, String name, String name_ar, String name_fr, String time) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getName_fr() {
        return name_fr;
    }

    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
