package com.apptech.lava_retailer.ui.warranty.serialize;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.SerializeFragmentBinding;
import com.apptech.lava_retailer.ui.qr.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerializeFragment extends Fragment implements ScannerFragment.BackPress  , BarCodeScannerFragment.BackPressBarCode{

    private SerializeViewModel mViewModel;
    SerializeFragmentBinding binding;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    private static final String TAG = "SerializeFragment";
    String mess = "this imei not exists in our system please go to non serialized warranty system";
    ScannerFragment scannerFragment;
    BarCodeScannerFragment barCodeScannerFragment;
    boolean onetime = true;
    NavController navController;
    AlertDialog alertDialog1;
    int imeiCount = 0;
    View rowView;
    TextView textView , Modal , ModalTitle;
    LinearLayout mainLayout;
    LinearLayout removeBtn;
    String SELL_DATE="", HANDSET_REPLACE="", REPLACE_ITEM= "";
    private boolean NoData = true, Wrongdatra = true;
    String NEW_IMEI="", Replace_Imei="";
    String IMEI ="" , WARRANTY_TYPE_MOB = "", WARRANTY_TYPE_CHAR = "", WARRANTY_TYPE_EAR = "" , WARRANTY_PERIOD_MOB ="" , WARRANTY_PERIOD_CHAR ="" , WARRANTY_PERIOD_EAR ="" , ITEM_PURCHASE_DATE_date ="";
    JsonObject mainObject = new JsonObject();
    boolean WARRANTY_TYPE_MOB1 = false , WARRANTY_TYPE_EAR1 =false , WARRANTY_TYPE_CHAR1 = false;
    boolean WarrantyMob = false , WarrantyPhone = false , WarrantyEarPhone = false;
    JSONObject Replace_item;
    int k=0;

    public static SerializeFragment newInstance() {
        return new SerializeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SerializeFragmentBinding.inflate(inflater , container  , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SerializeViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());
        scannerFragment = new ScannerFragment(this);
        barCodeScannerFragment = new BarCodeScannerFragment(this);
        Replace_item= new JSONObject();




        binding.addBtn.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                if (validation()) {
                    WarrantyCheck(binding.ImeiEdittext.getText().toString().trim());
                    Replace_item= new JSONObject();
                    return;
                }
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });

        binding.submitBtn.setOnClickListener(v -> {
            Log.e(TAG, "onActivityCreated: " +  Replace_item.length() );
            if(Replace_item.length()!=0){
                if(binding.MobCheckbox.isChecked()){
                    withouthandset();
//                    CheckIMei(NEW_IMEI , 1);
                }else {
                    withouthandset();
                }

            }else {
                AlertDialogfailure("Select Item");
            }

        });

        binding.scanBtn.setOnClickListener(v -> {
            binding.ImeiEdittext.setError(null);
            loadfragment(barCodeScannerFragment);
        });


        binding.MobCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            WARRANTY_TYPE_MOB1 = isChecked;

            if(isChecked) {
                WARRANTY_TYPE_MOB = "MOBILE";
            }else {
                WARRANTY_TYPE_MOB = "";
            }
        });

        binding.ChargerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            WARRANTY_TYPE_CHAR1 = isChecked;

            if(isChecked) {
                WARRANTY_TYPE_CHAR = "CHARGER";
            }else {
                WARRANTY_TYPE_CHAR = "";
            }
        });

        binding.EarphoneCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            WARRANTY_TYPE_EAR1 = isChecked;

            if(isChecked) {
                WARRANTY_TYPE_EAR = "EARPHONE";
            }else {
                WARRANTY_TYPE_EAR = "";
            }
        });


//        binding.submitBtn.setOnClickListener(v -> {
//            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
//                if(validation1()){
//                    WarrantySubmit();
//                }
//                return;
//            }
//            Toast.makeText(requireContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
//        });

        binding.MobCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                HANDSET_REPLACE="YES";
                REPLACE_ITEM="HANDSET";
                HandsetReturn(1, NEW_IMEI);
//               CheckIMei(binding.ImeiEdittext.getText().toString(),1);
//               binding.MobCheckbox.setEnabled(false);
            }else {
                Replace_item.remove("1");
            }
        });

        binding.BatteryCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                HANDSET_REPLACE="NO";
                REPLACE_ITEM="BATTERY";
//                NEW_IMEI ="";
//                Submit(2);
                try {
                    Replace_item.putOpt("2","BATTERY");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                binding.BatteryCheckbox.setEnabled(false);
            }else {
                Replace_item.remove("2");

            }
        });

        binding.AdapterCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                HANDSET_REPLACE="NO";
                REPLACE_ITEM="ADAPTER";
//                NEW_IMEI ="";
//                Submit(3);
                try {
                    Replace_item.putOpt("3","ADAPTER");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                binding.AdapterCheckbox.setEnabled(false);
            }else {
                Replace_item.remove("3");
            }
        });

        binding.EarphoneCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                HANDSET_REPLACE="NO";
                REPLACE_ITEM="EARPHONE";
//                NEW_IMEI ="";
//                Submit(4);
                try {
                    Replace_item.putOpt("4","EARPHONE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                binding.EarphoneCheckbox.setEnabled(false);
            }else {
                Replace_item.remove("4");
            }
        });

        binding.ChargerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                HANDSET_REPLACE="NO";
                REPLACE_ITEM="CHARGER";
//                NEW_IMEI ="";
//                Submit(5);
                try {
                    Replace_item.putOpt("5","CHARGER");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                binding.ChargerCheckbox.setEnabled(false);
            }else {
                Replace_item.remove("5");
            }
        });

        binding.USBCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                HANDSET_REPLACE="NO";
                REPLACE_ITEM="USB";
//                NEW_IMEI ="";
//                Submit(6);
                try {
                    Replace_item.putOpt("6","USB");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                binding.ChargerCheckbox.setEnabled(false);
            }else {
                Replace_item.remove("6");
            }
        });



    }

    private boolean validation1(){
        if(WARRANTY_TYPE_MOB1 || WARRANTY_TYPE_CHAR1 || WARRANTY_TYPE_EAR1){
            return  true;
        }
        Toast.makeText(getActivity(), "Select accessories first", Toast.LENGTH_SHORT).show();
        return false ;
    }


    private void WarrantySubmit() {

        binding.progressbar.setVisibility(View.VISIBLE);

        String id = sessionManage.getUserDetails().get("ID");
        String name = sessionManage.getUserDetails().get("NAME");

        JsonArray jsonElements = new JsonArray();
        mainObject.addProperty("retailer_id" , id);
        mainObject.addProperty("retailer_name" , name);

        if(WARRANTY_TYPE_MOB1){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("imei" , IMEI);
            jsonObject.addProperty("warranty_type" , WARRANTY_TYPE_MOB);
            jsonObject.addProperty("war_period" , WARRANTY_PERIOD_MOB);
            jsonObject.addProperty("item_purchase_date" , ITEM_PURCHASE_DATE_date);
            jsonElements.add(jsonObject);
        }

        if(WARRANTY_TYPE_EAR1){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("imei" , IMEI);
            jsonObject.addProperty("warranty_type" , WARRANTY_TYPE_MOB);
            jsonObject.addProperty("war_period" , WARRANTY_PERIOD_MOB);
            jsonObject.addProperty("item_purchase_date" , ITEM_PURCHASE_DATE_date);
            jsonElements.add(jsonObject);
        }
        if(WARRANTY_TYPE_CHAR1){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("imei" , IMEI);
            jsonObject.addProperty("warranty_type" , WARRANTY_TYPE_MOB);
            jsonObject.addProperty("war_period" , WARRANTY_PERIOD_MOB);
            jsonObject.addProperty("item_purchase_date" , ITEM_PURCHASE_DATE_date);
            jsonElements.add(jsonObject);
        }
        mainObject.add("item_list" , jsonElements);

        Log.e(TAG, "WarrantySubmit: " + mainObject.toString());
        lavaInterface.SUBMIT_SERI(mainObject).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");

                    if(error.equalsIgnoreCase("false")){
//                        startActivity(new Intent(getContext() , MainActivity.class));
                        navController.navigate(R.id.serializeFragment);
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
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.LoadScanner, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Replacement_process));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Calendar Date_Convert_String_To_Calender(String d) throws ParseException {
        Date date = df.parse(d);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return  cal;
    }

    private boolean validation() {
        return ImeiValid(binding.ImeiEdittext.getText().toString().trim()) ;
    }

    private boolean ImeiValid(String imei) {
        if (imei.isEmpty()) {
            binding.ImeiError.setVisibility(View.VISIBLE);
            binding.ImeiError.setText(getResources().getString(R.string.field_required));
            binding.ImeiEdittext.setError(getResources().getString(R.string.field_required));
            return false;
        }
        if (imei.length() != 15) {
            binding.ImeiError.setText("Invalid Imei code");
            binding.ImeiError.setVisibility(View.VISIBLE);
            binding.ImeiEdittext.setError("Invalid Imei code");
            return false;
        }
        binding.ImeiEdittext.setError(null);
        binding.ImeiError.setVisibility(View.GONE);
        return true;
    }


    public long getMilliFromDate(String dateFormat) {
        Date date = new Date();
        try {
            date = df.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " + date);
        return date.getTime();
    }


    public static String getNormalDate(long timeInMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.format(timeInMillies);
        System.out.println("Today is " + date);
        return date;
    }


    private void WarrantyCheck(String imei) {

        binding.submit.setEnabled(false);
        binding.progressbar.setVisibility(View.VISIBLE);
        Map<String , String> map = new HashMap<>();
        map.put("imei",imei);
//        map.put("retailer_id","ff2326e2cf317a74fef52f267662a1ef");
        map.put("retailer_id",sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID));
        map.put("country_name",sessionManage.getUserDetails().get(SessionManage.COUNTRY_NAME));
        map.put("country_id",sessionManage.getUserDetails().get(SessionManage.COUNTRY_ID));

//        lavaInterface.WARRANTY_CHECK(id,imei).enqueue(new Callback<Object>() {
        lavaInterface.WARRENTYCHECK(map).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: "  + response.body().toString());

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");
                    binding.addLayout.removeAllViews();

                    if(error.equalsIgnoreCase("false")){

                        JSONObject object = jsonObject.getJSONObject("detail");
                        switch (error_code){
//                            case 200:
//                                switch (sessionManage.getUserDetails().get("LANGUAGE")){
//                                    case "en":
//                                    case "fr":
//                                        addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model") ,object.optString("imei") , object.optString("distributor_name") , message );
//                                        break;
//                                    case "ar":
//                                        addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model_ar") ,object.optString("imei"),object.optString("distributor_name") , message);
//                                        break;
//                                }
//                                break;
                            case 301:
                                switch (sessionManage.getUserDetails().get("LANGUAGE")){
                                    case "en":
                                    case "fr":
                                        addView(ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_background, null) , 301 , object.optString("model") ,object.optString("imei") , object.optString("distributor_name") ,message);
                                        break;
                                    case "ar":
                                        addView(ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_background, null) , 301 , object.optString("model_ar"),object.optString("imei") , object.optString("distributor_name"), message);
                                        break;
                                }
                                binding.progressbar.setVisibility(View.GONE);
                                return;

                        }



                        binding.addLayout.removeAllViews();
                        JSONObject order_detail = jsonObject.getJSONObject("detail");


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


//                       sell out of this imei is not reported.
                        if(order_detail.optString("sell_out_date").equals("")){
                            binding.progressbar.setVisibility(View.GONE);
                            AlertDialogfailure("Tertiary Date or Sell out Date Not found in our System Please Contact to Distributer!");
                            return;
                        }

                        binding.IssueDateTitle.setVisibility(View.GONE);
                        binding.IssueDateMob.setVisibility(View.GONE);
                        binding.MobCheckbox.setChecked(false);
                        binding.MobCheckbox.setEnabled(true);
                        binding.BatteryCheckbox.setChecked(false);
                        binding.BatteryCheckbox.setEnabled(true);
                        binding.AdapterCheckbox.setChecked(false);
                        binding.AdapterCheckbox.setEnabled(true);
                        binding.EarphoneCheckbox.setChecked(false);
                        binding.EarphoneCheckbox.setEnabled(true);
                        binding.ChargerCheckbox.setChecked(false);
                        binding.ChargerCheckbox.setEnabled(true);
                        binding.USBCheckbox.setChecked(false);
                        binding.USBCheckbox.setEnabled(true);




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

                        binding.ProductLayout.setVisibility(View.VISIBLE);

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

                        return;
                    }

                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.submit.setEnabled(true);
                    binding.progressbar.setVisibility(View.GONE);
                    AlertDialogfailure(message);
                    return;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                }

                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.submit.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Time out" , Toast.LENGTH_SHORT).show();
                binding.submit.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void Onbackpress(String imei) {
        binding.ImeiEdittext.setText(imei);
        if (onetime) {
            if(validation()){

                getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
                WarrantyCheck(imei);
                Replace_item= new JSONObject();
            }
        }
        onetime = false;

    }

    @Override
    public void OnbackpressBarcode(String imei) {
        binding.ImeiEdittext.setText(imei);
        if (onetime) {
            if(validation()){
                binding.ImeiEdittext.setText(imei);
                getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
                WarrantyCheck(imei);
                Replace_item= new JSONObject();
            }
        }
        onetime = false;
    }



    private void HandsetReturn(int i , String imei){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_warrant_imei_replace , null);
        LinearLayout close = view.findViewById(R.id.close);
        LinearLayout submit = view.findViewById(R.id.submit);
        TextView msg= view.findViewById(R.id.msg);
        TextInputLayout addressEdittext = view.findViewById(R.id.addressEdittext);
        addressEdittext.getEditText().setText(imei);
        close.setOnClickListener(v -> {
            switch (i) {
                case 1:
                    binding.IssueDateTitle.setVisibility(View.GONE);
                    binding.IssueDateMob.setVisibility(View.GONE);
                    binding.MobCheckbox.setChecked(false);
                    binding.MobCheckbox.setEnabled(true);
                    break;
                case 2:
                    binding.BatteryCheckbox.setChecked(false);
                    binding.BatteryCheckbox.setEnabled(true);
                    break;
                case 3:
                    binding.AdapterCheckbox.setChecked(false);
                    binding.AdapterCheckbox.setEnabled(true);
                    break;
                case 4:
                    binding.EarphoneCheckbox.setChecked(false);
                    binding.EarphoneCheckbox.setEnabled(true);
                    break;
                case 5:
                    binding.ChargerCheckbox.setChecked(false);
                    binding.ChargerCheckbox.setEnabled(true);
                    break;
                case 6:
                    binding.USBCheckbox.setChecked(false);
                    binding.USBCheckbox.setEnabled(true);
                    break;
            }
            alertDialog1.dismiss();});
        submit.setOnClickListener(v -> {
            if(addressEdittext.getEditText().getText().toString().trim().isEmpty() || addressEdittext.getEditText().getText().length()!=15){
                addressEdittext.setError(getResources().getString(R.string.INVALID_IMEI));
                addressEdittext.setErrorEnabled(true);
                msg.setVisibility(View.GONE);
                return;
            }
            addressEdittext.setError(null);
            addressEdittext.setErrorEnabled(false);
            NEW_IMEI = addressEdittext.getEditText().getText().toString();
//            CheckIMei(addressEdittext.getEditText().getText().toString(),i);
            binding.IssueDateMob.setText(NEW_IMEI);
            binding.IssueDateTitle.setVisibility(View.VISIBLE);
            binding.IssueDateMob.setVisibility(View.VISIBLE);
            binding.IssueDateTitle.setOnClickListener(v1 -> {
                HandsetReturn(i ,NEW_IMEI);
            });
            CheckIMei(addressEdittext.getEditText().getText().toString(), i ,alertDialog1, msg);

//            alertDialog1.dismiss();
        });

        builder.setView(view);

        alertDialog1 = builder.create();
        alertDialog1.setCancelable(false);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

    };

    private void addView(Drawable drawable , int type , String modals , String imeis , String distributor_name , String mess ) {

        int a = imeiCount += 1;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.add_view, null);
        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);

        textView = rowView.findViewById(R.id.imei);
        mainLayout = rowView.findViewById(R.id.mainLayout);
        Modal = rowView.findViewById(R.id.Modal);
        TextView ModalTitle = rowView.findViewById(R.id.ModalTitle);
        TextView imei = rowView.findViewById(R.id.imei);
        TextView distributorName = rowView.findViewById(R.id.distributorName);
        TextView msg = rowView.findViewById(R.id.msg);
        ImageView icon  = rowView.findViewById(R.id.icon);

        mainLayout.setBackground(drawable);
        textView.setText(binding.ImeiEdittext.getText().toString().trim());
        textView.setTag(String.valueOf(a));
        removeBtn = rowView.findViewById(R.id.remove);

        if (type == 200) {
            icon.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
            distributorName.setText(distributor_name);
            msg.setText(mess);
            msg.setTextColor(getResources().getColor(R.color.green));
        } else {
            icon.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic__check, null));
            distributorName.setText(distributor_name);
            msg.setText(mess);
            msg.setTextColor(getResources().getColor(R.color.yellow));
        }
        Modal.setText(modals);
        imei.setText(imeis);


        removeView();
        binding.ImeiEdittext.setText(null);
        NoData = true;
        Wrongdatra = true;
    }

    private void removeView() {
        removeBtn.setOnClickListener(v -> {
            binding.addLayout.removeView((View) v.getParent().getParent().getParent());
            if (binding.addLayout.getChildCount() == 0) {
                NoData = true;
                Wrongdatra = true;
                imeiCount = 0;
                binding.msgShowWrongImei.setVisibility(View.GONE);
            }
        });
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

    void AlertAllReturn(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Message")
                .setMessage("Do You Wan't replace All Items!")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    binding.MobCheckbox.setChecked(true);
                    binding.AdapterCheckbox.setChecked(true);
                    binding.BatteryCheckbox.setChecked(true);
                    binding.EarphoneCheckbox.setChecked(true);
                    binding.ChargerCheckbox.setChecked(true);
                    binding.USBCheckbox.setChecked(true);
                    dialogInterface.cancel();
                })
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .show();
    }

    void Submit(int i,boolean alert){



        Map<String, String> map = new HashMap<>();
        map.put("country_name", sessionManage.getUserDetails().get(SessionManage.COUNTRY_NAME));
        map.put("country_id", sessionManage.getUserDetails().get(SessionManage.COUNTRY_ID));
        map.put("retailer_id", sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID));
        map.put("retailer_name", sessionManage.getUserDetails().get(SessionManage.NAME));
        map.put("locality_id", sessionManage.getUserDetails().get(SessionManage.LOCALITY_ID));
        map.put("locality_name", sessionManage.getUserDetails().get(SessionManage.LOCALITY));
        map.put("imei", Replace_Imei);
        map.put("sell_date", SELL_DATE);
        map.put("handest_replace", HANDSET_REPLACE);
        map.put("item_name", REPLACE_ITEM);
        map.put("imei_original", binding.ImeiEdittext.getText().toString());
        lavaInterface.REPLACEMENT_WARENTY(map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + response.body().toString());

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");


                    if (error.equalsIgnoreCase("false")) {
                        if(alert){
                            AlertDialogfailure(message);
                            binding.submitBtn.setVisibility(View.GONE);
                            binding.ProductLayout.setVisibility(View.GONE);
                            binding.ImeiEdittext.setText("");
                        }
                        switch (i) {
                            case 1:
                                binding.MobCheckbox.setChecked(true);
                                binding.MobCheckbox.setEnabled(false);
                                break;
                            case 2:
                                binding.BatteryCheckbox.setChecked(true);
                                binding.BatteryCheckbox.setEnabled(false);
                                break;
                            case 3:
                                binding.AdapterCheckbox.setChecked(true);
                                binding.AdapterCheckbox.setEnabled(false);
                                break;
                            case 4:
                                binding.EarphoneCheckbox.setChecked(true);
                                binding.EarphoneCheckbox.setEnabled(false);
                                break;
                            case 5:
                                binding.ChargerCheckbox.setChecked(true);
                                binding.ChargerCheckbox.setEnabled(false);
                                break;
                            case 6:
                                binding.USBCheckbox.setChecked(true);
                                binding.USBCheckbox.setEnabled(false);
                                break;

                        }
                        return;
                    }

                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.submit.setEnabled(true);
                    binding.progressbar.setVisibility(View.GONE);
                    AlertDialogfailure(message);
                    switch (i) {
                        case 1:
                            binding.MobCheckbox.setChecked(false);
                            binding.MobCheckbox.setEnabled(true);
                            break;
                        case 2:
                            binding.BatteryCheckbox.setChecked(false);
                            binding.BatteryCheckbox.setEnabled(true);
                            break;
                        case 3:
                            binding.AdapterCheckbox.setChecked(false);
                            binding.AdapterCheckbox.setEnabled(true);
                            break;
                        case 4:
                            binding.EarphoneCheckbox.setChecked(false);
                            binding.EarphoneCheckbox.setEnabled(true);
                            break;
                        case 5:
                            binding.ChargerCheckbox.setChecked(false);
                            binding.ChargerCheckbox.setEnabled(true);
                            break;
                        case 6:
                            binding.USBCheckbox.setChecked(false);
                            binding.USBCheckbox.setEnabled(true);
                            break;

                    }
                    return;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.submit.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
                binding.submit.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

    void withouthandset(){
        Iterator<String> keys = Replace_item.keys();
        k=0;
        while(keys.hasNext()) {
            String key = keys.next();
            k++;
            if (key.equals("1")) {
                HANDSET_REPLACE = "YES";
                Replace_Imei = NEW_IMEI;
            } else {
                HANDSET_REPLACE = "NO";
                Replace_Imei= "";

            }
            Log.e(TAG, "withouthandset: "+ k+ "  ===  "+ Replace_item.length() );
            if(Replace_item.length()==k){
                REPLACE_ITEM = Replace_item.optString(key);
                Submit(Integer.parseInt(key),true);
            }else {
                REPLACE_ITEM = Replace_item.optString(key);
                Submit(Integer.parseInt(key),false);
            }
        }
    }


    void CheckIMei(String imei , int i , Dialog dialog, TextView msg){
        lavaInterface.IMEI_CHECK(imei, sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID)
        ,sessionManage.getUserDetails().get(SessionManage.COUNTRY_NAME)
                ,sessionManage.getUserDetails().get(SessionManage.COUNTRY_ID))
                .enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: "  + response.body().toString());

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");


                    if(error.equalsIgnoreCase("false")){
                        Replace_item.putOpt("1","MOBILE");
                        Replace_Imei = NEW_IMEI;
                        NEW_IMEI = imei;


                        msg.setVisibility(View.GONE);
                        dialog.cancel();
//                        k=0;
//                        Iterator<String> keys = jsonObject.keys();
//
//                        while(keys.hasNext()) {
//                            String key = keys.next();
//                            k++;
//                            if (key.equals("1")) {
//                                HANDSET_REPLACE = "YES";
//                            } else {
//                                HANDSET_REPLACE = "NO";
//                            }
//                            if(jsonObject.length()==k){
//                                REPLACE_ITEM = jsonObject.optString(key);
//                                Submit(Integer.parseInt(key),true);
//                            }else {
//                                REPLACE_ITEM = jsonObject.optString(key);
//                                Submit(Integer.parseInt(key),false);
//                            }
//                        }

//                        HandsetReturn(i);
                        return;
                    }

//                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.submit.setEnabled(true);
                    binding.progressbar.setVisibility(View.GONE);
                    msg.setText(message);
                    msg.setVisibility(View.VISIBLE);
//                    AlertDialogfailure(message);
                    switch (i){
                        case 1:
//                            binding.MobCheckbox.setChecked(false);
//                            binding.MobCheckbox.setEnabled(true);

                    }
                    return;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                }

                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.submit.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Time out" , Toast.LENGTH_SHORT).show();
                binding.submit.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

}































