package com.apptech.lava_retailer.ui.sell_out.report_sell_out_entries;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ReportSellOutEntriesFragmentBinding;
import com.apptech.lava_retailer.fragment.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportSellOutEntriesFragment extends Fragment implements ScannerFragment.BackPress , BarCodeScannerFragment.BackPressBarCode {

    private ReportSellOutEntriesViewModel mViewModel;
    ReportSellOutEntriesFragmentBinding binding;
    NavController navController;
    String IMEI ;
    private static final String TAG = "ReportSellOutEntriesFra";
    int imeiCount = 0;
    View rowView;
    TextView textView , Modal , ModalTitle;
    LinearLayout removeBtn;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String USER_ID = "", TodayDate = "";
    DatePickerDialog picker;

    ScannerFragment scannerFragment;
    boolean onetime = true;
    private boolean NoData = true, Wrongdatra = true;
    JsonArray jsonElements = new JsonArray();
    JsonObject mainJsonObject = new JsonObject();
    LinearLayout mainLayout;
    BarCodeScannerFragment barCodeScannerFragment;
    String CHECK_STATUS =""  , STATUS = "";



    public static ReportSellOutEntriesFragment newInstance() {
        return new ReportSellOutEntriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Reports sell out entries");

        binding = ReportSellOutEntriesFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportSellOutEntriesViewModel.class);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        // TODO: Use the ViewModel

        binding.startDatetime.setText(getCurrentDate());
        scannerFragment = new ScannerFragment(this);
        barCodeScannerFragment = new BarCodeScannerFragment(this);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());

        USER_ID = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);
        TodayDate = getCurrentDate();


        binding.addBtn.setOnClickListener(v -> {
            addImei();
        });

        binding.startDatetime.setOnClickListener(v -> {

            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
//                            eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        binding.startDatetime.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();

        });

        binding.scanBtn.setOnClickListener(v -> {
            onetime = true;
//            loadfragment(scannerFragment);
            loadfragment(barCodeScannerFragment);
        });


        binding.submitBtn.setOnClickListener(v -> {

            binding.submitBtn.setClickable(false);
            binding.submitBtn.setEnabled(false);

            if (NoData) {
                if (new NetworkCheck().haveNetworkConnection(requireActivity())) {

                    if(binding.addLayout.getChildCount() > 0){

                        binding.progressbar.setVisibility(View.VISIBLE);

                        AlertDialog();
//                        binding.addLayout.removeAllViews();
                        return;

                    }
                    Toast.makeText(getContext(), "IMEI not exits", Toast.LENGTH_SHORT).show();
                    binding.submitBtn.setClickable(true);
                    binding.submitBtn.setEnabled(true);
                    return;

                }
                binding.submitBtn.setClickable(true);
                binding.submitBtn.setEnabled(true);
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.remove_first_invalid_imei), Toast.LENGTH_SHORT).show();
            binding.submitBtn.setClickable(true);
            binding.submitBtn.setEnabled(true);

        });

    }


    private void AlertDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comfirmation_layout , null );
        builder.setView(v);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);



        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();


            jsonElements = new JsonArray();
            for (int i = 0; i < binding.addLayout.getChildCount(); i++) {
                JsonObject jsonObject = new JsonObject();
                try {
                    int getid = i + 1;

                    TextView textView = binding.addLayout.findViewWithTag(String.valueOf(getid));

                    Log.e(TAG, "AlertDialog: " + textView.getHint());

                    jsonObject.addProperty("imei", textView.getText().toString());
                    jsonObject.addProperty("check_status", textView.getHint().toString());
                    jsonObject.addProperty("status", textView.getHint().toString());
                    jsonElements.add(jsonObject);
                } catch (NullPointerException e) {
                    Log.e(TAG, "onActivityCreated: " + e.getMessage());
                }
            }

            submitImei();
            binding.addLayout.removeAllViews();
        });
        no.setOnClickListener(view -> {
            submit.setEnabled(true);
            submit.setClickable(true);
            alertDialog.dismiss();
        binding.progressbar.setVisibility(View.GONE);});



    }



    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getResources().getString(R.string.Report_sell_through));
    }



    void submitImei() {
        Log.e(TAG, "submitImei: " + binding.startDatetime.getText().toString().trim());

        mainJsonObject.addProperty("date", binding.startDatetime.getText().toString().trim());
        mainJsonObject.addProperty("retailer_id", USER_ID);
        mainJsonObject.addProperty("retailer_name", sessionManage.getUserDetails().get(SessionManage.NAME));
        mainJsonObject.addProperty("locality_name", sessionManage.getUserDetails().get(SessionManage.LOCALITY));
        mainJsonObject.addProperty("locality_id",  sessionManage.getUserDetails().get(SessionManage.LOCALITY_ID));

        mainJsonObject.add("imei_list", jsonElements);

        Log.e(TAG, "submitImei: " + mainJsonObject);

        Call call = lavaInterface.SELL_OUT_IMEI(mainJsonObject);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");
                    if (error.equalsIgnoreCase("false")) {

                        imeiCount = 0;
                        binding.addLayout.removeAllViews();

                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        binding.submitBtn.setClickable(true);
                        binding.submitBtn.setEnabled(true);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.submitBtn.setVisibility(View.GONE);
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    binding.submitBtn.setEnabled(true);
                    binding.submitBtn.setClickable(true);
                    Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.submitBtn.setClickable(true);
                binding.submitBtn.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.submitBtn.setClickable(true);
                binding.submitBtn.setEnabled(true);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


//    private void wrongimei(String imei) {
//        int a = imeiCount += 1;
//        Log.e(TAG, "addImei: idset" + a);
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        rowView = inflater.inflate(R.layout.add_view, null);
//        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);
//        textView = rowView.findViewById(R.id.imei);
//        mainLayout = rowView.findViewById(R.id.mainLayout);
//        mainLayout.setBackground(getResources().getDrawable(R.drawable.wrong_imei));
//        textView.setText(imei);
//        textView.setTag(String.valueOf(a));
//        removeBtn = rowView.findViewById(R.id.remove);
//        removeView();
//        binding.ImeiEdittext.setText(null);
//        Wrongdatra = false;
//    }

    private void removeView() {
        removeBtn.setOnClickListener(v -> {
            binding.addLayout.removeView((View) v.getParent().getParent().getParent());
            if (binding.addLayout.getChildCount() == 0) {
                NoData = true;
                Wrongdatra = true;
                imeiCount = 0;
                binding.msgShowWrongImei.setVisibility(View.GONE);
                binding.submitBtn.setVisibility(View.GONE);
            }
        });
    }


    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.LoadFragment, fragment).addToBackStack(null).commit();
    }


    @Override
    public void Onbackpress(String imei) {
        Log.e(TAG, "Onbackpress: " + imei);
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(scannerFragment).addToBackStack(null).commit();
            addImei();
        }
        onetime = false;
    }

    private void addImei() {
        if (ImeiValid(binding.ImeiEdittext.getText().toString().trim())) {
            IMEI_CHECK();
        }
    }

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
        textView.setTag(String.valueOf(a));;
        removeBtn = rowView.findViewById(R.id.remove);

        if (type == 200) {
            CHECK_STATUS = "APPROVED";
            STATUS = "APPROVED";
            textView.setHint("APPROVED");

            icon.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
            distributorName.setText(distributor_name);
            msg.setText(mess);
            msg.setTextColor(getResources().getColor(R.color.green));
        } else {
            icon.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic__check, null));
            distributorName.setText(distributor_name);
            msg.setText(mess);
            msg.setTextColor(getResources().getColor(R.color.yellow));
            CHECK_STATUS = "PENDING";
            STATUS = "PENDING";
            textView.setHint("PENDING");
        }
        Modal.setText(modals);
        imei.setText(imeis);
        binding.submitBtn.setVisibility(View.VISIBLE);

        removeView();
        binding.ImeiEdittext.setText(null);
        NoData = true;
        Wrongdatra = true;
    }

    private boolean ImeiValid(String imei) {
        if (imei.isEmpty()) {
            Toast.makeText(requireContext(), getResources().getString(R.string.field_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imei.length() != 15) {

//            Toast.makeText(requireContext(), "Invalid Imei code", Toast.LENGTH_SHORT).show();
            AlertDialogfailure("Imei Code Invalid");
            return false;
        }
        return true;
    }

    private void AlertDialogfailure(String msg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_imei_not_exits , null );
        builder.setView(v);

        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView des = v.findViewById(R.id.des);
        MaterialTextView Title = v.findViewById(R.id.Title);
        Title.setText("Alert");
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


    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void OnbackpressBarcode(String imei) {
        Log.e(TAG, "Onbackpress: " + imei);
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
            IMEI_CHECK();
        }
        onetime = false;
    }


    void IMEI_CHECK(){

        binding.progressbar.setVisibility(View.VISIBLE);

        String a = "123456789123458";

        lavaInterface.IMEI_CHECK(binding.ImeiEdittext.getText().toString().trim() , USER_ID).enqueue(new Callback<Object>() {
//        lavaInterface.IMEI_CHECK(a , "13").enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()) );

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    String message = jsonObject.optString("message");
                    String error = jsonObject.getString("error");
                    int error_code = jsonObject.getInt("error_code");

                    if(error.equalsIgnoreCase("FALSE")){

                        JSONObject object = jsonObject.getJSONObject("list");

                        switch (error_code){
                            case 200:
                                switch (sessionManage.getUserDetails().get("LANGUAGE")){
                                    case "en":
                                    case "fr":
                                        addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model") ,object.optString("imei") , object.optString("distributor_name") , message  );
                                        break;
                                    case "ar":
                                        addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model_ar") ,object.optString("imei"),object.optString("distributor_name") , message);
                                        break;
                                }
                                break;
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

                        }

//                        if(error_code == 200){
//                            switch (sessionManage.getUserDetails().get("LANGUAGE")){
//                                case "en":
//                                case "fr":
//                                    addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model") ,object.optString("imei") , "");
//                                    break;
//                                case "ar":
//                                    addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model_ar") ,object.optString("imei"),"");
//                                    break;
//                            }
//                        }else {
//                            switch (sessionManage.getUserDetails().get("LANGUAGE")){
//                                case "en":
//                                case "fr":
//                                    addView(ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_background, null) , 301 , object.optString("model") ,object.optString("imei") , object.optString("distributor_name"));
//                                    break;
//                                case "ar":
//                                    addView(ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_background, null) , 301 , object.optString("model_ar"),object.optString("imei") , object.optString("distributor_name"));
//                                    break;
//                            }
//                        }
                        binding.progressbar.setVisibility(View.GONE);

                    }else {

                        if (error_code == 500){
                            JSONObject object = jsonObject.optJSONObject("list");
                            if(object == null){
                                ImeiDialogOpen(501 , message , "" , "" ,"","" );
                            }else {
                                ImeiDialogOpen(500 , message , object.optString("distributor_name") , object.optString("sku") ,object.optString("imei"),object.optString("time") );
                            }
                        }

                    }
                    binding.ImeiEdittext.setText("");
//                    MessageDilaog(message);
                    binding.progressbar.setVisibility(View.GONE);
                    return;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
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


    private void ImeiDialogOpen(int code , String mess , String distributor_name  , String sku , String imeis , String dates) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sell_out_imei_dialog , null);
        builder.setCancelable(false);

        TextView msg = view.findViewById(R.id.msg);
        TextView distributorName = view.findViewById(R.id.distributorName);
        TextView skuid = view.findViewById(R.id.skuid);
        TextView imei = view.findViewById(R.id.imei);
        TextView date = view.findViewById(R.id.date);
        LinearLayout closeDialog = view.findViewById(R.id.closeDialog);
        ConstraintLayout Layout = view.findViewById(R.id.Layout);

        if (code != 500){
            Layout.setVisibility(View.GONE);
            msg.setText(mess);
            msg.setTextSize(18);
            msg.setPadding(0,10,0,0);
        } else {
            Layout.setVisibility(View.VISIBLE);
            msg.setText(mess);
            distributorName.setText(distributor_name);
            skuid.setText(sku);
            imei.setText(imeis);
            date.setText(dates.substring(0,10));
        }

        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        closeDialog.setOnClickListener(v -> {alertDialog.dismiss();});


    }


    private void MessageDilaog(String errormsg){
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



































