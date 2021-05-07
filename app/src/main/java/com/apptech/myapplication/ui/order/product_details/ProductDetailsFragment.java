package com.apptech.myapplication.ui.order.product_details;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.CartActivity;
import com.apptech.myapplication.adapter.ProductGalleryAdapter;
import com.apptech.myapplication.databinding.ProductDetailsFragmentBinding;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.modal.productgallery.ProductGalleryList;
import com.apptech.myapplication.other.LanguageChange;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsFragment extends Fragment {

    private ProductDetailsViewModel mViewModel;
    ProductList list;
    ProductDetailsFragmentBinding binding;
    SessionManage sessionManage;
    private static final String TAG = "ProductDetailsFragment";
    JSONObject MainjsonObject = new JSONObject();
    LavaInterface lavaInterface;
    JSONObject ProductJsonObject = new JSONObject();
    String BRAND_ID = "";
    NavController navController;
    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProductDetailsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        BRAND_ID = sessionManage.getUserDetails().get("BRAND_ID");

        assert getArguments() != null;
        list = ProductDetailsFragmentArgs.fromBundle(getArguments()).getProductList();


        getGallary(list.getId());

        qtyset1();
        cardQuntyUpdate();

        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            binding.productName.setText(list.getMarketing_name());
            binding.productDic.setText(list.getDes());
        } else {
            binding.productName.setText(list.getMarketing_name_ar());
            binding.productDic.setText(list.getDes_ar());
        }

        binding.DisName.setText("Distributor Name : " + list.getDistributor_name());

        binding.productAmt.setText(getResources().getString(R.string.egp)  + list.getDis_price());
        binding.productAmtDic.setText(getResources().getString(R.string.egp)  + list.getActual_price());
        binding.productAmtDic.setPaintFlags(binding.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);




        binding.plus.setOnClickListener(v -> {
            int add = Integer.parseInt(binding.qtyCount.getText().toString().trim()) + 1;
            binding.qtyCount.setText(String.valueOf(add));
            try {
                MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sessionManage.addcard(MainjsonObject.toString());
        });


        binding.sub.setOnClickListener(v -> {


            int a = Integer.parseInt(binding.qtyCount.getText().toString().trim());
            if (a > 1) {
                binding.ProductAdd.setVisibility(View.GONE);
                binding.PlusMinusLayout.setVisibility(View.VISIBLE);

                int add = Integer.parseInt(binding.qtyCount.getText().toString().trim()) - 1;
                binding.qtyCount.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "minus: " + MainjsonObject.toString());
                sessionManage.addcard(MainjsonObject.toString());

            } else {

                binding.ProductAdd.setVisibility(View.VISIBLE);
                binding.PlusMinusLayout.setVisibility(View.GONE);

                try {
                    MainjsonObject.getJSONObject(BRAND_ID).remove(list.getId());
                    if(MainjsonObject.getJSONObject(BRAND_ID).length() == 0) MainjsonObject.remove(BRAND_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sessionManage.addcard(MainjsonObject.toString());
                Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
                if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
                cardQuntyUpdate();
            }


        });

        binding.ProductAdd.setOnClickListener(v -> {
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
                MainjsonObject.put(BRAND_ID , ProductJsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            sessionManage.addcard(MainjsonObject.toString());
            cardQuntyUpdate();
            binding.ProductAdd.setVisibility(View.GONE);
            binding.PlusMinusLayout.setVisibility(View.VISIBLE);

        });

        binding.viewCardFloat.setOnClickListener(v -> {
            navController.navigate(R.id.action_global_cartFragment);
        });
    }

    private void getGallary(String productId) {

        lavaInterface.GetGallery(productId).enqueue(new Callback<ProductGalleryList>() {
            @Override
            public void onResponse(Call<ProductGalleryList> call, Response<ProductGalleryList> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if(response.isSuccessful()){
                    if (!response.body().getError()){
                        if(response.body().getList().size() > 0){
                            binding.viewpager.setAdapter(new ProductGalleryAdapter(response.body().getList() , getContext()));
                        }
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ProductGalleryList> call, Throwable t) {
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });

    }


    private void qtyset1() {
        String json = sessionManage.getUserDetails().get("CARD_DATA");
        if (json != null) {
            try {
                MainjsonObject = new JSONObject(json);
                ProductJsonObject = MainjsonObject.getJSONObject(BRAND_ID);
                binding.qtyCount.setText(ProductJsonObject.getJSONObject(list.getId()).get("qty").toString());
                binding.PlusMinusLayout.setVisibility(View.VISIBLE);
                binding.ProductAdd.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
                binding.ProductAdd.setVisibility(View.VISIBLE);
                binding.PlusMinusLayout.setVisibility(View.GONE);
            }
        } else {
            binding.ProductAdd.setVisibility(View.VISIBLE);
            binding.PlusMinusLayout.setVisibility(View.GONE);
        }
    }

    private void qtyset() {
        String json = sessionManage.getUserDetails().get("CARD_DATA");

        if (json != null) {
            JSONObject issueObj = null;
            try {
                MainjsonObject = new JSONObject(json);
                ProductJsonObject = MainjsonObject.getJSONObject(BRAND_ID);
                issueObj = new JSONObject(json);
                Iterator iterator = issueObj.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = issueObj.getJSONObject(key);
                    //  get id from  issue
                    String _pubKey = issue.optString("id");
                    if (list.getId().equals(_pubKey)) {
                        JSONObject jo = ProductJsonObject.getJSONObject(_pubKey);
                        binding.qtyCount.setText(jo.getJSONObject(_pubKey).getString("qty"));
                        binding.PlusMinusLayout.setVisibility(View.VISIBLE);
                        binding.ProductAdd.setVisibility(View.GONE);
                        break;
                    } else {
                        binding.PlusMinusLayout.setVisibility(View.GONE);
                        binding.ProductAdd.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                binding.ProductAdd.setVisibility(View.VISIBLE);
                binding.PlusMinusLayout.setVisibility(View.GONE);
            }
        } else {
            binding.ProductAdd.setVisibility(View.VISIBLE);
            binding.PlusMinusLayout.setVisibility(View.GONE);
        }
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


}


































