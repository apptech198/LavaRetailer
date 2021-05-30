
package com.apptech.lava_retailer.list.announcelist;

import android.app.Application;
import android.content.Context;

import com.apptech.lava_retailer.other.SessionManage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class PriceDrop {
    Context context;

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
    private String nameAr;
    @SerializedName("name_fr")
    @Expose
    private String nameFr;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("active")
    @Expose
    private String active;


    public PriceDrop(String id, String name, String startDate, String endDate, String nameAr, String nameFr, String time, String active,Context context ) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.time = time;
        this.active = active;
        this.context=context;
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

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        SessionManage sessionManage = SessionManage.getInstance(context);
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            return name;
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
                if(nameFr.isEmpty()){
                    return name;
                }else {
                    return nameFr;
                }
        } else {
            if(nameAr.isEmpty()){
                return name;
            }else {
                return nameAr;
            }
        }
    }
}
