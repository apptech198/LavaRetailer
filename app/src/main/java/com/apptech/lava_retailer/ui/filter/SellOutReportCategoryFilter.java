package com.apptech.lava_retailer.ui.filter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.apptech.lava_retailer.adapter.CategoryAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportCategoryFilterAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.databinding.FragmentSellOutReportCategoryFilterBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.facebook.internal.LockOnGetVariable;
import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellOutReportCategoryFilter extends BottomSheetDialogFragment implements SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface {


    FragmentSellOutReportCategoryFilterBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    ProgressDialog progressDialog;
    OnClickBackPress comodityLists;
    List<ComodityLists> categoryLists;
    JSONObject MainObject = new JSONObject();
    private static final String TAG = "SellOutReportCategoryFi";
    SellOutReportCategoryFilterAdapter sellOutReportCategoryFilterAdapter;
    SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface onItemClickInterface;
    boolean AllBTN_CLICK = false;


    public SellOutReportCategoryFilter(OnClickBackPress comodityLists , List<ComodityLists> categoryLists , JSONObject PriceDropReportReturnCategoryObject) {
        this.comodityLists = comodityLists;
        this.categoryLists = categoryLists;
        this.MainObject =  PriceDropReportReturnCategoryObject;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentSellOutReportCategoryFilterBinding.inflate(inflater , container , false);
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


        sellOutReportCategoryFilterAdapter = new SellOutReportCategoryFilterAdapter(this , categoryLists , AllBTN_CLICK);
        binding.categoryRecyclerView.setAdapter(sellOutReportCategoryFilterAdapter);


        if (MainObject.length() > 0){

            binding.checkBoxtAllClick.setChecked(MainObject.length() == categoryLists.size());

            Iterator<String> iterator = MainObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                try {
                    JSONObject object = MainObject.getJSONObject(key);
                    int pos = Integer.parseInt(object.get("pos").toString());
                    ComodityLists l = categoryLists.get(pos);
                    l.setCheckable(true);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

        }else {
            for (int i=0; i< categoryLists.size(); i++){
                ComodityLists lists = categoryLists.get(i);
               lists.setCheckable(false);
                MainObject.remove(lists.getId());;
            }
        }






        binding.checkBoxtAllClick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(categoryLists.size() > 0){
                for (int i=0; i< categoryLists.size(); i++){
                    SellOutReportCategoryFilterAdapter.Viewholder  viewholder = (SellOutReportCategoryFilterAdapter.Viewholder) binding.categoryRecyclerView.findViewHolderForAdapterPosition(i);
                    CheckBox checkBox = viewholder.itemView.findViewById(R.id.CheckBtn);
                    checkBox.setChecked(isChecked);
                    ComodityLists lists = categoryLists.get(i);
                    if(isChecked){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(lists.getId() , lists.getId());
                            jsonObject.put("name" , lists.getName());
                            jsonObject.put("pos" , String.valueOf(i));
                            MainObject.put(lists.getId() , jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        MainObject.remove(lists.getId());
                    }
                }
            }
            binding.checkBoxtAllClick.setEnabled(true);
        });


        binding.Filterbtn.setOnClickListener(v -> {
            comodityLists.OnClickItem(MainObject);
        });




    }



    @Override
    public void AddItem(ComodityLists lists , int pos) {


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(lists.getId() , lists.getId());
            jsonObject.put("name" , lists.getName());
            jsonObject.put("pos" , String.valueOf(pos));
            MainObject.put(lists.getId() , jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RemoveItem(ComodityLists lists , int pos) {


        categoryLists.get(pos).setCheckable(false);
        MainObject.remove(lists.getId());;
    }


    public interface OnClickBackPress {
        void OnClickItem(JSONObject jsonObject);
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




































