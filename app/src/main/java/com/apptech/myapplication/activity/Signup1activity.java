package com.apptech.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.ActivitySignup1activityBinding;

public class Signup1activity extends AppCompatActivity {

    ActivitySignup1activityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignup1activityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




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
        binding.etPasswordLayout.setBoxStrokeColorStateList(myColorList);




    }
}