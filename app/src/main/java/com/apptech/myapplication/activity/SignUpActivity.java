package com.apptech.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.CityAdapter;
import com.apptech.myapplication.adapter.GovernateAdapter;
import com.apptech.myapplication.adapter.LocalityAdapter;
import com.apptech.myapplication.databinding.ActivitySignUpBinding;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.NetworkCheck;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
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

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    private static final String TAG = "SignUpActivity";
    List<String> governatelist = new ArrayList<>();
    List<String> citylist = new ArrayList<>();
    List<String> localityList = new ArrayList<>();
    boolean GOVERNATE = false, CITY = false, LOCALITY = false;
    String GovernateSelect = "", CitySelect = "", Locality = "";
    GovernateAdapter governateAdapter;
    CityAdapter cityAdapter;
//    LocalitiyAdapter localitiyAdapter;
    String Languages = "EN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManage = SessionManage.getInstance(this);
        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            new LanguageChange(this, "ar");
        } else {
            new LanguageChange(this, "en");
        }

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        sessionManage = SessionManage.getInstance(this);
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            Languages = "EN";
        } else {
            Languages = "AR";
        }


        Log.e(TAG, "onCreate: " + Languages);

/*        governateAdapter = new GovernateAdapter(SignUpActivity.this, R.layout.spinner_list);

        localitiyAdapter = new LocalitiyAdapter(SignUpActivity.this, R.layout.spinner_list);

        cityAdapter = new CityAdapter(SignUpActivity.this, R.layout.spinner_list);*/

        getGovernate();


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
                } else {
                    sessionManage.setlanguage("ar");
                }
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                return true;
            });

            popupMenu.show();//showing popup menu

        });

    }

    private void SignUp() {

        binding.progressbar.setVisibility(View.VISIBLE);

        Call call = lavaInterface.Signup(binding.name.getText().toString().trim(), binding.number.getText().toString().trim(), binding.password.getText().toString().trim(), binding.email.getText().toString().trim(), Locality, CitySelect, GovernateSelect, binding.address.getText().toString().trim());
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
                            Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
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

    private void getGovernate() {

        governatelist.clear();
        Log.e(TAG, "getGovernate: " + Languages);
        Call call = lavaInterface.Governate(Languages);
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
                            JSONObject objec = jsonArray.getJSONObject(0);
                            Iterator iterator = objec.keys();
                            String key = "";
                            while (iterator.hasNext()) {
                                key = (String) iterator.next();
                                Log.e(TAG, "onResponse: " + key);
                                break;
                            }

                            if (key.equals("governate_en")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    governatelist.add(json_data.getString("governate_en"));
                                }
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    governatelist.add(json_data.getString("governate_ar"));
                                }
                            }
                            SelectSmartSearchGovernate();
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

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
                            JSONObject objec = jsonArray.getJSONObject(0);
                            Iterator iterator = objec.keys();
                            String key = "";
                            while (iterator.hasNext()) {
                                key = (String) iterator.next();
                                Log.e(TAG, "onResponse: " + key);
                                break;
                            }

                            if (key.equals("locality_en")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    localityList.add(json_data.getString("locality_en"));
                                }
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json_data = jsonArray.getJSONObject(i);
                                    localityList.add(json_data.getString("locality_ar"));
                                }
                            }

                            SelectSmartSearchLocality();
       /*                     localitiyAdapter.addAll(localityList);
                            localitiyAdapter.add("Select Locality");
                            binding.locality.setAdapter(localitiyAdapter);
                            binding.locality.setSelection(localitiyAdapter.getCount());

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
                    return;
                }
                Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

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

/*
    public class GovernateAdapter extends ArrayAdapter<String> {
        public GovernateAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }

    public static class CityAdapter extends ArrayAdapter<String> {
        public CityAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }

    public static class LocalitiyAdapter extends ArrayAdapter<String> {
        public LocalitiyAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            int count = super.getCount();
            return count > 0 ? count - 1 : count;
        }
    }
*/

    @Override
    protected void onStart() {
        super.onStart();
//        binding.progressbar.setVisibility(View.VISIBLE);
    }

    boolean validation() {
        if (NameValidation(binding.name.getText().toString().trim())
                && NumberCheck(binding.number.getText().toString().trim())
                && EmailCheck(binding.email.getText().toString())
                && PasswordCheck(binding.password.getText().toString().trim())
                && AddressCheck(binding.address.getText().toString().trim())
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
            return false;
        }
        binding.name.setError(null);
        return true;
    }

    private boolean NumberCheck(String number) {
        if (number.isEmpty()) {
            binding.number.setError(getResources().getString(R.string.field_required));
            return false;
        }
        binding.number.setError(null);
        return true;
    }

    private boolean GovernateValid() {
        if (GovernateSelect.isEmpty()) {
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
        if (GovernateSelect.isEmpty()) {
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

    private boolean EmailCheck(String email) {
        if (email.isEmpty()) {
            binding.email.setError(getResources().getString(R.string.field_required));
            return false;
        }
        binding.email.setError(null);
        return true;
    }

    private boolean AddressCheck(String add) {
        if (add.isEmpty()) {
            binding.address.setError(getResources().getString(R.string.field_required));
            return false;
        }
        binding.address.setError(null);
        return true;
    }




    private void SelectSmartSearchGovernate(){

/*        List<String> governatelist = new ArrayList<>();

        governatelist.add("India");
        governatelist.add("Afghanistan");
        governatelist.add("Australia");
        governatelist.add("Bahamas");
        governatelist.add("Chile");
        governatelist.add("Costa Rica");
        governatelist.add("Cyprus");*/

        com.apptech.myapplication.adapter.GovernateAdapter.GovernateInterface governateInterface = (text) -> {
            binding.SelectGovernate.setText(text);
            GovernateSelect = text;
            binding.GovernateRecyclerViewLayout.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
//            getcity();
            getLocality();
        };

        com.apptech.myapplication.adapter.GovernateAdapter governateAdapter1 = new com.apptech.myapplication.adapter.GovernateAdapter(governateInterface , governatelist);
        binding.GovernateRecyclerView.setAdapter(governateAdapter1);

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
    private void SelectSmartSearchCity(){

//        List<String> citylist = new ArrayList<>();
//        citylist.add("Mumbai");
//        citylist.add("New Delhi");
//        citylist.add("Pune");
//        citylist.add("Nagpur");
//        citylist.add("Nashik");

        com.apptech.myapplication.adapter.CityAdapter.CityInterface cityInterface = text -> {
            binding.SelectCity.setText(text);
            binding.CityRecyclerViewLayout.setVisibility(View.GONE);
            CitySelect = text;
//            SelectSmartSearchLocality();
            binding.progressbar.setVisibility(View.VISIBLE);


        };
        com.apptech.myapplication.adapter.CityAdapter cityAdapter = new com.apptech.myapplication.adapter.CityAdapter(cityInterface , citylist);
        binding.CityRecyclerView.setAdapter(cityAdapter);

        binding.SelectCity.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                binding.CityRecyclerViewLayout.setVisibility(View.VISIBLE);
                return;
            }
            binding.CityRecyclerViewLayout.setVisibility(View.GONE);
        });

        binding.SelectCity.setOnClickListener(v -> {
            binding.CityRecyclerViewLayout.setVisibility(View.VISIBLE);
        });

        binding.SelectCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(cityAdapter != null){
                        cityAdapter.getFilter().filter(s.toString());
                    }
            }
        });

    }
    private void SelectSmartSearchLocality(){

/*        List<String> localityList = new ArrayList<>();
        localityList.add("Hazira");
        localityList.add("City center");*/

        LocalityAdapter.LocalityInterface localityInterface = text -> {
            binding.SelectLocality.setText(text);
            binding.LocalityRecyclerViewLayout.setVisibility(View.GONE);
            Locality = text;
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


}
































