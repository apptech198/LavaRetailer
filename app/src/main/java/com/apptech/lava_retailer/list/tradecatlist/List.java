
package com.apptech.lava_retailer.list.tradecatlist;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("trading_cat")
    @Expose
    private String tradingCat;
    @SerializedName("trading_cat_name")
    @Expose
    private Object tradingCatName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_ar")
    @Expose
    private Object nameAr;
    @SerializedName("name_fr")
    @Expose
    private Object nameFr;
    @SerializedName("img_en")
    @Expose
    private String imgEn;
    @SerializedName("img_ar")
    @Expose
    private Object imgAr;
    @SerializedName("img_fr")
    @Expose
    private Object imgFr;
    @SerializedName("time")
    @Expose
    private Object time;
    @SerializedName("date")
    @Expose
    private Object date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradingCat() {
        return tradingCat;
    }

    public void setTradingCat(String tradingCat) {
        this.tradingCat = tradingCat;
    }

    public Object getTradingCatName() {
        return tradingCatName;
    }

    public void setTradingCatName(Object tradingCatName) {
        this.tradingCatName = tradingCatName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImgEn() {
        return imgEn;
    }

    public void setImgEn(String imgEn) {
        this.imgEn = imgEn;
    }

    public Object getImgAr() {
        return imgAr;
    }

    public void setImgAr(Object imgAr) {
        this.imgAr = imgAr;
    }

    public Object getImgFr() {
        return imgFr;
    }

    public void setImgFr(Object imgFr) {
        this.imgFr = imgFr;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

}
