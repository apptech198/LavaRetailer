package com.apptech.lava_retailer.ui.filter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutReportCategoryFilterAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.databinding.FragmentSellOutReportModalFilterBinding;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.sell_out.report_sell_out_report.ReportSellOutReportFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellOutReportModalFilter extends BottomSheetDialogFragment implements SellOutReportModalFilterAdapter.OnItemClickInterface{


    FragmentSellOutReportModalFilterBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    ProgressDialog progressDialog;
    OnItemClickBackPress onItemClickBackPress;
    List<String> modalList;
    JSONObject MainObject = new JSONObject();
    private static final String TAG = "SellOutReportModalFilte";
    SellOutReportModalFilterAdapter sellOutReportModalFilterAdapter;

    public SellOutReportModalFilter(OnItemClickBackPress onItemClickBackPress ,     List<String> modalList) {
        this.onItemClickBackPress = onItemClickBackPress;
        this.modalList = modalList;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSellOutReportModalFilterBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManage = SessionManage.getInstance(getContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


/*        if (new NetworkCheck().haveNetworkConnection(getActivity())){
            if (modalList.isEmpty()){
                getModel();
            }else {
                sellOutReportModalFilterAdapter = new SellOutReportModalFilterAdapter(onItemClickInterface , modalList);
                binding.ModalRecyclerView.setAdapter(sellOutReportModalFilterAdapter);
            }
        }else {
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
*/


                sellOutReportModalFilterAdapter = new SellOutReportModalFilterAdapter(this , modalList);
                binding.ModalRecyclerView.setAdapter(sellOutReportModalFilterAdapter);


        binding.Filterbtn.setOnClickListener(v -> {
            onItemClickBackPress.Onitem(MainObject);
        });


        binding.MaterialChecKall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.MaterialChecKall.setEnabled(false);
            if(modalList.size() > 0){
                for (int i=0; i< modalList.size(); i++){
                    SellOutReportModalFilterAdapter.Viewholder  viewholder = (SellOutReportModalFilterAdapter.Viewholder) binding.ModalRecyclerView.findViewHolderForAdapterPosition(i);
                    CheckBox checkBox = viewholder.itemView.findViewById(R.id.CheckBtn);
                    checkBox.setChecked(isChecked);
                    String lists = modalList.get(i);
                    if(isChecked){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(lists , lists);
                            jsonObject.put("name" , lists);
                            MainObject.put(lists , jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        MainObject.remove(lists);
                    }
                }
            }
            binding.MaterialChecKall.setEnabled(true);
        });


    }

    private void getModel() {

        String josn = "{\n" +
                "  \"comodity_list\": [\n" +
                "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"name\": \"SMART PHONE\",\n" +
                "      \"name_ar\": \"???????? ??????\",\n" +
                "      \"name_fr\": \"\",\n" +
                "      \"brand_id\": null,\n" +
                "      \"brand_name\": null,\n" +
                "      \"form_type\": null,\n" +
                "      \"time\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"2\",\n" +
                "      \"name\": \"USB CABLE\",\n" +
                "      \"name_ar\": \"???????? USB\",\n" +
                "      \"name_fr\": \"\",\n" +
                "      \"brand_id\": null,\n" +
                "      \"brand_name\": null,\n" +
                "      \"form_type\": null,\n" +
                "      \"time\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"5\",\n" +
                "      \"name\": \"FEATURE PHONE\",\n" +
                "      \"name_ar\": \"?????????????? ????????????\",\n" +
                "      \"name_fr\": \"\",\n" +
                "      \"brand_id\": null,\n" +
                "      \"brand_name\": null,\n" +
                "      \"form_type\": null,\n" +
                "      \"time\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"6\",\n" +
                "      \"name\": \"WIRED EARPHONE\",\n" +
                "      \"name_ar\": \"?????????? ??????????\",\n" +
                "      \"name_fr\": \"\",\n" +
                "      \"brand_id\": null,\n" +
                "      \"brand_name\": null,\n" +
                "      \"form_type\": null,\n" +
                "      \"time\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"7\",\n" +
                "      \"name\": \"BLUETOOTH EARPHONE\",\n" +
                "      \"name_ar\": \"?????????? ????????????\",\n" +
                "      \"name_fr\": \"\",\n" +
                "      \"brand_id\": null,\n" +
                "      \"brand_name\": null,\n" +
                "      \"form_type\": null,\n" +
                "      \"time\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"8\",\n" +
                "      \"name\": \"EARPHONE\",\n" +
                "      \"name_ar\": \"?????????? ??????????\",\n" +
                "      \"name_fr\": \"\",\n" +
                "      \"brand_id\": null,\n" +
                "      \"brand_name\": null,\n" +
                "      \"form_type\": null,\n" +
                "      \"time\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"model_list\": [\n" +
                "    {\n" +
                "      \"model\": \"Y30\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"model\": \"Y31\"\n" +
                "    }\n" +
                "    ,\n" +
                "    {\n" +
                "      \"model\": \"Y32\"\n" +
                "    }\n" +
                "    ,\n" +
                "    {\n" +
                "      \"model\": \"Y33\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"error\": false,\n" +
                "  \"error_code\": 200,\n" +
                "  \"message\": \" all IMEI   \"\n" +
                "}";


        progressDialog.show();
        lavaInterface.SELL_OUT_CATEGORY_MODAL_FILTER().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject  = new JSONObject(new Gson().toJson(response.body()));
//                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        jsonObject = new JSONObject(josn);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){
                            JSONArray model_list = jsonObject.getJSONArray("model_list");
                            modalList.clear();

                            for (int i=0; i<model_list.length(); i++){
                                JSONObject op = model_list.getJSONObject(i);
                                modalList.add(op.optString("model"));
                            }

//                            sellOutReportModalFilterAdapter = new SellOutReportModalFilterAdapter(this , modalList);
//                            binding.ModalRecyclerView.setAdapter(sellOutReportModalFilterAdapter);

                            progressDialog.dismiss();
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    @Override
    public void AddItem(String l) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(l, l);
            jsonObject.put("name" , l);
            MainObject.put(l , jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RemoveItem(String l) {
        MainObject.remove(l);
    }


    public interface OnItemClickBackPress{
        void Onitem(JSONObject object);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight - 400;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }



}



































