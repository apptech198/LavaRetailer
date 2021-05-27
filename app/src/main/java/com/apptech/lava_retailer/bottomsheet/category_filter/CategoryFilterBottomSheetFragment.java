package com.apptech.lava_retailer.bottomsheet.category_filter;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.CategoryAdapter;
import com.apptech.lava_retailer.adapter.TradeProgramTabAdapter;
import com.apptech.lava_retailer.databinding.CategoryFilterBottomSheetFragmentBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.modal.category.CategoryList;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
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

public class CategoryFilterBottomSheetFragment extends BottomSheetDialogFragment  {

    private CategoryFilterBottomSheetViewModel mViewModel;
    CategoryFilterBottomSheetFragmentBinding binding;
    List<ComodityLists> comodityLists =new ArrayList<>();
    LavaInterface lavaInterface;
    ProgressDialog progressDialog;
    CategoryAdapter.CategoryItemClickInterface categoryItemClickInterface;
    CategoryInterface categoryInterface;

    public CategoryFilterBottomSheetFragment(CategoryInterface categoryInterface) {
        this.categoryInterface = categoryInterface;
    }

    //    public static CategoryFilterBottomSheetFragment newInstance(List<ComodityLists> comodityLists) {
//        return new CategoryFilterBottomSheetFragment(comodityLists);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CategoryFilterBottomSheetFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoryFilterBottomSheetViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        GetComodity_list();
        categoryItemClickInterface = category -> categoryInterface.OnItemCategoryClick(category);
        
    }


    private void GetComodity_list(){
        lavaInterface.GRT_COMODITY().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

//                        if(error.equalsIgnoreCase("FALSE")){

                            JSONArray comodity_list = jsonObject.getJSONArray("comodity_list");


                            for (int i=0; i<comodity_list.length(); i++){
                                JSONObject op = comodity_list.getJSONObject(i);
                                comodityLists.add(new ComodityLists(
                                        op.optString("id")
                                        ,op.optString("name")
                                        ,op.optString("name_ar")
                                        ,op.optString("name_fr")
                                        ,op.optString("brand_id")
                                        ,op.optString("brand_name")
                                        ,op.optString("form_type")
                                        ,op.optString("time")
                                ));
                            }

                            binding.categoryRecyclerView.setAdapter(new CategoryAdapter(comodityLists,categoryItemClickInterface));
                            progressDialog.dismiss();
                            return;
//                        }
//                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public interface CategoryInterface{
        void OnItemCategoryClick(ComodityLists comodityLists);
    }

}




























