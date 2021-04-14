package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.R;
import com.apptech.myapplication.other.SessionManage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    SessionManage sessionManage;
    private static final String TAG = "SplashActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManage = SessionManage.getInstance(this);

        /*
         * Firebase
         */


        FirebaseMessaging.getInstance().subscribeToTopic("global").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

                String msg = "Successfull";
                if (!task.isSuccessful()) {
                    msg = "Failed";
                }
                Log.e(TAG, "onComplete: " + msg);
            }
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
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);


    }


}