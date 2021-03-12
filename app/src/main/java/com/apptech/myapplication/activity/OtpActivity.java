package com.apptech.myapplication.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.ActivityOtpBinding;
import com.apptech.myapplication.other.NetworkCheck;

public class OtpActivity extends AppCompatActivity implements TextWatcher  {

    ActivityOtpBinding binding;
    public int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        conuter();
        binding.resendOtpCount.setEnabled(false);
        binding.resendOtpCount.setClickable(false);


        binding.VerifyOtpBtn.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {
                if (otpValidation(binding.OtpInputLayout.getText().toString().trim())) {
                    confirmOtp();
                    VerifyOtp();
                }
            } else {
                Toast.makeText(OtpActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }
        });

//        resend otp

        binding.resendOtpCount.setOnClickListener(v -> {
            ResendOtp();
            conuter();
            binding.resendOtpCount.setEnabled(false);
            binding.resendOtpCount.setClickable(false);
        });

//        pasw confirm check

        binding.ConfirmPsw.setOnClickListener(v -> {

            if (new NetworkCheck().haveNetworkConnection(this)) {
                if (pswvalid(binding.PswLayout.getText().toString().trim())) {

                }
            } else {
                Toast.makeText(OtpActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }
        });


//        binding.ConfirmPswLayout.getEditText().addTextChangedListener(this);


    }

    private boolean MatchsPsw(String psw, String conpsw) {
        if (!psw.equals(conpsw)) {
            binding.ConfirmPswLayout.setError("Password do not match");
//            binding.ConfirmPswLayout.setErrorEnabled(true);
            return false;
        }
        binding.ConfirmPswLayout.setError(null);
//        binding.ConfirmPswLayout.setErrorEnabled(false);
        return true;
    }


    private boolean pswvalid(String psw) {
        if (psw.isEmpty()) {
            binding.PswLayout.setError(getResources().getString(R.string.field_required));
//            binding.PswLayout.setErrorEnabled(true);
            return false;
        } else if (psw.length() <= 6) {
            binding.PswLayout.setError(getResources().getString(R.string.psw_short));
//            binding.PswLayout.setErrorEnabled(true);
            return false;
        }
        binding.PswLayout.setError(null);
//        binding.PswLayout.setErrorEnabled(false);
        return true;
    }


    private void ResendOtp() {

    }

    private void VerifyOtp() {

    }

    private void confirmOtp() {
        binding.OtpInputLayout.setEnabled(false);

        binding.otptitle.setText("Process with your \nOtp");

        binding.VerifyOtpBtn.setVisibility(View.GONE);
        binding.resendOtpCount.setVisibility(View.GONE);
        binding.OtpInputLayout.setVisibility(View.GONE);

        binding.PswLayout.setVisibility(View.VISIBLE);
        binding.ConfirmPswLayout.setVisibility(View.VISIBLE);
        binding.ConfirmPsw.setVisibility(View.VISIBLE);
    }


    private boolean otpValidation(String otp) {
        if (otp.isEmpty()) {
            binding.OtpInputLayout.setError(getResources().getString(R.string.field_required));
//            binding.OtpInputLayout.setErrorEnabled(true);
            return false;
        } else if (otp.length() < 4) {
            binding.OtpInputLayout.setError(getResources().getString(R.string.otp_four_digit));
//            binding.OtpInputLayout.setErrorEnabled(true);
            return false;
        }
        binding.OtpInputLayout.setError(null);
//        binding.OtpInputLayout.setErrorEnabled(false);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        MatchsPsw(binding.PswLayout.getEditText().getText().toString().trim(), binding.ConfirmPswLayout.getEditText().getText().toString().trim());
    }
}




















































