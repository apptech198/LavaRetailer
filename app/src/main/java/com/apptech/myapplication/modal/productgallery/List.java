
package com.apptech.myapplication.modal.productgallery;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pro_id")
    @Expose
    private String proId;
    @SerializedName("pro_name")
    @Expose
    private Object proName;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("_img_url_ar")
    @Expose
    private String imgUrlAr;
    @SerializedName("time")
    @Expose
    private Object time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public Object getProName() {
        return proName;
    }

    public void setProName(Object proName) {
        this.proName = proName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrlAr() {
        return imgUrlAr;
    }

    public void setImgUrlAr(String imgUrlAr) {
        this.imgUrlAr = imgUrlAr;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

}
