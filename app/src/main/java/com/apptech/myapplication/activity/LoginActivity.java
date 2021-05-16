package com.apptech.myapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.ActivityLoginBinding;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.NetworkCheck;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";
    LavaInterface lavaInterface;
    boolean showPsw = true;
    SessionManage sessionManage;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean REMEMBER_ME = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openCamera();

        sessionManage = SessionManage.getInstance(this);

        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "ar");
        } else {
            new LanguageChange(this, "en");
        }


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        lavaInterface = ApiClient.getClient().create(LavaInterface.class);



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
        binding.PasswordInputLayout.setBoxStrokeColorStateList(myColorList);


        binding.PasswordInputLayout.setStartIconDrawable(R.drawable.ic_baseline_lock_24);
        binding.PasswordInputLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.login_icon_color));


        binding.Login.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {

                if (NumberCheck(binding.NumberInputLayout.getText().toString().trim()) && PasswordCheck(binding.PasswordInputLayout.getEditText().getText().toString().trim())) {
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

/*        binding.PasswordInputLayout.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                binding.PasswordAnimsation.animate().scaleX(0.8f).setDuration(600);
                binding.PasswordAnimsation.animate().scaleY(0.8f).withEndAction(() -> {
                    binding.PasswordAnimsation.animate().scaleX(1.0f).setDuration(600);
                    binding.PasswordAnimsation.animate().scaleY(1.0f).setDuration(600);
                }).setDuration(300);
            }
        });*/

        binding.SignUpBtn.setOnClickListener(v -> startActivity(new Intent(this, SocialActivity.class)));

/*        binding.passwordshow.setOnClickListener(v -> {
            if (showPsw) {
                binding.PasswordInputLayout.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                binding.PasswordInputLayout.setInputType(InputType.TYPE_CLASS_TEXT);
                showPsw = false;
            } else {
                binding.PasswordInputLayout.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                binding.PasswordInputLayout.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPsw = true;
            }
        });*/


        if(sessionManage.getUserDetails().get("REMEMBER_MOB") != null && sessionManage.getUserDetails().get("REMEMBER_PSW") != null ){
            binding.NumberInputLayout.setText(sessionManage.getUserDetails().get("REMEMBER_MOB"));
            binding.etPassword.setText(sessionManage.getUserDetails().get("REMEMBER_PSW"));
        }

        binding.SelectLanguage.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE);
            PopupMenu popupMenu = new PopupMenu(wrapper, binding.SelectLanguage);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("English")) {
                    sessionManage.setlanguage("en");
                } else {
                    sessionManage.setlanguage("ar");
                }
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            });


            popupMenu.show();//showing popup menu

        });


        binding.NumberInputLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.NumberInputLayoutError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                binding.PasswordInputLayoutError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            REMEMBER_ME = isChecked;
        });

    }

    private void SignIn() {

        binding.progressbar.setVisibility(View.VISIBLE);

        Call call = lavaInterface.Login(binding.NumberInputLayout.getText().toString().trim(), binding.PasswordInputLayout.getEditText().getText().toString().trim());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                    Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");


                        if (error.equalsIgnoreCase("false")) {

                            String user_detail = jsonObject.getString("user_detail");
                            JSONObject jsonObject1 = new JSONObject(user_detail);

                            if(jsonObject1.optString("name").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.name_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("email").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.email_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("mobile").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.phone_number_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

//                            if(jsonObject1.optString("user_type").isEmpty()){
//                                ErrorDilaog("Pleace mobile");
//                                binding.progressbar.setVisibility(View.GONE);
//                                return;
//                            }

                            if(jsonObject1.optString("governate").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.governate__fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("locality").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.locality_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("locality_ar").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.locality_ar_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("address").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.address_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("locality_id").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.locality_id_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("outlet_name").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.locality_id_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }


                            sessionManage.UserDetail(jsonObject1.getString("id"),
                                    jsonObject1.getString("name"),
                                    jsonObject1.getString("email"),
                                    jsonObject1.getString("mobile"),
                                    jsonObject1.optString("user_type"),
                                    jsonObject1.optString("password"),
                                    jsonObject1.getString("governate"),
                                    jsonObject1.getString("locality_ar"),
                                    jsonObject1.getString("locality"),
                                    jsonObject1.optString("time"),
                                    jsonObject1.getString("address"),
                                    jsonObject1.getString("locality_id"),
                                    jsonObject1.getString("outlet_name"),
                                    ""
                            );

                            if (REMEMBER_ME){
                                sessionManage.RememberMe(binding.NumberInputLayout.getText().toString().trim() , binding.PasswordInputLayout.getEditText().getText().toString().trim());
                            }


                            startActivity(new Intent(LoginActivity.this, MessageShowActivity.class));
                            sessionManage.FirstTimeLanguage("true");
                            finish();
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        binding.progressbar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });

    }




    private String optString_1(final JSONObject json, final String key) {
        return json.optString(key).isEmpty() ? "" : json.optString(key);
    }

    private boolean NumberCheck(String number) {
        if (number.isEmpty()) {
//            Toast.makeText(this, "number empty", Toast.LENGTH_SHORT).show();
            binding.NumberInputLayout.setError(getResources().getString(R.string.field_required));
            binding.NumberInputLayoutError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.NumberInputLayout.setError(null);
        binding.NumberInputLayoutError.setVisibility(View.GONE);
        return true;
    }

    private boolean PasswordCheck(String psw) {
        if (psw.isEmpty()) {
//            Toast.makeText(this, "pasw empty", Toast.LENGTH_SHORT).show();
            binding.PasswordInputLayout.setError(getResources().getString(R.string.field_required));
//            binding.PasswordInputLayoutError.setVisibility(View.VISIBLE);
            return false;
        } else if (psw.length() <= 6) {
//            Toast.makeText(this, "pasw 6 small", Toast.LENGTH_SHORT).show();
            binding.PasswordInputLayout.setError(getResources().getString(R.string.psw_short));
//            binding.PasswordInputLayoutError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.PasswordInputLayout.setError(null);
//        binding.PasswordInputLayoutError.setVisibility(View.GONE);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    @AfterPermissionGranted(123)
    private void openCamera() {
        String[] perms = {Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Opening camera", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                    123, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {
        Log.e(TAG, "onPermissionsGranted: ");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {

        Log.e(TAG, "onPermissionsDenied: ");

//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this).build().show();
//        }
    }

    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> {
                doubleBackToExitPressedOnce = false;
            }, 2000);

        } else {
            super.onBackPressed();
            return;
        }
    }




    private void ErrorDilaog(String errormsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error Message");
        builder.setMessage(errormsg);
//        builder.setNegativeButton("" , (dialog, which) -> {
//            dialog.dismiss();
//        });
        builder.setPositiveButton("Close" , (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }




}




























