
package com.apptech.myapplication.modal.productlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("locality_id")
    @Expose
    private String localityId;
    @SerializedName("marketing_name")
    @Expose
    private String marketingName;
    @SerializedName("marketing_name_ar")
    @Expose
    private String marketingNameAr;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("des_ar")
    @Expose
    private String desAr;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("dis_price")
    @Expose
    private String disPrice;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("thumb_ar")
    @Expose
    private String thumbAr;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("commodity")
    @Expose
    private String commodity;
    @SerializedName("commodity_ar")
    @Expose
    private String commodityAr;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("brand_ar")
    @Expose
    private String brandAr;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("model_ar")
    @Expose
    private String modelAr;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("serialized")
    @Expose
    private String serialized;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("video_ar")
    @Expose
    private String videoAr;
    @SerializedName("acce_mobile")
    @Expose
    private String acceMobile;
    @SerializedName("acce_mobile_war")
    @Expose
    private String acceMobileWar;
    @SerializedName("acce_charger")
    @Expose
    private String acceCharger;
    @SerializedName("acce_charger_war")
    @Expose
    private String acceChargerWar;
    @SerializedName("acce_earphone")
    @Expose
    private String acceEarphone;
    @SerializedName("acce_earphone_war")
    @Expose
    private String acceEarphoneWar;
    @SerializedName("available_qty")
    @Expose
    private String availableQty;
    @SerializedName("hide")
    @Expose
    private String hide;
    @SerializedName("total_sale")
    @Expose
    private Object totalSale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }

    public String getMarketingName() {
        return marketingName;
    }

    public void setMarketingName(String marketingName) {
        this.marketingName = marketingName;
    }

    public String getMarketingNameAr() {
        return marketingNameAr;
    }

    public void setMarketingNameAr(String marketingNameAr) {
        this.marketingNameAr = marketingNameAr;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDesAr() {
        return desAr;
    }

    public void setDesAr(String desAr) {
        this.desAr = desAr;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(String disPrice) {
        this.disPrice = disPrice;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbAr() {
        return thumbAr;
    }

    public void setThumbAr(String thumbAr) {
        this.thumbAr = thumbAr;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getCommodityAr() {
        return commodityAr;
    }

    public void setCommodityAr(String commodityAr) {
        this.commodityAr = commodityAr;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandAr() {
        return brandAr;
    }

    public void setBrandAr(String brandAr) {
        this.brandAr = brandAr;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelAr() {
        return modelAr;
    }

    public void setModelAr(String modelAr) {
        this.modelAr = modelAr;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSerialized() {
        return serialized;
    }

    public void setSerialized(String serialized) {
        this.serialized = serialized;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoAr() {
        return videoAr;
    }

    public void setVideoAr(String videoAr) {
        this.videoAr = videoAr;
    }

    public String getAcceMobile() {
        return acceMobile;
    }

    public void setAcceMobile(String acceMobile) {
        this.acceMobile = acceMobile;
    }

    public String getAcceMobileWar() {
        return acceMobileWar;
    }

    public void setAcceMobileWar(String acceMobileWar) {
        this.acceMobileWar = acceMobileWar;
    }

    public String getAcceCharger() {
        return acceCharger;
    }

    public void setAcceCharger(String acceCharger) {
        this.acceCharger = acceCharger;
    }

    public String getAcceChargerWar() {
        return acceChargerWar;
    }

    public void setAcceChargerWar(String acceChargerWar) {
        this.acceChargerWar = acceChargerWar;
    }

    public String getAcceEarphone() {
        return acceEarphone;
    }

    public void setAcceEarphone(String acceEarphone) {
        this.acceEarphone = acceEarphone;
    }

    public String getAcceEarphoneWar() {
        return acceEarphoneWar;
    }

    public void setAcceEarphoneWar(String acceEarphoneWar) {
        this.acceEarphoneWar = acceEarphoneWar;
    }

    public String getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(String availableQty) {
        this.availableQty = availableQty;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public Object getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Object totalSale) {
        this.totalSale = totalSale;
    }

}
