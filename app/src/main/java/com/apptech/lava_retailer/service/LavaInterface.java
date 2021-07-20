package com.apptech.lava_retailer.service;

import com.apptech.lava_retailer.list.notificationList.NotificationModel;
import com.apptech.lava_retailer.list.pending_warranty.PendingWarrentyList;
import com.apptech.lava_retailer.modal.message.NotificationListBrandWise;
import com.apptech.lava_retailer.modal.order_statusList.OrderStatusList;
import com.apptech.lava_retailer.modal.product.ProductList;
import com.apptech.lava_retailer.modal.productgallery.ProductGalleryList;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface LavaInterface{

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
                @Field("locality_ar") String locality_ar,
                @Field("governate_id") String governate_id ,
                @Field("country_id") String country_id
             , @Field("country_name") String country_name
            );

    @POST("login")
    @FormUrlEncoded
    Call<Object> Login(@Field("mobile") String mobile, @Field("password") String password , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("brand_list")
    @FormUrlEncoded
    Call<Object> Brand(@Field("country_name") String mobile , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("country")
    Call<Object> Country();

    @POST("passbook_filter")
    @FormUrlEncoded
    Call<Object> GETCLAIMTYPE(@Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("passbook_list")
    @FormUrlEncoded
    Call<Object> GetPASSBOOK(@Field("start_date") String mobile,@Field("end_date") String mobile1 , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("governate")
    @FormUrlEncoded
    Call<Object> Governate(@Field("lang") String lang , @Field("Country_name") String Country_name , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("city")
    @FormUrlEncoded
    Call<Object> getcity(@Field("lang") String lang, @Field("governate") String governate , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("locality")
    @FormUrlEncoded
    Call<Object> getlocality(@Field("lang") String lang, @Field("governate_id") String governate_id , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("sale_sellout_imei")
    Call<Object> SELL_OUT_IMEI(@Body JsonObject imei);

    @POST("pricedrop_imei")
    Call<Object> PRICE_DROP_IMEI(@Body JsonObject imei);

    @POST("pricedrop_imei_list_pending_block")
    @FormUrlEncoded
    Call<SellOutPendingVerificationList> PRICE_DROP_IMEI_LIST(@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("warranty_pending_list")
    @FormUrlEncoded
    Call<PendingWarrentyList> WARRANTY_PENDING (@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date , @Field("country_id") String country_id ,  @Field("country_name") String country_name);


    @POST("accesories_warranty_list")
    @FormUrlEncoded
    Call<PendingWarrentyList> WARRANTY_PENDING_ACCESSORIES (@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("sellout_imei_list_pending_block")
    @FormUrlEncoded
    Call<Object> SELL_OUT_IMEI_LIST(@Field("retailer_id") String retailer_id, @Field("start_date") String start_date, @Field("end_date") String end_date , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("notification_list_latest")
    Call<Object> NotificationList();

    @POST("notification_list_latest")
    @FormUrlEncoded
    Call<NotificationModel> NotificationList1(@Field("country_id") String country_id ,  @Field("country_name") String country_name);


    @POST("notification_list_brand_wise")
    @FormUrlEncoded
    Call<NotificationListBrandWise> NotificationListBrandWise(@Field("brand_id") String brand_id , @Field("country_id") String country_id
        , @Field("start_date") String start_date, @Field("end_date") String end_date ,   @Field("country_name") String country_name );

    @POST("send_otp")
    @FormUrlEncoded
    Call<Object> SEND_OTP (@Field("mobile") String mobile , @Field("country") String country , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("otp_verify")
    @FormUrlEncoded
    Call<Object> VERIFY_OTP (@Field("mobile") String mobile , @Field("otp") String otp , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("resend_otp")
    @FormUrlEncoded
    Call<Object> RESEND_OTP (@Field("mobile") String mobile , @Field("country") String country , @Field("country_id") String country_id ,  @Field("country_name") String country_name );

    @POST("forget_pass_otp_send")
    @FormUrlEncoded
    Call<Object> FORGOT_PASSWORD (@Field("mobile") String mobile , @Field("country") String country , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("forget_otp_verify")
    @FormUrlEncoded
    Call<Object> FORGOT_PASS_OTP_SEND (@Field("mobile") String mobile , @Field("otp") String otp , @Field("password") String password
    , @Field("country_id") String country_id ,  @Field("country_name") String country_name);


    @POST("get_products")
    @FormUrlEncoded
    Call<JsonObject> PRODUCT_LIST (@Field("brand_id") String brand_id , @Field("retailer_id") String retailer_id , @Field("locality_id") String locality_id );

    @POST("get_products")
    @FormUrlEncoded
    Call<Object> PRODUCT_LIST2 (@Field("brand_id") String brand_id , @Field("retailer_id") String retailer_id , @Field("locality_id") String locality_id
            , @Field("country_id") String country_id ,  @Field("country_name") String country_name);

    @POST("get_products")
    @FormUrlEncoded
    Call<ProductList> PRODUCT_LIST1 (@Field("brand_id") String brand_id , @Field("retailer_id") String retailer_id , @Field("locality_id") String locality_id );

    @POST("buy_products")
    Call<Object> BuyProduct(@Body JsonObject jsonObject);

    @POST("my_orders")
    @FormUrlEncoded
    Call<OrderStatusList> ORDER_STATUS_LIST_CALL(@Field("ret_id") String ret_id  , @Field("start_date") String start_date, @Field("end_date") String end_date , @Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("product_gallary")
    @FormUrlEncoded
    Call<ProductGalleryList> GetGallery (@Field("product_id") String product_id , @Field("country_id") String country_id  , @Field("country_name") String country_name  );

    @POST("profile_update")
    @Multipart
    Call<Object> PROFILE_UPDATE (@Part MultipartBody.Part img_url , @Part("id") RequestBody id , @Part("name") RequestBody name
        , @Part("email") RequestBody email, @Part("locality") RequestBody locality
        , @Part("governate") RequestBody governate, @Part("address") RequestBody address, @Part("outlet_name") RequestBody outlet_name
        , @Part("locality_id") RequestBody locality_id
        , @Part("locality_ar") RequestBody locality_ar
        , @Part("country_id") RequestBody country_id
        , @Part("country_name") RequestBody country_name
    );

    @POST("profile_update_first_time")
    @Multipart
    Call<Object> PROFILE_UPDATE_FIRST_TIME (
        @Part MultipartBody.Part img_url , @Part("id") RequestBody id , @Part("name") RequestBody name , @Part("email") RequestBody email, @Part("locality") RequestBody locality
            , @Part("governate") RequestBody governate, @Part("address") RequestBody address, @Part("outlet_name") RequestBody outlet_name, @Part("locality_id") RequestBody locality_id
            , @Part("locality_ar") RequestBody locality_ar
            , @Part("country_id") RequestBody country_id
            , @Part("password") RequestBody password
            , @Part("number") RequestBody number
            , @Part("country_name") RequestBody country_name
    );

    @POST("warranty_check_nonseries")
    @Multipart
    Call<Object> WARRANTY_NO_SERIALIZED (@Part MultipartBody.Part img_url , @Part("retailer_id") RequestBody retailer_id , @Part("retailer_name") RequestBody retailer_name
        , @Part("date_sale") RequestBody date_sale, @Part("des") RequestBody des);

    @POST("accesories_replacement_warranty")
    @Multipart
    Call<Object> ACCESORIES_REPLACEMENT_WARRENTY (@Part MultipartBody.Part img_url
            , @Part("retailer_id") RequestBody sell_date
            , @Part("retailer_name") RequestBody type
            , @Part("locality_id") RequestBody srno
            , @Part("locality_name") RequestBody retailer_id
            , @Part("srno") RequestBody retailer_name
            , @Part("sell_date") RequestBody locality_id
            , @Part("type") RequestBody locality_name
            , @Part("replacment_date") RequestBody country_id
            , @Part("original_srno") RequestBody country_name
            , @Part("country_name") RequestBody country_named
            , @Part("item_name") RequestBody item_name
            , @Part("country_id") RequestBody country_idg );

    @POST("warranty_check_series")
    @FormUrlEncoded
    Call<Object> WARRANTY_CHECK (@Field("retailer_id") String retailer_id , @Field("imei") String imei );

    @POST("warranty_check_series_submit")
    Call<Object> SUBMIT_SERI(@Body JsonObject jsonObject);

    @POST("check_imei")
    @FormUrlEncoded
    Call<Object> IMEI_CHECK(@Field("imei") String imei , @Field("ret_id") String ret_id,@Field("country_name") String country_name,@Field("country_id") String country_id);

    @POST("accespries_warranty_exist_check")
    @FormUrlEncoded
    Call<Object> IMEI_CHECK_SERIAL(@Field("srno") String imei , @Field("retailer_id") String ret_id,@Field("country_name") String country_name,@Field("country_id") String country_id);

    @POST("price_drop_check_imei")
    @FormUrlEncoded
    Call<Object> PROOCE_DROP_IMEI_CHECK(@Field("imei") String imei , @Field("ret_id") String ret_id , @Field("start_date") String start_date , @Field("end_date") String end_date , @Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("profile_detail")
    @FormUrlEncoded
    Call<Object> PROFILE_DETAILS(@Field("user_id") String user_id , @Field("country_id") String country_id  , @Field("country_name") String country_name);

    @POST("warranty_check")
    @FormUrlEncoded
    Call<Object> WARRENTYCHECK(@FieldMap Map<String,String> params);

    @POST("accespries_warranty_check")
    @FormUrlEncoded
    Call<Object> ACCESORIES_WARENTY_CHECK(@FieldMap Map<String,String> params);

    @POST("send_otp_auth")
    @FormUrlEncoded
    Call<Object> SEND_OTP_AUTH(@Field("mobile") String mobile , @Field("country") String country , @Field("country_id") String country_id  , @Field("country_name") String country_name);

    @POST("trading_scheme_category")
    @FormUrlEncoded
    Call<Object> TAB(@Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("trading_scheme_list")
    @FormUrlEncoded
    Call<Object> GETTRADEDATALIST(@FieldMap Map<String,String> params);

    @POST("replacement_warranty")
    @Multipart
//    Call<Object> REPLACEMENT_WARENTY(@FieldMap Map<String,String> params);
    Call<Object> REPLACEMENT_WARENTY(@Part MultipartBody.Part img_url
            , @Part("country_name") RequestBody country_name
            , @Part("country_id") RequestBody country_id
            , @Part("retailer_id") RequestBody retailer_id
            , @Part("retailer_name") RequestBody retailer_name
            , @Part("locality_id") RequestBody locality_id
            , @Part("locality_name") RequestBody locality_name
            , @Part("imei") RequestBody imei
            , @Part("sell_date") RequestBody sell_date
            , @Part("handest_replace") RequestBody handest_replace
            , @Part("item_name") RequestBody item_name
            , @Part("imei_original") RequestBody imei_original
            , @Part("additional_accessories") RequestBody additional_accessories
            , @Part("warranty_end_date") RequestBody warranty_end_date
    );

    @POST("comodity_list")
    @FormUrlEncoded
    Call<Object> GRT_COMODITY(@Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("sellout_report_filters")
    @FormUrlEncoded
    Call<Object> SELL_OUT_CATEGORY_MODAL_FILTER(@Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("sellout_report")
    @FormUrlEncoded
    Call<Object> SELLOUT_REPORT(@Field("retailer_id") String retailer_id , @Field("start_date") String start_date
            , @Field("end_date") String end_date , @Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("pricedrop_report")
    @FormUrlEncoded
    Call<Object> PRICE_DROP_REPORT(@Field("retailer_id") String retailer_id , @Field("start_date") String start_date , @Field("end_date") String end_date
            , @Field("ano_start_date") String start_dates , @Field("ano_end_date") String end_dates , @Field("country_id") String country_id  , @Field("country_name") String country_name
    );

    @POST("price_drop_annoucment")
    @FormUrlEncoded
    Call<Object> GetAnnounceList(@Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("order_cancel")
    @FormUrlEncoded
    Call<Object> CENCEL_ORDER(@Field("id") String id , @Field("country_id") String country_id  , @Field("country_name") String country_name );

    @POST("email_check")
    @FormUrlEncoded
    Call<Object> EMAIL_CHECK(@Field("email") String email , @Field("country_id") String country_id  , @Field("country_name") String country_name );

    @GET("version_control")
    Call<Object> fetch_version();

}



























