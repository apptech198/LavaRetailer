
package com.apptech.lava_retailer.list.announcelist;

import android.app.Application;
import android.content.Context;

import com.apptech.lava_retailer.other.SessionManage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class PriceDrop {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("drop_amount")
    @Expose
    private String drop_amount;
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

    Context context;

    public PriceDrop(String id, String drop_amount, String name, String startDate, String endDate, String nameAr, String nameFr, String time, String active, Context context) {
        this.id = id;
        this.drop_amount = drop_amount;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.time = time;
        this.active = active;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getDrop_amount() {
        return drop_amount;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getNameFr() {
        return nameFr;
    }

    public String getTime() {
        return time;
    }

    public String getActive() {
        return active;
    }

    public Context getContext() {
        return context;
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
