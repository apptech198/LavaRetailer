package com.apptech.lava_retailer.list;

public class LocalityList {
    String id;
    String governate_id;
    String governate_name;
    String name;
    String name_ar;
    String name_fr;
    String time;

    public LocalityList(String id, String governate_id, String governate_name, String name, String name_ar, String name_fr, String time) {
        this.id = id;
        this.governate_id = governate_id;
        this.governate_name = governate_name;
        this.name = name;
        this.name_ar = name_ar;
        this.name_fr = name_fr;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getGovernate_id() {
        return governate_id;
    }

    public String getGovernate_name() {
        return governate_name;
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
        return "LocalityList{" +
                "id='" + id + '\'' +
                ", governate_id='" + governate_id + '\'' +
                ", governate_name='" + governate_name + '\'' +
                ", name='" + name + '\'' +
                ", name_ar='" + name_ar + '\'' +
                ", name_fr='" + name_fr + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
