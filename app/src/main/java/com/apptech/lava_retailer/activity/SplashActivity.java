package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.other.SessionManage;
import com.google.firebase.messaging.FirebaseMessaging;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    SessionManage sessionManage;
    private static final String TAG = "SplashActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManage = SessionManage.getInstance(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getSupportActionBar().hide();

        /*
         * Firebase
         */


        FirebaseMessaging.getInstance().subscribeToTopic("global").addOnCompleteListener(task -> {

            String msg = "Successfull";
            if (!task.isSuccessful()) {
                msg = "Failed";
            }
            Log.e(TAG, "onComplete: " + msg);
        });


        if (sessionManage.getUserDetails().get("LANGUAGE") == null) {
            sessionManage.setlanguage("en");
        }

        new Handler().postDelayed(() -> {

            if (sessionManage.getUserDetails().get("FIRST_TIME_LANGUAGE") != null) {
                Intent i = new Intent(SplashActivity.this, MessageShowActivity.class);
                startActivity(i);
                finish();
                return;
            }
            if (sessionManage.getUserDetails().get("PROFILE_VERIFICATION") != null) {
                Intent i = new Intent(SplashActivity.this, ClientDatashowActivity.class);
                startActivity(i);
                finish();
                return;
            }
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);


    }


}