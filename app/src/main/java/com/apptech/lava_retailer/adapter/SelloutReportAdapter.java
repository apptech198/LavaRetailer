package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowSellOutReportBinding;
import com.apptech.lava_retailer.list.sell_out_report.SellOutReportList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomCategoryList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomModalList;
import com.apptech.lava_retailer.other.SessionManage;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class SelloutReportAdapter extends RecyclerView.Adapter<SelloutReportAdapter.Viewholder> {


    Context context;
    private static final String TAG = "SelloutReportAdapter";
    List<SellOutCustomCategoryList> sellOutCustomCategoryLists;
    SessionManage sessionManage;



    public SelloutReportAdapter(List<SellOutCustomCategoryList> sellOutCustomCategoryLists) {
        this.sellOutCustomCategoryLists = sellOutCustomCategoryLists;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowSellOutReportBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        SellOutCustomCategoryList l = sellOutCustomCategoryLists.get(position);
        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                holder.binding.commodityName.setText(l.getCommodity());
                break;
            case "ar":
                holder.binding.commodityName.setText(l.getCommodity_ar());
                break;
        }

        holder.binding.qty.setText(l.getQty());
        holder.binding.value.setText(l.getValue());

        holder.binding.InnerRecyclerView.setAdapter(new SellOutReportInnerAdapter(l.getSellOutCustomModalLists()));

    }


    @Override
    public int getItemCount() {
        return sellOutCustomCategoryLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutReportBinding binding;
        SellOutReportInnerAdapter sellOutReportInnerAdapter;

        public Viewholder(@NonNull RowSellOutReportBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }


    }

    public void UpdateList(List<SellOutCustomCategoryList> sellOutCustomCategoryLists){
        this.sellOutCustomCategoryLists = sellOutCustomCategoryLists;
        notifyDataSetChanged();
    }


}












