package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowSellOutPendingVerificationBinding;
import com.apptech.lava_retailer.other.SessionManage;


public class WarrantyPendingReplacementAdapter extends RecyclerView.Adapter<WarrantyPendingReplacementAdapter.Viewholder> {


    Context context;
    SessionManage sessionManage;

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RowSellOutPendingVerificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull RowSellOutPendingVerificationBinding itemView) {
            super(itemView.getRoot());


        }
    }
}














