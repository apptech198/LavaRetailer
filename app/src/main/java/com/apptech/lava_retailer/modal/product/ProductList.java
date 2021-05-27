package com.apptech.lava_retailer.modal.product;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductList implements Comparable<ProductList> , Parcelable {


    String id;
    String marketing_name;
    String marketing_name_ar;
    String des;
    String des_ar;
    String actual_price;
    String dis_price;
    String thumb;
    String thumb_ar;
    String sku;
    String commodity_id;
    String format;
    String commodity;
    String commodity_ar;
    String brand_id;
    String brand;
    String brand_ar;
    String model;
    String model_ar;
    String category;
    String serialized;
    String video;
    String video_ar;
    String prowar;
    String pro_war_days;
    String battery_war;
    String battery_war_days;
    String charging_adapter_war;
    String charging_adapter_war_days;
    String charger_war;
    String charger_war_days;
    String usb_war;
    String usb_war_days;
    String wired_earphone_war;
    String wired_earphone_war_days;
    String available_qty;
    String hide;
    String total_sale;
    String seller_id;
    String seller_name;
    String time;
    String marketing_name_fr;
    String des_fr;

    public ProductList(String id, String marketing_name, String marketing_name_ar,String marketing_name_fr, String des, String des_ar, String des_fr, String actual_price, String dis_price, String thumb, String thumb_ar, String sku, String commodity_id, String format, String commodity, String commodity_ar, String brand_id, String brand, String brand_ar, String model, String model_ar, String category, String serialized, String video, String video_ar, String prowar, String pro_war_days, String battery_war, String battery_war_days, String charging_adapter_war, String charging_adapter_war_days, String charger_war, String charger_war_days, String usb_war, String usb_war_days, String wired_earphone_war, String wired_earphone_war_days, String available_qty, String hide, String total_sale, String seller_id, String seller_name, String time) {
        this.id = id;
        this.marketing_name = marketing_name;
        this.marketing_name_ar = marketing_name_ar;
        this.des = des;
        this.des_ar = des_ar;
        this.actual_price = actual_price;
        this.dis_price = dis_price;
        this.thumb = thumb;
        this.thumb_ar = thumb_ar;
        this.sku = sku;
        this.commodity_id = commodity_id;
        this.format = format;
        this.commodity = commodity;
        this.commodity_ar = commodity_ar;
        this.brand_id = brand_id;
        this.brand = brand;
        this.brand_ar = brand_ar;
        this.model = model;
        this.model_ar = model_ar;
        this.category = category;
        this.serialized = serialized;
        this.video = video;
        this.video_ar = video_ar;
        this.prowar = prowar;
        this.pro_war_days = pro_war_days;
        this.battery_war = battery_war;
        this.battery_war_days = battery_war_days;
        this.charging_adapter_war = charging_adapter_war;
        this.charging_adapter_war_days = charging_adapter_war_days;
        this.charger_war = charger_war;
        this.charger_war_days = charger_war_days;
        this.usb_war = usb_war;
        this.usb_war_days = usb_war_days;
        this.wired_earphone_war = wired_earphone_war;
        this.wired_earphone_war_days = wired_earphone_war_days;
        this.available_qty = available_qty;
        this.hide = hide;
        this.total_sale = total_sale;
        this.seller_id = seller_id;
        this.seller_name = seller_name;
        this.time = time;
        this.des_fr=des_fr;
        this.marketing_name_fr=marketing_name_fr;
    }

    protected ProductList(Parcel in) {
        marketing_name_fr= in.readString();
        des_fr= in.readString();
        id = in.readString();
        marketing_name = in.readString();
        marketing_name_ar = in.readString();
        des = in.readString();
        des_ar = in.readString();
        actual_price = in.readString();
        dis_price = in.readString();
        thumb = in.readString();
        thumb_ar = in.readString();
        sku = in.readString();
        commodity_id = in.readString();
        format = in.readString();
        commodity = in.readString();
        commodity_ar = in.readString();
        brand_id = in.readString();
        brand = in.readString();
        brand_ar = in.readString();
        model = in.readString();
        model_ar = in.readString();
        category = in.readString();
        serialized = in.readString();
        video = in.readString();
        video_ar = in.readString();
        prowar = in.readString();
        pro_war_days = in.readString();
        battery_war = in.readString();
        battery_war_days = in.readString();
        charging_adapter_war = in.readString();
        charging_adapter_war_days = in.readString();
        charger_war = in.readString();
        charger_war_days = in.readString();
        usb_war = in.readString();
        usb_war_days = in.readString();
        wired_earphone_war = in.readString();
        wired_earphone_war_days = in.readString();
        available_qty = in.readString();
        hide = in.readString();
        total_sale = in.readString();
        seller_id = in.readString();
        seller_name = in.readString();
        time = in.readString();

    }

    public static final Creator<ProductList> CREATOR = new Creator<ProductList>() {
        @Override
        public ProductList createFromParcel(Parcel in) {
            return new ProductList(in);
        }

        @Override
        public ProductList[] newArray(int size) {
            return new ProductList[size];
        }
    };

    public String getMarketing_name_fr() {
        return marketing_name_fr;
    }

    public String getDes_fr() {
        return des_fr;
    }

    public String getId() {
        return id;
    }

    public String getMarketing_name() {
        return marketing_name;
    }

    public String getMarketing_name_ar() {
        return marketing_name_ar;
    }

    public String getDes() {
        return des;
    }

    public String getDes_ar() {
        return des_ar;
    }

    public String getActual_price() {
        return actual_price;
    }

    public String getDis_price() {
        return dis_price;
    }

    public String getThumb() {
        return thumb;
    }

    public String getThumb_ar() {
        return thumb_ar;
    }

    public String getSku() {
        return sku;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public String getFormat() {
        return format;
    }

    public String getCommodity() {
        return commodity;
    }

    public String getCommodity_ar() {
        return commodity_ar;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getBrand() {
        return brand;
    }

    public String getBrand_ar() {
        return brand_ar;
    }

    public String getModel() {
        return model;
    }

    public String getModel_ar() {
        return model_ar;
    }

    public String getCategory() {
        return category;
    }

    public String getSerialized() {
        return serialized;
    }

    public String getVideo() {
        return video;
    }

    public String getVideo_ar() {
        return video_ar;
    }

    public String getProwar() {
        return prowar;
    }

    public String getPro_war_days() {
        return pro_war_days;
    }

    public String getBattery_war() {
        return battery_war;
    }

    public String getBattery_war_days() {
        return battery_war_days;
    }

    public String getCharging_adapter_war() {
        return charging_adapter_war;
    }

    public String getCharging_adapter_war_days() {
        return charging_adapter_war_days;
    }

    public String getCharger_war() {
        return charger_war;
    }

    public String getCharger_war_days() {
        return charger_war_days;
    }

    public String getUsb_war() {
        return usb_war;
    }

    public String getUsb_war_days() {
        return usb_war_days;
    }

    public String getWired_earphone_war() {
        return wired_earphone_war;
    }

    public String getWired_earphone_war_days() {
        return wired_earphone_war_days;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public String getHide() {
        return hide;
    }

    public String getTotal_sale() {
        return total_sale;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getTime() {
        return time;
    }


    @Override
    public int compareTo(ProductList o) {
        if (o.actual_price.equals(this.actual_price)) {
            return 0;
        }
        return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(marketing_name);
        dest.writeString(marketing_name_ar);
        dest.writeString(des);
        dest.writeString(des_ar);
        dest.writeString(actual_price);
        dest.writeString(dis_price);
        dest.writeString(thumb);
        dest.writeString(thumb_ar);
        dest.writeString(sku);
        dest.writeString(commodity_id);
        dest.writeString(format);
        dest.writeString(commodity);
        dest.writeString(commodity_ar);
        dest.writeString(brand_id);
        dest.writeString(brand);
        dest.writeString(brand_ar);
        dest.writeString(model);
        dest.writeString(model_ar);
        dest.writeString(category);
        dest.writeString(serialized);
        dest.writeString(video);
        dest.writeString(video_ar);
        dest.writeString(prowar);
        dest.writeString(pro_war_days);
        dest.writeString(battery_war);
        dest.writeString(battery_war_days);
        dest.writeString(charging_adapter_war);
        dest.writeString(charging_adapter_war_days);
        dest.writeString(charger_war);
        dest.writeString(charger_war_days);
        dest.writeString(usb_war);
        dest.writeString(usb_war_days);
        dest.writeString(wired_earphone_war);
        dest.writeString(wired_earphone_war_days);
        dest.writeString(available_qty);
        dest.writeString(hide);
        dest.writeString(total_sale);
        dest.writeString(seller_id);
        dest.writeString(seller_name);
        dest.writeString(time);
    }



}



































