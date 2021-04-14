package com.apptech.myapplication.modal.governate;

public class GovernateList {
    String governate_ar;

    public GovernateList(String governate_ar) {
        this.governate_ar = governate_ar;
    }

    public String getGovernate_ar() {
        return governate_ar;
    }

    public void setGovernate_ar(String governate_ar) {
        this.governate_ar = governate_ar;
    }

    @Override
    public String toString() {
        return "GovernateList{" +
                "governate_ar='" + governate_ar + '\'' +
                '}';
    }
}
