package com.apptech.myapplication.list;

public class LocalityList {
    String id;
    String governate_en;
    String locality_en;
    String time;
    String governate_ar;
    String locality_ar;

    public LocalityList(String id, String governate_en, String locality_en, String time, String governate_ar, String locality_ar) {
        this.id = id;
        this.governate_en = governate_en;
        this.locality_en = locality_en;
        this.time = time;
        this.governate_ar = governate_ar;
        this.locality_ar = locality_ar;
    }

    public String getId() {
        return id;
    }

    public String getGovernate_en() {
        return governate_en;
    }

    public String getLocality_en() {
        return locality_en;
    }

    public String getTime() {
        return time;
    }

    public String getGovernate_ar() {
        return governate_ar;
    }

    public String getLocality_ar() {
        return locality_ar;
    }


    @Override
    public String toString() {
        return "LocalityList{" +
                "id='" + id + '\'' +
                ", governate_en='" + governate_en + '\'' +
                ", locality_en='" + locality_en + '\'' +
                ", time='" + time + '\'' +
                ", governate_ar='" + governate_ar + '\'' +
                ", locality_ar='" + locality_ar + '\'' +
                '}';
    }
}
