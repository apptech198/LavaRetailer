
package com.apptech.myapplication.modal.productgallery;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ProductGalleryList {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("list")
    @Expose
    private java.util.List<List> list = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
