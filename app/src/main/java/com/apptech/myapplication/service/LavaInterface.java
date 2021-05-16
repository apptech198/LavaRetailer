package com.apptech.myapplication.service;

import com.apptech.myapplication.list.notificationList.NotificationModel;
import com.apptech.myapplication.modal.brand.BrandList;
import com.apptech.myapplication.modal.message.NotificationListBrandWise;
import com.apptech.myapplication.modal.order_statusList.OrderStatusList;
import com.apptech.myapplication.modal.productgallery.ProductGalleryList;
import com.apptech.myapplication.modal.productlist.ProductList;
import com.apptech.myapplication.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LavaInterface {

    @POST("register")
    @FormUrlEncoded
    Call<Object> Signup(@Field("name") String name,
                        @Field("mobile") String mobile,
                        @Field("password") String password,
                        @Field("email") String email,
                        @Field("locality") String locality,
                        @Field("governate") String governate,
                        @Field("address") String address,
                        @Field("signup_type") String signup_type,
                        @Field("social_auth_token") String social_auth_token,
                        @Field("outlet_name") String outlet_name,
                        @Field("locality_id") String locality_id,
                        @Field("locality_ar") String locality_ar
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

    @POST("sale_sellout_imei")
    Call<Object> SELL_OUT_IMEI(@Body JsonObject imei);

    @POST("sale_stock_imei")
    Call<Object> PRICE_DROP_IMEI(@Body JsonObject imei);

    @POST("stock_imei_list")
    @FormUrlEncoded
    Call<SellOutPendingVerificationList> PRICE_DROP_IMEI_LIST(@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date);

    @POST("sellout_imei_list")
    @FormUrlEncoded
    Call<SellOutPendingVerificationList> SELL_OUT_IMEI_LIST(@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date);

    @POST("notification_list_latest")
    Call<Object> NotificationList();

    @POST("notification_list_latest")
    Call<NotificationModel> NotificationList1();

    @POST("notification_list_brand_wise")
    @FormUrlEncoded
    Call<NotificationListBrandWise> NotificationListBrandWise(@Field("brand_id") String brand_id);

    @POST("send_otp")
    @FormUrlEncoded
    Call<Object> SEND_OTP (@Field("mobile") String mobile);

    @POST("otp_verify")
    @FormUrlEncoded
    Call<Object> VERIFY_OTP (@Field("mobile") String mobile , @Field("otp") String otp);

    @POST("resend_otp")
    @FormUrlEncoded
    Call<Object> RESEND_OTP (@Field("mobile") String mobile);

    @POST("forget_pass_otp_send")
    @FormUrlEncoded
    Call<Object> FORGOT_PASSWORD (@Field("mobile") String mobile);

    @POST("forget_otp_verify")
    @FormUrlEncoded
    Call<Object> FORGOT_PASS_OTP_SEND (@Field("mobile") String mobile , @Field("otp") String otp , @Field("password") String password );

    @POST("get_products")
    @FormUrlEncoded
    Call<JsonObject> PRODUCT_LIST (@Field("brand_id") String brand_id , @Field("retailer_id") String retailer_id , @Field("locality_id") String locality_id );


    @POST("get_products")
    @FormUrlEncoded
    Call<ProductList> PRODUCT_LIST1 (@Field("brand_id") String brand_id , @Field("retailer_id") String retailer_id , @Field("locality_id") String locality_id );


    @POST("buy_products")
    Call<Object> BuyProduct(@Body JsonObject jsonObject);


    @POST("my_orders")
    @FormUrlEncoded
    Call<OrderStatusList> ORDER_STATUS_LIST_CALL(@Field("ret_id") String ret_id  , @Field("start_date") String start_date, @Field("end_date") String end_date );

    @POST("product_gallary")
    @FormUrlEncoded
    Call<ProductGalleryList> GetGallery (@Field("product_id") String product_id );

    @POST("profile_update")
    @Multipart
    Call<Object> PROFILE_UPDATE (@Part MultipartBody.Part img_url , @Part("id") RequestBody id , @Part("name") RequestBody name , @Part("email") RequestBody email, @Part("locality") RequestBody locality
            , @Part("governate") RequestBody governate, @Part("address") RequestBody address, @Part("outlet_name") RequestBody outlet_name, @Part("locality_id") RequestBody locality_id
            , @Part("locality_ar") RequestBody locality_ar  );

    @POST("warranty_check_nonseries")
    @Multipart
    Call<Object> WARRANTY_NO_SERIALIZED (@Part MultipartBody.Part img_url ,  @Part("retailer_id") RequestBody retailer_id , @Part("retailer_name") RequestBody retailer_name
            , @Part("date_sale") RequestBody date_sale, @Part("des") RequestBody des);

    @POST("warranty_check_series")
    @FormUrlEncoded
    Call<Object> WARRANTY_CHECK (@Field("retailer_id") String retailer_id , @Field("imei") String imei );

    @POST("warranty_check_series_submit")
    Call<Object> SUBMIT_SERI(@Body JsonObject jsonObject);


}



























