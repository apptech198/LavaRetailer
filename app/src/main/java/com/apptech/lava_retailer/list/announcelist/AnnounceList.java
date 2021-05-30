
package com.apptech.lava_retailer.list.announcelist;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnnounceList {

    @SerializedName("price_drop_list")
    @Expose
    private List<PriceDrop> priceDropList = null;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("message")
    @Expose
    private String message;

    public List<PriceDrop> getPriceDropList() {
        return priceDropList;
    }

    public void setPriceDropList(List<PriceDrop> priceDropList) {
        this.priceDropList = priceDropList;
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
