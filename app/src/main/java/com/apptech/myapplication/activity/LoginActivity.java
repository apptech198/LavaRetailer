package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.ActivityLoginBinding;
import com.apptech.myapplication.other.NetworkCheck;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.Login.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {

                if (NumberCheck(binding.NumberInputLayout.getText().toString().trim()) && PasswordCheck(binding.PasswordInputLayout.getText().toString().trim())) {
                    SignIn();
                }

            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }

        });
        binding.forgotBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
        });

        binding.NumberInputLayout.setOnClickListener(v -> {
//            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_animation);
//            binding.NumberAnimation.startAnimation(animation);

//            AnimationDrawable d = (AnimationDrawable) getResources().getDrawable(R.drawable.ic_baseline_phone_iphone_24);
//            binding.NumberInputLayout.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
//            d.start();


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

        binding.PasswordInputLayout.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                binding.PasswordAnimsation.animate().scaleX(0.8f).setDuration(600);
                binding.PasswordAnimsation.animate().scaleY(0.8f).withEndAction(() -> {
                    binding.PasswordAnimsation.animate().scaleX(1.0f).setDuration(600);
                    binding.PasswordAnimsation.animate().scaleY(1.0f).setDuration(600);
                }).setDuration(300);
            }
        });


    }

    private void SignIn() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private boolean NumberCheck(String number) {
        if (number.isEmpty()) {
//            Toast.makeText(this, "number empty", Toast.LENGTH_SHORT).show();
            binding.NumberInputLayout.setError(getResources().getString(R.string.field_required));
//            binding.NumberInputLayout.setErrorEnabled(true);
            return false;
        }
        binding.NumberInputLayout.setError(null);
//        binding.NumberInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean PasswordCheck(String psw) {
        if (psw.isEmpty()) {
//            Toast.makeText(this, "pasw empty", Toast.LENGTH_SHORT).show();
            binding.PasswordInputLayout.setError(getResources().getString(R.string.field_required));
//            binding.PasswordInputLayout.setErrorEnabled(true);
            return false;
        } else if (psw.length() <= 6) {
//            Toast.makeText(this, "pasw 6 small", Toast.LENGTH_SHORT).show();
            binding.PasswordInputLayout.setError(getResources().getString(R.string.psw_short));
//            binding.PasswordInputLayout.setErrorEnabled(true);
            return false;
        }
        binding.PasswordInputLayout.setError(null);
//        binding.PasswordInputLayout.setErrorEnabled(false);
        return true;
    }


}




























