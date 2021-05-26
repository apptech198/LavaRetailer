package com.apptech.lava_retailer.ui.warranty.serialize;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.SerializeFragmentBinding;
import com.apptech.lava_retailer.fragment.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    String IMEI ="" , WARRANTY_TYPE_MOB = "", WARRANTY_TYPE_CHAR = "", WARRANTY_TYPE_EAR = "" , WARRANTY_PERIOD_MOB ="" , WARRANTY_PERIOD_CHAR ="" , WARRANTY_PERIOD_EAR ="" , ITEM_PURCHASE_DATE_date ="";
    JsonObject mainObject = new JsonObject();
    boolean WARRANTY_TYPE_MOB1 = false , WARRANTY_TYPE_EAR1 =false , WARRANTY_TYPE_CHAR1 = false;
    boolean WarrantyMob = false , WarrantyPhone = false , WarrantyEarPhone = false;

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





        binding.submit.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                if (validation()) {
                    WarrantyCheck(binding.Imei.getText().toString().trim());
                    return;
                }
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });

        binding.ScanImei.setOnClickListener(v -> {
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


        binding.submitBtn.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                if(validation1()){
                    WarrantySubmit();
                }
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
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
        title.setText("Serialize Warranty");
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
        return ImeiValid(binding.Imei.getText().toString().trim()) ;
    }

    private boolean ImeiValid(String imei) {
        if (imei.isEmpty()) {
            binding.ImeiError.setVisibility(View.VISIBLE);
            binding.ImeiError.setText(getResources().getString(R.string.field_required));
            binding.Imei.setError(getResources().getString(R.string.field_required));
            return false;
        }
        if (imei.length() != 15) {
            binding.ImeiError.setText("Invalid Imei code");
            binding.ImeiError.setVisibility(View.VISIBLE);
            binding.Imei.setError("Invalid Imei code");
            return false;
        }
        binding.Imei.setError(null);
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

        String id = sessionManage.getUserDetails().get("ID");

//        lavaInterface.WARRANTY_CHECK(id,imei).enqueue(new Callback<Object>() {
        lavaInterface.WARRANTY_CHECK("98","121212121212121").enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: "  + response.body().toString());

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");


                    if(error.equalsIgnoreCase("false")){

                        JSONObject order_detail = jsonObject.getJSONObject("order_detail");
                        String time = order_detail.getString("time");

                        String acce_mobile = order_detail.getString("acce_mobile");
                        String acce_mobile_war = order_detail.getString("acce_mobile_war");

                        String acce_charger = order_detail.getString("acce_charger");
                        String acce_charger_war = order_detail.getString("acce_charger_war");

                        String acce_earphone = order_detail.getString("acce_earphone");
                        String acce_earphone_war = order_detail.getString("acce_earphone_war");

                        IMEI = order_detail.getString("imei");;
                        WARRANTY_PERIOD_MOB = acce_mobile_war;
                        WARRANTY_PERIOD_CHAR = acce_charger_war;
                        WARRANTY_PERIOD_EAR = acce_earphone_war;
                        ITEM_PURCHASE_DATE_date= time;


                        int MobWar = 0;
                        int Charge_War = 0;
                        int Earphone_War = 0;
                        try {
                            MobWar = Integer.parseInt(acce_mobile_war);
                            Charge_War = Integer.parseInt(acce_charger_war);
                            Earphone_War = Integer.parseInt(acce_earphone_war);
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
                            MobcalendarFutureDate.add(Calendar.MONTH, MobWar);
                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
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

                            }else {
//                                out of warranty
                                binding.MobCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.MobMessage.setText("out of warrtanty");
                                binding.MobMessage.setTextColor(getResources().getColor(R.color.red));
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
                            MobcalendarFutureDate.add(Calendar.MONTH, Charge_War);
                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
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

                            }else {
//                                out of warranty
                                binding.ChargerCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.ChargerMessage.setText("out of warrtanty");
                                binding.ChargerMessage.setTextColor(getResources().getColor(R.color.red));
                            }


                        }else {
                            binding.Chargerayout.setVisibility(View.GONE);
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
                            MobcalendarFutureDate.add(Calendar.MONTH, Earphone_War);
                            MobcalendarFutureDate.set(Calendar.DAY_OF_WEEK  , Integer.parseInt(MobDatesplit[2]));
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

                            }else {
//                                out of warranty
                                binding.EarphoneCheckbox.setEnabled(false);
//                                Toast.makeText(getContext(), "out of warrant", Toast.LENGTH_SHORT).show();

                                binding.EarphoneMessage.setText("out of warrtanty");
                                binding.EarphoneMessage.setTextColor(getResources().getColor(R.color.red));
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
        if (onetime) {
            if(validation()){
                getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
                WarrantyCheck(imei);
            }
        }
        onetime = false;

    }

    @Override
    public void OnbackpressBarcode(String imei) {
        if (onetime) {
            if(validation()){
                getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
                WarrantyCheck(imei);
            }
        }
        onetime = false;
    }





    private void HandsetReturn(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_warrant_imei_replace , null);
        LinearLayout close = view.findViewById(R.id.close);
        LinearLayout submit = view.findViewById(R.id.submit);

        TextInputLayout addressEdittext = view.findViewById(R.id.addressEdittext);
        close.setOnClickListener(v -> {alertDialog1.dismiss();});
        submit.setOnClickListener(v -> {
            if(addressEdittext.getEditText().getText().toString().trim().isEmpty()){
                addressEdittext.setError(getResources().getString(R.string.field_required));
                addressEdittext.setErrorEnabled(true);
                return;
            }
            addressEdittext.setError(null);
            addressEdittext.setErrorEnabled(false);

            String add = addressEdittext.getEditText().getText().toString().trim();
            sessionManage.AddressChange(add);
            alertDialog1.dismiss();
        });

        builder.setView(view);
        alertDialog1 = builder.create();
        alertDialog1.show();

    };


}































