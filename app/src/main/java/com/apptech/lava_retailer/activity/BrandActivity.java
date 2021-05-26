package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.BrandsAdapter;
import com.apptech.lava_retailer.databinding.ActivityBrandBinding;
import com.apptech.lava_retailer.list.brand.Brandlist;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandActivity extends AppCompatActivity {

    ActivityBrandBinding binding;
    private static final String TAG = "BrandActivity";
    SessionManage sessionManage;
    LavaInterface lavaInterface;
    BrandsAdapter.BrandInterfaces brandInterfaces;
    List<Brandlist> brandlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManage = SessionManage.getInstance(this);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);


        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "ar");
        } else {
            new LanguageChange(this, "en");
        }

        binding = ActivityBrandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        brand();

        brandInterfaces = (list , text  , text_ar) -> {
            sessionManage.brandSelect(list.getId() , text , text_ar);
            startActivity(new Intent(BrandActivity.this, MainActivity.class));
            finish();
        };

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    void brand() {

        lavaInterface.Brand().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if(error.equalsIgnoreCase("false")){

                        JSONArray array = jsonObject.getJSONArray("list");
                        if(array.length() > 0){

                            for (int i=0 ; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                brandlists.add(new Brandlist(object.optString("id")
                                        , object.optString("name")
                                        ,object.optString("time")
                                        ,object.optString("name_ar")
                                        , object.optString("img")
                                ));

                                try {
                                    binding.BrandRecyclerView.setAdapter(new BrandsAdapter(brandlists , brandInterfaces));
                                    binding.progressbar.setVisibility(View.GONE);
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                    Log.e(TAG, "onResponse: " + e.getMessage() );
                                }
                            }
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(BrandActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(BrandActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


}




































