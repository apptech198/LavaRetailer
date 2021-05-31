
package com.apptech.lava_retailer.list.warrentylist;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Detail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("distributor_id")
    @Expose
    private String distributorId;
    @SerializedName("distributor_name")
    @Expose
    private String distributorName;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("imei2")
    @Expose
    private String imei2;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("marketing_name")
    @Expose
    private String marketingName;
    @SerializedName("marketing_name_ar")
    @Expose
    private String marketingNameAr;
    @SerializedName("marketing_name_fr")
    @Expose
    private Object marketingNameFr;
    @SerializedName("des_fr")
    @Expose
    private Object desFr;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("des_ar")
    @Expose
    private Object desAr;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("dis_price")
    @Expose
    private String disPrice;
    @SerializedName("thumb")
    @Expose
    private Object thumb;
    @SerializedName("thumb_ar")
    @Expose
    private Object thumbAr;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("commodity_id")
    @Expose
    private String commodityId;
    @SerializedName("format")
    @Expose
    private String format;
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
    private Object category;
    @SerializedName("serialized")
    @Expose
    private String serialized;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("video_ar")
    @Expose
    private String videoAr;
    @SerializedName("warranty_type")
    @Expose
    private Object warrantyType;
    @SerializedName("prowar")
    @Expose
    private String prowar;
    @SerializedName("pro_war_days")
    @Expose
    private String proWarDays;
    @SerializedName("battery_war")
    @Expose
    private String batteryWar;
    @SerializedName("battery_war_days")
    @Expose
    private String batteryWarDays;
    @SerializedName("charging_adapter_war")
    @Expose
    private String chargingAdapterWar;
    @SerializedName("charging_adapter_war_days")
    @Expose
    private String chargingAdapterWarDays;
    @SerializedName("charger_war")
    @Expose
    private String chargerWar;
    @SerializedName("charger_war_days")
    @Expose
    private String chargerWarDays;
    @SerializedName("usb_war")
    @Expose
    private String usbWar;
    @SerializedName("usb_war_days")
    @Expose
    private String usbWarDays;
    @SerializedName("wired_earphone_war")
    @Expose
    private String wiredEarphoneWar;
    @SerializedName("wired_earphone_war_days")
    @Expose
    private String wiredEarphoneWarDays;
    @SerializedName("available_qty")
    @Expose
    private Object availableQty;
    @SerializedName("hide")
    @Expose
    private Object hide;
    @SerializedName("total_sale")
    @Expose
    private String totalSale;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("sell_replace")
    @Expose
    private String sellReplace;
    @SerializedName("sellout")
    @Expose
    private String sellout;
    @SerializedName("sell_out_date")
    @Expose
    private String sellOutDate;
    @SerializedName("warranty_start_date")
    @Expose
    private String warrantyStartDate;
    @SerializedName("warranty_end_date")
    @Expose
    private Object warrantyEndDate;
    @SerializedName("tertiary_warranty_date")
    @Expose
    private Object tertiaryWarrantyDate;
    @SerializedName("srno")
    @Expose
    private Object srno;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("price_drop")
    @Expose
    private String priceDrop;
    @SerializedName("retailer_id")
    @Expose
    private String retailerId;
    @SerializedName("retailer_name")
    @Expose
    private Object retailerName;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("order_dispatch_date")
    @Expose
    private String orderDispatchDate;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("locality")
    @Expose
    private Object locality;
    @SerializedName("locality_id")
    @Expose
    private Object localityId;

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

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public Object getMarketingNameFr() {
        return marketingNameFr;
    }

    public void setMarketingNameFr(Object marketingNameFr) {
        this.marketingNameFr = marketingNameFr;
    }

    public Object getDesFr() {
        return desFr;
    }

    public void setDesFr(Object desFr) {
        this.desFr = desFr;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Object getDesAr() {
        return desAr;
    }

    public void setDesAr(Object desAr) {
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

    public Object getThumb() {
        return thumb;
    }

    public void setThumb(Object thumb) {
        this.thumb = thumb;
    }

    public Object getThumbAr() {
        return thumbAr;
    }

    public void setThumbAr(Object thumbAr) {
        this.thumbAr = thumbAr;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
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

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
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

    public Object getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(Object warrantyType) {
        this.warrantyType = warrantyType;
    }

    public String getProwar() {
        return prowar;
    }

    public void setProwar(String prowar) {
        this.prowar = prowar;
    }

    public String getProWarDays() {
        return proWarDays;
    }

    public void setProWarDays(String proWarDays) {
        this.proWarDays = proWarDays;
    }

    public String getBatteryWar() {
        return batteryWar;
    }

    public void setBatteryWar(String batteryWar) {
        this.batteryWar = batteryWar;
    }

    public String getBatteryWarDays() {
        return batteryWarDays;
    }

    public void setBatteryWarDays(String batteryWarDays) {
        this.batteryWarDays = batteryWarDays;
    }

    public String getChargingAdapterWar() {
        return chargingAdapterWar;
    }

    public void setChargingAdapterWar(String chargingAdapterWar) {
        this.chargingAdapterWar = chargingAdapterWar;
    }

    public String getChargingAdapterWarDays() {
        return chargingAdapterWarDays;
    }

    public void setChargingAdapterWarDays(String chargingAdapterWarDays) {
        this.chargingAdapterWarDays = chargingAdapterWarDays;
    }

    public String getChargerWar() {
        return chargerWar;
    }

    public void setChargerWar(String chargerWar) {
        this.chargerWar = chargerWar;
    }

    public String getChargerWarDays() {
        return chargerWarDays;
    }

    public void setChargerWarDays(String chargerWarDays) {
        this.chargerWarDays = chargerWarDays;
    }

    public String getUsbWar() {
        return usbWar;
    }

    public void setUsbWar(String usbWar) {
        this.usbWar = usbWar;
    }

    public String getUsbWarDays() {
        return usbWarDays;
    }

    public void setUsbWarDays(String usbWarDays) {
        this.usbWarDays = usbWarDays;
    }

    public String getWiredEarphoneWar() {
        return wiredEarphoneWar;
    }

    public void setWiredEarphoneWar(String wiredEarphoneWar) {
        this.wiredEarphoneWar = wiredEarphoneWar;
    }

    public String getWiredEarphoneWarDays() {
        return wiredEarphoneWarDays;
    }

    public void setWiredEarphoneWarDays(String wiredEarphoneWarDays) {
        this.wiredEarphoneWarDays = wiredEarphoneWarDays;
    }

    public Object getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(Object availableQty) {
        this.availableQty = availableQty;
    }

    public Object getHide() {
        return hide;
    }

    public void setHide(Object hide) {
        this.hide = hide;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSellReplace() {
        return sellReplace;
    }

    public void setSellReplace(String sellReplace) {
        this.sellReplace = sellReplace;
    }

    public String getSellout() {
        return sellout;
    }

    public void setSellout(String sellout) {
        this.sellout = sellout;
    }

    public String getSellOutDate() {
        return sellOutDate;
    }

    public void setSellOutDate(String sellOutDate) {
        this.sellOutDate = sellOutDate;
    }

    public String getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public void setWarrantyStartDate(String warrantyStartDate) {
        this.warrantyStartDate = warrantyStartDate;
    }

    public Object getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public void setWarrantyEndDate(Object warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public Object getTertiaryWarrantyDate() {
        return tertiaryWarrantyDate;
    }

    public void setTertiaryWarrantyDate(Object tertiaryWarrantyDate) {
        this.tertiaryWarrantyDate = tertiaryWarrantyDate;
    }

    public Object getSrno() {
        return srno;
    }

    public void setSrno(Object srno) {
        this.srno = srno;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPriceDrop() {
        return priceDrop;
    }

    public void setPriceDrop(String priceDrop) {
        this.priceDrop = priceDrop;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public Object getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(Object retailerName) {
        this.retailerName = retailerName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDispatchDate() {
        return orderDispatchDate;
    }

    public void setOrderDispatchDate(String orderDispatchDate) {
        this.orderDispatchDate = orderDispatchDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Object getLocality() {
        return locality;
    }

    public void setLocality(Object locality) {
        this.locality = locality;
    }

    public Object getLocalityId() {
        return localityId;
    }

    public void setLocalityId(Object localityId) {
        this.localityId = localityId;
    }

}
