package com.apptech.lava_retailer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ActivityConfirmPasswordBinding;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmPasswordActivity extends AppCompatActivity {



    ActivityConfirmPasswordBinding binding;
    LavaInterface lavaInterface;
    private static final String TAG = "ConfirmPasswordActivity";
    String mob = "" , otp ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_focused}, // focused
                new int[] { android.R.attr.state_hovered}, // hovered
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {}  //
        };

        int[] colors = new int[] {
                getResources().getColor(R.color.login_tint_color),
                getResources().getColor(R.color.login_tint_color),
                getResources().getColor(R.color.login_tint_color),
                getResources().getColor(R.color.login_tint_color)
        };

        ColorStateList myColorList = new ColorStateList(states, colors);
        binding.password.setBoxStrokeColorStateList(myColorList);
        binding.Confirmpassword.setBoxStrokeColorStateList(myColorList);

        try {
            Intent intent = getIntent();
            if(intent != null){
                mob = intent.getStringExtra("mob");
                otp = intent.getStringExtra("otp");
                Toast.makeText(this, "" + otp, Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }


        binding.changePsw.setOnClickListener(v -> {
            if(new NetworkCheck().haveNetworkConnection(this)){
                if(validation()){
                    Changepassword();
                    return;
                }
                return;
            }
            Toast.makeText(ConfirmPasswordActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        });



        binding.OtpInputLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.OtpInputLayoutError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.ConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim() , binding.Confirmpassword.getEditText().getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void Changepassword() {
        binding.progressbar.setVisibility(View.VISIBLE);
        lavaInterface.FORGOT_PASS_OTP_SEND(mob , binding.OtpInputLayout.getText().toString().trim(),binding.password.getEditText().getText().toString().trim()).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("false")) {
                        startActivity(new Intent(ConfirmPasswordActivity.this , LoginActivity.class));
                        finish();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(ConfirmPasswordActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ConfirmPasswordActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ConfirmPasswordActivity.this, "Time out", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private boolean validation(){
        return  otpValidation(binding.OtpInputLayout.getText().toString().trim())
                && PasswordCheck(binding.password.getEditText().getText().toString().trim())
                && ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim() , binding.Confirmpassword.getEditText().getText().toString().trim());
    }


    private boolean otpValidation(String otp) {
        if (otp.isEmpty()) {
            binding.OtpInputLayout.setError(getResources().getString(R.string.field_required));
            binding.OtpInputLayoutError.setVisibility(View.VISIBLE);
            binding.OtpInputLayoutError.setText(getResources().getString(R.string.field_required));
            return false;
        } else if (otp.length() < 4) {
            binding.OtpInputLayout.setError(getResources().getString(R.string.otp_four_digit));
            binding.OtpInputLayoutError.setVisibility(View.VISIBLE);
            binding.OtpInputLayoutError.setText(getResources().getString(R.string.otp_four_digit));
            return false;
        }
        binding.OtpInputLayout.setError(null);
        binding.OtpInputLayoutError.setVisibility(View.GONE);
        return true;
    }

    private boolean PasswordCheck(String psw) {
        if (psw.isEmpty()) {
            binding.password.setError(getResources().getString(R.string.field_required));
            return false;
        } else if (psw.length() <= 6) {
            binding.password.setError(getResources().getString(R.string.psw_short));
            return false;
        }
        binding.password.setError(null);
        return true;
    }

    private boolean ConfirmPasswordCheck(String psw , String confirmpass) {
        if (confirmpass.isEmpty()) {
            binding.Confirmpassword.setError(getResources().getString(R.string.field_required));
            return false;
        } else if (confirmpass.length() != psw.length()) {
            binding.Confirmpassword.setError(getResources().getString(R.string.psw_not_match));
            return false;
        } else if (!psw.equalsIgnoreCase(confirmpass)) {
            binding.Confirmpassword.setError(getResources().getString(R.string.psw_not_match));

            return false;
        }
        binding.Confirmpassword.setError(null);
        return true;
    }





}