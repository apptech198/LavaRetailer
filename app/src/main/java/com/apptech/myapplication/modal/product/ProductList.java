package com.apptech.myapplication.modal.product;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.apptech.myapplication.modal.message.MessageList;
import com.bumptech.glide.Glide;

public class ProductList implements Comparable<ProductList> , Parcelable {


    String id;
    String product_id;
    String distributor_id;
    String distributor_name;
    String locality;
    String locality_id;
    String marketing_name;
    String marketing_name_ar;
    String des;
    String des_ar;
    String actual_price;
    String dis_price;
    String thumb;
    String thumb_ar;
    String sku;
    String imei;
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
    String acce_mobile;
    String acce_mobile_war;
    String acce_charger;
    String acce_charger_war;
    String acce_earphone;
    String acce_earphone_war;
    String available_qty;
    String hide;
    String total_sale;

    public ProductList(String id, String product_id, String distributor_id, String distributor_name, String locality, String locality_id, String marketing_name, String marketing_name_ar, String des, String des_ar, String actual_price, String dis_price, String thumb, String thumb_ar, String sku, String imei, String commodity, String commodity_ar, String brand_id, String brand, String brand_ar, String model, String model_ar, String category, String serialized, String video, String video_ar, String acce_mobile, String acce_mobile_war, String acce_charger, String acce_charger_war, String acce_earphone, String acce_earphone_war, String available_qty, String hide, String total_sale) {
        this.id = id;
        this.product_id = product_id;
        this.distributor_id = distributor_id;
        this.distributor_name = distributor_name;
        this.locality = locality;
        this.locality_id = locality_id;
        this.marketing_name = marketing_name;
        this.marketing_name_ar = marketing_name_ar;
        this.des = des;
        this.des_ar = des_ar;
        this.actual_price = actual_price;
        this.dis_price = dis_price;
        this.thumb = thumb;
        this.thumb_ar = thumb_ar;
        this.sku = sku;
        this.imei = imei;
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
        this.acce_mobile = acce_mobile;
        this.acce_mobile_war = acce_mobile_war;
        this.acce_charger = acce_charger;
        this.acce_charger_war = acce_charger_war;
        this.acce_earphone = acce_earphone;
        this.acce_earphone_war = acce_earphone_war;
        this.available_qty = available_qty;
        this.hide = hide;
        this.total_sale = total_sale;
    }

    public String getId() {
        return id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getDistributor_id() {
        return distributor_id;
    }

    public String getDistributor_name() {
        return distributor_name;
    }

    public String getLocality() {
        return locality;
    }

    public String getLocality_id() {
        return locality_id;
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

    public String getImei() {
        return imei;
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

    public String getAcce_mobile() {
        return acce_mobile;
    }

    public String getAcce_mobile_war() {
        return acce_mobile_war;
    }

    public String getAcce_charger() {
        return acce_charger;
    }

    public String getAcce_charger_war() {
        return acce_charger_war;
    }

    public String getAcce_earphone() {
        return acce_earphone;
    }

    public String getAcce_earphone_war() {
        return acce_earphone_war;
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

    }
}



































