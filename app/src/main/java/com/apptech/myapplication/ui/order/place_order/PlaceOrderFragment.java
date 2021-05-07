package com.apptech.myapplication.ui.order.place_order;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.myapplication.MobileNavigationDirections;
import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.CartActivity;
import com.apptech.myapplication.activity.MainActivity;
import com.apptech.myapplication.adapter.PurchaseNowAdapter;
import com.apptech.myapplication.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.myapplication.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.myapplication.databinding.PlaceOrderFragmentBinding;
import com.apptech.myapplication.databinding.PurchaseRequestNowFragmentBinding;
import com.apptech.myapplication.fragment.purchase_request_now.PurchaseRequestNowFragment;
import com.apptech.myapplication.fragment.purchase_request_now.PurchaseRequestNowViewModel;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.other.SpacesItemDecoration;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderFragment extends Fragment implements ShortFilterBottomSheetFragment.ShortItemClck {

    private PlaceOrderViewModel mViewModel;
    SessionManage sessionManage;
    String json;
    PurchaseRequestNowFragment.onbackFragmentLoad onbackFragmentLoad;
    JSONObject MainjsonObject = new JSONObject();
    private static final String TAG = "PlaceOrderFragment";
    List<ProductList> productLists = new ArrayList<>();
    PlaceOrderFragmentBinding binding;
    LavaInterface lavaInterface;
    PurchaseNowAdapter purchaseNowAdapter;
    PurchaseNowAdapter.PurchaseNowIterface purchaseNowIterface;
    ShortFilterBottomSheetFragment shortFilterBottomSheetFragment;
    JSONObject ProductJsonObject = new JSONObject();
    String BRAND_ID = "";
    NavController navController;

    public static PlaceOrderFragment newInstance() {
        return new PlaceOrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PlaceOrderFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlaceOrderViewModel.class);
        sessionManage = SessionManage.getInstance(requireContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        // TODO: Use the ViewModel

        BRAND_ID = sessionManage.getUserDetails().get("BRAND_ID");


        json = sessionManage.getUserDetails().get("CARD_DATA");
        if (json != null) {
            try {
                MainjsonObject = new JSONObject(json);
                ProductJsonObject = new JSONObject(MainjsonObject.getJSONObject(BRAND_ID).toString());
                cardQuntyUpdate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "onActivityCreated: " + MainjsonObject.toString() );

        cardQuntyUpdate();
        productLists.clear();
        adapterinterface();
        getProduct();

        binding.viewCardFloat.setOnClickListener(v -> {
            navController.navigate(R.id.action_global_cartFragment);
        });

        binding.noproduct.setVisibility(View.GONE);
        binding.PurchaseNowRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

        binding.filterShort.setOnClickListener(v -> {
            shortFilterBottomSheetFragment = new ShortFilterBottomSheetFragment(this);
            shortFilterBottomSheetFragment.show(getChildFragmentManager(), "short bottom sheet");
        });
        binding.filterPrice.setOnClickListener(v -> {
            new CategoryFilterBottomSheetFragment().show(getChildFragmentManager(), "category filter");
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    void cardQuntyUpdate() {
//        Log.e(TAG, "cardQuntyUpdate: " + MainjsonObject.toString() );
        if (MainjsonObject.length() == 0) {
            binding.card.cardRound.setVisibility(View.GONE);
        } else {
            try {
                if(MainjsonObject.getJSONObject(BRAND_ID).length() > 0){
                    binding.card.cardRound.setVisibility(View.VISIBLE);
                    binding.card.countCard.setText(String.valueOf(MainjsonObject.getJSONObject(BRAND_ID).length()));
                }else {
                    binding.card.cardRound.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                binding.card.cardRound.setVisibility(View.GONE);
            }
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




    private void getProduct(){

        binding.progressbar.setVisibility(View.VISIBLE);

        String brandid = sessionManage.getUserDetails().get("BRAND_ID");
        String retailer_id = sessionManage.getUserDetails().get("ID");
        String locality_id = sessionManage.getUserDetails().get("LOCALITY_ID");

        Log.e(TAG, "getProduct: " + brandid );
        Log.e(TAG, "getProduct: " + retailer_id );
        Log.e(TAG, "getProduct: " + locality_id );


        lavaInterface.PRODUCT_LIST(brandid , retailer_id,locality_id).enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String error = response.body().get("error").toString();
                if(error.equals("false")){

                    JsonArray jsonArray = new JsonArray();
                    jsonArray = response.body().getAsJsonArray("list");

                    for (int i=0 ; i< jsonArray.size(); i++){
//                        JsonObject jo = jsonArray.getAsJsonObject().ge;
                        JsonObject jo = jsonArray.get(i).getAsJsonObject();


                        productLists.add(new ProductList(
                                jo.get("id").getAsString()
                                ,jo.get("product_id").getAsString()
                                ,jo.get("distributor_id").getAsString()
                                ,jo.get("distributor_name").toString()
                                ,jo.get("locality").getAsString()
                                ,jo.get("locality_id").getAsString()
                                ,jo.get("marketing_name").getAsString()
                                ,jo.get("marketing_name_ar").getAsString()
                                ,jo.get("des").getAsString()
                                ,jo.get("des_ar").getAsString()
                                ,jo.get("actual_price").getAsString()
                                ,jo.get("dis_price").getAsString()
                                ,jo.get("thumb").getAsString()
                                ,jo.get("thumb_ar").getAsString()
                                ,jo.get("sku").getAsString()
                                ,jo.get("imei").getAsString()
                                ,jo.get("commodity").getAsString()
                                ,jo.get("commodity_ar").getAsString()
                                ,jo.get("brand_id").getAsString()
                                ,jo.get("brand").getAsString()
                                ,jo.get("brand_ar").getAsString()
                                ,jo.get("model").getAsString()
                                ,jo.get("model_ar").getAsString()
                                ,jo.get("category").getAsString()
                                ,jo.get("serialized").getAsString()
                                ,jo.get("video").getAsString()
                                ,jo.get("video_ar").getAsString()
                                ,jo.get("acce_mobile").getAsString()
                                ,jo.get("acce_mobile_war").getAsString()
                                ,jo.get("acce_charger").getAsString()
                                ,jo.get("acce_charger_war").getAsString()
                                ,jo.get("acce_earphone").getAsString()
                                ,jo.get("acce_earphone_war").getAsString()
                                ,jo.get("available_qty").getAsString()
                                ,jo.get("hide").getAsString()
                                ,jo.get("total_sale").toString()
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
                }else {
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noproduct.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void adapterinterface(){
        purchaseNowIterface = new PurchaseNowAdapter.PurchaseNowIterface() {
            @Override
            public void itemClick(ProductList list) {

            }

            @Override
            public void addItem(ProductList list, int position, TextView textView) {

                Log.e(TAG, "addItem start: " + MainjsonObject.toString() );

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", list.getId());
                    jsonObject.put("product_id", list.getProduct_id());
                    jsonObject.put("distributor_id", list.getDistributor_id());
                    jsonObject.put("distributor_name", list.getDistributor_name());
                    jsonObject.put("locality", list.getLocality());
                    jsonObject.put("locality_id", list.getLocality_id());
                    jsonObject.put("marketing_name", list.getMarketing_name());
                    jsonObject.put("marketing_name_ar", list.getMarketing_name_ar());
                    jsonObject.put("des", list.getDes());
                    jsonObject.put("des_ar", list.getDes_ar());
                    jsonObject.put("actual_price", list.getActual_price());
                    jsonObject.put("dis_price", list.getDis_price());
                    jsonObject.put("thumb", list.getThumb());
                    jsonObject.put("thumb_ar", list.getThumb_ar());
                    jsonObject.put("sku", list.getSku());
                    jsonObject.put("imei", list.getImei());
                    jsonObject.put("commodity", list.getCommodity());
                    jsonObject.put("commodity_ar", list.getCommodity_ar());
                    jsonObject.put("brand_id", list.getBrand_id());
                    jsonObject.put("brand", list.getBrand());
                    jsonObject.put("brand_ar", list.getBrand_ar());
                    jsonObject.put("model", list.getModel());
                    jsonObject.put("model_ar", list.getModel_ar());
                    jsonObject.put("category", list.getCategory());
                    jsonObject.put("serialized", list.getSerialized());
                    jsonObject.put("video", list.getVideo());
                    jsonObject.put("video_ar", list.getVideo_ar());
                    jsonObject.put("acce_mobile", list.getAcce_mobile());
                    jsonObject.put("acce_mobile_war", list.getAcce_mobile_war());
                    jsonObject.put("acce_charger", list.getAcce_charger());
                    jsonObject.put("acce_charger_war", list.getAcce_charger_war());
                    jsonObject.put("acce_earphone", list.getAcce_earphone());
                    jsonObject.put("acce_earphone_war", list.getAcce_earphone_war());
                    jsonObject.put("available_qty", list.getAvailable_qty());
                    jsonObject.put("hide", list.getHide());
                    jsonObject.put("total_sale", list.getTotal_sale());
                    jsonObject.put("qty", "1");

                    ProductJsonObject.put(list.getId() , jsonObject);
                    Log.e(TAG, "addItem ProductJsonObject: " + ProductJsonObject.toString() );
                    MainjsonObject.put(BRAND_ID , ProductJsonObject);
                    Log.e(TAG, "addItem end: " + MainjsonObject.toString() );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
                cardQuntyUpdate();







/*                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", list.getId());
                    jsonObject.put("product_id", list.getProduct_id());
                    jsonObject.put("distributor_id", list.getDistributor_id());
                    jsonObject.put("distributor_name", list.getDistributor_name());
                    jsonObject.put("locality", list.getLocality());
                    jsonObject.put("locality_id", list.getLocality_id());
                    jsonObject.put("marketing_name", list.getMarketing_name());
                    jsonObject.put("marketing_name_ar", list.getMarketing_name_ar());
                    jsonObject.put("des", list.getDes());
                    jsonObject.put("des_ar", list.getDes_ar());
                    jsonObject.put("actual_price", list.getActual_price());
                    jsonObject.put("dis_price", list.getDis_price());
                    jsonObject.put("thumb", list.getThumb());
                    jsonObject.put("thumb_ar", list.getThumb_ar());
                    jsonObject.put("sku", list.getSku());
                    jsonObject.put("imei", list.getImei());
                    jsonObject.put("commodity", list.getCommodity());
                    jsonObject.put("commodity_ar", list.getCommodity_ar());
                    jsonObject.put("brand_id", list.getBrand_id());
                    jsonObject.put("brand", list.getBrand());
                    jsonObject.put("brand_ar", list.getBrand_ar());
                    jsonObject.put("model", list.getModel());
                    jsonObject.put("model_ar", list.getModel_ar());
                    jsonObject.put("category", list.getCategory());
                    jsonObject.put("serialized", list.getSerialized());
                    jsonObject.put("video", list.getVideo());
                    jsonObject.put("video_ar", list.getVideo_ar());
                    jsonObject.put("acce_mobile", list.getAcce_mobile());
                    jsonObject.put("acce_mobile_war", list.getAcce_mobile_war());
                    jsonObject.put("acce_charger", list.getAcce_charger());
                    jsonObject.put("acce_charger_war", list.getAcce_charger_war());
                    jsonObject.put("acce_earphone", list.getAcce_earphone());
                    jsonObject.put("acce_earphone_war", list.getAcce_earphone_war());
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
 */
            }

            @Override
            public void minus(ProductList list, int position, TextView county) {

                int add = Integer.parseInt(county.getText().toString().trim()) - 1;
                county.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "minus: " + MainjsonObject.toString());
                sessionManage.addcard(MainjsonObject.toString());

                Log.e(TAG, "QtyAdd: " + MainjsonObject.toString() );


/*
                int add = Integer.parseInt(county.getText().toString().trim()) - 1;
                county.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "minus: " + MainjsonObject.toString());
                sessionManage.addcard(MainjsonObject.toString());
*/

            }

            @Override
            public void RemoveItem(ProductList list, int position, TextView county) {

                try {
                    MainjsonObject.getJSONObject(BRAND_ID).remove(list.getId());
                    if(MainjsonObject.getJSONObject(BRAND_ID).length() == 0) MainjsonObject.remove(BRAND_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
                if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
                Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
                cardQuntyUpdate();



/*
                MainjsonObject.remove(list.getId());
                sessionManage.addcard(MainjsonObject.toString());
                Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
                if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
                cardQuntyUpdate();
*/

            }

            @Override
            public void QtyAdd(ProductList list, int position, TextView countqty) {

                int add = Integer.parseInt(countqty.getText().toString().trim()) + 1;
                countqty.setText(String.valueOf(add));
                try {
//                    MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
                    MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());

                Log.e(TAG, "QtyAdd: " + MainjsonObject.toString() );


/*
                int add = Integer.parseInt(countqty.getText().toString().trim()) + 1;
                countqty.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
*/


            }
        };
    }

    private static class PriceshortLowToHight implements Comparator<ProductList> {

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





































