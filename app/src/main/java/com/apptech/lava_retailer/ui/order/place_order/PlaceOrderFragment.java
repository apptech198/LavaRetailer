package com.apptech.lava_retailer.ui.order.place_order;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.PurchaseNowAdapter;
import com.apptech.lava_retailer.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.lava_retailer.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.lava_retailer.databinding.PlaceOrderFragmentBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.modal.product.ProductList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.NumberConvertArabic;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.other.SpacesItemDecoration;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PlaceOrderFragment extends Fragment implements ShortFilterBottomSheetFragment.ShortItemClck , CategoryFilterBottomSheetFragment.CategoryInterface {

    private PlaceOrderViewModel mViewModel;
    SessionManage sessionManage;
    String json;
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
    List<ComodityLists>  comodityLists = new ArrayList<>();
    String Country_id = "";
    CategoryFilterBottomSheetFragment categoryFilterBottomSheetFragment;


    public static PlaceOrderFragment newInstance() {
        return new PlaceOrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Place_Order));

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
        }else {
            cardQuntyUpdate();
        }

        Log.e(TAG, "onActivityCreated: " + MainjsonObject.toString() );

        cardQuntyUpdate();
        productLists.clear();
        adapterinterface();

        if (new NetworkCheck().haveNetworkConnection(requireActivity())){
            getProduct();
        }else {
            binding.noproduct.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.GONE);
            CheckInternetAleart();
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

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
             categoryFilterBottomSheetFragment = new CategoryFilterBottomSheetFragment(this);
            categoryFilterBottomSheetFragment.show(getChildFragmentManager(), "category filter");
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


    void CheckInternetAleart(){

        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())

                .setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("No Internet")

                .setMessage("Please Check Your Internet Connection!")

                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    navController.popBackStack();
                    navController.navigate(R.id.placeOrderFragment);
                })
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

    }


    void cardQuntyUpdate() {

        SessionManage sessionManage1 = SessionManage.getInstance(getContext());
        String json1 = sessionManage1.getUserDetails().get("CARD_DATA");

        if(sessionManage1.getUserDetails().get("CARD_DATA") != null){
            JSONObject object = null;
            try {
                object = new JSONObject(json1);
                Log.e(TAG, "cardQuntyUpdate: " + object.toString() );
                JSONObject productObjct = new JSONObject(object.getJSONObject(BRAND_ID).toString());

                if (object.length() > 0){
                    if (productObjct.length() > 0){
                        binding.card.cardRound.setVisibility(View.VISIBLE);
                        binding.card.countCard.setText(String.valueOf(productObjct.length()));
                    }else {
                        binding.card.cardRound.setVisibility(View.GONE);
                    }
                }else {
                    binding.card.cardRound.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                binding.card.cardRound.setVisibility(View.GONE);
            }
        }else {
            Log.e(TAG, "cardQuntyUpdate: " + "empty cart" );
            binding.card.cardRound.setVisibility(View.GONE);
        }

/*
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
*/


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
        try {
            if(purchaseNowAdapter != null){
                Collections.sort(productLists, new NewesShort());
                purchaseNowAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "POPULARITY: " + e.getMessage() );
        }
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
                Collections.sort(productLists , new PriceshortDate());
                purchaseNowAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "POPULARITY: " + e.getMessage() );
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Place_Order));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getProduct(){
        binding.progressbar.setVisibility(View.VISIBLE);

        String brandid = sessionManage.getUserDetails().get("BRAND_ID");
        String retailer_id = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);
        String locality_id = sessionManage.getUserDetails().get("LOCALITY_ID");

        if(sessionManage.getUserDetails().get("LOGIN_COUNTRY_ID") != null){
            Country_id =  sessionManage.getUserDetails().get("LOGIN_COUNTRY_ID");
        }

        lavaInterface.PRODUCT_LIST2(brandid , retailer_id,locality_id , Country_id , sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME)).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                try {


                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()) );

                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        if (error.equalsIgnoreCase("false")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("list");

                            productLists.clear();
                            for (int i=0 ; i< jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                productLists.add(new ProductList(
                                        object.getString("id")
                                        ,object.getString("marketing_name")
                                        ,object.getString("marketing_name_ar")
                                        ,object.optString("marketing_name_fr")
                                        ,object.getString("des")
                                        ,object.getString("des_ar")
                                        ,object.optString("des_fr")
                                        ,object.getString("actual_price")
                                        ,object.getString("dis_price")
                                        ,object.getString("thumb")
                                        ,object.optString("thumb_ar")
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
                                        ,object.getString("seller_id")
                                        ,object.getString("seller_name")
                                        ,object.getString("time")
                                ));
                                Log.e(TAG, "onResponse time : " + object.getString("marketing_name"));
                                Log.e(TAG, "onResponse time : " + object.getString("time"));
                            }

                            if(productLists.size() > 0){
                                purchaseNowAdapter = new PurchaseNowAdapter(productLists, purchaseNowIterface);
                                binding.PurchaseNowRecyclerView.setAdapter(purchaseNowAdapter);
                                binding.noproduct.setVisibility(View.GONE);
                                binding.progressbar.setVisibility(View.GONE);
                                binding.PurchaseNowRecyclerView.setVisibility(View.VISIBLE);
                                return;
                            }

                            binding.noproduct.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.PurchaseNowRecyclerView.setVisibility(View.GONE);
                            return;
                        }

                        binding.noproduct.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "" + message , Toast.LENGTH_SHORT).show();
                        return;
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e(TAG, "onResponse: " + e.getMessage() );
                    }
                    binding.noproduct.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );

                }

                if(binding != null){
                    binding.noproduct.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.noproduct.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });

    }






    private void ErrorDilaog(String errormsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error Message");
        builder.setMessage(errormsg);
//        builder.setNegativeButton("" , (dialog, which) -> {
//            dialog.dismiss();
//        });
        builder.setPositiveButton("Close" , (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void adapterinterface(){
        purchaseNowIterface = new PurchaseNowAdapter.PurchaseNowIterface() {
            @Override
            public void itemClick(ProductList list) {

            }

            @Override
            public void addItem(ProductList list, int position, TextView textView) {

                try {
                    if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                        textView.setText(new NumberConvertArabic().NumberConvertArabic(1));
                    }
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Log.e(TAG, "addItem: " + e.getMessage() );
                }



                JSONObject jsonObject = new JSONObject();
                try {
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
                    jsonObject.put("commodity_id", list.getCommodity_id());
                    jsonObject.put("format", list.getFormat());
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
                    jsonObject.put("prowar", list.getProwar());
                    jsonObject.put("pro_war_days", list.getPro_war_days());
                    jsonObject.put("battery_war", list.getBattery_war());
                    jsonObject.put("battery_war_days", list.getBattery_war_days());
                    jsonObject.put("charging_adapter_war", list.getCharging_adapter_war());
                    jsonObject.put("charging_adapter_war_days", list.getCharging_adapter_war_days());
                    jsonObject.put("charger_war", list.getCharger_war());
                    jsonObject.put("charger_war_days", list.getCharger_war_days());
                    jsonObject.put("usb_war", list.getUsb_war());
                    jsonObject.put("usb_war_days", list.getUsb_war_days());
                    jsonObject.put("wired_earphone_war", list.getWired_earphone_war());
                    jsonObject.put("wired_earphone_war_days", list.getWired_earphone_war_days());
                    jsonObject.put("available_qty", list.getAvailable_qty());
                    jsonObject.put("hide", list.getHide());
                    jsonObject.put("total_sale", list.getTotal_sale());
                    jsonObject.put("seller_id", list.getSeller_id());
                    jsonObject.put("seller_name", list.getSeller_name());
                    jsonObject.put("time", list.getTime());
                    jsonObject.put("qty", "1");
                    jsonObject.put("marketing_name_fr", list.getMarketing_name_fr());
                    jsonObject.put("des_fr", list.getDes_fr());
                    jsonObject.put("marketing_name_fr", list.getMarketing_name_fr());
                    jsonObject.put("des_fr", list.getDes_fr());

                    ProductJsonObject.put(list.getId() , jsonObject);
                    Log.e(TAG, "addItem ProductJsonObject: " + ProductJsonObject.toString() );
                    MainjsonObject.put(BRAND_ID , ProductJsonObject);
                    Log.e(TAG, "addItem end: " + MainjsonObject.toString() );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
                cardQuntyUpdate();


            }

            @Override
            public void minus(ProductList list, int position, TextView county) {

                try {

                    if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {
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
                    }else {

                        int AR_Num = Integer.parseInt(county.getText().toString().trim());
                        String RomNum = new NumberConvertArabic().arabicNumberCovert(AR_Num);
                        int add = Integer.parseInt(RomNum) - 1;
                        county.setText(String.valueOf(new NumberConvertArabic().NumberConvertArabic(add)));

                        try {
                            MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sessionManage.addcard(MainjsonObject.toString());
                        Log.e(TAG, "QtyAdd: " + MainjsonObject.toString() );
                    }

                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Log.e(TAG, "minus: " + e.getMessage() );
                }


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


            }

            @Override
            public void QtyAdd(ProductList list, int position, TextView countqty) {


                if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {

                    int add = Integer.parseInt(countqty.getText().toString().trim()) + 1;
                    countqty.setText(String.valueOf(add));
                    try {
                        MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {

                    try {

                        int AR_Num = Integer.parseInt(countqty.getText().toString().trim());
                        String RomNum = new NumberConvertArabic().arabicNumberCovert(AR_Num);
                        int add = Integer.parseInt(RomNum) + 1;
//                    Log.e(TAG, "QtyAdd: " + add );
//                    Log.e(TAG, "QtyAdd: " + AR_Num );
                        countqty.setText(new NumberConvertArabic().NumberConvertArabic(add));
                        try {
                            MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        Log.e(TAG, "QtyAdd: " + e.getMessage() );
                    }

                }
                sessionManage.addcard(MainjsonObject.toString());


            }
        };
    }

    @Override
    public void OnItemCategoryClick(ComodityLists comodityLists) {

        binding.progressbar.setVisibility(View.VISIBLE);

        List<ProductList> productFilter = new ArrayList<>();

        categoryFilterBottomSheetFragment.dismiss();

        try {
            if((purchaseNowAdapter != null) && (!productLists.isEmpty())){

//                switch (sessionManage.getUserDetails().get("LANGUAGE")){
//                    case "en":
//                        holder.binding.categoryName.setText(list.getName());
//                        break;
//                    case "fr":
//                        if(list.getName_fr().isEmpty()){
//                            holder.binding.categoryName.setText(list.getName());
//                        }else {
//                            holder.binding.categoryName.setText(list.getName_fr());
//                        }
//                        break;
//                    case "ar":
//                        holder.binding.categoryName.setText(list.getName_ar());
//                        break;
//                }
//
                for (ProductList list : productLists){
                    if(comodityLists.getName().trim().equalsIgnoreCase(list.getCommodity().trim())){
                        productFilter.add(list);
                    }
                }

                if(productFilter.size() > 0){
                    purchaseNowAdapter = new PurchaseNowAdapter(productFilter , purchaseNowIterface);
                    binding.PurchaseNowRecyclerView.setAdapter(purchaseNowAdapter);
                    purchaseNowAdapter.notifyDataSetChanged();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noproduct.setVisibility(View.GONE);
                    binding.PurchaseNowRecyclerView.setVisibility(View.VISIBLE);
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                binding.noproduct.setVisibility(View.VISIBLE);
                binding.PurchaseNowRecyclerView.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.e(TAG, "POPULARITY: " + e.getMessage() );
        }
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

    private static class PriceshortDate implements Comparator<ProductList>{

        @Override
        public int compare(ProductList o1, ProductList o2) {
            return o2.getTime().compareTo(o1.getTime());
        }
    }


    private static class NewesShort implements Comparator<ProductList>{

        @Override
        public int compare(ProductList o1, ProductList o2) {
            int a = 0;
            int b = 0;
            try {
                a = Integer.parseInt(o1.getTotal_sale());
                b = Integer.parseInt(o2.getTotal_sale());
            }catch (NullPointerException e){
                Log.e(TAG, "compare: " + e.getMessage() );
            }
            return   b-a ;
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





































