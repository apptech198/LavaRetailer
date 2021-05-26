package com.apptech.lava_retailer.list.comodity_list;

public class ComodityLists {

    String id;
    String name;
    String name_ar;
    String brand_id;
    String brand_name;
    String form_type;
    String time;

    public ComodityLists(String id, String name, String name_ar, String brand_id, String brand_name, String form_type, String time) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.form_type = form_type;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getForm_type() {
        return form_type;
    }

    public String getTime() {
        return time;
    }
}
