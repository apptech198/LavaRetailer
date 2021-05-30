
package com.apptech.lava_retailer.list.passbook;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("claim_type")
    @Expose
    private String claimType;
    @SerializedName("claim_code")
    @Expose
    private String claimCode;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_reference")
    @Expose
    private String paymentReference;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;
    @SerializedName("time")
    @Expose
    private String time;


    public List(String id, String claimType, String claimCode, String value, String status, String paymentReference, String paymentDate, String time) {
        this.id = id;
        this.claimType = claimType;
        this.claimCode = claimCode;
        this.value = value;
        this.status = status;
        this.paymentReference = paymentReference;
        this.paymentDate = paymentDate;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
