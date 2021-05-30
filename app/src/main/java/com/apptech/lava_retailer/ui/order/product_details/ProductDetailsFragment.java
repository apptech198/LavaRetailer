package com.apptech.lava_retailer.ui.order.product_details;

import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.ProductGalleryAdapter;
import com.apptech.lava_retailer.databinding.ProductDetailsFragmentBinding;
import com.apptech.lava_retailer.list.product_gallery.ProductGalleryLists;
import com.apptech.lava_retailer.modal.product.ProductList;
import com.apptech.lava_retailer.modal.productgallery.ProductGalleryList;
import com.apptech.lava_retailer.other.NumberConvertArabic;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    List<ProductGalleryLists> galleryLists = new ArrayList<>();


    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Price_Details));

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

        binding.ProductAdd.setClickable(true);
        binding.ProductAdd.setEnabled(true);


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

            binding.productName.setText(list.getMarketing_name());
            binding.modelName.setText("Model - "+list.getModel());
            binding.brandName.setText("Brand - "+list.getBrand());


//            binding.productDic.loadData(list.getDes(), "text/html", "UTF-8");

            String htmlData = "<font color='#c4c4c4'>" + list.getDes() + "</font>";
            binding.productDic.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);
//            binding.productDic.loadData(list.getDes(), "text/html", "UTF-8");

            binding.productAmt.setText(getResources().getString(R.string.egp) + list.getDis_price());
            binding.productAmtDic.setText(getResources().getString(R.string.egp) + list.getActual_price());

            try {
                int Actual_price = Integer.parseInt(list.getActual_price());
                int Dis_price = Integer.parseInt(list.getDis_price());
                if(Dis_price >= Actual_price){
                    binding.productAmtDic.setVisibility(View.GONE);
                }

            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }



            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){

                binding.ProductAdd.setClickable(false);
                binding.ProductAdd.setEnabled(false);

                SpannableString string = new SpannableString(list.getActual_price());
                MaskFilter blurMask = new BlurMaskFilter(9f, BlurMaskFilter.Blur.NORMAL);
                string.setSpan(new MaskFilterSpan(blurMask), 0, list.getActual_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.productAmtDic.setText(string);

                SpannableString string1 = new SpannableString(list.getDis_price());
                MaskFilter blurMask1 = new BlurMaskFilter(9f, BlurMaskFilter.Blur.NORMAL);
                string1.setSpan(new MaskFilterSpan(blurMask1), 0, list.getDis_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.productAmt.setText(string);

            }




        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            binding.modelName.setText("Model - "+list.getModel());

            binding.brandName.setText("Brand - "+list.getBrand());


            if(list.getMarketing_name_fr().isEmpty()){
                binding.productName.setText(list.getMarketing_name());
            }else {
                binding.productName.setText(list.getMarketing_name_fr());
            }
            String htmlData;
            if(list.getDes_fr().isEmpty()){
                htmlData = "<font color='#c4c4c4'>" + list.getDes() + "</font>";
                //                binding.productDic.loadData(list.getDes(), "text/html", "UTF-8");
            }else {
                htmlData = "<font color='#c4c4c4'>" + list.getDes_fr() + "</font>";
                //                binding.productDic.loadData(list.getDes_fr(), "text/html", "UTF-8");
            }
            binding.productDic.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);

            binding.productAmt.setText(getResources().getString(R.string.egp) + list.getDis_price());
            binding.productAmtDic.setText(getResources().getString(R.string.egp) + list.getActual_price());

            try {
                int Actual_price = Integer.parseInt(list.getActual_price());
                int Dis_price = Integer.parseInt(list.getDis_price());
                if(Dis_price >= Actual_price){
                    binding.productAmtDic.setVisibility(View.GONE);
                }

            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }



            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){

                binding.ProductAdd.setClickable(false);
                binding.ProductAdd.setEnabled(false);

                SpannableString string = new SpannableString(list.getActual_price());
                MaskFilter blurMask = new BlurMaskFilter(9f, BlurMaskFilter.Blur.NORMAL);
                string.setSpan(new MaskFilterSpan(blurMask), 0, list.getActual_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.productAmtDic.setText(string);

                SpannableString string1 = new SpannableString(list.getDis_price());
                MaskFilter blurMask1 = new BlurMaskFilter(9f, BlurMaskFilter.Blur.NORMAL);
                string1.setSpan(new MaskFilterSpan(blurMask1), 0, list.getDis_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.productAmt.setText(string);
            }


        } else {
            binding.brandName.setText("Brand - "+list.getBrand_ar());
            binding.modelName.setText("Model - "+list.getModel_ar());
            binding.productName.setText(list.getMarketing_name_ar());
//            binding.productDic.loadData(list.getDes_ar(), "text/html", "UTF-8");

//            String htmlData = "<body style= color='#ffffff'>" + list.getDes_ar() + "</body>";
//            binding.productDic.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);

//            String htmlData = "<body style=\"color: white;\">   "</body>";
            String htmlData = "<body style=\"color: #c4c4c4;\"> "+ list.getDes_ar() +" </body>";
            binding.productDic.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null);



            String Dis_price =  new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getDis_price()));
            String Actual_price = new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getActual_price()));

            binding.productAmt.setText(getResources().getString(R.string.egp)  + Dis_price);
            binding.productAmtDic.setText(getResources().getString(R.string.egp)  + Actual_price);


            try {
                int Actual_price1 = Integer.parseInt(list.getActual_price());
                int Dis_price1 = Integer.parseInt(list.getDis_price());
                if(Dis_price1 >= Actual_price1){
                    binding.productAmtDic.setVisibility(View.GONE);
                }

            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }


            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){

                binding.ProductAdd.setClickable(false);
                binding.ProductAdd.setEnabled(false);

                SpannableString string = new SpannableString(Actual_price);
                MaskFilter blurMask = new BlurMaskFilter(9f, BlurMaskFilter.Blur.NORMAL);
                string.setSpan(new MaskFilterSpan(blurMask), 0, Actual_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.productAmtDic.setText(string);

                SpannableString string1 = new SpannableString(Dis_price);
                MaskFilter blurMask1 = new BlurMaskFilter(9f, BlurMaskFilter.Blur.NORMAL);
                string1.setSpan(new MaskFilterSpan(blurMask1), 0, Dis_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.productAmt.setText(string);

            }


        }

//        binding.productAmtDic.setPaintFlags(binding.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        binding.productDic.setWebViewClient(new WebViewClient() {
//            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:document.body.style.color=\"#ffffff\";");
//            }
//        });

//        binding.productDic.setBackgroundColor(getActivity().getResources().getColor(R.color.webblack));



        binding.productDic.setBackgroundColor(Color.TRANSPARENT);






        binding.plus.setOnClickListener(v -> {
            if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {
                int add = Integer.parseInt(binding.qtyCount.getText().toString().trim()) + 1;
                binding.qtyCount.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {

                try {
                    int AR_Num = Integer.parseInt(binding.qtyCount.getText().toString().trim());
                    String RomNum = new NumberConvertArabic().arabicNumberCovert(AR_Num);
                    int add = Integer.parseInt(RomNum) + 1;
                    binding.qtyCount.setText(String.valueOf(new NumberConvertArabic().NumberConvertArabic(add)));
                    try {
                        MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Log.e(TAG, "onActivityCreated: " + e.getMessage() );
                }

            }
            sessionManage.addcard(MainjsonObject.toString());

        });


        binding.sub.setOnClickListener(v -> {


            int a = Integer.parseInt(binding.qtyCount.getText().toString().trim());
            if (a > 1) {

                binding.ProductAdd.setVisibility(View.GONE);
                binding.PlusMinusLayout.setVisibility(View.VISIBLE);

                if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {

                    int add = Integer.parseInt(binding.qtyCount.getText().toString().trim()) - 1;
                    binding.qtyCount.setText(String.valueOf(add));
                    try {
                        MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    try {

                        int AR_Num = Integer.parseInt(binding.qtyCount.getText().toString().trim());
                        String RomNum = new NumberConvertArabic().arabicNumberCovert(AR_Num);
                        int add = Integer.parseInt(RomNum) - 1;
                        binding.qtyCount.setText(String.valueOf(new NumberConvertArabic().NumberConvertArabic(add)));

                        try {
                            MainjsonObject.getJSONObject(BRAND_ID).getJSONObject(list.getId()).put("qty", String.valueOf(add));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        Log.e(TAG, "onActivityCreated: " + e.getMessage() );
                    }

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

            try {
                if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
                    binding.qtyCount.setText(new NumberConvertArabic().NumberConvertArabic(1));
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

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        TextView brand_name = getActivity().findViewById(R.id.brand_name);
        brand_name.setVisibility(View.GONE);
        title.setText(getActivity().getString(R.string.Price_Details));
    }


    private void getGallary(String productId) {

        lavaInterface.GetGallery(productId).enqueue(new Callback<ProductGalleryList>() {
            @Override
            public void onResponse(Call<ProductGalleryList> call, Response<ProductGalleryList> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                try {
                    if(response.isSuccessful()){
                        if (!response.body().getError()){
                            if(response.body().getList().size() > 0){

                                for (int i=0 ; i < response.body().getList().size(); i++){
                                    com.apptech.lava_retailer.modal.productgallery.List l = response.body().getList().get(i);
                                    galleryLists.add(new ProductGalleryLists(
                                            l.getId()
                                            ,l.getProId()
                                            ,l.getProName()
                                            ,l.getImgUrl()
                                            ,l.getImgUrlAr()
                                            ,l.getTime()
                                            ,""
                                            ,""
                                            ,"IMAGE"
                                    ));
                                }

//                                galleryLists.add(new ProductGalleryLists(
//                                        ""
//                                        ,""
//                                        ,""
//                                        ,""
//                                        ,""
//                                        ,""
//                                        ,list.getVideo()
//                                        ,list.getVideo_ar()
//                                        ,"VIDEO"
//                                ));


                                binding.viewpager.setAdapter(new ProductGalleryAdapter(galleryLists , getContext()));

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

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ProductGalleryList> call, Throwable t) {
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TextView brand_name = getActivity().findViewById(R.id.brand_name);
        brand_name.setVisibility(View.VISIBLE);
        binding = null;
    }

    private void qtyset1() {
        String json = sessionManage.getUserDetails().get("CARD_DATA");
        if (json != null) {
            try {
                MainjsonObject = new JSONObject(json);
                ProductJsonObject = MainjsonObject.getJSONObject(BRAND_ID);

                try {

                    if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {
                        binding.qtyCount.setText(ProductJsonObject.getJSONObject(list.getId()).get("qty").toString());
                    }else {
                        int qty = Integer.parseInt(ProductJsonObject.getJSONObject(list.getId()).get("qty").toString());
                        binding.qtyCount.setText(String.valueOf(new NumberConvertArabic().NumberConvertArabic(qty)));
                    }

//                    binding.qtyCount.setText(ProductJsonObject.getJSONObject(list.getId()).get("qty").toString());
                    binding.PlusMinusLayout.setVisibility(View.VISIBLE);
                    binding.ProductAdd.setVisibility(View.GONE);

                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Log.e(TAG, "qtyset: " + e.getMessage() );
                }
//                binding.qtyCount.setText(ProductJsonObject.getJSONObject(list.getId()).get("qty").toString());
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

//                        try {
//
//                            if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
//                                binding.qtyCount.setText(jo.getJSONObject(_pubKey).getString("qty"));
//                                return;
//                            }
//                            int qty = Integer.parseInt(jo.getJSONObject(_pubKey).getString("qty"));
//                            binding.qtyCount.setText(qty);
//
//                        }catch (NumberFormatException e){
//                            e.printStackTrace();
//                            Log.e(TAG, "qtyset: " + e.getMessage() );
//                        }

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




}


































