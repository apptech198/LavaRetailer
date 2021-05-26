package com.apptech.lava_retailer.list.country;

public class Country_list {

    String id;
    String name;
    String name_ar;
    String name_fr;
    String time;

    public Country_list(String id, String name, String name_ar, String name_fr, String time) {
        this.id = id;
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
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

    public String getName_fr() {
        return name_fr;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Country_list{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", name_ar='" + name_ar + '\'' +
                ", name_fr='" + name_fr + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

