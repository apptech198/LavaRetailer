package com.apptech.lava_retailer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowPriceDropReportBinding;


public class PriceDropReportadapter extends RecyclerView.Adapter<PriceDropReportadapter.ViewBinding> {
    @NonNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewBinding(RowPriceDropReportBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBinding holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewBinding extends RecyclerView.ViewHolder {

        RowPriceDropReportBinding binding;

        public ViewBinding(@NonNull RowPriceDropReportBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
