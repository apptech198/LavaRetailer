package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ActivityForgotBinding;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {


    ActivityForgotBinding binding;
    LavaInterface lavaInterface;
    private static final String TAG = "ForgotActivity";
    SessionManage sessionManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sessionManage = SessionManage.getInstance(this);


        binding.forgotBtn.setOnClickListener(v -> {
            if(new NetworkCheck().haveNetworkConnection(this)){
                if(validation()){
                    binding.forgotBtn.setEnabled(false);
                    binding.forgotBtn.setClickable(false);
                    ForgotPassword();
                    return;
                }
            }else {
                Toast.makeText(ForgotActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }



        });

        binding.NumberInputLayout.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                binding.NumberAnimation.animate().scaleX(0.8f).setDuration(600);
                binding.NumberAnimation.animate().scaleY(0.8f).withEndAction(() -> {
                    binding.NumberAnimation.animate().scaleX(1.0f).setDuration(600);
                    binding.NumberAnimation.animate().scaleY(1.0f).setDuration(600);
                }).setDuration(300);
            }
        });

        binding.NumberInputLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.NumberInputLayoutError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void ForgotPassword() {

        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        binding.progressbar.setVisibility(View.VISIBLE);

        lavaInterface.FORGOT_PASSWORD(binding.NumberInputLayout.getText().toString().trim() ,sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME) , country_id , country_name ).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("false")) {
                        JSONObject userinfo = jsonObject.getJSONObject("user_info");
                        String otp = jsonObject.optString("otp");
                        startActivity(new Intent(ForgotActivity.this  , ConfirmPasswordActivity.class).putExtra("mob" , binding.NumberInputLayout.getText().toString().trim()).putExtra("otp" , otp));
                        binding.progressbar.setVisibility(View.GONE);

                        binding.forgotBtn.setEnabled(true);
                        binding.forgotBtn.setClickable(true);

                        return;
                    }

                    Toast.makeText(ForgotActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.forgotBtn.setEnabled(true);
                    binding.forgotBtn.setClickable(true);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                binding.progressbar.setVisibility(View.GONE);
                binding.forgotBtn.setEnabled(true);;
                binding.forgotBtn.setClickable(true);
                Toast.makeText(ForgotActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(ForgotActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
                binding.forgotBtn.setEnabled(true);;
                binding.forgotBtn.setClickable(true);
            }
        });
    }

    private boolean validation(){
        return NumberCheck(binding.NumberInputLayout.getText().toString().trim());
    }

    private boolean NumberCheck(String number) {
        if (number.isEmpty()) {
            binding.NumberInputLayout.setError(getResources().getString(R.string.field_required));
            binding.NumberInputLayoutError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.NumberInputLayout.setError(null);
        binding.NumberInputLayoutError.setVisibility(View.GONE);
        return true;
    }



}








































