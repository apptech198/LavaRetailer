
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
    private String tradingCatName;
    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("name_fr")
    @Expose
    private String nameFr;
    @SerializedName("img_en")
    @Expose
    private String imgEn;
    @SerializedName("img_ar")
    @Expose
    private String imgAr;
    @SerializedName("img_fr")
    @Expose
    private String imgFr;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;

    public List(String id, String tradingCat, String tradingCatName, String fileType, String name, String nameAr, String nameFr, String imgEn, String imgAr, String imgFr, String time, String date) {
        this.id = id;
        this.tradingCat = tradingCat;
        this.tradingCatName = tradingCatName;
        this.fileType = fileType;
        this.name = name;
        this.nameAr = nameAr;
        this.nameFr = nameFr;
        this.imgEn = imgEn;
        this.imgAr = imgAr;
        this.imgFr = imgFr;
        this.time = time;
        this.date = date;
    }


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

    public String getTradingCatName() {
        return tradingCatName;
    }

    public void setTradingCatName(String tradingCatName) {
        this.tradingCatName = tradingCatName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImgEn() {
        return imgEn;
    }

    public void setImgEn(String imgEn) {
        this.imgEn = imgEn;
    }

    public String getImgAr() {
        return imgAr;
    }

    public void setImgAr(String imgAr) {
        this.imgAr = imgAr;
    }

    public String getImgFr() {
        return imgFr;
    }

    public void setImgFr(String imgFr) {
        this.imgFr = imgFr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
