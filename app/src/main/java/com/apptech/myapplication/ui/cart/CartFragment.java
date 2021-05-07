package com.apptech.myapplication.ui.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.CartActivity;
import com.apptech.myapplication.activity.SuccessActivity;
import com.apptech.myapplication.adapter.CardAdapter;
import com.apptech.myapplication.databinding.ActivityCartBinding;
import com.apptech.myapplication.databinding.CartFragmentBinding;
import com.apptech.myapplication.modal.card.CardList;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CardAdapter.CardInterface {

    private CartViewModel mViewModel;
    CartFragmentBinding binding;
    private static final String TAG = "CartFragment";

    SessionManage sessionManage;
    List<CardList> cardData = new ArrayList<>();
    CardAdapter cardAdapter;
    JSONObject MainjsonObject = new JSONObject();
    int TotalproductAmt = 0 , DisAmt = 0 , item = 0 , DeliveryTotalAmt = 0;
    AlertDialog alertDialog;
    JsonObject orderPlace = new JsonObject();
    JsonArray OPjsonArray = new JsonArray() ;
    LavaInterface lavaInterface;
    String BRAND_ID = "";
    JSONObject ProductJsonObject = new JSONObject();


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CartFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        sessionManage = SessionManage.getInstance(getContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        String json = sessionManage.getUserDetails().get("CARD_DATA");
        BRAND_ID = sessionManage.getUserDetails().get("BRAND_ID");

        // TODO: Use the ViewModel

        if (json != null) {
            binding.PriceDetailsLayout.setVisibility(View.VISIBLE);
            binding.NoItem.setVisibility(View.GONE);

            JSONObject issueObj = null;
            try {

                MainjsonObject = new JSONObject(json);
                ProductJsonObject = MainjsonObject.getJSONObject(BRAND_ID);

                Iterator iterator = ProductJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = ProductJsonObject.getJSONObject(key);
                    String _pubKey = issue.optString("id");
                    JSONObject jo = ProductJsonObject.getJSONObject(_pubKey);
                    cardData.add(new CardList(
                            jo.get("id").toString()
                            ,jo.get("product_id").toString()
                            ,jo.get("distributor_id").toString()
                            ,jo.get("distributor_name").toString()
                            ,jo.get("locality").toString()
                            ,jo.get("locality_id").toString()
                            ,jo.get("marketing_name").toString()
                            ,jo.get("marketing_name_ar").toString()
                            ,jo.get("des").toString()
                            ,jo.get("des_ar").toString()
                            ,jo.get("actual_price").toString()
                            ,jo.get("dis_price").toString()
                            ,jo.get("thumb").toString()
                            ,jo.get("thumb_ar").toString()
                            ,jo.get("sku").toString()
                            ,jo.get("imei").toString()
                            ,jo.get("commodity").toString()
                            ,jo.get("commodity_ar").toString()
                            ,jo.get("brand_id").toString()
                            ,jo.get("brand").toString()
                            ,jo.get("brand_ar").toString()
                            ,jo.get("model").toString()
                            ,jo.get("model_ar").toString()
                            ,jo.get("category").toString()
                            ,jo.get("serialized").toString()
                            ,jo.get("video").toString()
                            ,jo.get("video_ar").toString()
                            ,jo.get("acce_mobile").toString()
                            ,jo.get("acce_mobile_war").toString()
                            ,jo.get("acce_charger").toString()
                            ,jo.get("acce_charger_war").toString()
                            ,jo.get("acce_earphone").toString()
                            ,jo.get("acce_earphone_war").toString()
                            ,jo.get("available_qty").toString()
                            ,jo.get("hide").toString()
                            ,jo.get("total_sale").toString()
                            ,jo.get("qty").toString()
                    ));

                    try {
                        int amt = Integer.parseInt(jo.getString("actual_price")) * Integer.parseInt(jo.getString("qty"));
                        int disamt = Integer.parseInt(jo.getString("dis_price")) * Integer.parseInt(jo.getString("qty"));
                        TotalproductAmt +=  amt;
                        int disAmt = amt - disamt;
                        DisAmt += disAmt;
                    }catch (NumberFormatException e){
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    item += 1;
                }
                Log.e(TAG, "onCreate: " + cardData.size());
                cardAdapter = new CardAdapter(cardData, this);
                binding.CardRecyclerView.setAdapter(cardAdapter);
                binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  TotalproductAmt));
                binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  DisAmt));

                DeliveryTotalAmt = TotalproductAmt - DisAmt;
                binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));
                binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));

                String itemnum = "Price (" + String.valueOf(item) + " Item)";
                binding.ItemQty.setText(itemnum);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onCreate: " + e.getMessage() );
                binding.PriceDetailsLayout.setVisibility(View.GONE);
                binding.CardDetails.setVisibility(View.GONE);
                binding.NoItem.setVisibility(View.VISIBLE);
            }
        }else {
            binding.PriceDetailsLayout.setVisibility(View.GONE);
            binding.CardDetails.setVisibility(View.GONE);
            binding.NoItem.setVisibility(View.VISIBLE);
        }

        binding.PlaceOrder.setOnClickListener(v -> {
            PlaceOrder();
        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void removeItem(int postion, CardList list) {

        cardData.remove(postion);
        try {
            MainjsonObject.getJSONObject(BRAND_ID).remove(list.getId());
            if (MainjsonObject.getJSONObject(BRAND_ID).length() == 0) {
                MainjsonObject.remove(BRAND_ID);
                binding.PriceDetailsLayout.setVisibility(View.GONE);
                binding.NoItem.setVisibility(View.VISIBLE);
                binding.CardDetails.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sessionManage.addcard(MainjsonObject.toString());

        if (MainjsonObject.length() == 0) {
            sessionManage.clearaddcard();
            binding.PriceDetailsLayout.setVisibility(View.GONE);
            binding.NoItem.setVisibility(View.VISIBLE);
            binding.CardDetails.setVisibility(View.GONE);
        }
        cardAdapter.notifyDataSetChanged();
        CalCart();
    }

    @Override
    public void addQty(int postion, CardList list, TextView cartQty) {
        int addQty = 0;
        try {
            int qty = Integer.parseInt(cartQty.getText().toString().trim());
            addQty = qty += 1;
            cartQty.setText(String.valueOf(addQty));
            Toast.makeText(getContext(), ""+addQty, Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        try {
            MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(addQty));
            sessionManage.addcard(MainjsonObject.toString());
            CalCart();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void minQty(int postion, CardList list, TextView cartQty) {
        int addQty = 0;
        int qty = 0;
        qty = Integer.parseInt(cartQty.getText().toString().trim());
        if(qty == 1){
            Toast.makeText(getContext(), "minimum quantity one is required", Toast.LENGTH_SHORT).show();
            return;
        }else {
            addQty = qty -= 1;
            cartQty.setText(String.valueOf(addQty));
            Toast.makeText(getContext(), ""+addQty, Toast.LENGTH_SHORT).show();
            try {
                MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(addQty));
                sessionManage.addcard(MainjsonObject.toString());
                CalCart();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void CalCart(){

        int TotalproductAmt = 0 , DisAmt = 0 , item = 0 , DeliveryTotalAmt = 0;


        String json = sessionManage.getUserDetails().get("CARD_DATA");

        if (json != null) {

//            binding.PriceDetailsLayout.setVisibility(View.VISIBLE);
//            binding.NoItem.setVisibility(View.GONE);

            JSONObject issueObj = null;
            try {
                MainjsonObject = new JSONObject(json);;
                ProductJsonObject = MainjsonObject.getJSONObject(BRAND_ID);

                Iterator iterator = ProductJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = ProductJsonObject.getJSONObject(key);
                    String _pubKey = issue.optString("id");
                    JSONObject jo = ProductJsonObject.getJSONObject(_pubKey);
                    try {
                        int amt = Integer.parseInt(jo.getString("actual_price")) * Integer.parseInt(jo.getString("qty"));
                        int disamt = Integer.parseInt(jo.getString("dis_price")) * Integer.parseInt(jo.getString("qty"));
                        TotalproductAmt +=  amt;
                        int disAmt = amt - disamt;
                        DisAmt += disAmt;
                    }catch (NumberFormatException e){
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    item += 1;
                }


                binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  TotalproductAmt));
                binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  DisAmt));

                DeliveryTotalAmt = TotalproductAmt - DisAmt;
                binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));
                binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));

                String itemnum = "Price (" + String.valueOf(item) + " Item)";
                binding.ItemQty.setText(itemnum);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
        }else {
            binding.PriceDetailsLayout.setVisibility(View.GONE);
            binding.CardDetails.setVisibility(View.GONE);
            binding.NoItem.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Cart");
    }


    private void PlaceOrder(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_place_order , null);
        LinearLayout submit = view.findViewById(R.id.submit);
        LinearLayout no = view.findViewById(R.id.no);
        submit.setOnClickListener( v -> orderPlace());

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        no.setOnClickListener( v -> alertDialog.dismiss());
    }


    private void orderPlace(){

        String ret_id = sessionManage.getUserDetails().get("ID");
        String ret_name = sessionManage.getUserDetails().get("NAME");
//        String dis_id = sessionManage.getUserDetails().get("");
//        String dis_name = sessionManage.getUserDetails().get("");
        String ret_mobile = sessionManage.getUserDetails().get("MOBILE");
        String address = sessionManage.getUserDetails().get("ADDRESS");
        String pretotal = binding.totalPrice.getText().toString().trim();
        String order_total = binding.TotalAmount.getText().toString().trim();
        String discount = binding.disamt.getText().toString().trim();



        orderPlace.addProperty("ret_id" , ret_id);
        orderPlace.addProperty("ret_name" , ret_name);
//        orderPlace.addProperty("dis_id" , "");
//        orderPlace.addProperty("dis_name" , "");
        orderPlace.addProperty("ret_mobile" , ret_mobile);
        orderPlace.addProperty("address" , address);
        orderPlace.addProperty("pretotal" , pretotal.substring(1));
        orderPlace.addProperty("order_total" , order_total.substring(1));
        orderPlace.addProperty("discount" , discount.substring(1));
        orderPlace.add("items" , OPjsonArray);
        OrderPlaceAddData();

        Log.e(TAG, "orderPlace: " + orderPlace.toString());

        lavaInterface.BuyProduct(orderPlace).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()){

                    MainjsonObject.remove(BRAND_ID);

                    if(MainjsonObject.length() == 0){
                        sessionManage.clearaddcard();
                    }else {
                        sessionManage.addcard(MainjsonObject.toString());
                    }

                    startActivity(new Intent(getContext() , SuccessActivity.class).putExtra("amt" , binding.totalAmt.getText().toString()));
                    getActivity().finish();;
                }

//                try {
//                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
//                    String error = jsonObject.getString("error");
//                    String message = jsonObject.getString("message");
//                    if(error.equalsIgnoreCase("false")){
//                        sessionManage.clearaddcard();
//                        return;
//                    }
//                    Toast.makeText(CartActivity.this, "" + message, Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


    private void OrderPlaceAddData(){


        String json = sessionManage.getUserDetails().get("CARD_DATA");

        if (json != null) {
            binding.PriceDetailsLayout.setVisibility(View.VISIBLE);
            binding.NoItem.setVisibility(View.GONE);

            try {
                MainjsonObject = new JSONObject(json);
                ProductJsonObject = MainjsonObject.getJSONObject(BRAND_ID);

                Iterator iterator = ProductJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = ProductJsonObject.getJSONObject(key);
                    String _pubKey = issue.optString("id");
                    JSONObject jo = ProductJsonObject.getJSONObject(_pubKey);
                    try {
                        int itemt = Integer.parseInt(jo.getString("dis_price")) * Integer.parseInt(jo.getString("qty"));
                        JsonObject object = new JsonObject();
                        object.addProperty("product_ivt_id", jo.getString("id"));
                        object.addProperty("product_id", jo.getString("product_id"));
                        object.addProperty("product_name", jo.getString("marketing_name"));
                        object.addProperty("imei", jo.getString("imei"));
                        object.addProperty("qty", jo.getString("qty"));
                        object.addProperty("actual_price", jo.getString("actual_price"));
                        object.addProperty("discount_price", jo.getString("dis_price"));
                        object.addProperty("dis_id", jo.getString("distributor_id"));
                        object.addProperty("dis_name", jo.getString("distributor_name"));
                        object.addProperty("item_total", String.valueOf(itemt));
                        OPjsonArray.add(object);

                    }catch (NumberFormatException e){
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
        }else {
            binding.PriceDetailsLayout.setVisibility(View.GONE);
            binding.CardDetails.setVisibility(View.GONE);
            binding.NoItem.setVisibility(View.VISIBLE);
        }


    }



}