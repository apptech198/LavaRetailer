package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.modal.product.ProductList;
import com.apptech.lava_retailer.other.NumberConvertArabic;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.ui.order.place_order.PlaceOrderFragmentDirections;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class PurchaseNowAdapter extends RecyclerView.Adapter<PurchaseNowAdapter.ViewBinding> implements Filterable {

    List<ProductList> productLists;
    List<ProductList> AllProductList = new ArrayList<>();
    Context context;
    PurchaseNowIterface purchaseNowIterface;
    SessionManage sessionManage;
    private static final String TAG = "PurchaseNowAdapter";
    String currency;

    public PurchaseNowAdapter(List<ProductList> productLists, PurchaseNowIterface purchaseNowIterface) {
        this.productLists = productLists;
        this.purchaseNowIterface = purchaseNowIterface;
        AllProductList.addAll(productLists);
    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        currency = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_CURRENCY_SYMBOL);
        return new ViewBinding(LayoutInflater.from(context).inflate(R.layout.row_purachase_request_now, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {
        ProductList list = productLists.get(position);;

        Glide.with(context).load(ApiClient.Image_URL + list.getThumb()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.GipLoader.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.image);





//        String [] countries = { "US", "CA", "MX", "GB", "DE", "PL", "RU", "JP", "CN" , "IN" };

//        try {
//            new NumberConvertArabic().GetCurreny(sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_CURRENCY));
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }

        Log.e(TAG, "onBindViewHolder: " + currency);


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

            holder.productName.setText(list.getMarketing_name());
            holder.brandName.setText("Brand : " + list.getBrand());
            holder.modalName.setText("Modal : " +  list.getModel());
            holder.productAmt.setText(currency + list.getDis_price());
            holder.productAmtDic.setText(currency + list.getActual_price());
            holder.addBtnLayout1.setEnabled(true);
            holder.addBtnLayout1.setClickable(true);

            try {
                int Actual_price = Integer.parseInt(list.getActual_price());
                int Dis_price = Integer.parseInt(list.getDis_price());
                if(Dis_price >= Actual_price){
                    holder.productAmtDic.setVisibility(View.GONE);
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }




            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){

//                holder.addBtnLayout1.setEnabled(false);
//                holder.addBtnLayout1.setClickable(false);

                SpannableString string = new SpannableString(list.getActual_price());
                MaskFilter blurMask = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
                string.setSpan(new MaskFilterSpan(blurMask), 0, list.getActual_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.productAmtDic.setText(string);

                SpannableString string1 = new SpannableString(list.getDis_price());
                MaskFilter blurMask1 = new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL);
                string1.setSpan(new MaskFilterSpan(blurMask1), 0, list.getDis_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.productAmt.setText(string);

            }




        } else if(sessionManage.getUserDetails().get("LANGUAGE").equals("ar")) {

            holder.productName.setText(list.getMarketing_name_ar());
            holder.brandName.setText("Brand : " + list.getBrand_ar());
            holder.modalName.setText("Modal : " +  list.getModel_ar());

            String Dis_price = new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getDis_price()));
            String Actual_price = new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getActual_price()));


            holder.productAmt.setText(currency + Dis_price);
            holder.productAmtDic.setText(currency +  Actual_price);
            holder.addBtnLayout1.setEnabled(true);
            holder.addBtnLayout1.setClickable(true);


            try {
                int Actual_price1 = Integer.parseInt(list.getActual_price());
                int Dis_price1 = Integer.parseInt(list.getDis_price());
                if(Dis_price1 >= Actual_price1){
                    holder.productAmtDic.setVisibility(View.GONE);
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }

            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){


//                holder.addBtnLayout1.setEnabled(false);
//                holder.addBtnLayout1.setClickable(false);

                SpannableString string = new SpannableString(Actual_price);
                MaskFilter blurMask = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
                string.setSpan(new MaskFilterSpan(blurMask), 0, Actual_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.productAmtDic.setText(string);

                SpannableString string1 = new SpannableString(Dis_price);
                MaskFilter blurMask1 = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
                string1.setSpan(new MaskFilterSpan(blurMask1), 0, Dis_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.productAmt.setText(string);

            }



        }else {

            holder.addBtnLayout1.setEnabled(true);
            holder.addBtnLayout1.setClickable(true);

            if(list.getMarketing_name_fr().isEmpty()){
                holder.productName.setText(list.getMarketing_name());
            }else {
                holder.productName.setText(list.getMarketing_name_fr());
            }


            holder.brandName.setText("Brand : " + list.getBrand());
            holder.modalName.setText("Modal : " +  list.getModel());

//            String Dis_price = new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getDis_price()));
//            String Actual_price = new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getActual_price()));

            holder.productAmt.setText(currency + list.getDis_price());
            holder.productAmtDic.setText(currency + list.getActual_price());


            try {
                int Actual_price = Integer.parseInt(list.getActual_price());
                int Dis_price = Integer.parseInt(list.getDis_price());
                if(Dis_price >= Actual_price){
                    holder.productAmtDic.setVisibility(View.GONE);
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
                Log.e(TAG, "onActivityCreated: " + e.getMessage() );
            }


            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){


//                holder.addBtnLayout1.setEnabled(false);
//                holder.addBtnLayout1.setClickable(false);

                SpannableString string = new SpannableString(list.getActual_price());
                MaskFilter blurMask = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
                string.setSpan(new MaskFilterSpan(blurMask), 0, list.getActual_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.productAmtDic.setText(string);

                SpannableString string1 = new SpannableString(list.getDis_price());
                MaskFilter blurMask1 = new BlurMaskFilter(15f, BlurMaskFilter.Blur.NORMAL);
                string1.setSpan(new MaskFilterSpan(blurMask1), 0, list.getDis_price().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.productAmt.setText(string);

            }




        }
        holder.productAmtDic.setPaintFlags(holder.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        CardProductCheck(holder.addBtnLayout1, holder.plus_minusLayout, list, holder.countTextView);




    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProductList> filterlist = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filterlist.addAll(AllProductList);
            }else {

                String filterPattern = constraint.toString().toLowerCase().trim();
                if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {

                    for (ProductList list : AllProductList){

                        Log.e(TAG, "performFiltering: " + filterPattern );
                        Log.e(TAG, "performFiltering: " +  list.getMarketing_name().toLowerCase().trim());


                        if(list.getMarketing_name().toLowerCase().trim().contains(filterPattern)
                                || list.getModel().toLowerCase().trim().contains(filterPattern)){
                            filterlist.add(list);
                        }else {

                            String[] breackProd = list.getMarketing_name().split(" ");
                            String[] filterPattern1 = constraint.toString().toLowerCase().trim().split(" ");
                            String filterPattern2 = filterPattern1[filterPattern1.length - 1];
                            Log.e(TAG, "performFiltering   aaaaaaaaaa: " + filterPattern2 );

                            for (String s : breackProd) {
                                Log.e(TAG, "performFiltering: " + s);
                                Log.e(TAG, "performFiltering: " + filterPattern );
                                if (s.toLowerCase().trim().contains(filterPattern2)) {
                                    filterlist.add(list);
                                }
                            }

                        }

                    }



                } else {

                    for (ProductList list : AllProductList){
                        if(list.getMarketing_name_ar().toLowerCase().trim().contains(filterPattern)
                                || list.getModel_ar().toLowerCase().trim().contains(filterPattern)
                        ){
                            filterlist.add(list);
                        }
                    }

                }

            }

            FilterResults results = new FilterResults();
            results.values = filterlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productLists.clear();
            productLists.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewBinding extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        ConstraintLayout mainLayout;
        TextView productName, productAmt, productAmtDic, countTextView , brandName , modalName;
        LinearLayout plus, minus, addBtnLayout1, plus_minusLayout , GipLoader;

        public ViewBinding(@NonNull @NotNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            productName = itemView.findViewById(R.id.productName);
            productAmt = itemView.findViewById(R.id.productAmt);
            productAmtDic = itemView.findViewById(R.id.productAmtDic);
            addBtnLayout1 = itemView.findViewById(R.id.addBtnLayout);
            plus_minusLayout = itemView.findViewById(R.id.plus_minusLayout);
            plus = itemView.findViewById(R.id.plus);
            countTextView = itemView.findViewById(R.id.countTextView);
            minus = itemView.findViewById(R.id.minus);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            brandName = itemView.findViewById(R.id.brandName);
            modalName = itemView.findViewById(R.id.modalName);
            GipLoader = itemView.findViewById(R.id.GipLoader);

            addBtnLayout1.setOnClickListener(this);
            plus_minusLayout.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            mainLayout.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            ProductList list = productLists.get(getAdapterPosition());
            NavController navController = Navigation.findNavController(v);

            switch (v.getId()) {
                case R.id.addBtnLayout:
                    if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")){
                        Toast.makeText(context, "Your Account is not Verify", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    purchaseNowIterface.addItem(list, getAdapterPosition(), countTextView);
                    plus_minusLayout.setVisibility(View.VISIBLE);
                    addBtnLayout1.setVisibility(View.GONE);
                    break;
                case R.id.mainLayout:
                    NavDirections action = PlaceOrderFragmentDirections.actionPlaceOrderFragmentToProductDetailsFragment(list);
                    navController.navigate(action);
                    purchaseNowIterface.itemClick(list);
                    break;
                case R.id.plus:
                    purchaseNowIterface.QtyAdd(list, getAdapterPosition(), countTextView);
                    break;
                case R.id.minus:
                    Log.e(TAG, "onClick: " + countTextView.getText().toString().trim());
                    int a = Integer.parseInt(countTextView.getText().toString().trim());
                    if (a > 1) {
                        purchaseNowIterface.minus(list, getAdapterPosition(), countTextView);
                    } else {
                        purchaseNowIterface.RemoveItem(list, getAdapterPosition(), countTextView);
                        plus_minusLayout.setVisibility(View.GONE);
                        addBtnLayout1.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }


    }


    private void CardProductCheck(LinearLayout addBtnLayout, LinearLayout plus_minusLayout, ProductList list, TextView count) {

        String json = sessionManage.getUserDetails().get("CARD_DATA");
        String BRAND_ID = sessionManage.getUserDetails().get("BRAND_ID");


        if (json != null ) {
            JSONObject issueObj = null;
            try {

                JSONObject object = new JSONObject(json);
//                issueObj = new JSONObject(json);
                issueObj = object.getJSONObject(BRAND_ID);

                Iterator iterator = issueObj.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = issueObj.getJSONObject(key);
                    //  get id from  issue
                    String _pubKey = issue.optString("id");
                    if (list.getId().equals(_pubKey)) {

                        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en") || sessionManage.getUserDetails().get("LANGUAGE").equals("fr")) {
                            count.setText(issueObj.getJSONObject(_pubKey).getString("qty"));
                        }else {
                            try {
                                count.setText(new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(issueObj.getJSONObject(_pubKey).getString("qty"))));
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                                Log.e(TAG, "CardProductCheck: " + e.getMessage() );
                            }
                        }

//                        count.setText(issueObj.getJSONObject(_pubKey).getString("qty"));

                        plus_minusLayout.setVisibility(View.VISIBLE);
                        addBtnLayout.setVisibility(View.GONE);
                        break;
                    } else {
                        plus_minusLayout.setVisibility(View.GONE);
                        addBtnLayout.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                plus_minusLayout.setVisibility(View.GONE);
                addBtnLayout.setVisibility(View.VISIBLE);
            }
        } else {
            plus_minusLayout.setVisibility(View.GONE);
            addBtnLayout.setVisibility(View.VISIBLE);
        }
    }

    public interface PurchaseNowIterface {
        void itemClick(ProductList list);

        void addItem(ProductList list, int position, TextView textView);

        void minus(ProductList list, int position, TextView county);

        void RemoveItem(ProductList list, int position, TextView county);

        void QtyAdd(ProductList list, int position, TextView countqty);
    }



}




































