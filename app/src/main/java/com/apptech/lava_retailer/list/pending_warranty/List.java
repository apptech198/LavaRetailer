
package com.apptech.lava_retailer.list.pending_warranty;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("retailer_id")
    @Expose
    private String retailerId;
    @SerializedName("retailer_name")
    @Expose
    private String retailerName;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("srno")
    @Expose
    private String srno;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("handest_replace")
    @Expose
    private String handestReplace;
    @SerializedName("check_status")
    @Expose
    private String checkStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("sell_date")
    @Expose
    private String sellDate;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("locality_id")
    @Expose
    private String localityId;
    @SerializedName("locality_name")
    @Expose
    private String localityName;
    @SerializedName("p_type")
    @Expose
    private String pType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getHandestReplace() {
        return handestReplace;
    }

    public void setHandestReplace(String handestReplace) {
        this.handestReplace = handestReplace;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

}
