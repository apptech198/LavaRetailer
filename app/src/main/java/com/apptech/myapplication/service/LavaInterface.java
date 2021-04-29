package com.apptech.myapplication.service;

import com.apptech.myapplication.modal.brand.BrandList;
import com.apptech.myapplication.modal.message.NotificationListBrandWise;
import com.apptech.myapplication.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LavaInterface {

    @POST("register")
    @FormUrlEncoded
    Call<Object> Signup(@Field("name") String name,
                        @Field("mobile") String mobile,
                        @Field("password") String password,
                        @Field("email") String email,
                        @Field("locality") String locality,
                        @Field("city") String city,
                        @Field("governate") String governate,
                        @Field("address") String address
    );

    @POST("login")
    @FormUrlEncoded
    Call<Object> Login(@Field("mobile") String mobile, @Field("password") String password);

    @POST("brand_list")
    Call<BrandList> Brand();

    @POST("governate")
    @FormUrlEncoded
    Call<Object> Governate(@Field("lang") String lang);

    @POST("city")
    @FormUrlEncoded
    Call<Object> getcity(@Field("lang") String lang, @Field("governate") String governate);

    @POST("locality")
    @FormUrlEncoded
    Call<Object> getlocality(@Field("lang") String lang, @Field("city") String city);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("sale_stock_imei")
    Call<Object> SellOut_PriceDropEntry(@Body JsonObject imei);

    @POST("stock_imei_list")
    @FormUrlEncoded
    Call<SellOutPendingVerificationList> SellOut_Stock_List_DateFilter(@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date);

    @POST("notification_list_latest")
    Call<Object> NotificationList();

    @POST("notification_list_brand_wise")
    @FormUrlEncoded
    Call<NotificationListBrandWise> NotificationListBrandWise(@Field("brand_id") String brand_id);







}



























