package com.apptech.myapplication.ui.profile;

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

import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.MainActivity;
import com.apptech.myapplication.activity.SignUpActivity;
import com.apptech.myapplication.adapter.LocalityAdapter;
import com.apptech.myapplication.databinding.ProfileFragmentBinding;
import com.apptech.myapplication.list.LocalityList;
import com.apptech.myapplication.other.NetworkCheck;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    ProfileFragmentBinding binding;
    List<String> governatelist = new ArrayList<>();
    private static final String TAG = "profileFragment";
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String GovernateSelect = "", CitySelect = "", Locality = "" , Locality_id ="" , Locality_ar = "";
    String Languages = "EN";
    List<LocalityList> localityList = new ArrayList<>();
    private Uri fileUri;
    MultipartBody.Part filePart= null;
    boolean firstTime = true;


    public static profileFragment newInstance() {
        return new profileFragment();
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

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());


        binding.NameEdit.setText(sessionManage.getUserDetails().get("NAME"));
        binding.EmailEdit.setText(sessionManage.getUserDetails().get("EMAIL"));
        binding.outletNameEdit.setText(sessionManage.getUserDetails().get("OUTLET_NAME"));
        binding.AddressEdit.setText(sessionManage.getUserDetails().get("ADDRESS"));
        binding.SelectGovernate.setText(sessionManage.getUserDetails().get("GOVERNATE"));
        binding.SelectLocality.setText(sessionManage.getUserDetails().get("LOCALITY"));

        GovernateSelect = sessionManage.getUserDetails().get("GOVERNATE");
        Locality = sessionManage.getUserDetails().get("LOCALITY");
        Locality_id = sessionManage.getUserDetails().get("LOCALITY_ID");
        Locality_ar = sessionManage.getUserDetails().get("LOCALITY_AR");

        if(!Objects.requireNonNull(sessionManage.getUserDetails().get("USER_IMG")).isEmpty()){
            Glide.with(getContext()).load(ApiClient.Image_URL + sessionManage.getUserDetails().get("USER_IMG")).placeholder(R.drawable.ic_user__1_).into(binding.profileImage);
        }


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            Languages = "EN";
        } else {
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

        getGovernate();

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
        });



    }

    private void getGovernate() {

        governatelist.clear();
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
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    private void SelectSmartSearchGovernate(){

        com.apptech.myapplication.adapter.GovernateAdapter.GovernateInterface governateInterface = (text) -> {
            binding.SelectGovernate.setText(text);
            GovernateSelect = text;
            binding.GovernateRecyclerViewLayout.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
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


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Profile");
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


                            for (int i=0; i< jsonArray.length(); i++){

                                JSONObject jo = jsonArray.getJSONObject(i);

                                localityList.add(new LocalityList(
                                        jo.getString("id")
                                        ,jo.getString("governate_en")
                                        ,jo.getString("locality_en")
                                        ,jo.getString("time")
                                        ,jo.getString("governate_ar")
                                        ,jo.getString("locality_ar")
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
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
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

    private void SelectSmartSearchLocality(){

        LocalityAdapter.LocalityInterface localityInterface = list -> {

            if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                binding.SelectLocality.setText(list.getLocality_ar());
                Locality = list.getLocality_ar();
            } else {
                binding.SelectLocality.setText(list.getLocality_en());
                Locality = list.getLocality_en();
            }
            Locality_id = list.getId();
            Locality_ar = list.getLocality_ar();
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
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(sessionManage.getUserDetails().get("ID")));

//        Log.e(TAG, "getProfileUpdate id : " + id );
//        Log.e(TAG, "getProfileUpdate name : " + name );
//        Log.e(TAG, "getProfileUpdate email : " + email );
//        Log.e(TAG, "getProfileUpdate address : " + address );
//        Log.e(TAG, "getProfileUpdate outlet : " + outlet );
//        Log.e(TAG, "getProfileUpdate governate : " + governate );
//        Log.e(TAG, "getProfileUpdate locality : " + locality );
//        Log.e(TAG, "getProfileUpdate locality_id : " + locality_id );
//        Log.e(TAG, "getProfileUpdate locality_ar : " + locality_ar );
        Log.e(TAG, "getProfileUpdate file : " + filePart );


//        RequestBody nameMulti = RequestBody.create(MediaType.parse("multipart/form-data"), name);





        lavaInterface.PROFILE_UPDATE(filePart , id , name ,email ,locality ,governate,address,outlet,locality_id,locality_ar).enqueue(new Callback<Object>() {

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
                                jsonObject1.optString("img_url")
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
                && EmailCheck(binding.Email.getEditText().getText().toString())
                && OutletNameValidation(binding.outletname.getEditText().getText().toString().trim())
                && AddressCheck(binding.address.getEditText().getText().toString().trim())
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


    private boolean GovernateValid() {
        if (GovernateSelect.isEmpty()) {
            Toast.makeText(getContext(), "Governate field is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean LocalityValid() {
        if (GovernateSelect.isEmpty()) {
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



}












































