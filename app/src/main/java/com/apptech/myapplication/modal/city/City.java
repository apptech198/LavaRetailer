
package com.apptech.myapplication.modal.city;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class City {

    @SerializedName("city_list")
    @Expose
    private List<City__1> cityList = null;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;

    public List<City__1> getCityList() {
        return cityList;
    }

    public void setCityList(List<City__1> cityList) {
        this.cityList = cityList;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
