package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.adapter.BrandsAdapter;
import com.apptech.myapplication.databinding.ActivityBrandBinding;
import com.apptech.myapplication.modal.brand.BrandList;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandActivity extends AppCompatActivity {

    ActivityBrandBinding binding;
    private static final String TAG = "BrandActivity";
    SessionManage sessionManage;
    LavaInterface lavaInterface;
    BrandsAdapter.BrandInterfaces brandInterfaces;

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
        lavaInterface.Brand().enqueue(new Callback<BrandList>() {
            @Override
            public void onResponse(Call<BrandList> call, Response<BrandList> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    if (!response.body().getError()) {

                        binding.BrandRecyclerView.setAdapter(new BrandsAdapter(response.body().getList() , brandInterfaces));
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(BrandActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }

                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<BrandList> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }


}




































