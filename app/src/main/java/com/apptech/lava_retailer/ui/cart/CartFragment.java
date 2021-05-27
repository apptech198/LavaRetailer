package com.apptech.lava_retailer.ui.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.activity.SuccessActivity;
import com.apptech.lava_retailer.adapter.CardAdapter;
import com.apptech.lava_retailer.adapter.TradeProgramTabAdapter;
import com.apptech.lava_retailer.databinding.CartFragmentBinding;
import com.apptech.lava_retailer.modal.card.CardList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.NumberConvertArabic;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
    AlertDialog alertDialog  , alertDialog1;
    JsonObject orderPlace = new JsonObject();
    JsonArray OPjsonArray = new JsonArray() ;
    LavaInterface lavaInterface;
    String BRAND_ID = "";
    JSONObject ProductJsonObject = new JSONObject();
    int PRODUCT_TOTAL_AMT = 0 , PRODUCT_DISCOUNT_AMT = 0 , PRODUCT_ACTUCAL_AMT = 0;
    private ProgressDialog progressDialog;


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView brand_name = getActivity().findViewById(R.id.brand_name);
        brand_name.setVisibility(View.GONE);

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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        binding.addressEdittext.setText(sessionManage.getUserDetails().get(SessionManage.ADDRESS));
        binding.up.setOnClickListener(v -> {
            AddressChange();
        });


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
                    JSONObject object = ProductJsonObject.getJSONObject(_pubKey);

                    cardData.add(new CardList(
                            object.getString("id")
                            ,object.getString("marketing_name")
                            ,object.getString("marketing_name_ar")
                            ,object.getString("des")
                            ,object.getString("des_ar")
                            ,object.getString("actual_price")
                            ,object.getString("dis_price")
                            ,object.getString("thumb")
                            ,object.getString("thumb_ar")
                            ,object.getString("sku")
                            ,object.optString("commodity_id")
                            ,object.getString("format")
                            ,object.getString("commodity")
                            ,object.getString("commodity_ar")
                            ,object.getString("brand_id")
                            ,object.getString("brand")
                            ,object.getString("brand_ar")
                            ,object.getString("model")
                            ,object.optString("model_ar")
                            ,object.optString("category")
                            ,object.getString("serialized")
                            ,object.getString("video")
                            ,object.getString("video_ar")
                            ,object.getString("prowar")
                            ,object.getString("pro_war_days")
                            ,object.getString("battery_war")
                            ,object.getString("battery_war_days")
                            ,object.getString("charging_adapter_war")
                            ,object.getString("charging_adapter_war_days")
                            ,object.getString("charger_war")
                            ,object.getString("charger_war_days")
                            ,object.getString("usb_war")
                            ,object.getString("usb_war_days")
                            ,object.getString("wired_earphone_war")
                            ,object.getString("wired_earphone_war_days")
                            ,object.optString("available_qty")
                            ,object.optString("hide")
                            ,object.getString("total_sale")
                            ,object.optString("seller_id")
                            ,object.optString("seller_name")
                            ,object.optString("time")
                            ,object.optString("qty")
                            ,object.optString("marketing_name_fr")
                            ,object.optString("des_fr")
                    ));

                    try {
                        int amt = Integer.parseInt(object.getString("actual_price")) * Integer.parseInt(object.getString("qty"));
                        int disamt = Integer.parseInt(object.getString("dis_price")) * Integer.parseInt(object.getString("qty"));
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


                ((CardList)getActivity().getApplicationContext()).setCardLists(cardData);

                PRODUCT_TOTAL_AMT = TotalproductAmt;
                PRODUCT_DISCOUNT_AMT = DisAmt;
                DeliveryTotalAmt = TotalproductAmt - DisAmt;
                PRODUCT_ACTUCAL_AMT = DeliveryTotalAmt;

                if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {

                    binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  TotalproductAmt));
                    binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  DisAmt));
                    binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));
                    binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));
                    String itemnum = "Price (" + String.valueOf(item) + " Item)";
                    binding.ItemQty.setText(itemnum);

                }else {

                    try {

                        binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(TotalproductAmt)));
                        binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(DisAmt)));
                        binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(DeliveryTotalAmt)));
                        binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(DeliveryTotalAmt)));

                        String itemnum = "Price (" + new NumberConvertArabic().NumberConvertArabic(item) + " Item)";
                        binding.ItemQty.setText(itemnum);

                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        Log.e(TAG, "onActivityCreated: " + e.getMessage());
                    }

                }





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
            binding.addressLayout.setVisibility(View.GONE);
        }

        binding.PlaceOrder.setOnClickListener(v -> {
            PlaceOrder();
        });

        binding.AddressTextView.setText(sessionManage.getUserDetails().get("ADDRESS").toString());

        binding.ChangeAddress.setOnClickListener(v1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_address_change_dialog , null);
            LinearLayout close = view.findViewById(R.id.close);
            LinearLayout submit = view.findViewById(R.id.submit);

            TextInputLayout addressEdittext = view.findViewById(R.id.addressEdittext);
            close.setOnClickListener(v -> {alertDialog1.dismiss();});
            submit.setOnClickListener(v -> {
                if(addressEdittext.getEditText().getText().toString().trim().isEmpty()){
                    addressEdittext.setError(getResources().getString(R.string.field_required));
                    addressEdittext.setErrorEnabled(true);
                    return;
                }
                addressEdittext.setError(null);
                addressEdittext.setErrorEnabled(false);

                String add = addressEdittext.getEditText().getText().toString().trim();
                sessionManage.AddressChange(add);
                binding.AddressTextView.setText(add);
                alertDialog1.dismiss();
            });

            builder.setView(view);
            alertDialog1 = builder.create();
            alertDialog1.show();
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
                binding.addressLayout.setVisibility(View.GONE);
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
            binding.addressLayout.setVisibility(View.GONE);
        }
        cardAdapter.notifyDataSetChanged();
        CalCart();
    }

    @Override
    public void addQty(int postion, CardList list, TextView cartQty) {

        int addQty = 0;
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {
            try {
                int qty = Integer.parseInt(cartQty.getText().toString().trim());
                addQty = qty += 1;
                cartQty.setText(String.valueOf(addQty));
//            Toast.makeText(getContext(), ""+addQty, Toast.LENGTH_SHORT).show();
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }else {

            try {
                int AR_Num = Integer.parseInt(cartQty.getText().toString().trim());
                String RomNum = new NumberConvertArabic().arabicNumberCovert(AR_Num);
                addQty = Integer.parseInt(RomNum) + 1;
                cartQty.setText(String.valueOf(new NumberConvertArabic().NumberConvertArabic(addQty)));

            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }

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

        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {
            qty = Integer.parseInt(cartQty.getText().toString().trim());
            if(qty == 1){
                Toast.makeText(getContext(), "minimum quantity one is required", Toast.LENGTH_SHORT).show();
                return;
            }else {
                addQty = qty -= 1;
                cartQty.setText(String.valueOf(addQty));
                try {
                    MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(addQty));
                    sessionManage.addcard(MainjsonObject.toString());
                    CalCart();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {

            qty = Integer.parseInt(cartQty.getText().toString().trim());
            if(qty == 1){
                Toast.makeText(getContext(), "minimum quantity one is required", Toast.LENGTH_SHORT).show();
                return;
            }else {

                int AR_Num = Integer.parseInt(String.valueOf(qty));
                String RomNum = new NumberConvertArabic().arabicNumberCovert(AR_Num);
                int aa = Integer.parseInt(RomNum);
                addQty = aa -= 1;
                cartQty.setText(String.valueOf(new NumberConvertArabic().NumberConvertArabic(addQty)));
                try {
                    MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(addQty));
                    sessionManage.addcard(MainjsonObject.toString());
                    CalCart();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

                PRODUCT_TOTAL_AMT = TotalproductAmt;
                PRODUCT_DISCOUNT_AMT = DisAmt;
                DeliveryTotalAmt = TotalproductAmt - DisAmt;
                PRODUCT_ACTUCAL_AMT = DeliveryTotalAmt;

                if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {

                    binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  TotalproductAmt));
                    binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  DisAmt));
                    binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));
                    binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));

                    String itemnum = "Price (" + String.valueOf(item) + " Item)";
                    binding.ItemQty.setText(itemnum);

                }else {
                    try {

                        binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(TotalproductAmt)));
                        binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(DisAmt)));
                        binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(DeliveryTotalAmt)));
                        binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  new NumberConvertArabic().NumberConvertArabic(DeliveryTotalAmt)));

                        String itemnum = "Price (" + new NumberConvertArabic().NumberConvertArabic(item) + " Item)";
                        binding.ItemQty.setText(itemnum);

                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        Log.e(TAG, "CalCart: " + e.getMessage() );
                    }

                }
//
//                binding.totalPrice.setText(String.valueOf(getResources().getString(R.string.egp) +  TotalproductAmt));
//                binding.disamt.setText(String.valueOf(getResources().getString(R.string.egp) +  DisAmt));
//                binding.totalAmt.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));
//                binding.TotalAmount.setText(String.valueOf(getResources().getString(R.string.egp) +  DeliveryTotalAmt));




            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
        }else {
            binding.PriceDetailsLayout.setVisibility(View.GONE);
            binding.CardDetails.setVisibility(View.GONE);
            binding.NoItem.setVisibility(View.VISIBLE);
            binding.addressLayout.setVisibility(View.GONE);
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
        LinearLayout addressLayout = view.findViewById(R.id.addressLayout);
        TextView address = view.findViewById(R.id.address);

        address.setText(sessionManage.getUserDetails().get("ADDRESS"));

        submit.setOnClickListener( v -> {

            if(new NetworkCheck().haveNetworkConnection(getActivity())){
//                submit.setClickable(false);
//                submit.setEnabled(false);
                alertDialog.dismiss();
                AlertDialog();
                return;
            }
            Toast.makeText(getContext(), "" + getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });

        addressLayout.setOnClickListener(v -> {
            alertDialog.dismiss();
            AddressChange();
        });

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        no.setOnClickListener( v -> alertDialog.dismiss());
    }

    private void AddressChange(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.row_address_change_dialog , null);
        LinearLayout close = view.findViewById(R.id.close);
        LinearLayout submit = view.findViewById(R.id.submit);

        TextInputLayout addressEdittext = view.findViewById(R.id.addressEdittext);
        close.setOnClickListener(v -> {alertDialog1.dismiss();});
        submit.setOnClickListener(v -> {
            if(addressEdittext.getEditText().getText().toString().trim().isEmpty()){
                addressEdittext.setError(getResources().getString(R.string.field_required));
                addressEdittext.setErrorEnabled(true);
                return;
            }
            addressEdittext.setError(null);
            addressEdittext.setErrorEnabled(false);

            String add = addressEdittext.getEditText().getText().toString().trim();
            sessionManage.AddressChange(add);
            binding.AddressTextView.setText(add);
            binding.addressEdittext.setText(sessionManage.getUserDetails().get(SessionManage.ADDRESS));
            alertDialog1.dismiss();
        });

        builder.setView(view);
        alertDialog1 = builder.create();
        alertDialog1.show();
    };

    private void AlertDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.row_custom_alert_dialog , null );
        builder.setView(v);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.no);



        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
            orderPlace();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});



    }

    private void orderPlace(){

        progressDialog.show();

        String ret_id = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);
        String ret_name = sessionManage.getUserDetails().get("NAME");
        String ret_mobile = sessionManage.getUserDetails().get("MOBILE");
        String address = sessionManage.getUserDetails().get("ADDRESS");

//        String pretotal = binding.totalPrice.getText().toString().trim();
        String pretotal = String.valueOf(PRODUCT_TOTAL_AMT);
//        String order_total = binding.TotalAmount.getText().toString().trim();
        String order_total = String.valueOf(PRODUCT_ACTUCAL_AMT);
//        String discount = binding.disamt.getText().toString().trim();
        String discount = String.valueOf(PRODUCT_DISCOUNT_AMT);


        Log.e(TAG, "orderPlace: " + pretotal);
        Log.e(TAG, "orderPlace: " + order_total);
        Log.e(TAG, "orderPlace: " + discount);


        orderPlace.addProperty("ret_id" , ret_id);
        orderPlace.addProperty("ret_name" , ret_name);
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

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){
                                MainjsonObject.remove(BRAND_ID);
                                if(MainjsonObject.length() == 0){
                                    sessionManage.clearaddcard();
                                }else {
                                    sessionManage.addcard(MainjsonObject.toString());
                                }
//                                startActivity(new Intent(getContext() , SuccessActivity.class)
//                                        .putExtra("amt" , binding.totalAmt.getText().toString())
//                                );
//                                getActivity().finish();;

                                progressDialog.dismiss();
                                OrderSuccess();
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
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
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });


    }




    void OrderSuccess(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.order_success_dailog , null);
        LinearLayout buymore = view.findViewById(R.id.buymore);
        LinearLayout no = view.findViewById(R.id.no);
        LinearLayout myorder = view.findViewById(R.id.Myorder);


        buymore.setOnClickListener( v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
            navController.navigate(R.id.placeOrderFragment);
        });

        myorder.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
            navController.navigate(R.id.orderStatusFragment);
        });


        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        no.setOnClickListener( v -> alertDialog.dismiss());
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
            binding.addressLayout.setVisibility(View.GONE);
        }


    }



}