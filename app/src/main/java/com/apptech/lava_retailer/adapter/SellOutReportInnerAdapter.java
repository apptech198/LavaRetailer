package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowSellOutReportBinding;
import com.apptech.lava_retailer.databinding.RowSellOutReportInnerBinding;
import com.apptech.lava_retailer.list.sell_out_report.SellOutReportList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomModalList;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.List;

public class SellOutReportInnerAdapter extends RecyclerView.Adapter<SellOutReportInnerAdapter.Viewholder> {

    List<SellOutCustomModalList> sellOutReportModalLists;
    Context context;
    SessionManage sessionManage;
    private static final String TAG = "SellOutReportInnerAdapt";

    public SellOutReportInnerAdapter(List<SellOutCustomModalList> sellOutCustomModalLists) {
        this.sellOutReportModalLists = sellOutCustomModalLists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowSellOutReportInnerBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        SellOutCustomModalList l = sellOutReportModalLists.get(position) ;
            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                case "en":
                    holder.binding.modalname.setText(l.getModel());
                    break;
                case "ar":
                    holder.binding.modalname.setText(l.getModelAr());
                    break;
            }

            holder.binding.qty.setText(l.getQty());
            holder.binding.value.setText(l.getValue());


    }

    @Override
    public int getItemCount() {
        return sellOutReportModalLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutReportInnerBinding binding;

        public Viewholder(@NonNull RowSellOutReportInnerBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}
