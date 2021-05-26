package com.apptech.lava_retailer.fragment.purchase_request_now;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.adapter.PurchaseNowAdapter;
import com.apptech.lava_retailer.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.lava_retailer.databinding.PurchaseRequestNowFragmentBinding;
import com.apptech.lava_retailer.modal.product.ProductList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.other.SpacesItemDecoration;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PurchaseRequestNowFragment extends Fragment implements ShortFilterBottomSheetFragment.ShortItemClck   {

    private PurchaseRequestNowViewModel mViewModel;
    PurchaseRequestNowFragmentBinding binding;
    List<ProductList> productLists = new ArrayList<>();
    private static final String TAG = "PurchaseRequestNowFragm";
    SessionManage sessionManage;
    String json;
    onbackFragmentLoad onbackFragmentLoad;
    JSONObject MainjsonObject = new JSONObject();
    LavaInterface lavaInterface;
    PurchaseNowAdapter purchaseNowAdapter;
    PurchaseNowAdapter.PurchaseNowIterface purchaseNowIterface;
    ShortFilterBottomSheetFragment shortFilterBottomSheetFragment;

    public PurchaseRequestNowFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PurchaseRequestNowFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PurchaseRequestNowViewModel.class);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        // TODO: Use the ViewModel
//        sessionManage = new SessionManage(requireContext());
//        sessionManage.clearaddcard( );

        json = sessionManage.getUserDetails().get("CARD_DATA");
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                MainjsonObject = jsonObject;
                cardQuntyUpdate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cardQuntyUpdate();
        productLists.clear();
        adapterinterface();
        getProduct();

        binding.viewCardFloat.setOnClickListener(v -> {
//            startActivity(new Intent(requireContext(), CartActivity.class));
        });

        binding.noproduct.setVisibility(View.GONE);
        binding.PurchaseNowRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

        binding.filterShort.setOnClickListener(v -> {
            shortFilterBottomSheetFragment = new ShortFilterBottomSheetFragment(this);
            shortFilterBottomSheetFragment.show(getChildFragmentManager(), "short bottom sheet");
        });
        binding.filterPrice.setOnClickListener(v -> {
//            new CategoryFilterBottomSheetFragment().show(getChildFragmentManager(), "category filter");
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(purchaseNowAdapter != null){
                        purchaseNowAdapter.getFilter().filter(s.toString());
                    }
                }catch (NullPointerException e){
                    Log.e(TAG, "POPULARITY: " + e.getMessage());
                }
            }
        });


    }



    void cardQuntyUpdate() {
        Log.e(TAG, "cardQuntyUpdate: " + MainjsonObject.toString() );
        if (MainjsonObject.length() == 0) {
            binding.card.cardRound.setVisibility(View.GONE);
        } else {
            binding.card.cardRound.setVisibility(View.VISIBLE);
            binding.card.countCard.setText(String.valueOf(MainjsonObject.length()));
        }
    }

    @Override
    public void onItemClick(String text) {
        switch (text){
            case "POPULARITY":
                shortFilterBottomSheetFragment.dismiss();
                POPULARITY();
                break;
            case "LOW_TO_HIGHT":
                shortFilterBottomSheetFragment.dismiss();
                LOW_TO_HIGHT();
                break;
            case "HIGH_TO_LOW":
                shortFilterBottomSheetFragment.dismiss();
                HIGH_TO_LOW();
                break;
            case "NEWES_FIRST":
                shortFilterBottomSheetFragment.dismiss();
                NEWES_FIRST();
                break;
        }
    }

    private void NEWES_FIRST() {
        Toast.makeText(getContext(), "NEWES_FIRST", Toast.LENGTH_SHORT).show();
    }

    private void HIGH_TO_LOW() {
        try {
            if(purchaseNowAdapter != null){
                Collections.sort(productLists , new PriceshortHightoLow());
                purchaseNowAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "POPULARITY: " + e.getMessage() );
        }
    }

    private void LOW_TO_HIGHT() {
        try {
            if(purchaseNowAdapter != null){
                Collections.sort(productLists , new PriceshortLowToHight());
                purchaseNowAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "POPULARITY: " + e.getMessage() );
        }

    }

    private void POPULARITY() {
        Toast.makeText(getContext(), "POPULARITY", Toast.LENGTH_SHORT).show();
        try {
            if(purchaseNowAdapter != null){
                Collections.sort(productLists , new Popularity());
                purchaseNowAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "POPULARITY: " + e.getMessage() );
        }
    }

    public interface onbackFragmentLoad {
        void onBack(ProductList list);
    }


    private void getProduct(){

        binding.progressbar.setVisibility(View.VISIBLE);

        String brandid = sessionManage.getUserDetails().get("BRAND_ID");
        String retailer_id = sessionManage.getUserDetails().get("ID");
        String locality_id = sessionManage.getUserDetails().get("LOCALITY_ID");

/*

        lavaInterface.PRODUCT_LIST(brandid , retailer_id,locality_id).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e(TAG, "onResponse: " + response.body().get("error") );
                Log.e(TAG, "onResponse: " + response.body().get("error") );

                String error = response.body().get("error").toString();
                if(error.equals("false")){

                    JsonArray jsonArray = new JsonArray();
                    jsonArray = response.body().getAsJsonArray("list");

                    for (int i=0 ; i< jsonArray.size(); i++){
//                        JsonObject jo = jsonArray.getAsJsonObject().ge;
                        JsonObject jo = jsonArray.get(i).getAsJsonObject();

//                        productLists.add(new ProductList(
//                                jo.get("id").getAsString()
//                                ,  jo.get("marketing_name").getAsString()
//                                ,  jo.get("marketing_name_ar").getAsString()
//                                ,  jo.get("des").getAsString()
//                                ,  jo.get("des_ar").getAsString()
//                                ,  jo.get("actual_price").getAsString()
//                                ,  jo.get("dis_price").getAsString()
//                                ,  jo.get("thumb").getAsString()
//                                ,  jo.get("thumb_ar").getAsString()
//                                ,  jo.get("sku").getAsString()
//                                ,  jo.get("commodity").getAsString()
//                                ,  jo.get("commodity_ar").getAsString()
//                                ,  jo.get("brand_id").getAsString()
//                                ,  jo.get("brand").getAsString()
//                                ,  jo.get("brand_ar").getAsString()
//                                ,  jo.get("model").getAsString()
//                                ,  jo.get("model_ar").getAsString()
//                                ,  jo.get("category").getAsString()
//                                ,  jo.get("serialized").getAsString()
//                                ,  jo.get("video").getAsString()
//                                ,  jo.get("video_ar").getAsString()
//                                ,  jo.get("acce_mobile").getAsString()
//                                ,  jo.get("acce_mobile_war").getAsString()
//                                ,  jo.get("acce_charger").getAsString()
//                                ,  jo.get("acce_charger_war").getAsString()
//                                ,  jo.get("acce_earphone").getAsString()
//                                ,  jo.get("acce_earphone_war").getAsString()
//                                ,  jo.get("available_qty").getAsString()
//                                ,  jo.get("hide").getAsString()
//                                ,  jo.get("total_sale").toString()
//                        ));
                    }
                    if(productLists.size() > 0){
                        purchaseNowAdapter = new PurchaseNowAdapter(productLists, purchaseNowIterface);
                        binding.PurchaseNowRecyclerView.setAdapter(purchaseNowAdapter);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.noproduct.setVisibility(View.GONE);
                    }
                    else {
                        binding.progressbar.setVisibility(View.GONE);
                        binding.noproduct.setVisibility(View.VISIBLE);
                    }
                }else {
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noproduct.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }


*/
/*
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equalsIgnoreCase("false")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i=0 ; i< jsonArray.length(); i++){
                            JSONObject jo = jsonArray.getJSONObject(i);

                            String total_sale = "";
                            if(jo.getString("total_sale") != null){
                                total_sale = jo.getString("total_sale");
                            }

                            productLists.add(new ProductList(
                                    jo.getString("id")
                                    ,  jo.getString("marketing_name")
                                    ,  jo.getString("marketing_name_ar")
                                    ,  jo.getString("des")
                                    ,  jo.getString("des_ar")
                                    ,  jo.getString("actual_price")
                                    ,  jo.getString("dis_price")
                                    ,  jo.getString("thumb")
                                    ,  jo.getString("thumb_ar")
                                    ,  jo.getString("sku")
                                    ,  jo.getString("commodity")
                                    ,  jo.getString("commodity_ar")
                                    ,  jo.getString("brand_id")
                                    ,  jo.getString("brand")
                                    ,  jo.getString("brand_ar")
                                    ,  jo.getString("model")
                                    ,  jo.getString("model_ar")
                                    ,  jo.getString("category")
                                    ,  jo.getString("serialized")
                                    ,  jo.getString("video")
                                    ,  jo.getString("video_ar")
                                    ,  jo.getString("acce_mobile")
                                    ,  jo.getString("acce_mobile_war")
                                    ,  jo.getString("acce_charger")
                                    ,  jo.getString("acce_charger_war")
                                    ,  jo.getString("acce_earphone")
                                    ,  jo.getString("acce_earphone_war")
                                    ,  jo.getString("available_qty")
                                    ,  jo.getString("hide")
                                    ,  total_sale
                            ));
                        }
                        if(productLists.size() > 0){
                            purchaseNowAdapter = new PurchaseNowAdapter(productLists, purchaseNowIterface);
                            binding.PurchaseNowRecyclerView.setAdapter(purchaseNowAdapter);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noproduct.setVisibility(View.GONE);
                        }
                        else {
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noproduct.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noproduct.setVisibility(View.VISIBLE);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noproduct.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
*//*


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
*/

    }


    /*private void getProduct(){

        binding.progressbar.setVisibility(View.VISIBLE);

        String brandid = sessionManage.getUserDetails().get("BRAND_ID");
        String retailer_id = sessionManage.getUserDetails().get("ID");
//        String locality_id = sessionManage.getUserDetails().get("LOCALITY_ID");
        String locality_id = "2";

        lavaInterface.PRODUCT_LIST1(brandid , retailer_id,locality_id).enqueue(new Callback<com.apptech.myapplication.modal.productlist.ProductList>() {
            @Override
            public void onResponse(Call<com.apptech.myapplication.modal.productlist.ProductList> call, Response<com.apptech.myapplication.modal.productlist.ProductList> response) {
                if(response.isSuccessful()){
                    if(!response.body().getError()){

                        if(response.body().getList().size() > 0){
                            purchaseNowAdapter = new PurchaseNowAdapter(response.body()., purchaseNowIterface);
                            binding.PurchaseNowRecyclerView.setAdapter(purchaseNowAdapter);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noproduct.setVisibility(View.GONE);
                        }else {
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noproduct.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noproduct.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), response.body().getMessage() , Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                binding.noproduct.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<com.apptech.myapplication.modal.productlist.ProductList> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.noproduct.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }
*/
    private void adapterinterface(){
        purchaseNowIterface = new PurchaseNowAdapter.PurchaseNowIterface() {
            @Override
            public void itemClick(ProductList list) {
                onbackFragmentLoad.onBack(list);
            }

            @Override
            public void addItem(ProductList list, int position, TextView textView) {
                JSONObject jsonObject = new JSONObject();
                try {
//                    jsonObject.put("id", list.getId());
//                    jsonObject.put("name", list.getMarketing_name());
//                    jsonObject.put("img", list.getThumb());
//                    jsonObject.put("qty", "1");

//                    ***************************************************************************
                    jsonObject.put("id", list.getId());
                    jsonObject.put("marketing_name", list.getMarketing_name());
                    jsonObject.put("marketing_name_ar", list.getMarketing_name_ar());
                    jsonObject.put("des", list.getDes());
                    jsonObject.put("des_ar", list.getDes_ar());
                    jsonObject.put("actual_price", list.getActual_price());
                    jsonObject.put("dis_price", list.getDis_price());
                    jsonObject.put("thumb", list.getThumb());
                    jsonObject.put("thumb_ar", list.getThumb_ar());
                    jsonObject.put("sku", list.getSku());
                    jsonObject.put("commodity", list.getCommodity());
                    jsonObject.put("commodity_ar", list.getCommodity_ar());
                    jsonObject.put("brand_id", list.getBrand_id());
                    jsonObject.put("brand", list.getBrand());
                    jsonObject.put("brand_ar", list.getBrand_ar());
                    jsonObject.put("model", list.getModel());
                    jsonObject.put("model_ar", list.getModel_ar());
                    jsonObject.put("category", list.getCommodity());
                    jsonObject.put("serialized", list.getCommodity_ar());
                    jsonObject.put("video", list.getVideo());
                    jsonObject.put("video_ar", list.getVideo_ar());
//                    jsonObject.put("acce_mobile", list.getAcce_mobile());
//                    jsonObject.put("acce_mobile_war", list.getAcce_mobile_war());
//                    jsonObject.put("acce_charger", list.getAcce_charger());
//                    jsonObject.put("acce_charger_war", list.getAcce_charger_war());
//                    jsonObject.put("acce_earphone", list.getAcce_earphone());
//                    jsonObject.put("acce_earphone_war", list.getAcce_earphone_war());
                    jsonObject.put("available_qty", list.getAvailable_qty());
                    jsonObject.put("hide", list.getHide());
                    jsonObject.put("total_sale", list.getTotal_sale());
                    jsonObject.put("qty", "1");

                    MainjsonObject.put(list.getId(), jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
                cardQuntyUpdate();
            }

            @Override
            public void minus(ProductList list, int position, TextView county) {
                int add = Integer.parseInt(county.getText().toString().trim()) - 1;
                county.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "minus: " + MainjsonObject.toString());
                sessionManage.addcard(MainjsonObject.toString());
            }

            @Override
            public void RemoveItem(ProductList list, int position, TextView county) {
                MainjsonObject.remove(list.getId());
                sessionManage.addcard(MainjsonObject.toString());
                Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
                if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
                cardQuntyUpdate();
            }

            @Override
            public void QtyAdd(ProductList list, int position, TextView countqty) {
                int add = Integer.parseInt(countqty.getText().toString().trim()) + 1;
                countqty.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
            }
        };
    }

    private static class PriceshortLowToHight implements Comparator<ProductList>{

        @Override
        public int compare(ProductList o1, ProductList o2) {
            int a = 0;
            int b = 0;
            try {
                 a = Integer.parseInt(o1.getDis_price());
                 b = Integer.parseInt(o2.getDis_price());
            }catch (NullPointerException e){
                Log.e(TAG, "compare: " + e.getMessage() );
            }
            return   a - b ;
        }
    }

    private static class PriceshortHightoLow implements Comparator<ProductList>{

        @Override
        public int compare(ProductList o1, ProductList o2) {
            int a = 0;
            int b = 0;
            try {
                 a = Integer.parseInt(o1.getDis_price());
                 b = Integer.parseInt(o2.getDis_price());
            }catch (NullPointerException e){
                Log.e(TAG, "compare: " + e.getMessage() );
            }
            return   b - a ;
        }
    }

    private static class Popularity implements Comparator<ProductList>{

        @Override
        public int compare(ProductList o1, ProductList o2) {
            if(!o1.getTotal_sale().equals("null") && o2.getTotal_sale().equals("null")){
                int a = 0;
                int b = 0;
                try {
                    a = Integer.parseInt(o1.getTotal_sale());
                    b = Integer.parseInt(o2.getTotal_sale());

                }catch (NullPointerException e){
                    Log.e(TAG, "compare: " + e.getMessage() );
                }
                return   b - a ;
            }else {
                return  0;
            }
        }
    }

}






























































