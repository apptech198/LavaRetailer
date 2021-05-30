
package com.apptech.lava_retailer.list.announcelist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PriceDrop {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("name_ar")
    @Expose
    private Object nameAr;
    @SerializedName("name_fr")
    @Expose
    private Object nameFr;
    @SerializedName("time")
    @Expose
    private Object time;
    @SerializedName("active")
    @Expose
    private String active;


    public PriceDrop(String id, String name, String startDate, String endDate, Object nameAr, Object nameFr, Object time, String active) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.time = time;
        this.active = active;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getNameAr() {
        return nameAr;
    }

    public void setNameAr(Object nameAr) {
        this.nameAr = nameAr;
    }

    public Object getNameFr() {
        return nameFr;
    }

    public void setNameFr(Object nameFr) {
        this.nameFr = nameFr;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
