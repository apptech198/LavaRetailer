package com.apptech.lava_retailer.ui.price_drop.price_drop_entry;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.PriceDropEntryFragmentBinding;
import com.apptech.lava_retailer.fragment.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
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

public class PriceDropEntryFragment extends Fragment implements ScannerFragment.BackPress , BarCodeScannerFragment.BackPressBarCode {

    private PriceDropEntryViewModel mViewModel;
    PriceDropEntryFragmentBinding binding;
    private static final String TAG = "PriceDropEntryFragment";
    DatePickerDialog picker;
    View rowView;
    LinearLayout removeBtn;
    ScannerFragment scannerFragment;
    BarCodeScannerFragment barCodeScannerFragment;
    boolean onetime = true;
    TextView textView ,Modal;
    int imeiCount = 0;
    JsonArray jsonElements = new JsonArray();
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String USER_ID = "", TodayDate = "";
    JsonObject mainJsonObject = new JsonObject();
    LinearLayout mainLayout;
    private boolean NoData = true, Wrongdatra = true;

    public static PriceDropEntryFragment newInstance() {
        return new PriceDropEntryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PriceDropEntryFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PriceDropEntryViewModel.class);
        // TODO: Use the ViewModel

        binding.startDatetime.setText(getCurrentDate());
        scannerFragment = new ScannerFragment(this);
        barCodeScannerFragment = new BarCodeScannerFragment(this);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());

        USER_ID = sessionManage.getUserDetails().get("ID");
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
                        binding.startDatetime.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

        binding.scanBtn.setOnClickListener(v -> {
            onetime = true;
            loadfragment(barCodeScannerFragment);
        });


        binding.submitBtn.setOnClickListener(v -> {

            binding.submitBtn.setClickable(false);
            binding.submitBtn.setEnabled(false);

            if (NoData) {
                if (new NetworkCheck().haveNetworkConnection(requireActivity())) {

                    if(binding.addLayout.getChildCount() > 0){

                        binding.progressbar.setVisibility(View.VISIBLE);
                        jsonElements = new JsonArray();
                        for (int i = 0; i < binding.addLayout.getChildCount(); i++) {
                            JsonObject jsonObject = new JsonObject();
                            try {
                                int getid = i + 1;
                                TextView textView = binding.addLayout.findViewWithTag(String.valueOf(getid));
                                jsonObject.addProperty("imei", textView.getText().toString());
                                jsonElements.add(jsonObject);
                            } catch (NullPointerException e) {
                                Log.e(TAG, "onActivityCreated: " + e.getMessage());
                            }
                        }

                        submitImei();
                        binding.addLayout.removeAllViews();
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

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Price Drop Entry");
    }

    void submitImei() {

        mainJsonObject.addProperty("date", binding.startDatetime.getText().toString().trim());
        mainJsonObject.addProperty("retailer_id", USER_ID);
        mainJsonObject.add("imei_list", jsonElements);

        Log.e(TAG, "submitImei: " + mainJsonObject);

        Call call = lavaInterface.PRICE_DROP_IMEI(mainJsonObject);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    int error_code = jsonObject.getInt("error_code");
                    if (error.equalsIgnoreCase("false")) {

                        imeiCount = 0;
                        binding.addLayout.removeAllViews();

/*
                        try {
                            if (error_code == 401) {
                                String jsonArraySrtring = jsonObject.getString("dl");
                                JSONArray jsonArray = new JSONArray(jsonArraySrtring);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    wrongimei(jsonArray.getJSONObject(i).getString("imei"));
                                }
                                binding.msgShowWrongImei.setText(getResources().getString(R.string.imei_allready_exists));
                                binding.msgShowWrongImei.setVisibility(View.VISIBLE);
                                NoData = false;
                            } else {
                                binding.msgShowWrongImei.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
*/

                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        binding.submitBtn.setClickable(true);
                        binding.submitBtn.setEnabled(true);
                        binding.progressbar.setVisibility(View.GONE);
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


    private void addImei() {
        if (ImeiValid(binding.ImeiEdittext.getText().toString().trim())) {
            IMEI_CHECK();
        }
    }


    private void addView(Drawable drawable , int type , String modals ) {

        int a = imeiCount += 1;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.add_view, null);
        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);

        textView = rowView.findViewById(R.id.imei);
        mainLayout = rowView.findViewById(R.id.mainLayout);
        Modal = rowView.findViewById(R.id.Modal);
        TextView ModalTitle = rowView.findViewById(R.id.ModalTitle);
        ImageView icon  = rowView.findViewById(R.id.icon);

        mainLayout.setBackground(drawable);
        textView.setText(binding.ImeiEdittext.getText().toString().trim());
        textView.setTag(String.valueOf(a));
        removeBtn = rowView.findViewById(R.id.remove);

        switch (type){
            case 200:
                icon.setBackground(ResourcesCompat.getDrawable(getResources() , R.drawable.ic_check , null));
                Modal.setVisibility(View.VISIBLE);
                ModalTitle.setVisibility(View.VISIBLE);
                Modal.setText(modals);
                break;
            case 301:
                icon.setBackground(ResourcesCompat.getDrawable(getResources() , R.drawable.ic__check , null));
                Modal.setVisibility(View.GONE);
                ModalTitle.setVisibility(View.GONE);
                break;
        }


        removeView();
        binding.ImeiEdittext.setText(null);
        NoData = true;
        Wrongdatra = true;
    }



    private void wrongimei(String imei) {
        int a = imeiCount += 1;
//        Log.e(TAG, "addImei: idset" + a);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.add_view, null);
        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);
        textView = rowView.findViewById(R.id.imei);
        mainLayout = rowView.findViewById(R.id.mainLayout);
        mainLayout.setBackground(getResources().getDrawable(R.drawable.wrong_imei));
        textView.setText(imei);
        textView.setTag(String.valueOf(a));
        removeBtn = rowView.findViewById(R.id.remove);
        removeView();
        binding.ImeiEdittext.setText(null);
        Wrongdatra = false;
    }

    private boolean ImeiValid(String imei) {
        if (imei.isEmpty()) {
            Toast.makeText(requireContext(), getResources().getString(R.string.field_required), Toast.LENGTH_SHORT).show();
            return false;
        } else if (imei.length() != 15) {
            Toast.makeText(requireContext(), "Invalid Imei code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void IMEI_CHECK(){

        binding.progressbar.setVisibility(View.VISIBLE);

//        lavaInterface.IMEI_CHECK("123456789123456" , "14").enqueue(new Callback<Object>() {
        lavaInterface.IMEI_CHECK(binding.ImeiEdittext.getText().toString().trim() , USER_ID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()) );

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));

                    String error = jsonObject.getString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");

                    if(error.equalsIgnoreCase("FALSE")){

                        if(error_code == 200){
                            JSONObject object = jsonObject.getJSONObject("list");
                            addView(ResourcesCompat.getDrawable(getResources(), R.drawable.green_background, null) , 200 , object.optString("model_name"));
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }

                        if(error_code == 301){
                            addView(ResourcesCompat.getDrawable(getResources(), R.drawable.yellow_background, null) , 301 ,"");
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }

                    }
                    binding.ImeiEdittext.setText("");
                    MessageDilaog(message);
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

    private void MessageDilaog(String errormsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error Message");
        builder.setMessage(errormsg);
//        builder.setNegativeButton("" , (dialog, which) -> {
//            dialog.dismiss();
//        });
        builder.setPositiveButton("Close" , (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



}





















































