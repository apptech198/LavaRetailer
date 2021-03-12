package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.databinding.ActivityForgotBinding;

public class ForgotActivity extends AppCompatActivity {


    ActivityForgotBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.forgotBtn.setOnClickListener(v -> {
            startActivity(new Intent(ForgotActivity.this, OtpActivity.class));
        });

    }


}








































