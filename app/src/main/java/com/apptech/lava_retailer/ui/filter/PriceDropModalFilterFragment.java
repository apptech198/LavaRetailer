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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.databinding.FragmentProceDropModalFilterBinding;
import com.apptech.lava_retailer.list.modelList.ModelList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PriceDropModalFilterFragment extends BottomSheetDialogFragment {


    FragmentProceDropModalFilterBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    ProgressDialog progressDialog;
    SellOutReportModalFilterAdapter sellOutReportModalFilterAdapter;
    List<ModelList> modalList = new ArrayList<>();
    SellOutReportModalFilterAdapter.OnItemClickInterface onItemClickInterface;
    JSONObject MainObject = new JSONObject();
    PriceDropModalInterFace priceDropModalInterFace;


    public PriceDropModalFilterFragment(PriceDropModalInterFace priceDropModalInterFace) {
        this.priceDropModalInterFace = priceDropModalInterFace;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProceDropModalFilterBinding.inflate(inflater , container , false);
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



        onItemClickInterface = new SellOutReportModalFilterAdapter.OnItemClickInterface() {
            @Override
            public void AddItem(ModelList l , int pos) {
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
        };

        binding.Filterbtn.setOnClickListener(v -> priceDropModalInterFace.ModalResult(MainObject));

    }

    @NonNull
    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
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


    public interface PriceDropModalInterFace{
        void ModalResult(JSONObject object);
    };


}




































