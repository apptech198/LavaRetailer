package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ActivityOtpBinding;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.service.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity   {

    ActivityOtpBinding binding;
    public int counter;
    LavaInterface lavaInterface;
    private static final String TAG = "OtpActivity";
    String mob ="", otp ="";
    SessionManage sessionManage;
    SmsBroadcastReceiver smsBroadcastReceiver;
    int REQ_USER_CONSENT  = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//            binding.OtpInputLayout.setGravity(Gravity.END);
//        } else {
//            binding.OtpInputLayout.setGravity(Gravity.START);
//        }

        try {
            Intent intent = getIntent();
            if(intent != null){
                mob = getIntent().getStringExtra("mobile");
                otp = getIntent().getStringExtra("otp");
                Toast.makeText(this, ""+ otp, Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "onCreate: " + e.getMessage());
        }

        startSmsUserConsent();

        conuter();
        binding.resendOtpCount.setEnabled(false);
        binding.resendOtpCount.setClickable(false);


        binding.VerifyOtpBtn.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {
                if (otpValidation(binding.OtpInputLayout.getText().toString().trim())) {
                    VerifyOtp();
                }
            } else {
                Toast.makeText(OtpActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }
        });

        binding.resendOtpCount.setOnClickListener(v -> {
            ResendOtp();
            conuter();
            binding.resendOtpCount.setEnabled(false);
            binding.resendOtpCount.setClickable(false);
        });


    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void ResendOtp() {

        binding.progressbar.setVisibility(View.VISIBLE);
        lavaInterface.RESEND_OTP(mob , sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME")).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("false")) {
                        JSONObject js = jsonObject.getJSONObject("user_info");
                        Toast.makeText(OtpActivity.this, "" + js.getString("login_otp"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(OtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(OtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(OtpActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(OtpActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void VerifyOtp() {

        binding.progressbar.setVisibility(View.VISIBLE);

        lavaInterface.VERIFY_OTP(mob ,binding.OtpInputLayout.getText().toString().trim()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                        if (error.equalsIgnoreCase("false")) {

                            startActivity(new Intent(OtpActivity.this , SignUpActivity.class).putExtra("MOB" , mob).putExtra("TYPE" , "OTP"));
                            Toast.makeText(OtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }

                    Toast.makeText(OtpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                    } catch (JSONException e) {
                    e.printStackTrace();
                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(OtpActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(OtpActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });

    }



    private boolean otpValidation(String otp) {
        if (otp.isEmpty()) {
            binding.OtpInputLayout.setError(getResources().getString(R.string.field_required));
            return false;
        } else if (otp.length() < 4) {
            binding.OtpInputLayout.setError(getResources().getString(R.string.otp_four_digit));
            return false;
        }
        binding.OtpInputLayout.setError(null);
        return true;
    }


    private void conuter() {
        new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                //txt_counter.setText(String.valueOf(counter));
                binding.countdown.setVisibility(View.VISIBLE);
                binding.countdown.setText(String.valueOf(l / 1000));
                counter++;
                //Toast.makeText(CheckOtp.this, "Please Enter 4 digit OtP", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {

                binding.countdown.setVisibility(View.GONE);
                binding.resendOtpCount.setEnabled(true);
                binding.resendOtpCount.setClickable(true);

            }
        }.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "onActivityResult: " + message);
//                textViewMessage.setText(String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String otp = matcher.group(0);
//            Log.e(TAG, "getOtpFromMessage: " +  matcher.group(0));
//            Toast.makeText(this, "" + matcher.group(0), Toast.LENGTH_SHORT).show();
//            otpText.setText(matcher.group(0));
            binding.OtpInputLayout.setText(otp);
            VerifyOtp();

        }
    }


    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }


}




















































