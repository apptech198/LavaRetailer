
package com.apptech.myapplication.modal.locality;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Locality {

    @SerializedName("locality_list")
    @Expose
    private List<Locality__1> localityList = null;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Locality__1> getLocalityList() {
        return localityList;
    }

    public void setLocalityList(List<Locality__1> localityList) {
        this.localityList = localityList;
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
