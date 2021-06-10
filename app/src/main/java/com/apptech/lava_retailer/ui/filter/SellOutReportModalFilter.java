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
import android.util.Log;
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
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.list.modelList.ModelList;
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
import java.util.Iterator;
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
    List<ModelList> modalList;
    JSONObject MainObject = new JSONObject();
    private static final String TAG = "SellOutReportModalFilte";
    SellOutReportModalFilterAdapter sellOutReportModalFilterAdapter;

    public SellOutReportModalFilter(OnItemClickBackPress onItemClickBackPress , List<ModelList> modalList , JSONObject ReturnModelJsonObject) {
        this.onItemClickBackPress = onItemClickBackPress;
        this.modalList = modalList;
        MainObject = ReturnModelJsonObject;
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


        if (MainObject.length() > 0){

            binding.MaterialChecKall.setChecked(MainObject.length() == modalList.size());

            Iterator<String> iterator = MainObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                try {
                    JSONObject object = MainObject.getJSONObject(key);
                    int pos = Integer.parseInt(object.get("pos").toString());
                    ModelList l = modalList.get(pos);
                    l.setCheckable(true);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

        }else {
            for (int i=0; i< modalList.size(); i++){
                ModelList lists = modalList.get(i);
                lists.setCheckable(false);
                MainObject.remove(lists.getModel());;
            }
        }



        sellOutReportModalFilterAdapter = new SellOutReportModalFilterAdapter(this , modalList);
        binding.ModalRecyclerView.setAdapter(sellOutReportModalFilterAdapter);

        binding.Filterbtn.setOnClickListener(v -> onItemClickBackPress.Onitem(MainObject));

        binding.MaterialChecKall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.MaterialChecKall.setEnabled(false);
            if(modalList.size() > 0){
                for (int i=0; i< modalList.size(); i++){
                    SellOutReportModalFilterAdapter.Viewholder  viewholder = (SellOutReportModalFilterAdapter.Viewholder) binding.ModalRecyclerView.findViewHolderForAdapterPosition(i);
                    CheckBox checkBox = viewholder.itemView.findViewById(R.id.CheckBtn);
                    checkBox.setChecked(isChecked);
                    ModelList lists = modalList.get(i);
                    if(isChecked){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(lists.getModel() , lists.getModel());
                            jsonObject.put("name" , lists.getModel());
                            jsonObject.put("pos" , String.valueOf(i));
                            MainObject.put(lists.getModel() , jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        MainObject.remove(lists.getModel());
                    }
                }
            }
            binding.MaterialChecKall.setEnabled(true);
        });


    }


    @Override
    public void AddItem(ModelList l ,  int pos) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(l.getModel(), l.getModel());
            jsonObject.put("name" , l.getModel());
            jsonObject.put("pos" , String.valueOf(pos));
            MainObject.put(l.getModel() , jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RemoveItem(ModelList l , int pos) {
        modalList.get(pos).setCheckable(false);
        MainObject.remove(l.getModel());
    }


    public interface OnItemClickBackPress{
        void Onitem(JSONObject object);
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
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



































