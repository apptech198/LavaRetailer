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

        binding.NumberInputLayout.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                binding.NumberAnimation.animate().scaleX(0.8f).setDuration(600);
                binding.NumberAnimation.animate().scaleY(0.8f).withEndAction(() -> {
                    binding.NumberAnimation.animate().scaleX(1.0f).setDuration(600);
                    binding.NumberAnimation.animate().scaleY(1.0f).setDuration(600);
                }).setDuration(300);
            }
        });

    }


}








































