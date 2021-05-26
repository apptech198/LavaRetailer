package com.apptech.lava_retailer.list.brand;

public class Brandlist {
    String id;
    String name;
    String time;
    String name_ar;
    String img;
    String name_fr;

    public Brandlist(String id, String name, String time, String name_ar, String img,String name_fr) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.name_ar = name_ar;
        this.img = img;
        this.name_fr=name_fr;
    }

    public String getName_fr() {
        return name_fr;
    }

    public void setName_fr(String name_fr) {
        this.name_fr = name_fr;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getImg() {
        return img;
    }

    @Override
    public String toString() {
        return "Brandlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", name_ar='" + name_ar + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
