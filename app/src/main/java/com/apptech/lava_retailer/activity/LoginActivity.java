package com.apptech.lava_retailer.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ActivityLoginBinding;
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    List<Country_list> countryLists = new ArrayList<>();
    ProgressDialog progressDialog;
    PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openCamera();

        sessionManage = SessionManage.getInstance(this);

        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "en");
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            new LanguageChange(this, "fr");
        } else {
            new LanguageChange(this, "ar");
        }




        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        DialogProgress();

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
        binding.NumberInputLayout.setBoxStrokeColorStateList(myColorList);


        binding.PasswordInputLayout.setStartIconDrawable(R.drawable.ic_baseline_lock_24);
        binding.PasswordInputLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.login_icon_color));
        binding.NumberInputLayout.setStartIconDrawable(R.drawable.ic_baseline_phone_iphone_24);
        binding.NumberInputLayout.setStartIconTintList(ContextCompat.getColorStateList(this,R.color.login_icon_color));


        binding.Login.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {

                if (NumberCheck(binding.NumberInputLayout.getEditText().getText().toString().trim()) && PasswordCheck(binding.PasswordInputLayout.getEditText().getText().toString().trim())) {
                    SignIn();
                }

            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }

        });
        binding.forgotBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
        });


        binding.SignUpBtn.setOnClickListener(v -> startActivity(new Intent(this, SocialActivity.class)));


        if(sessionManage.getUserDetails().get("REMEMBER_MOB") != null && sessionManage.getUserDetails().get("REMEMBER_PSW") != null ){
            binding.NumberInputLayout.getEditText().setText(sessionManage.getUserDetails().get("REMEMBER_MOB"));
            binding.etPassword.setText(sessionManage.getUserDetails().get("REMEMBER_PSW"));
        }

        binding.SelectLanguage.setOnClickListener(v -> {
            Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE);
            PopupMenu popupMenu = new PopupMenu(wrapper, binding.SelectLanguage);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("English")) {
                    sessionManage.setlanguage("en");
                } else if (item.getTitle().equals("French")){
                    sessionManage.setlanguage("fr");
                }else {
                    sessionManage.setlanguage("ar");
                }
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            });
            popupMenu.show();//showing popup menu
        });

        if(new NetworkCheck().haveNetworkConnection(this)){
            Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE);
            popupMenu = new PopupMenu(wrapper, binding.SelectCountry);
            getCountry();
        }else {
            Toast.makeText(this, "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

        if (sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME") != null){
            binding.countryName.setText(sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME"));
        }

        binding.SelectCountry.setOnClickListener(v -> {
            popupMenu.setOnMenuItemClickListener(item -> {

                int pos = item.getGroupId();



//                Log.e(TAG, "onCreate: " +  ""+ countryLists.get(pos).getCurrency_symbol()+"" );
//                Log.e(TAG, "onCreate: " +  ""+countryLists.get(pos).getCurrency_symbol()+"" );


                sessionManage.LOGIN_COUNTRY(String.valueOf(item.getItemId()) , item.getTitle().toString() , countryLists.get(pos).getCurrency()
                        , countryLists.get(pos).getCurrency_symbol());

                binding.countryName.setText(item.getTitle());
                return false;
            });
            popupMenu.show();

        });





        binding.Number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.NumberInputLayout.setError(null);
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
                binding.PasswordInputLayout.setError(null);
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

        Call call = lavaInterface.Login(binding.NumberInputLayout.getEditText().getText().toString().trim(), binding.PasswordInputLayout.getEditText().getText().toString().trim());
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
                            if(jsonObject1.optString("user_unique_id").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.user_unique_id_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

//                            if(jsonObject1.optString("email").isEmpty()){
//                                ErrorDilaog(getResources().getString(R.string.email_fiels_missing));
//                                binding.progressbar.setVisibility(View.GONE);
//                                return;
//                            }

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

                            if(jsonObject1.optString("backend_register").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.backend_register_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("backend_verify").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.backend_verify_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("country_name").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.country_name_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }

                            if(jsonObject1.optString("country_id").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.country_id_fiels_missing));
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }


                            sessionManage.UserDetail(
                                    jsonObject1.getString("id"),
                                    jsonObject1.getString("user_unique_id"),
                                    jsonObject1.getString("name"),
                                    jsonObject1.optString("email"),
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
                                    jsonObject1.optString("img_url"),
                                    jsonObject1.optString("country_name"),
                                    jsonObject1.optString("country_id")
                            );

                            if (REMEMBER_ME){
                                sessionManage.RememberMe(binding.NumberInputLayout.getEditText().getText().toString().trim() , binding.PasswordInputLayout.getEditText().getText().toString().trim());
                            }


                            if(
                                    jsonObject1.getString("backend_register").equalsIgnoreCase("YES") && jsonObject1.getString("backend_verify").equalsIgnoreCase("NO")
                                    || jsonObject1.getString("backend_register").equalsIgnoreCase("NO") && jsonObject1.getString("backend_verify").equalsIgnoreCase("NO")
                            ){
                                sessionManage.PROFILE_VERIFICATION("true");
                                startActivity(new Intent(LoginActivity.this, ClientDatashowActivity.class));
                                return;
                            }

                            startActivity(new Intent(LoginActivity.this, MessageShowActivity.class));
                            sessionManage.FirstTimeLanguage("true");
                            sessionManage.PROFILE_VERIFY_CHECK(jsonObject1.getString("backend_verify"));
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

    private void getCountry(){

        progressDialog.show();

        lavaInterface.Country().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray array = jsonObject.getJSONArray("country_list");

                        for (int i=0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            countryLists.add(new Country_list(
                                    object.getString("id")
                                    ,object.getString("name")
                                    ,object.getString("name_ar")
                                    ,object.optString("name_fr")
                                    ,object.getString("time")
                                    ,object.getString("currency")
                                    ,object.optString("currency_symbol")
                            ));

                            int id = Integer.parseInt(object.getString("id"));

                            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                                case "en":
                                    popupMenu.getMenu().add(i, id, i ,object.getString("name"));
                                    break;
                                case "fr":
                                    if(countryLists.get(0).getName_fr().isEmpty()){
                                        popupMenu.getMenu().add(i, id, i ,object.getString("name"));
                                    }else {
                                        popupMenu.getMenu().add(i, id, i ,object.getString("name_fr"));
                                    }
                                    break;
                                case "ar":
                                    popupMenu.getMenu().add(i, id, i ,object.getString("name_ar"));
                                    break;
                            }


                        }


                        try {
                            if (sessionManage.getUserDetails().get("LOGIN_COUNTRY_NAME") == null){
                                sessionManage.LOGIN_COUNTRY(countryLists.get(0).getId() , countryLists.get(0).getName() ,  countryLists.get(0).getCurrency()  ,  countryLists.get(0).getCurrency_symbol());
                                binding.countryName.setText(countryLists.get(0).getName());
                            }
                        }catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                        progressDialog.cancel();
                        return;
                    }
                    Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(LoginActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                progressDialog.cancel();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.cancel();
            }
        });
    }

    private boolean NumberCheck(String number) {
        if (number.isEmpty()) {
            binding.NumberInputLayout.setError(getResources().getString(R.string.field_required));
            return false;
        }
        binding.NumberInputLayout.setError(null);
        return true;
    }

    private boolean PasswordCheck(String psw) {
        if (psw.isEmpty()) {
            binding.PasswordInputLayout.setError(getResources().getString(R.string.field_required));
            return false;
        } else if (psw.length() <= 6) {
            binding.PasswordInputLayout.setError(getResources().getString(R.string.psw_short));
            return false;
        }
        binding.PasswordInputLayout.setError(null);
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
        }
    }




    private void ErrorDilaog(String errormsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error Message");
        builder.setMessage(errormsg);
        builder.setPositiveButton("Close" , (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void  DialogProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
    }



}




























