package com.apptech.lava_retailer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ActivitySignup1activityBinding;

public class Signup1activity extends AppCompatActivity {

    ActivitySignup1activityBinding binding;

    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;

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

        SGD = new ScaleGestureDetector(this, new ScaleListener());



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SGD.onTouchEvent(event);
        return true;

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f , Math.min(scale , 5f));
            matrix.setScale(scale ,scale);
            binding.image.setImageMatrix(matrix);
            return true;
        }
    }






}