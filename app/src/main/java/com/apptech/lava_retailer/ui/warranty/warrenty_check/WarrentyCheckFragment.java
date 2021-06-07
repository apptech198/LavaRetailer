package com.apptech.lava_retailer.ui.warranty.warrenty_check;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
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
import com.apptech.lava_retailer.ui.qr.ScannerFragment;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String SELL_DATE="", HANDSET_REPLACE="", REPLACE_ITEM= "";
    private boolean NoData = true, Wrongdatra = true;
    String NEW_IMEI="";
    String IMEI ="" , WARRANTY_TYPE_MOB = "", WARRANTY_TYPE_CHAR = "", WARRANTY_TYPE_EAR = "" , WARRANTY_PERIOD_MOB ="" , WARRANTY_PERIOD_CHAR ="" , WARRANTY_PERIOD_EAR ="" , ITEM_PURCHASE_DATE_date ="";
    JsonObject mainObject = new JsonObject();
    boolean WARRANTY_TYPE_MOB1 = false , WARRANTY_TYPE_EAR1 =false , WARRANTY_TYPE_CHAR1 = false;
    boolean WarrantyMob = false , WarrantyPhone = false , WarrantyEarPhone = false;

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
                binding.addBtn.setClickable(true);
                binding.addBtn.setFocusable(true);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");

                    if (error.equalsIgnoreCase("false")) {

                        JSONObject object = jsonObject.getJSONObject("detail");
                        switch (error_code){
                            case 200:
                                binding.progressbar.setVisibility(View.GONE);
                                binding.ProductLayout.setBackground(getResources().getDrawable(R.drawable.warrenty_check_green));
                                break;
                            case 301:
                                binding.progressbar.setVisibility(View.GONE);
                                binding.ProductLayout.setBackground(getResources().getDrawable(R.drawable.warrenty_check_yellow));
                                break;

                        }
                        JSONObject order_detail = jsonObject.getJSONObject("detail");



                        binding.des.setText( order_detail.optString("marketing_name"));
                        binding.skuName.setText( order_detail.optString("sku"));
                        binding.modelName.setText( order_detail.optString("model"));
                        binding.type.setText("Warrenty Type : "+ order_detail.optString("warranty_type"));
                        binding.status.setVisibility(View.GONE);
                        binding.message.setText( message);






                        Log.e(TAG, "onResponse: "+ order_detail.optString("sellout") );
                        if(order_detail.optString("sellout").equals("NO")){
                            binding.progressbar.setVisibility(View.GONE);
                            AlertDialogfailure("Sell Out of this IMEI is not Reported!");
                            return;
                        }



                        Log.e(TAG, "onResponse: "+ order_detail.optString("warranty_type") );
                        if(!order_detail.optString("warranty_type").equals("REPLACEMENT")){
                            binding.progressbar.setVisibility(View.GONE);
                            AlertDialogfailure("This IMEI is not for Replacement only for Repair!");
                            return;
                        }


//           sell out of this imei is not reported.
                        if(order_detail.optString("sell_out_date").equals("")){
                            binding.progressbar.setVisibility(View.GONE);
                            AlertDialogfailure("Tertiary Date or Sell out Date Not found in our System Please Contact to Distributer!");
                            return;
                        }

                        binding.ProductLayout.setVisibility(View.VISIBLE);

                        String time = order_detail.getString("sell_out_date");
                        SELL_DATE = order_detail.getString("sell_out_date");
                        String tertiary_date = order_detail.optString("tertiary_warranty_date");

                        String acce_mobile = order_detail.getString("prowar");
                        String acce_mobile_war = order_detail.getString("pro_war_days");

                        String acce_charger = order_detail.getString("charger_war");
                        String acce_charger_war = order_detail.getString("charger_war_days");

                        String acce_earphone = order_detail.getString("wired_earphone_war");
                        String acce_earphone_war = order_detail.getString("wired_earphone_war_days");

                        String acce_battert = order_detail.getString("battery_war");
                        String acce_battery_war = order_detail.getString("battery_war_days");

                        String acce_usb = order_detail.getString("usb_war");
                        String acce_usb_war = order_detail.getString("usb_war_days");


                        String acce_adapter = order_detail.getString("charging_adapter_war");
                        String acce_adapter_war = order_detail.getString("charging_adapter_war_days");

                        IMEI = order_detail.getString("imei");;
                        WARRANTY_PERIOD_MOB = acce_mobile_war;
                        WARRANTY_PERIOD_CHAR = acce_charger_war;
                        WARRANTY_PERIOD_EAR = acce_earphone_war;

//                        time ="2019-05-29 00:00:00";
                        if(tertiary_date==null) {
                            Calendar ORDERDATE = Date_Convert_String_To_Calender(time);
                            Calendar TERTIARY = Date_Convert_String_To_Calender(tertiary_date);
                            if (ORDERDATE.after(TERTIARY)) {
                                time = tertiary_date;
                            }
                        }



                        ITEM_PURCHASE_DATE_date= time;


                        int MobWar = 0;
                        int Charge_War = 0;
                        int Earphone_War = 0;
                        int Battery_War = 0;
                        int USB_War = 0;
                        int Adapter_War = 0;
                        try {
                            MobWar = Integer.parseInt(acce_mobile_war);
                            Charge_War = Integer.parseInt(acce_charger_war);
                            Earphone_War = Integer.parseInt(acce_earphone_war);
                            Battery_War = Integer.parseInt(acce_battery_war);
                            USB_War = Integer.parseInt(acce_usb_war);
                            Adapter_War = Integer.parseInt(acce_adapter_war);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage() );
                        }

//                        binding.ProductLayout.setVisibility(View.VISIBLE);

                        if(acce_mobile.equalsIgnoreCase("YES")){

                            binding.MobLayout.setVisibility(View.VISIBLE);
                            binding.NoWaranty.setVisibility(View.GONE);
                            binding.submitBtn.setVisibility(View.VISIBLE);

                            WarrantyMob = false;

                            String d = df.format(new Date().getTime());

                            Calendar MobIssueDate =  Date_Convert_String_To_Calender(time);

                            String[] MobDatesplit = df.format(MobIssueDate.getTime()).split("-");
                            Calendar MobtodayDate =  Date_Convert_String_To_Calender(d);

                            Calendar MobcalendarFutureDate = Calendar.getInstance();
                            MobcalendarFutureDate.set(Integer.parseInt(MobDatesplit[0]),Integer.parseInt(MobDatesplit[1]),Integer.parseInt(MobDatesplit[2]));
                            MobcalendarFutureDate.add(Calendar.MONTH, MobWar);
//                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
//                            MobcalendarFutureDate.set(Calendar.YEAR  , Integer.parseInt(MobDatesplit[0]));




                            Log.e(TAG, "onResponse MobIssueDate: " + df.format(MobIssueDate.getTime()) );
                            Log.e(TAG, "onResponse MobtodayDate: " + df.format(MobtodayDate.getTime()));
                            Log.e(TAG, "onResponse futherdate : " + df.format(MobcalendarFutureDate.getTime()) );

                            binding.IssueDateMob.setText(String.valueOf(df.format(MobIssueDate.getTime())));
                            binding.WarrantyDateMob.setText(String.valueOf(df.format(MobcalendarFutureDate.getTime())));


                            if(MobcalendarFutureDate.after(MobtodayDate)){
//                                warranty
                                binding.MobCheckbox.setEnabled(true);
//                                Toast.makeText(getContext(), "warrant", Toast.LENGTH_SHORT).show();


                                binding.MobMessage.setText("Warranty available");
                                binding.MobMessage.setTextColor(getResources().getColor(R.color.green));
                                binding.WarrantyDateMob.setTextColor(getResources().getColor(R.color.green));

                            }else {
//                                out of warranty
                                binding.MobCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.MobMessage.setText("out of warrtanty");
                                binding.MobMessage.setTextColor(getResources().getColor(R.color.red));
                                binding.WarrantyDateMob.setTextColor(getResources().getColor(R.color.red));
                            }
                        }else {
                            binding.MobLayout.setVisibility(View.GONE);
                            WarrantyMob = true ;
                        }




                        if(acce_charger.equalsIgnoreCase("YES")){

                            binding.Chargerayout.setVisibility(View.VISIBLE);
                            binding.NoWaranty.setVisibility(View.GONE);
                            binding.submitBtn.setVisibility(View.VISIBLE);

                            WarrantyPhone = false;
                            String d = df.format(new Date().getTime());

                            Calendar MobIssueDate =  Date_Convert_String_To_Calender(time);

                            String[] MobDatesplit = df.format(MobIssueDate.getTime()).split("-");
                            Calendar MobtodayDate =  Date_Convert_String_To_Calender(d);

                            Calendar MobcalendarFutureDate = Calendar.getInstance();
                            MobcalendarFutureDate.set(Integer.parseInt(MobDatesplit[0]),Integer.parseInt(MobDatesplit[1]),Integer.parseInt(MobDatesplit[2]));
                            MobcalendarFutureDate.add(Calendar.MONTH, Charge_War);
//                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
//                            MobcalendarFutureDate.set(Calendar.YEAR  , Integer.parseInt(MobDatesplit[0]));


                            Log.e(TAG, "onResponse MobIssueDate: " + df.format(MobIssueDate.getTime()) );
                            Log.e(TAG, "onResponse MobtodayDate: " + df.format(MobtodayDate.getTime()));
                            Log.e(TAG, "onResponse futherdate : " + df.format(MobcalendarFutureDate.getTime()) );

                            binding.IssueDateChar.setText(String.valueOf(df.format(MobIssueDate.getTime())));
                            binding.WarrantyDateChar.setText(String.valueOf(df.format(MobcalendarFutureDate.getTime())));

                            if(MobcalendarFutureDate.after(MobtodayDate)){
//                                warranty
                                binding.ChargerCheckbox.setEnabled(true);
//                                Toast.makeText(getContext(), "warrant", Toast.LENGTH_SHORT).show();


                                binding.ChargerMessage.setText("Warranty available");
                                binding.ChargerMessage.setTextColor(getResources().getColor(R.color.green));
                                binding.WarrantyDateChar.setTextColor(getResources().getColor(R.color.green));

                            }else {
//                                out of warranty
                                binding.ChargerCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.ChargerMessage.setText("out of warrtanty");
                                binding.ChargerMessage.setTextColor(getResources().getColor(R.color.red));
                                binding.WarrantyDateChar.setTextColor(getResources().getColor(R.color.red));
                            }


                        }else {
                            binding.Chargerayout.setVisibility(View.GONE);
                            WarrantyPhone = true ;
                        }



                        if(acce_adapter.equalsIgnoreCase("YES")){

                            binding.AdapterLayout.setVisibility(View.VISIBLE);
                            binding.NoWaranty.setVisibility(View.GONE);
                            binding.submitBtn.setVisibility(View.VISIBLE);

                            WarrantyPhone = false;
                            String d = df.format(new Date().getTime());

                            Calendar MobIssueDate =  Date_Convert_String_To_Calender(time);

                            String[] MobDatesplit = df.format(MobIssueDate.getTime()).split("-");
                            Calendar MobtodayDate =  Date_Convert_String_To_Calender(d);

                            Calendar MobcalendarFutureDate = Calendar.getInstance();
                            MobcalendarFutureDate.set(Integer.parseInt(MobDatesplit[0]),Integer.parseInt(MobDatesplit[1]),Integer.parseInt(MobDatesplit[2]));
                            MobcalendarFutureDate.add(Calendar.MONTH, Adapter_War);
//                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
//                            MobcalendarFutureDate.set(Calendar.YEAR  , Integer.parseInt(MobDatesplit[0]));


                            Log.e(TAG, "onResponse MobIssueDate: " + df.format(MobIssueDate.getTime()) );
                            Log.e(TAG, "onResponse MobtodayDate: " + df.format(MobtodayDate.getTime()));
                            Log.e(TAG, "onResponse futherdate : " + df.format(MobcalendarFutureDate.getTime()) );

                            binding.IssueDateAdapter.setText(String.valueOf(df.format(MobIssueDate.getTime())));
                            binding.WarrantyDateAdapter.setText(String.valueOf(df.format(MobcalendarFutureDate.getTime())));

                            if(MobcalendarFutureDate.after(MobtodayDate)){
//                                warranty
                                binding.AdapterCheckbox.setEnabled(true);
//                                Toast.makeText(getContext(), "warrant", Toast.LENGTH_SHORT).show();


                                binding.AdapterMessage.setText("Warranty available");
                                binding.AdapterMessage.setTextColor(getResources().getColor(R.color.green));
                                binding.WarrantyDateAdapter.setTextColor(getResources().getColor(R.color.green));

                            }else {
//                                out of warranty
                                binding.AdapterCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.AdapterMessage.setText("out of warrtanty");
                                binding.AdapterMessage.setTextColor(getResources().getColor(R.color.red));
                                binding.WarrantyDateAdapter.setTextColor(getResources().getColor(R.color.red));
                            }


                        }else {
                            binding.AdapterLayout.setVisibility(View.GONE);
                            WarrantyPhone = true ;
                        }



                        if(acce_battert.equalsIgnoreCase("YES")){

                            binding.BatteryLayout.setVisibility(View.VISIBLE);
                            binding.NoWaranty.setVisibility(View.GONE);
                            binding.submitBtn.setVisibility(View.VISIBLE);

                            WarrantyPhone = false;
                            String d = df.format(new Date().getTime());

                            Calendar MobIssueDate =  Date_Convert_String_To_Calender(time);

                            String[] MobDatesplit = df.format(MobIssueDate.getTime()).split("-");
                            Calendar MobtodayDate =  Date_Convert_String_To_Calender(d);

                            Calendar MobcalendarFutureDate = Calendar.getInstance();
                            MobcalendarFutureDate.set(Integer.parseInt(MobDatesplit[0]),Integer.parseInt(MobDatesplit[1]),Integer.parseInt(MobDatesplit[2]));
                            MobcalendarFutureDate.add(Calendar.MONTH, Battery_War);
//                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
//                            MobcalendarFutureDate.set(Calendar.YEAR  , Integer.parseInt(MobDatesplit[0]));


                            Log.e(TAG, "onResponse MobIssueDate: " + df.format(MobIssueDate.getTime()) );
                            Log.e(TAG, "onResponse MobtodayDate: " + df.format(MobtodayDate.getTime()));
                            Log.e(TAG, "onResponse futherdate : " + df.format(MobcalendarFutureDate.getTime()) );

                            binding.IssueDateBat.setText(String.valueOf(df.format(MobIssueDate.getTime())));
                            binding.WarrantyDateBat.setText(String.valueOf(df.format(MobcalendarFutureDate.getTime())));

                            if(MobcalendarFutureDate.after(MobtodayDate)){
//                                warranty
                                binding.BatteryCheckbox.setEnabled(true);
//                                Toast.makeText(getContext(), "warrant", Toast.LENGTH_SHORT).show();


                                binding.BatteryMessage.setText("Warranty available");
                                binding.BatteryMessage.setTextColor(getResources().getColor(R.color.green));
                                binding.WarrantyDateBat.setTextColor(getResources().getColor(R.color.green));

                            }else {
//                                out of warranty
                                binding.BatteryCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.BatteryMessage.setText("out of warrtanty");
                                binding.BatteryMessage.setTextColor(getResources().getColor(R.color.red));
                                binding.WarrantyDateBat.setTextColor(getResources().getColor(R.color.red));
                            }


                        }else {
                            binding.BatteryLayout.setVisibility(View.GONE);
                            WarrantyPhone = true ;
                        }



                        if(acce_usb.equalsIgnoreCase("YES")){

                            binding.USBLayout.setVisibility(View.VISIBLE);
                            binding.NoWaranty.setVisibility(View.GONE);
                            binding.submitBtn.setVisibility(View.VISIBLE);

                            WarrantyPhone = false;
                            String d = df.format(new Date().getTime());

                            Calendar MobIssueDate =  Date_Convert_String_To_Calender(time);

                            String[] MobDatesplit = df.format(MobIssueDate.getTime()).split("-");
                            Calendar MobtodayDate =  Date_Convert_String_To_Calender(d);

                            Calendar MobcalendarFutureDate = Calendar.getInstance();
                            MobcalendarFutureDate.set(Integer.parseInt(MobDatesplit[0]),Integer.parseInt(MobDatesplit[1]),Integer.parseInt(MobDatesplit[2]));
                            MobcalendarFutureDate.add(Calendar.MONTH, USB_War);
//                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
//                            MobcalendarFutureDate.set(Calendar.YEAR  , Integer.parseInt(MobDatesplit[0]));


                            Log.e(TAG, "onResponse MobIssueDate: " + df.format(MobIssueDate.getTime()) );
                            Log.e(TAG, "onResponse MobtodayDate: " + df.format(MobtodayDate.getTime()));
                            Log.e(TAG, "onResponse futherdate : " + df.format(MobcalendarFutureDate.getTime()) );

                            binding.IssueDateUSB.setText(String.valueOf(df.format(MobIssueDate.getTime())));
                            binding.WarrantyDateUSB.setText(String.valueOf(df.format(MobcalendarFutureDate.getTime())));

                            if(MobcalendarFutureDate.after(MobtodayDate)){
//                                warranty
                                binding.USBCheckbox.setEnabled(true);
//                                Toast.makeText(getContext(), "warrant", Toast.LENGTH_SHORT).show();


                                binding.USBMessage.setText("Warranty available");
                                binding.USBMessage.setTextColor(getResources().getColor(R.color.green));
                                binding.WarrantyDateUSB.setTextColor(getResources().getColor(R.color.green));

                            }else {
//                                out of warranty
                                binding.USBCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.USBMessage.setText("out of warrtanty");
                                binding.USBMessage.setTextColor(getResources().getColor(R.color.red));
                                binding.WarrantyDateUSB.setTextColor(getResources().getColor(R.color.red));
                            }


                        }else {
                            binding.USBLayout.setVisibility(View.GONE);
                            WarrantyPhone = true ;
                        }










                        if(acce_earphone.equalsIgnoreCase("YES")){

                            binding.EarphoneLayout.setVisibility(View.VISIBLE);
                            binding.NoWaranty.setVisibility(View.GONE);
                            binding.submitBtn.setVisibility(View.VISIBLE);
                            WarrantyEarPhone = false;

                            String d = df.format(new Date().getTime());

                            Calendar MobIssueDate =  Date_Convert_String_To_Calender(time);

                            String[] MobDatesplit = df.format(MobIssueDate.getTime()).split("-");
                            Calendar MobtodayDate =  Date_Convert_String_To_Calender(d);

                            Calendar MobcalendarFutureDate = Calendar.getInstance();
                            MobcalendarFutureDate.set(Integer.parseInt(MobDatesplit[0]),Integer.parseInt(MobDatesplit[1]),Integer.parseInt(MobDatesplit[2]));
                            MobcalendarFutureDate.add(Calendar.MONTH, Earphone_War);
//                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
//                            MobcalendarFutureDate.set(Calendar.YEAR  , Integer.parseInt(MobDatesplit[0]));


                            Log.e(TAG, "onResponse MobIssueDate: " + df.format(MobIssueDate.getTime()) );
                            Log.e(TAG, "onResponse MobtodayDate: " + df.format(MobtodayDate.getTime()));
                            Log.e(TAG, "onResponse futherdate : " + df.format(MobcalendarFutureDate.getTime()) );

                            binding.IssueDateEar.setText(String.valueOf(df.format(MobIssueDate.getTime())));
                            binding.WarrantyDateEar.setText(String.valueOf(df.format(MobcalendarFutureDate.getTime())));

                            if(MobcalendarFutureDate.after(MobtodayDate)){
//                                warranty
                                binding.EarphoneCheckbox.setEnabled(true);
//                                Toast.makeText(getContext(), "warrant", Toast.LENGTH_SHORT).show();


                                binding.EarphoneMessage.setText("Warranty available");
                                binding.EarphoneMessage.setTextColor(getResources().getColor(R.color.green));
                                binding.WarrantyDateEar.setTextColor(getResources().getColor(R.color.green));

                            }else {
//                                out of warranty
                                binding.EarphoneCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.EarphoneMessage.setText("out of warrtanty");
                                binding.EarphoneMessage.setTextColor(getResources().getColor(R.color.red));
                                binding.WarrantyDateEar.setTextColor(getResources().getColor(R.color.red));
                            }


                        }else {
                            WarrantyEarPhone = true;
                            binding.EarphoneLayout.setVisibility(View.GONE);
                        }

                        if(WarrantyMob && WarrantyPhone && WarrantyEarPhone){
                            binding.NoWaranty.setText("This imei not exists in our system please go to non serialized warranty system");
                            binding.NoWaranty.setVisibility(View.VISIBLE);
                            binding.submitBtn.setVisibility(View.GONE);
                        }

                        binding.submit.setEnabled(true);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.submitBtn.setVisibility(View.GONE);








                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
                    Snackbar.make(binding.getRoot(),message,5000).show();
                    AlertDialogfailure(message);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                binding.addBtn.setClickable(true);
                binding.addBtn.setFocusable(true);
                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.addBtn.setClickable(true);
                binding.addBtn.setFocusable(true);
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


    private Calendar Date_Convert_String_To_Calender(String d) throws ParseException {
        Date date = df.parse(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return  cal;
    }
}