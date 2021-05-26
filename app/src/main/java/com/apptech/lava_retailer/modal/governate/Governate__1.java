package com.apptech.lava_retailer.modal.governate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Governate__1 {

    @SerializedName("governate_en")
    @Expose
    private String governateEn;

    public String getGovernateEn() {
        return governateEn;
    }

    public void setGovernateEn(String governateEn) {
        this.governateEn = governateEn;
    }


    @Override
    public String toString() {
        return  governateEn ;
    }
}
