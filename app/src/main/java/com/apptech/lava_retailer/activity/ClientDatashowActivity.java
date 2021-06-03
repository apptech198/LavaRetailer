package com.apptech.lava_retailer.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.CountryAdapter;
import com.apptech.lava_retailer.adapter.GovernateAdapter;
import com.apptech.lava_retailer.adapter.LocalityAdapter;
import com.apptech.lava_retailer.databinding.ActivityClientDatashowBinding;
import com.apptech.lava_retailer.list.LocalityList;
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.list.governate.GovernateList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.profile.ProfileViewModel;
import com.facebook.CallbackManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDatashowActivity extends AppCompatActivity {


    private ProfileViewModel mViewModel;
    List<GovernateList> governatelist = new ArrayList<>();
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String CountryName ="" , GovernateSelect = "", CitySelect = "", Locality = "" , Locality_id ="" , Locality_ar = "";
    String Languages = "EN";
    List<LocalityList> localityList = new ArrayList<>();
    private Uri fileUri;
    MultipartBody.Part filePart= null;
    boolean firstTime = true;
    private static final String TAG = "ClientDatashowActivity";
    ActivityClientDatashowBinding binding;
    List<Country_list> countryLists = new ArrayList<>();
    AlertDialog alertDialog1;
    int REQ_USER_CONSENT  = 2;
    String autootp = "";
    TextInputLayout OtpEdittext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientDatashowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setTitle("Verification");

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(this);

        binding.NameEdit.setText(sessionManage.getUserDetails().get("NAME"));
        binding.EmailEdit.setText(sessionManage.getUserDetails().get("EMAIL"));
        binding.outletNameEdit.setText(sessionManage.getUserDetails().get("OUTLET_NAME"));
        binding.AddressEdit.setText(sessionManage.getUserDetails().get("ADDRESS"));
        binding.SelectCountry.setText(sessionManage.getUserDetails().get("COUNTRY_NAME"));
        binding.SelectGovernate.setText(sessionManage.getUserDetails().get("GOVERNATE"));
        binding.SelectLocality.setText(sessionManage.getUserDetails().get("LOCALITY"));
        binding.password.getEditText().setText(sessionManage.getUserDetails().get("PASSWORD"));
        binding.Confirmpassword.getEditText().setText(sessionManage.getUserDetails().get("PASSWORD"));
        binding.Mobile.getEditText().setText(sessionManage.getUserDetails().get("MOBILE"));

        GovernateSelect = sessionManage.getUserDetails().get("GOVERNATE");
        Locality = sessionManage.getUserDetails().get("LOCALITY");
        Locality_id = sessionManage.getUserDetails().get("LOCALITY_ID");
        Locality_ar = sessionManage.getUserDetails().get("LOCALITY_AR");
        CountryName = sessionManage.getUserDetails().get("COUNTRY_ID");

//        if(sessionManage.getUserDetails().get("USER_IMG") != null || sessionManage.getUserDetails().get("USER_IMG").isEmpty()){
//            Glide.with(this).load(ApiClient.Image_URL + sessionManage.getUserDetails().get("USER_IMG")).placeholder(R.drawable.ic_user__1_).into(binding.profileImage);
//        }

        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            Languages = "EN";
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            Languages = "FR";
        }  else {
            Languages = "AR";
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
        binding.Name.setBoxStrokeColorStateList(myColorList);
        binding.Email.setBoxStrokeColorStateList(myColorList);
        binding.outletname.setBoxStrokeColorStateList(myColorList);
        binding.address.setBoxStrokeColorStateList(myColorList);
        binding.password.setBoxStrokeColorStateList(myColorList);
        binding.Confirmpassword.setBoxStrokeColorStateList(myColorList);
        binding.Mobile.setBoxStrokeColorStateList(myColorList);

//        getCountry();

        binding.UpdateProfile.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(this)) {
                if (validation()) {
//                    getProfileUpdate();
                    startSmsUserConsent();
                    OTP_SEND_AUTH(binding.Mobile.getEditText().getText().toString().trim());
                    return;
                }
                return;
            }
            Toast.makeText(this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });

        binding.updateImg.setOnClickListener(v -> {
            ImagePicker.Companion.with(this)
                    .crop()
                    .compress(64)
                    .maxResultSize(1080, 1080)
                    .start();
        });


        binding.Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim(), binding.Confirmpassword.getEditText().getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void OtpVerificationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.row_otp_verify , null);
        LinearLayout close = view.findViewById(R.id.close);
        LinearLayout submit = view.findViewById(R.id.submit);

        OtpEdittext = view.findViewById(R.id.OtpEdittext);
        close.setOnClickListener(v -> {alertDialog1.dismiss();});
        submit.setOnClickListener(v -> {
            if(OtpEdittext.getEditText().getText().toString().trim().isEmpty()){
                OtpEdittext.setError(getResources().getString(R.string.field_required));
                OtpEdittext.setErrorEnabled(true);
                return;
            }
            OtpEdittext.setError(null);
            OtpEdittext.setErrorEnabled(false);
            String add = OtpEdittext.getEditText().getText().toString().trim();
            VerifyOtp(add);
            alertDialog1.dismiss();
        });

        builder.setView(view);
        alertDialog1 = builder.create();
        alertDialog1.show();
    }


    private void VerifyOtp(String otp) {

        binding.progressbar.setVisibility(View.VISIBLE);

        lavaInterface.VERIFY_OTP(binding.Mobile.getEditText().getText().toString().trim() ,otp).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString() );
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equalsIgnoreCase("false")) {
                        getProfileUpdate();
                        Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ClientDatashowActivity.this, "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(ClientDatashowActivity.this, "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });

    }



    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid -> {
//                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
             autootp = matcher.group(0);
//            Log.e(TAG, "getOtpFromMessage: " +  matcher.group(0));
//            Toast.makeText(this, "" + matcher.group(0), Toast.LENGTH_SHORT).show();
//            otpText.setText(matcher.group(0));
            OtpEdittext.getEditText().setText(autootp);
            VerifyOtp(autootp);
        }
    }

/*
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
                                    ,object.getString("name_fr")
                                    ,object.getString("time")
                            ));
                        }
                        SelectSmaertCountry();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ClientDatashowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ClientDatashowActivity.this, "Time out", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

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
                                    object.optString("id")
                                    ,object.optString("name")
                                    ,object.optString("name_ar")
                                    ,object.optString("name_fr")
                                    ,object.optString("time")
                                    ,object.getString("currency")
                                    ,object.optString("currency_symbol")
                            ));
                        }
                        SelectSmaertCountry();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ClientDatashowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);

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
                                    json_data.optString("id")
                                    ,json_data.optString("country_id")
                                    ,json_data.optString("country_name")
                                    ,json_data.optString("name")
                                    ,json_data.optString("name_ar")
                                    ,json_data.optString("name_fr")
                                    ,json_data.optString("time")
                            ));
                        }

                        SelectSmartSearchGovernate();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ClientDatashowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                return;
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
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

/*
    private void getGovernate() {

        governatelist.clear();
        Call call = lavaInterface.Governate(Languages , CountryID);
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
                            JSONArray jsonArray = jsonObject.getJSONArray("governate_list");

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
                        Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Toast.makeText(ClientDatashowActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }
*/

    private void SelectSmartSearchGovernate(){

        com.apptech.lava_retailer.adapter.GovernateAdapter.GovernateInterface governateInterface = (list) -> {

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
//            getLocality();
        };

        GovernateAdapter governateAdapter1 = new GovernateAdapter(governateInterface , governatelist);
        binding.GovernateRecyclerView.setAdapter(governateAdapter1);

//        getLocality();

        binding.SelectGovernate.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                binding.GovernateRecyclerViewLayout.setVisibility(View.VISIBLE);
                return;
            }
            binding.GovernateRecyclerViewLayout.setVisibility(View.GONE);
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
                                        jo.optString("id")
                                        ,jo.optString("governate_id")
                                        ,jo.optString("governate_name")
                                        ,jo.optString("name")
                                        ,jo.optString("name_ar")
                                        ,jo.optString("name_fr")
                                        ,jo.optString("time")
                                ));
                            }

//                            JSONObject objec = jsonArray.getJSONObject(0);
//                            Iterator iterator = objec.keys();
//                            String key = "";
//                            while (iterator.hasNext()) {
//                                key = (String) iterator.next();
//                                Log.e(TAG, "onResponse: " + key);
//                                break;
//                            }
//
//                            if (key.equals("locality_en")) {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject json_data = jsonArray.getJSONObject(i);
//                                    localityList.add(json_data.getString("locality_en"));
//                                }
//                            } else {
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject json_data = jsonArray.getJSONObject(i);
//                                    localityList.add(json_data.getString("locality_ar"));
//                                }
//                            }

                            SelectSmartSearchLocality();
                            binding.progressbar.setVisibility(View.GONE);

                            return;
                        }
                        Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressbar.setVisibility(View.GONE);
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ClientDatashowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(ClientDatashowActivity.this, "Time out", Toast.LENGTH_SHORT).show();
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

    private void SelectSmartSearchLocality(){

        LocalityAdapter.LocalityInterface localityInterface = list -> {

            if (sessionManage.getUserDetails().get("LANGUAGE").equals("ar")) {
                binding.SelectLocality.setText(list.getName_ar());
                Locality = list.getName_ar();
            }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
                if(list.getName_fr().isEmpty()){
                    binding.SelectLocality.setText(list.getName());
                }else {
                    binding.SelectLocality.setText(list.getName_fr());
                }
                Locality = list.getName();
            } else {
                binding.SelectLocality.setText(list.getName());
                Locality = list.getName();
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

    private void getProfileUpdate(){

        binding.progressbar.setVisibility(View.VISIBLE);
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"),binding.Name.getEditText().getText().toString().trim());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"),binding.Email.getEditText().getText().toString().trim());
        RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"),binding.address.getEditText().getText().toString().trim());
        RequestBody outlet = RequestBody.create(MediaType.parse("multipart/form-data"),binding.outletname.getEditText().getText().toString().trim());
        RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"),binding.password.getEditText().getText().toString().trim());
        RequestBody governate = RequestBody.create(MediaType.parse("multipart/form-data"),GovernateSelect);
        RequestBody locality = RequestBody.create(MediaType.parse("multipart/form-data"),Locality);
        RequestBody locality_id = RequestBody.create(MediaType.parse("multipart/form-data"),Locality_id);
        RequestBody locality_ar = RequestBody.create(MediaType.parse("multipart/form-data"),Locality_ar);
        RequestBody country_id = RequestBody.create(MediaType.parse("multipart/form-data"),CountryName);
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID)));
        RequestBody number = RequestBody.create(MediaType.parse("multipart/form-data"), binding.Mobile.getEditText().getText().toString());

//        PROFILE_UPDATE_FIRST_TIME
        lavaInterface.PROFILE_UPDATE_FIRST_TIME(filePart , id , name ,email ,locality ,governate,address,outlet,locality_id,locality_ar , country_id , password , number).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()));

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");

                    if(error.equalsIgnoreCase("false")){

                        JSONObject jsonObject1 = jsonObject.getJSONObject("user_detail");

                        if(jsonObject1.optString("name").isEmpty()){
                            ErrorDilaog(getResources().getString(R.string.name_fiels_missing));
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }

//                        if(jsonObject1.optString("email").isEmpty()){
//                            ErrorDilaog(getResources().getString(R.string.email_fiels_missing));
//                            binding.progressbar.setVisibility(View.GONE);
//                            return;
//                        }

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

                        binding.progressbar.setVisibility(View.GONE);
                        startActivity(new Intent(ClientDatashowActivity.this , MessageShowActivity.class));
                        sessionManage.FirstTimeLanguage("true");
                        sessionManage.PROFILE_VERIFY_CHECK(jsonObject1.getString("backend_verify"));
                        finish();
                        return;
                    }
                    Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ClientDatashowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }


    private void OTP_SEND_AUTH(String num){

        Log.e(TAG, "OTP_SEND_AUTH: " + num );
        binding.progressbar.setVisibility(View.VISIBLE);

        lavaInterface.SEND_OTP_AUTH(num , sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME)).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");

                    if(error.equalsIgnoreCase("false")){

                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        String otp = jsonObject1.optString("login_otp");
                        Toast.makeText(ClientDatashowActivity.this, "" + otp, Toast.LENGTH_SHORT).show();
                        OtpVerificationDialog();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(ClientDatashowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(ClientDatashowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void SelectSmaertCountry(){

        binding.progressbar.setVisibility(View.GONE);

        CountryAdapter.CountryInterface  countryInterface = (text , list)  -> {

            binding.SelectCountry.setText(text);
            CountryName = list.getName();
            binding.CountryRecyclerViewLayout.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.SelectGovernate.setText("");
            binding.SelectLocality.setText("");
            GovernateSelect = "";
            Locality = "";
            Locality_id = "";
            Locality_ar = "";
//            getGovernate();

        };

        CountryAdapter countryAdapter =  new CountryAdapter(countryLists , countryInterface);
        binding.CountryRecyclerView.setAdapter(countryAdapter);

//        getGovernate();



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            fileUri = data.getData();
            binding.profileImage.setImageURI(fileUri);
            File file =  ImagePicker.Companion.getFile(data);
            filePart = MultipartBody.Part.createFormData("img_url", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "onActivityResult: " + message);
//                textViewMessage.setText(String.format("%s - %s", getString(R.string.received_message), message));
                getOtpFromMessage(message);

            }
        }



    }

    boolean validation() {
        return FileValidation()
                && NameValidation(binding.Name.getEditText().getText().toString().trim())
                && MobValidation(binding.Mobile.getEditText().getText().toString().trim())
//                && EmailCheck(binding.Email.getEditText().getText().toString())
                && PasswordCheck(binding.password.getEditText().getText().toString().trim())
                && ConfirmPasswordCheck(binding.password.getEditText().getText().toString().trim(), binding.Confirmpassword.getEditText().getText().toString().trim())
                && OutletNameValidation(binding.outletname.getEditText().getText().toString().trim())
                && AddressCheck(binding.address.getEditText().getText().toString().trim())
                && CountryValid()
                && GovernateValid()
                && LocalityValid();
    }


    private boolean NameValidation(String name) {
        if (name.isEmpty()) {
            binding.Name.setError(getResources().getString(R.string.field_required));
            binding.Name.setErrorEnabled(true);
            return false;
        }
        binding.Name.setError(null);
        binding.Name.setErrorEnabled(true);
        return true;
    }

    private boolean MobValidation(String name) {
        if (name.isEmpty()) {
            binding.Mobile.setError(getResources().getString(R.string.field_required));
            binding.Mobile.setErrorEnabled(true);
            return false;
        }
        binding.Mobile.setError(null);
        binding.Mobile.setErrorEnabled(true);
        return true;
    }

    private boolean OutletNameValidation(String name) {
        if (name.isEmpty()) {
            binding.outletname.setError(getResources().getString(R.string.field_required));
            binding.outletname.setErrorEnabled(true);
            return false;
        }
        binding.outletname.setError(null);
        binding.outletname.setErrorEnabled(true);
        return true;
    }

    private boolean GovernateValid() {
        if (GovernateSelect.isEmpty()) {
            binding.SelectGovernate.setText("");
            Toast.makeText(this, "Governate field is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.SelectGovernate.getText().toString().isEmpty()){
            binding.SelectGovernate.setText("");
            Toast.makeText(this, "Governate field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean CountryValid() {
        if (CountryName.isEmpty()) {
            binding.SelectCountry.setText("");
            Toast.makeText(this, "Country field is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.SelectCountry.getText().toString().isEmpty()){
            binding.SelectCountry.setText("");
            Toast.makeText(this, "Country field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean LocalityValid() {
        if (GovernateSelect.isEmpty()) {
            binding.SelectLocality.setText("");
            Toast.makeText(this, "Locality field is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.SelectLocality.getText().toString().isEmpty()){
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

    private boolean FileValidation(){
//        if (filePart == null) {
//            Toast.makeText(getContext(), "Upload Image", Toast.LENGTH_SHORT).show();
//            return  false;
//        }
        return  true;
    }

    private boolean EmailCheck(String email) {
        if (email.isEmpty()) {
            binding.Email.setError(getResources().getString(R.string.field_required));
            binding.Email.setErrorEnabled(true);
            return false;
        }
        binding.Email.setError(null);
        binding.Email.setErrorEnabled(false);
        return true;
    }

    private boolean AddressCheck(String add) {
        if (add.isEmpty()) {
            binding.address.setError(getResources().getString(R.string.field_required));
            binding.address.setErrorEnabled(true);
            return false;
        }
        binding.address.setError(null);
        binding.address.setErrorEnabled(false);
        return true;
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





































