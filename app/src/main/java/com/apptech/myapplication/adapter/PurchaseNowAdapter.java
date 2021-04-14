package com.apptech.myapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.RowPurachaseRequestNowBinding;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.other.SessionManage;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class PurchaseNowAdapter extends RecyclerView.Adapter<PurchaseNowAdapter.ViewBinding> {

    RowPurachaseRequestNowBinding binding;
    List<ProductList> productLists;
    Context context;
    PurchaseNowIterface purchaseNowIterface;
    SessionManage sessionManage;
    private static final String TAG = "PurchaseNowAdapter";

    public PurchaseNowAdapter(List<ProductList> productLists, PurchaseNowIterface purchaseNowIterface) {
        this.productLists = productLists;
        this.purchaseNowIterface = purchaseNowIterface;

    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
//        sessionManage = new SessionManage(context);
        sessionManage = SessionManage.getInstance(context);
        return new ViewBinding(LayoutInflater.from(context).inflate(R.layout.row_purachase_request_now, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {
        ProductList list = productLists.get(position);

        Glide.with(context).load(list.getImg()).fitCenter().into(holder.image);
        holder.productName.setText(list.getName());
        holder.productAmt.setText("1200");
        holder.productAmtDic.setText("2000");
        holder.productAmtDic.setPaintFlags(holder.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        CardProductCheck(holder.addBtnLayout1, holder.plus_minusLayout, list, holder.countTextView);


    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }


    public class ViewBinding extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        ConstraintLayout mainLayout;
        TextView productName, productAmt, productAmtDic, countTextView;
        LinearLayout plus, minus, addBtnLayout1, plus_minusLayout;

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

            addBtnLayout1.setOnClickListener(this);
            plus_minusLayout.setOnClickListener(this);
            plus.setOnClickListener(this);
            minus.setOnClickListener(this);
            mainLayout.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            ProductList list = productLists.get(getAdapterPosition());


            switch (v.getId()) {
                case R.id.addBtnLayout:
                    purchaseNowIterface.addItem(list, getAdapterPosition(), countTextView);
                    plus_minusLayout.setVisibility(View.VISIBLE);
                    addBtnLayout1.setVisibility(View.GONE);
                    break;
                case R.id.mainLayout:
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


        if (json != null ) {
            JSONObject issueObj = null;
            try {
                JSONObject jsonObject = new JSONObject(json);
                issueObj = new JSONObject(json);
                Iterator iterator = issueObj.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = issueObj.getJSONObject(key);
                    //  get id from  issue
                    String _pubKey = issue.optString("id");
                    if (list.getId().equals(_pubKey)) {
//                        Log.e(TAG, "CardProductCheck: " + jsonObject.getJSONObject(_pubKey).getString("qty"));
                        count.setText(jsonObject.getJSONObject(_pubKey).getString("qty"));
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




































