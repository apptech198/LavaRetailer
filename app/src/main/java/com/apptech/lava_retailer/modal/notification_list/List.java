
package com.apptech.lava_retailer.modal.notification_list;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class List implements Comparable, Cloneable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("time")
    @Expose
    private Object time;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public int compareTo(Object o) {
        List compare = (List) o;
        if (compare.heading.equals(this.heading) && compare.des.equals(this.des)) {
            return 0;
        }
        return 1;
    }

    @Override
    public List clone() {

        List clone;
        try {
            clone = (List) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }

        return clone;
    }

}
