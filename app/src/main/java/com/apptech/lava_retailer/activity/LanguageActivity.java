package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.databinding.ActivityLanguageBinding;
import com.apptech.lava_retailer.other.SessionManage;

public class LanguageActivity extends AppCompatActivity {

    ActivityLanguageBinding binding;
    SessionManage sessionManage;
    private static final String TAG = "LanguageActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sessionManage = SessionManage.getInstance(this);



        binding.RadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            Log.e(TAG, "onCreate: "+  radioButton.getTag());
            if (radioButton.getTag().equals("ENGLISH")) {
                sessionManage.setlanguage("en");
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            sessionManage.setlanguage("ar");
            startActivity(new Intent(this, LoginActivity.class));

        });


    }
}