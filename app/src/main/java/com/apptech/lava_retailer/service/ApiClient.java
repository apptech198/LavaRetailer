package com.apptech.lava_retailer.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    public static final String BASE_URL = "http://lavamobilesafrica.com/gbrretail/index.php/app/";
//    public static final String BASE_URL = "http://lavamobilesafrica.com/retailgbr/index.php/app/";

    public static final String BASE_URL = "http://gbrinternational.net/retailgbr/index.php/app/";
    public static final String Image_URL = "http://gbrinternational.net/retailgbr/";
    public static final String YOUTUBE_API_KEY = "AIzaSyAJjnImxDQ3D-gb0lkKeqPsuzMLtx2RW3I";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            OkHttpClient OClient = new OkHttpClient.Builder()
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(OClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
