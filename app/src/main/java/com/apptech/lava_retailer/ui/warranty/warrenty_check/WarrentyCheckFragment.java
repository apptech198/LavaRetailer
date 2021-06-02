package com.apptech.lava_retailer.ui.warranty.warrenty_check;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.WarrentyCheckFragmentBinding;
import com.apptech.lava_retailer.fragment.ScannerFragment;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class  WarrentyCheckFragment extends Fragment implements ScannerFragment.BackPress , BarCodeScannerFragment.BackPressBarCode{

    private WarrentyCheckViewModel mViewModel;
    WarrentyCheckFragmentBinding binding;
    boolean onetime = true;
    ScannerFragment scannerFragment;
    BarCodeScannerFragment barCodeScannerFragment;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    NavController navController;

    public static WarrentyCheckFragment newInstance() {
        return new WarrentyCheckFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = WarrentyCheckFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WarrentyCheckViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        navController= Navigation.findNavController(view);
        scannerFragment = new ScannerFragment(this);
        barCodeScannerFragment = new BarCodeScannerFragment(this);

        binding.scanBtn.setOnClickListener(v -> {
            onetime = true;
//            loadfragment(scannerFragment);
            binding.ImeiEdittext.setError(null);
            loadfragment(barCodeScannerFragment);
        });

        binding.ImeiEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
             if(binding.ImeiEdittext.getText().toString().isEmpty()){
                 binding.submitBtn.setClickable(false);
                 binding.submitBtn.setEnabled(false);
             }else {
                 binding.submitBtn.setClickable(true);
                 binding.submitBtn.setEnabled(true);
             }
            }
        });
        binding.addBtn.setOnClickListener(v -> {
            if(binding.ImeiEdittext.getText().toString().isEmpty()){
                binding.ImeiEdittext.setError("Enter IMEI");
                Snackbar.make(binding.getRoot(),"Enter IMEI",5000).show();
            }else {
                binding.addBtn.setClickable(false);
                binding.addBtn.setFocusable(false);
                binding.ImeiEdittext.setError(null);
                CheckWarrenty(binding.ImeiEdittext.getText().toString());
            }

        });
    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.LoadFragment, fragment).addToBackStack(null).commit();
    }

    @Override
    public void Onbackpress(String imei) {
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();

            CheckWarrenty(imei);
        }
        onetime = false;
        Log.e(TAG, "OnbackpressBarcode: "+ imei );

    }

    @Override
    public void OnbackpressBarcode(String imei) {
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
            CheckWarrenty(imei);
        }
        onetime = false;
        Log.e(TAG, "OnbackpressBarcode: "+ imei );
    }

    void CheckWarrenty(String imei){
        Map<String , String> map = new HashMap<>();
        map.put("imei",imei);
        map.put("retailer_id",sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID));
        binding.progressbar.setVisibility(View.VISIBLE);
      lavaInterface.WARRENTYCHECK(map).enqueue(new Callback<Object>() {
          @Override
          public void onResponse(Call<Object> call, Response<Object> response) {
              Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
              binding.addBtn.setClickable(false);
              binding.addBtn.setFocusable(false);
              JSONObject jsonObject = null;
              try {
                  jsonObject = new JSONObject(new Gson().toJson(response.body()));
                  String error = jsonObject.getString("error");
                  String message = jsonObject.optString("message");
                  int error_code = jsonObject.getInt("error_code");

                  if (error.equalsIgnoreCase("false")) {
                      JSONObject data = jsonObject.getJSONObject("detail");
                      String pName= data.optString("marketing_name");
                      String psku= data.optString("sku");
                      String pModel= data.optString("model");
                      String pNamear= data.optString("marketing_name_fr");
                      String pNamefr= data.optString("marketing_name_fr");
                      String Modelar= data.optString("model_ar");
                      if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

                          AlertDialog(pName,psku,pModel);

                      }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
                          if(pNamefr.isEmpty()){
                              AlertDialog(pName,psku,pModel);
                          }else {
                              AlertDialog(pName,psku,pModel);
                          }

                      } else {
                          if(pNamear.isEmpty()){
                              AlertDialog(pName,psku,pModel);
                          }else {
                              AlertDialog(pNamear,psku,Modelar);
                          }
                      }
                      Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                      binding.progressbar.setVisibility(View.GONE);
                      return;
                  }
                  binding.progressbar.setVisibility(View.GONE);
                  Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
                  Snackbar.make(binding.getRoot(),message,5000).show();
                  AlertDialogfailure(message);
              } catch (JSONException e) {
                  e.printStackTrace();
              }

              binding.addBtn.setClickable(true);
              binding.addBtn.setFocusable(true);
              binding.progressbar.setVisibility(View.GONE);
          }

          @Override
          public void onFailure(Call<Object> call, Throwable t) {
              binding.addBtn.setClickable(false);
              binding.addBtn.setFocusable(false);
              binding.progressbar.setVisibility(View.GONE);
              Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
              Log.e(TAG, "onFailure: " + t.getMessage());
          }
      });

    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Warrenty Check");
    }


    private void AlertDialog(String pname, String pSku, String pModel){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_imei_check , null );
        builder.setView(v);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView name = v.findViewById(R.id.des);
        MaterialTextView sku = v.findViewById(R.id.sku);
        MaterialTextView model = v.findViewById(R.id.model);


        name.setText(pname);
        sku.setText("SKU - "+ pSku);
        model.setText("Model - "+ pModel);



        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){

        } else {

        }




        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});



    }


    private void AlertDialogfailure(String msg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_imei_not_exits , null );
        builder.setView(v);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView des = v.findViewById(R.id.des);

        des.setText(msg);


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){

        } else {

        }




        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});



    }


}