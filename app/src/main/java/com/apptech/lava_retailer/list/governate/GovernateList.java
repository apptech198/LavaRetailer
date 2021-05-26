package com.apptech.lava_retailer.list.governate;

public class GovernateList {

    String id;
    String country_id;
    String country_name;
    String name;
    String name_ar;
    String name_fr;
    String time;

    public GovernateList(String id, String country_id, String country_name, String name, String name_ar, String name_fr, String time) {
        this.id = id;
        this.country_id = country_id;
        this.country_name = country_name;
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getName() {
        return name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getName_fr() {
        return name_fr;
    }

    public String getTime() {
        return time;
    }


    @Override
    public String toString() {
        return "GovernateList{" +
                "id='" + id + '\'' +
                ", country_id='" + country_id + '\'' +
                ", country_name='" + country_name + '\'' +
                ", name='" + name + '\'' +
                ", name_ar='" + name_ar + '\'' +
                ", name_fr='" + name_fr + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
