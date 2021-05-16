package com.apptech.myapplication.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PriceDropReportadapter extends RecyclerView.Adapter<PriceDropReportadapter.ViewBinding> {
    @NonNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBinding holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull View itemView) {
            super(itemView);
        }
    }
}
