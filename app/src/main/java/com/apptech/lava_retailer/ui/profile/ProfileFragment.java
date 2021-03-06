package com.apptech.lava_retailer.ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.activity.MainActivity;
import com.apptech.lava_retailer.activity.SignUpActivity;
import com.apptech.lava_retailer.adapter.CountryAdapter;
import com.apptech.lava_retailer.adapter.LocalityAdapter;
import com.apptech.lava_retailer.databinding.ProfileFragmentBinding;
import com.apptech.lava_retailer.list.LocalityList;
import com.apptech.lava_retailer.list.country.Country_list;
import com.apptech.lava_retailer.list.governate.GovernateList;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    ProfileFragmentBinding binding;
    List<GovernateList> governatelist = new ArrayList<>();
    private static final String TAG = "profileFragment";
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String CountryName ="" , GovernateSelect = "", CitySelect = "", Locality = "" , Locality_id ="" , Locality_ar = "";
    String Languages = "EN";
    List<LocalityList> localityList = new ArrayList<>();
    private Uri fileUri;
    MultipartBody.Part filePart= null;
    boolean firstTime = true;
    List<Country_list> countryLists = new ArrayList<>();


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel


        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Profile));

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());


        if(sessionManage.getUserDetails().get("USER_IMG") != null){
            if(!sessionManage.getUserDetails().get("USER_IMG").isEmpty()){
                Log.e(TAG, "onActivityCreated: " +  sessionManage.getUserDetails().get("USER_IMG") );
                Glide.with(getContext()).load(ApiClient.Image_URL + sessionManage.getUserDetails().get("USER_IMG")).placeholder(R.drawable.ic_user__1_).into(binding.profileImage);
            }
        }


//        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//            Languages = "EN";
//        } else {
//            Languages = "AR";
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

        if (new NetworkCheck().haveNetworkConnection(requireActivity())){
            Profile_details();
        }else {
            Toast.makeText(requireContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

/*        try {
            getCountry();
            getGovernate();
        }catch (NullPointerException e){
            Log.e(TAG, "onActivityCreated: " , e);
        }*/


        binding.UpdateProfile.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                if (validation()) {
                    getProfileUpdate();
                    return;
                }
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });


        binding.updateImg.setOnClickListener(v -> {
            ImagePicker.Companion.with(requireActivity())
                    .crop()
                    .compress(64)
                    .maxResultSize(1080, 1080)
                    .start();
            title.setText(getActivity().getString(R.string.Profile));
        });

    }

    void Profile_details(){
        binding.progressbar.setVisibility(View.VISIBLE);

        lavaInterface.PROFILE_DETAILS(sessionManage.getUserDetails().get("USER_UNIQUE_ID")).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                try {


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()) );

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


                            binding.NameEdit.setText(jsonObject1.getString("name"));
                            binding.EmailEdit.setText(jsonObject1.optString("email"));
                            binding.outletNameEdit.setText(jsonObject1.getString("outlet_name"));
                            binding.AddressEdit.setText(jsonObject1.getString("address"));
                            binding.SelectCountry.setText(jsonObject1.getString("country_name"));

                            Log.e(TAG, "onResponse: " + jsonObject1.getString("governate") );

                            binding.SelectGovernate.setText(jsonObject1.getString("governate"));


                            binding.SelectLocality.setText(jsonObject1.getString("locality"));
                            GovernateSelect = jsonObject1.getString("country_id");
                            Locality = jsonObject1.getString("locality");
                            Locality_id = jsonObject1.getString("locality_id");
                            Locality_ar = jsonObject1.getString("locality_ar");
                            CountryName = jsonObject1.getString("country_id");

                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);

                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                    binding.progressbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void getCountry() throws NullPointerException{
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
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);

            }
        });
    }


    private void SelectSmaertCountry(){

        try {

            binding.progressbar.setVisibility(View.GONE);

            CountryAdapter.CountryInterface  countryInterface = (text , list)  -> {
                binding.SelectCountry.setText(text);
                CountryName = list.getName();
                binding.SelectGovernate.setText("");
                binding.SelectLocality.setText("");
                binding.CountryRecyclerViewLayout.setVisibility(View.GONE);
                binding.progressbar.setVisibility(View.VISIBLE);

                GovernateSelect ="";
                Locality ="";

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
                    if(countryAdapter != null){
                        countryAdapter.getFilter().filter(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.e(TAG, "SelectSmaertCountry: ",e );
        }


    }

    private void getGovernate() throws NullPointerException{
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
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                return;
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

    private void SelectSmartSearchGovernate(){

        try {
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
                Locality ="";
                binding.GovernateRecyclerViewLayout.setVisibility(View.GONE);
                binding.progressbar.setVisibility(View.VISIBLE);
                getLocality();

            };

            com.apptech.lava_retailer.adapter.GovernateAdapter governateAdapter1 = new com.apptech.lava_retailer.adapter.GovernateAdapter(governateInterface , governatelist);
            binding.GovernateRecyclerView.setAdapter(governateAdapter1);
            getLocality();


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

        }catch (NullPointerException e){
            Log.e(TAG, "SelectSmartSearchGovernate: " ,e );
        }



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


                            SelectSmartSearchLocality();
                            binding.progressbar.setVisibility(View.GONE);

                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressbar.setVisibility(View.GONE);
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
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


    private void SelectSmartSearchLocality() throws NullPointerException{

        LocalityAdapter.LocalityInterface localityInterface = list -> {

            if (sessionManage.getUserDetails().get("LANGUAGE").equals("ar")) {
                binding.SelectLocality.setText(list.getName_ar());
            }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
                if(list.getName_fr().isEmpty()){
                    binding.SelectLocality.setText(list.getName());
                }else {
                    binding.SelectLocality.setText(list.getName_fr());
                }
            } else {
                binding.SelectLocality.setText(list.getName());
            }

            Locality = list.getName();
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
        RequestBody governate = RequestBody.create(MediaType.parse("multipart/form-data"),GovernateSelect);
        RequestBody locality = RequestBody.create(MediaType.parse("multipart/form-data"),Locality);
        RequestBody locality_id = RequestBody.create(MediaType.parse("multipart/form-data"),Locality_id);
        RequestBody locality_ar = RequestBody.create(MediaType.parse("multipart/form-data"),Locality_ar);
        RequestBody country_id = RequestBody.create(MediaType.parse("multipart/form-data"),CountryName);
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID)));


        lavaInterface.PROFILE_UPDATE(filePart , id , name ,email ,locality ,governate,address,outlet,locality_id,locality_ar , country_id).enqueue(new Callback<Object>() {

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

                        startActivity(new Intent(getContext() , MainActivity.class));
                        binding.progressbar.setVisibility(View.GONE);


                        return;
                    }
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Profile));

        if (resultCode == getActivity().RESULT_OK) {

            fileUri = data.getData();
            binding.profileImage.setImageURI(fileUri);
            File file =  ImagePicker.Companion.getFile(data);
            filePart = MultipartBody.Part.createFormData("img_url", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    boolean validation() {
        if (
                FileValidation()
                && NameValidation(binding.Name.getEditText().getText().toString().trim())
//                && EmailCheck(binding.Email.getEditText().getText().toString())
                && OutletNameValidation(binding.outletname.getEditText().getText().toString().trim())
                && AddressCheck(binding.address.getEditText().getText().toString().trim())
                && CountryValid()
                && GovernateValid()
                && LocalityValid()
        ) {
            return true;
        }
        return false;
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

    private boolean CountryValid() {
        if (CountryName.isEmpty()) {
            Toast.makeText(getContext(), "Country field is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.SelectCountry.getText().toString().isEmpty()){
            binding.SelectCountry.setText("");
            Toast.makeText(getContext(), "Country field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean GovernateValid() {
        if (GovernateSelect.isEmpty()) {
            Toast.makeText(getContext(), "Governate field is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.SelectGovernate.getText().toString().isEmpty()){
            binding.SelectGovernate.setText("");
            Toast.makeText(getContext(), "Governate field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean LocalityValid() {
        if (Locality.isEmpty()) {
            Toast.makeText(getContext(), "Locality field is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.SelectLocality.getText().toString().isEmpty()){
            binding.SelectLocality.setText("");
            Toast.makeText(getContext(), "Locality field is required", Toast.LENGTH_SHORT).show();
            return false;
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void ErrorDilaog(String errormsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error Message");
        builder.setMessage(errormsg);
        builder.setPositiveButton("Close" , (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }





}












































