package com.apptech.lava_retailer.ui.filter;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.CategoryAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportCategoryFilterAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.databinding.FragmentSellOutReportCategoryFilterBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SellOutReportCategoryFilter extends BottomSheetDialogFragment {


    FragmentSellOutReportCategoryFilterBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    ProgressDialog progressDialog;
    OnClickBackPress comodityLists;

    SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface onItemClickInterface;

    public SellOutReportCategoryFilter(OnClickBackPress comodityLists) {
        this.comodityLists = comodityLists;
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

        onItemClickInterface = new SellOutReportCategoryFilterAdapter.OnItemClickCategoryInterface() {
            @Override
            public void OnItemClick() {
                comodityLists.OnClickItem();
            }
        };


    }

    private void getModel(){

        lavaInterface.GRT_COMODITY().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){

                        JSONArray comodity_list = jsonObject.getJSONArray("comodity_list");


                        for (int i=0; i<comodity_list.length(); i++){
                            JSONObject op = comodity_list.getJSONObject(i);

                        }
                        binding.categoryRecyclerView.setAdapter(new SellOutReportCategoryFilterAdapter(onItemClickInterface));
                        progressDialog.dismiss();
                        return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public interface OnClickBackPress {
        void OnClickItem();
    }

}




































