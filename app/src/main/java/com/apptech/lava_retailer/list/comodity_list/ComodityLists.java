package com.apptech.lava_retailer.list.comodity_list;

public class ComodityLists {

    String id;
    String name;
    String name_ar;
    String  name_fr;
    String brand_id;
    String brand_name;
    String form_type;
    String time;
    boolean Checkable = false;

    public ComodityLists(String id, String name, String name_ar, String name_fr, String brand_id, String brand_name, String form_type, String time) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.form_type = form_type;
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

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getForm_type() {
        return form_type;
    }

    public void setForm_type(String form_type) {
        this.form_type = form_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCheckable() {
        return Checkable;
    }

    public void setCheckable(boolean checkable) {
        Checkable = checkable;
    }
}
