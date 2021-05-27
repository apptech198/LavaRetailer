package com.apptech.lava_retailer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.CountryAdapter;
import com.apptech.lava_retailer.adapter.GovernateAdapter;
import com.apptech.lava_retailer.adapter.LocalityAdapter;
import com.apptech.lava_retailer.databinding.ActivitySignUpBinding;
import com.apptech.lava_retailer.list.LocalityList;
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.list.governate.GovernateList;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements TextWatcher {

    ActivitySignUpBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    private static final String TAG = "SignUpActivity";
    List<GovernateList> governatelist = new ArrayList<>();
    List<String> citylist = new ArrayList<>();
    List<LocalityList> localityList = new ArrayList<>();
    String CountryName = "" , GovernateSelect = "", CitySelect = "", Locality = "" , Locality_id ="" , Locality_ar = "";
    String Languages = "EN";
    String signup_type ="" , social_auth_token = "" , MOB = "";
    List<Country_list> countryLists = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManage = SessionManage.getInstance(this);
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "en");
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            new LanguageChange(this, "fr");
        } else {
            new LanguageChange(this, "ar");
        }


        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            Languages = "EN";
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            Languages = "FR";
        }  else {
            Languages = "AR";
        }


        MOB = getIntent().getStringExtra("MOB");
        signup_type = getIntent().getStringExtra("TYPE");

        try {
            Intent intent = getIntent();
            if(intent != null){
                signup_type = intent.getStringExtra("TYPE");
                MOB = intent.getStringExtra("MOB");
               binding.number.setText(MOB);

               switch (signup_type){
                   case "OTP":
                       binding.number.setEnabled(false);
                       break;
               }
//               Toast.makeText(this, ""+ signup_type, Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }


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
        binding.password.setBoxStrokeColorStateList(myColorList);
        binding.Confirmpassword.setBoxStrokeColorStateList(myColorList);



        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


            binding.name.setText(personGivenName);
            binding.email.setText(personEmail);
            binding.email.setEnabled(false);
            social_auth_token = personId;
        }

//        if(getIntent() != null){
//            if(getIntent().getStringExtra("TYPE").equalsIgnoreCase("FACEBOOK")){
//                social_auth_token = getIntent().getStringExtra("UserId");
//            }
//        }


        getCountry();

        binding.Signup.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {
                if (validation()) {
                    SignUp();
                    return;
                }
                return;
            }
            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });

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
                Intent intent = new Intent(new Intent(this, SignUpActivity.class));
                intent.putExtra("MOB" , MOB);
                intent.putExtra("TYPE" , getIntent().getStringExtra("TYPE"));
                startActivity(intent);
                finish();
                return true;
            });
            popupMenu.show();//showing popup menu
        });
        Hideerror();


    }



    private void Hideerror(){
        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.nameError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.numberError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.emailError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                binding.password.setError(null);
//                binding.password.setErrorEnabled(false);
//                ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim() , binding.Confirmpassword.getEditText().getText().toString().trim());
                   PasswordCheck(binding.password.getEditText().getText().toString().trim());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.ConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim() , binding.Confirmpassword.getEditText().getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.outletName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.outletNameError.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.addressError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void SignUp() {

        binding.progressbar.setVisibility(View.VISIBLE);
        Log.e(TAG, "SignUp: " + social_auth_token );


        Log.e(TAG, "SignUp: " + binding.name.getText().toString().trim()  );
        Log.e(TAG, "SignUp: " + binding.number.getText().toString().trim()  );
        Log.e(TAG, "SignUp: " + binding.password.getEditText().getText().toString().trim()  );
        Log.e(TAG, "SignUp: " + binding.email.getText().toString().trim()  );
        Log.e(TAG, "SignUp: " + Locality  );
        Log.e(TAG, "SignUp: " + GovernateSelect  );
        Log.e(TAG, "SignUp: " + binding.address.getText().toString().trim()  );
        Log.e(TAG, "SignUp: " + binding.outletName.getText().toString().trim()  );
        Log.e(TAG, "SignUp: " + Locality_id  );
        Log.e(TAG, "SignUp: " + Locality_ar  );
        Log.e(TAG, "SignUp: " + signup_type  );
        Log.e(TAG, "SignUp: " + social_auth_token  );




        Call call = lavaInterface.Signup(
                binding.name.getText().toString().trim()
                ,binding.number.getText().toString().trim()
                ,binding.password.getEditText().getText().toString().trim()
                ,binding.email.getText().toString().trim()
                ,Locality
                ,GovernateSelect
                ,binding.address.getText().toString().trim()
                ,signup_type
                ,social_auth_token
                ,binding.outletName.getText().toString().trim()
                ,Locality_id
                ,Locality_ar
        );
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                    Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()));

                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if (error.equalsIgnoreCase("false")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("user_detail");



                            if(jsonObject1.optString("name").isEmpty()){
                                ErrorDilaog(getResources().getString(R.string.name_fiels_missing));
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
                                    jsonObject1.getString("country_name"),
                                    jsonObject1.getString("country_id")
                            );

                            startActivity(new Intent(SignUpActivity.this, MessageShowActivity.class));
                            sessionManage.FirstTimeLanguage("true");
                            sessionManage.PROFILE_VERIFY_CHECK(jsonObject1.getString("backend_verify"));
                            finish();
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });





    }

    private void getCountry(){
        countryLists.clear();
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
                            ));
                        }
                        SelectSmaertCountry();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(SignUpActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "Time out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGovernate() {
        governatelist.clear();
        Log.e(TAG, "getGovernate: " + Languages);
        Call call = lavaInterface.Governate(Languages , CountryName);
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
                            JSONArray jsonArray = jsonObject.getJSONArray("governate_list");
                            governatelist.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                governatelist.add(new GovernateList(
                                        json_data.getString("id")
                                        ,json_data.getString("country_id")
                                        ,json_data.getString("country_name")
                                        ,json_data.getString("name")
                                        ,json_data.getString("name_ar")
                                        ,json_data.optString("name_fr")
                                        ,json_data.getString("time")
                                ));
                            }

                            SelectSmartSearchGovernate();
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "Time out", Toast.LENGTH_SHORT).show();
            }
        });


/*        binding.governate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (GOVERNATE) {
                    binding.progressbar.setVisibility(View.VISIBLE);
                    Object item = parent.getItemAtPosition(position);
                    GovernateSelect = item.toString();
                    getcity();

                }
                GOVERNATE = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    private void getcity() {

        citylist.clear();

        Call call = lavaInterface.getcity(Languages, GovernateSelect);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        if (error.equalsIgnoreCase("false")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("city_list");
                            JSONObject objec = jsonArray.getJSONObject(0);
                            Iterator iterator = objec.keys();
                            String key = "";
                            while (iterator.hasNext()) {
                                key = (String) iterator.next();
                                Log.e(TAG, "onResponse: " + key);
                                break;
                            }

                            if (key.equals("city_en")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    citylist.add(json_data.getString("city_en"));
                                }
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    citylist.add(json_data.getString("city_ar"));
                                }
                            }

//                            SelectSmartSearchCity();

                   /*         cityAdapter.addAll(citylist);
                            cityAdapter.add("Select City");
                            binding.city.setAdapter(cityAdapter);
                            binding.city.setSelection(cityAdapter.getCount());


                    */
                            binding.progressbar.setVisibility(View.GONE);


                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressbar.setVisibility(View.GONE);
                    }

                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

/*
        binding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (CITY) {
                    binding.progressbar.setVisibility(View.VISIBLE);
                    Object item = parent.getItemAtPosition(position);
                    CitySelect = item.toString().trim();
                    getLocality();
                }
                CITY = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/


    }

    private void getLocality() {

        localityList.clear();

//        Call call = lavaInterface.getlocality(Languages, CitySelect);
        Call call = lavaInterface.getlocality(Languages, GovernateSelect);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        if (error.equalsIgnoreCase("false")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("locality_list");

                            localityList.clear();

                            for (int i=0; i< jsonArray.length(); i++){

                                JSONObject jo = jsonArray.getJSONObject(i);

                                localityList.add(new LocalityList(
                                        jo.getString("id")
                                        ,jo.getString("governate_id")
                                        ,jo.getString("governate_name")
                                        ,jo.getString("name")
                                        ,jo.getString("name_ar")
                                        ,jo.optString("name_fr")
                                        ,jo.getString("time")
                                ));
                            }


                            SelectSmartSearchLocality();
                            binding.progressbar.setVisibility(View.GONE);

                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressbar.setVisibility(View.GONE);
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
            }
        });


/*        binding.locality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (LOCALITY) {
                    Object item = parent.getItemAtPosition(position);
                    Locality = item.toString().trim();
                }
                LOCALITY = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }



    @Override
    protected void onStart() {
        super.onStart();
//        binding.progressbar.setVisibility(View.VISIBLE);
    }

    boolean validation() {
        if (NameValidation(binding.name.getText().toString().trim())
                && NumberCheck(binding.number.getText().toString().trim())
//                && EmailCheck(binding.email.getText().toString())
                && PasswordCheck(binding.password.getEditText().getText().toString().trim())
                && ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim() , binding.Confirmpassword.getEditText().getText().toString().trim())
                && OutletNameValidation(binding.outletName.getText().toString().trim())
                && AddressCheck(binding.address.getText().toString().trim())
                && CountryValid()
                && GovernateValid()
                && cityValid()
                && LocalityValid()

        ) {
            return true;
        }
        return false;
    }

    private boolean NameValidation(String name) {
        if (name.isEmpty()) {
            binding.name.setError(getResources().getString(R.string.field_required));
            binding.nameError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.name.setError(null);
        binding.nameError.setVisibility(View.GONE);
        return true;
    }

    private boolean OutletNameValidation(String name) {
        if (name.isEmpty()) {
            binding.outletName.setError(getResources().getString(R.string.field_required));
            binding.outletName.setVisibility(View.VISIBLE);
            return false;
        }
        binding.outletName.setError(null);
        binding.outletName.setVisibility(View.GONE);
        return true;
    }

    private boolean NumberCheck(String number) {
        if (number.isEmpty()) {
            binding.number.setError(getResources().getString(R.string.field_required));
            binding.numberError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.numberError.setVisibility(View.GONE);
        binding.number.setError(null);
        return true;
    }

    private boolean CountryValid() {
        if (CountryName.isEmpty()) {
            binding.SelectCountry.setText("");
            Toast.makeText(this, "Country field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean GovernateValid() {
        if (GovernateSelect.isEmpty()) {
            binding.SelectGovernate.setText("");
            Toast.makeText(this, "Governate field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean cityValid() {
        if (GovernateSelect.isEmpty()) {
            Toast.makeText(this, "City field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean LocalityValid() {
        if (Locality.isEmpty()) {
            binding.SelectLocality.setText("");
            Toast.makeText(this, "Locality field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean PasswordCheck(String psw) {
        if (psw.isEmpty()) {
            binding.password.setError(getResources().getString(R.string.field_required));
            return false;
        } else if (psw.length() <= 6) {
            binding.password.setError(getResources().getString(R.string.psw_short));
            return false;
        }
        binding.password.setError(null);
        return true;
    }

    private boolean ConfirmPasswordCheck(String psw , String confirmpass) {
        if (confirmpass.isEmpty()) {
            binding.Confirmpassword.setError(getResources().getString(R.string.field_required));
            binding.Confirmpassword.setErrorEnabled(true);
            return false;
        } else if (confirmpass.length() != psw.length()) {
            binding.Confirmpassword.setError(getResources().getString(R.string.psw_not_match));
            binding.Confirmpassword.setErrorEnabled(true);
            return false;
        } else if (!psw.equalsIgnoreCase(confirmpass)) {
            binding.Confirmpassword.setError(getResources().getString(R.string.psw_not_match));
            binding.Confirmpassword.setErrorEnabled(true);
            return false;
        }
        binding.Confirmpassword.setError(null);
        return true;
    }

    private boolean EmailCheck(String email) {
        if (email.isEmpty()) {
            binding.email.setError(getResources().getString(R.string.field_required));
            binding.emailError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.email.setError(null);
        binding.emailError.setVisibility(View.GONE);
        return true;
    }

    private boolean AddressCheck(String add) {
        if (add.isEmpty()) {
            binding.address.setError(getResources().getString(R.string.field_required));
            binding.addressError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.address.setError(null);
        binding.addressError.setVisibility(View.GONE);
        return true;
    }

    private void SelectSmaertCountry(){

        binding.progressbar.setVisibility(View.GONE);

        CountryAdapter.CountryInterface  countryInterface = (text , list)  -> {
            binding.SelectCountry.setText(text);

            CountryName = list.getName();

//            switch (sessionManage.getUserDetails().get("LANGUAGE")){
//                case "en":
//                    CountryName = list.getName();
//                    break;
//                case "fr":
//                    if(list.getName_fr().isEmpty()){
//                        CountryName = list.getName();
//                    }else {
//                        CountryName = list.getName_fr();
//                    }
//                    break;
//                case "ar":
//                    CountryName = list.getName_ar();
//                    break;
//            }

            binding.CountryRecyclerViewLayout.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.SelectGovernate.setText("");
            binding.SelectLocality.setText("");
            GovernateSelect = "";
            Locality = "";
            Locality_id = "";
            Locality_ar = "";
            governatelist.clear();
            localityList.clear();
            getGovernate();
        };

        CountryAdapter countryAdapter =  new CountryAdapter(countryLists , countryInterface);
        binding.CountryRecyclerView.setAdapter(countryAdapter);

        binding.SelectCountry.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                binding.CountryRecyclerViewLayout.setVisibility(View.VISIBLE);
                return;
            }
            binding.CountryRecyclerViewLayout.setVisibility(View.GONE);
        });

        binding.SelectCountry.setOnClickListener(v -> {
            binding.CountryRecyclerViewLayout.setVisibility(View.VISIBLE);
        });

        binding.SelectCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(countryAdapter != null){
                    countryAdapter.getFilter().filter(s.toString());
                }
            }
        });

    }

    private void SelectSmartSearchGovernate(){

        GovernateAdapter.GovernateInterface governateInterface = (list) -> {

//            if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//                binding.SelectGovernate.setText(list.getName());
//            } else {
//                binding.SelectGovernate.setText(list.getName_ar());
//            }

            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                case "en":
                    binding.SelectGovernate.setText(list.getName());
                    break;
                case "fr":
                    if(list.getName_fr().isEmpty()){
                        binding.SelectGovernate.setText(list.getName());
                    }else {
                        binding.SelectGovernate.setText(list.getName_fr());
                    }
                    break;
                case "ar":
                    binding.SelectGovernate.setText(list.getName_ar());
                    break;
            }

            GovernateSelect = list.getId();
            binding.GovernateRecyclerViewLayout.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.SelectLocality.setText("");
            Locality = "";
            Locality_id = "";
            Locality_ar = "";
            localityList.clear();
            getLocality();

        };

        GovernateAdapter governateAdapter1 = new GovernateAdapter(governateInterface , governatelist);
        binding.GovernateRecyclerView.setAdapter(governateAdapter1);

        binding.SelectGovernate.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                binding.GovernateRecyclerViewLayout.setVisibility(View.VISIBLE);
                return;
            }
            binding.GovernateRecyclerViewLayout.setVisibility(View.GONE);

            if (GovernateSelect.isEmpty()) {
                binding.SelectGovernate.setText("");
            }

        });

        binding.SelectGovernate.setOnClickListener(v -> {
            binding.GovernateRecyclerViewLayout.setVisibility(View.VISIBLE);
        });

        binding.SelectGovernate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(governateAdapter1 != null){
                    governateAdapter1.getFilter().filter(s.toString());
                }
            }
        });




    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void SelectSmartSearchLocality(){

/*        List<String> localityList = new ArrayList<>();
        localityList.add("Hazira");
        localityList.add("City center");*/

        LocalityAdapter.LocalityInterface localityInterface = list -> {

//            if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//                binding.SelectLocality.setText(list.getName_ar());
//                Locality = list.getName_ar();
//            } else {
//                binding.SelectLocality.setText(list.getName());
//                Locality = list.getName();
//            }

            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                case "en":
                    binding.SelectLocality.setText(list.getName());
                    Locality = list.getName();
                    break;
                case "fr":
                    if(list.getName_fr().isEmpty()){
                        binding.SelectLocality.setText(list.getName());
                        Locality = list.getName();
                    }else {
                        binding.SelectLocality.setText(list.getName_fr());
                        Locality = list.getName_fr();
                    }
                    break;
                case "ar":
                    binding.SelectLocality.setText(list.getName_ar());
                    Locality = list.getName_ar();
                    break;
            }


            Locality_id = list.getId();
            Locality_ar = list.getName_ar();
            binding.LocalityRecyclerViewLayout.setVisibility(View.GONE);
        };

        LocalityAdapter localityAdapter = new LocalityAdapter(localityInterface , localityList);
        binding.LocalityRecyclerView.setAdapter(localityAdapter);

        binding.SelectLocality.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                binding.LocalityRecyclerViewLayout.setVisibility(View.VISIBLE);
                return;
            }
            binding.LocalityRecyclerViewLayout.setVisibility(View.GONE);
            if (Locality.isEmpty()) {
                binding.SelectLocality.setText("");
            }
        });

        binding.SelectLocality.setOnClickListener(v -> {
            binding.LocalityRecyclerViewLayout.setVisibility(View.VISIBLE);
        });

        binding.SelectLocality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(localityAdapter != null){
                    localityAdapter.getFilter().filter(s.toString());
                }
            }
        });




    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        switch (binding.getRoot().getId()){

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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




}
































