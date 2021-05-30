package com.apptech.lava_retailer.list.sell_out_report;

import com.google.gson.annotations.SerializedName;

public class SellOutReportList {

        @SerializedName("id")
        private String id;
        @SerializedName("product_id")
        private String productId;
        @SerializedName("distributor_id")
        private String distributorId;
        @SerializedName("distributor_name")
        private String distributorName;
        @SerializedName("imei")
        private String imei;
        @SerializedName("retailer_id")
        private String retailerId;
        @SerializedName("retailer_name")
        private String retailerName;
        @SerializedName("country_id")
        private String countryId;
        @SerializedName("country_name")
        private String countryName;
        @SerializedName("marketing_name")
        private String marketingName;
        @SerializedName("marketing_name_ar")
        private String marketingNameAr;
        @SerializedName("marketing_name_fr")
        private Object marketingNameFr;
        @SerializedName("des_fr")
        private Object desFr;
        @SerializedName("des")
        private String des;
        @SerializedName("des_ar")
        private Object desAr;
        @SerializedName("actual_price")
        private String actualPrice;
        @SerializedName("dis_price")
        private String disPrice;
        @SerializedName("thumb")
        private Object thumb;
        @SerializedName("thumb_ar")
        private Object thumbAr;
        @SerializedName("sku")
        private String sku;
        @SerializedName("commodity_id")
        private String commodityId;
        @SerializedName("format")
        private String format;
        @SerializedName("commodity")
        private String commodity;
        @SerializedName("commodity_ar")
        private String commodityAr;
        @SerializedName("brand_id")
        private String brandId;
        @SerializedName("brand")
        private String brand;
        @SerializedName("brand_ar")
        private String brandAr;
        @SerializedName("model")
        private String model;
        @SerializedName("model_ar")
        private String modelAr;
        @SerializedName("category")
        private Object category;
        @SerializedName("serialized")
        private String serialized;
        @SerializedName("video")
        private String video;
        @SerializedName("video_ar")
        private String videoAr;
        @SerializedName("warranty_type")
        private Object warrantyType;
        @SerializedName("prowar")
        private String prowar;
        @SerializedName("pro_war_days")
        private String proWarDays;
        @SerializedName("battery_war")
        private String batteryWar;
        @SerializedName("battery_war_days")
        private String batteryWarDays;
        @SerializedName("charging_adapter_war")
        private String chargingAdapterWar;
        @SerializedName("charging_adapter_war_days")
        private String chargingAdapterWarDays;
        @SerializedName("charger_war")
        private String chargerWar;
        @SerializedName("charger_war_days")
        private String chargerWarDays;
        @SerializedName("usb_war")
        private String usbWar;
        @SerializedName("usb_war_days")
        private String usbWarDays;
        @SerializedName("wired_earphone_war")
        private String wiredEarphoneWar;
        @SerializedName("wired_earphone_war_days")
        private String wiredEarphoneWarDays;
        @SerializedName("available_qty")
        private Object availableQty;
        @SerializedName("hide")
        private Object hide;
        @SerializedName("total_sale")
        private String totalSale;
        @SerializedName("seller_id")
        private String sellerId;
        @SerializedName("seller_name")
        private String sellerName;
        @SerializedName("time")
        private String time;
        @SerializedName("sold_date")
        private String soldDate;
        @SerializedName("warranty_start_date")
        private String warrantyStartDate;
        @SerializedName("warranty_end_date")
        private Object warrantyEndDate;
        @SerializedName("locality_id")
        private Object localityId;
        @SerializedName("locality")
        private Object locality;
        @SerializedName("imei2")
        private String imei2;
        @SerializedName("srno")
        private Object srno;
        @SerializedName("qty")
        private String qty;
        @SerializedName("order_no")
        private String orderNo;
        @SerializedName("price_drop")
        private String priceDrop;
        @SerializedName("sellout")
        private String sellout;
        @SerializedName("order_dispatch_date")
        private String orderDispatchDate;
        @SerializedName("order_date")
        private String orderDate;


    public SellOutReportList(String id, String productId, String distributorId, String distributorName, String imei, String retailerId, String retailerName, String countryId, String countryName, String marketingName, String marketingNameAr, Object marketingNameFr, Object desFr, String des, Object desAr, String actualPrice, String disPrice, Object thumb, Object thumbAr, String sku, String commodityId, String format, String commodity, String commodityAr, String brandId, String brand, String brandAr, String model, String modelAr, Object category, String serialized, String video, String videoAr, Object warrantyType, String prowar, String proWarDays, String batteryWar, String batteryWarDays, String chargingAdapterWar, String chargingAdapterWarDays, String chargerWar, String chargerWarDays, String usbWar, String usbWarDays, String wiredEarphoneWar, String wiredEarphoneWarDays, Object availableQty, Object hide, String totalSale, String sellerId, String sellerName, String time, String soldDate, String warrantyStartDate, Object warrantyEndDate, Object localityId, Object locality, String imei2, Object srno, String qty, String orderNo, String priceDrop, String sellout, String orderDispatchDate, String orderDate) {
        this.id = id;
        this.productId = productId;
        this.distributorId = distributorId;
        this.distributorName = distributorName;
        this.imei = imei;
        this.retailerId = retailerId;
        this.retailerName = retailerName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.marketingName = marketingName;
        this.marketingNameAr = marketingNameAr;
        this.marketingNameFr = marketingNameFr;
        this.desFr = desFr;
        this.des = des;
        this.desAr = desAr;
        this.actualPrice = actualPrice;
        this.disPrice = disPrice;
        this.thumb = thumb;
        this.thumbAr = thumbAr;
        this.sku = sku;
        this.commodityId = commodityId;
        this.format = format;
        this.commodity = commodity;
        this.commodityAr = commodityAr;
        this.brandId = brandId;
        this.brand = brand;
        this.brandAr = brandAr;
        this.model = model;
        this.modelAr = modelAr;
        this.category = category;
        this.serialized = serialized;
        this.video = video;
        this.videoAr = videoAr;
        this.warrantyType = warrantyType;
        this.prowar = prowar;
        this.proWarDays = proWarDays;
        this.batteryWar = batteryWar;
        this.batteryWarDays = batteryWarDays;
        this.chargingAdapterWar = chargingAdapterWar;
        this.chargingAdapterWarDays = chargingAdapterWarDays;
        this.chargerWar = chargerWar;
        this.chargerWarDays = chargerWarDays;
        this.usbWar = usbWar;
        this.usbWarDays = usbWarDays;
        this.wiredEarphoneWar = wiredEarphoneWar;
        this.wiredEarphoneWarDays = wiredEarphoneWarDays;
        this.availableQty = availableQty;
        this.hide = hide;
        this.totalSale = totalSale;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.time = time;
        this.soldDate = soldDate;
        this.warrantyStartDate = warrantyStartDate;
        this.warrantyEndDate = warrantyEndDate;
        this.localityId = localityId;
        this.locality = locality;
        this.imei2 = imei2;
        this.srno = srno;
        this.qty = qty;
        this.orderNo = orderNo;
        this.priceDrop = priceDrop;
        this.sellout = sellout;
        this.orderDispatchDate = orderDispatchDate;
        this.orderDate = orderDate;
    }


    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public String getImei() {
        return imei;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getMarketingName() {
        return marketingName;
    }

    public String getMarketingNameAr() {
        return marketingNameAr;
    }

    public Object getMarketingNameFr() {
        return marketingNameFr;
    }

    public Object getDesFr() {
        return desFr;
    }

    public String getDes() {
        return des;
    }

    public Object getDesAr() {
        return desAr;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public String getDisPrice() {
        return disPrice;
    }

    public Object getThumb() {
        return thumb;
    }

    public Object getThumbAr() {
        return thumbAr;
    }

    public String getSku() {
        return sku;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public String getFormat() {
        return format;
    }

    public String getCommodity() {
        return commodity;
    }

    public String getCommodityAr() {
        return commodityAr;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getBrand() {
        return brand;
    }

    public String getBrandAr() {
        return brandAr;
    }

    public String getModel() {
        return model;
    }

    public String getModelAr() {
        return modelAr;
    }

    public Object getCategory() {
        return category;
    }

    public String getSerialized() {
        return serialized;
    }

    public String getVideo() {
        return video;
    }

    public String getVideoAr() {
        return videoAr;
    }

    public Object getWarrantyType() {
        return warrantyType;
    }

    public String getProwar() {
        return prowar;
    }

    public String getProWarDays() {
        return proWarDays;
    }

    public String getBatteryWar() {
        return batteryWar;
    }

    public String getBatteryWarDays() {
        return batteryWarDays;
    }

    public String getChargingAdapterWar() {
        return chargingAdapterWar;
    }

    public String getChargingAdapterWarDays() {
        return chargingAdapterWarDays;
    }

    public String getChargerWar() {
        return chargerWar;
    }

    public String getChargerWarDays() {
        return chargerWarDays;
    }

    public String getUsbWar() {
        return usbWar;
    }

    public String getUsbWarDays() {
        return usbWarDays;
    }

    public String getWiredEarphoneWar() {
        return wiredEarphoneWar;
    }

    public String getWiredEarphoneWarDays() {
        return wiredEarphoneWarDays;
    }

    public Object getAvailableQty() {
        return availableQty;
    }

    public Object getHide() {
        return hide;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getTime() {
        return time;
    }

    public String getSoldDate() {
        return soldDate;
    }

    public String getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public Object getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public Object getLocalityId() {
        return localityId;
    }

    public Object getLocality() {
        return locality;
    }

    public String getImei2() {
        return imei2;
    }

    public Object getSrno() {
        return srno;
    }

    public String getQty() {
        return qty;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getPriceDrop() {
        return priceDrop;
    }

    public String getSellout() {
        return sellout;
    }

    public String getOrderDispatchDate() {
        return orderDispatchDate;
    }

    public String getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "SellOutReportList{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", distributorId='" + distributorId + '\'' +
                ", distributorName='" + distributorName + '\'' +
                ", imei='" + imei + '\'' +
                ", retailerId='" + retailerId + '\'' +
                ", retailerName='" + retailerName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", countryName='" + countryName + '\'' +
                ", marketingName='" + marketingName + '\'' +
                ", marketingNameAr='" + marketingNameAr + '\'' +
                ", marketingNameFr=" + marketingNameFr +
                ", desFr=" + desFr +
                ", des='" + des + '\'' +
                ", desAr=" + desAr +
                ", actualPrice='" + actualPrice + '\'' +
                ", disPrice='" + disPrice + '\'' +
                ", thumb=" + thumb +
                ", thumbAr=" + thumbAr +
                ", sku='" + sku + '\'' +
                ", commodityId='" + commodityId + '\'' +
                ", format='" + format + '\'' +
                ", commodity='" + commodity + '\'' +
                ", commodityAr='" + commodityAr + '\'' +
                ", brandId='" + brandId + '\'' +
                ", brand='" + brand + '\'' +
                ", brandAr='" + brandAr + '\'' +
                ", model='" + model + '\'' +
                ", modelAr='" + modelAr + '\'' +
                ", category=" + category +
                ", serialized='" + serialized + '\'' +
                ", video='" + video + '\'' +
                ", videoAr='" + videoAr + '\'' +
                ", warrantyType=" + warrantyType +
                ", prowar='" + prowar + '\'' +
                ", proWarDays='" + proWarDays + '\'' +
                ", batteryWar='" + batteryWar + '\'' +
                ", batteryWarDays='" + batteryWarDays + '\'' +
                ", chargingAdapterWar='" + chargingAdapterWar + '\'' +
                ", chargingAdapterWarDays='" + chargingAdapterWarDays + '\'' +
                ", chargerWar='" + chargerWar + '\'' +
                ", chargerWarDays='" + chargerWarDays + '\'' +
                ", usbWar='" + usbWar + '\'' +
                ", usbWarDays='" + usbWarDays + '\'' +
                ", wiredEarphoneWar='" + wiredEarphoneWar + '\'' +
                ", wiredEarphoneWarDays='" + wiredEarphoneWarDays + '\'' +
                ", availableQty=" + availableQty +
                ", hide=" + hide +
                ", totalSale='" + totalSale + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", time='" + time + '\'' +
                ", soldDate='" + soldDate + '\'' +
                ", warrantyStartDate='" + warrantyStartDate + '\'' +
                ", warrantyEndDate=" + warrantyEndDate +
                ", localityId=" + localityId +
                ", locality=" + locality +
                ", imei2='" + imei2 + '\'' +
                ", srno=" + srno +
                ", qty='" + qty + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", priceDrop='" + priceDrop + '\'' +
                ", sellout='" + sellout + '\'' +
                ", orderDispatchDate='" + orderDispatchDate + '\'' +
                ", orderDate='" + orderDate + '\'' +
                '}';
    }
}
