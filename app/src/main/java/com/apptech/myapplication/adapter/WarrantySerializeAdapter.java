package com.apptech.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowWarrantySerializeBinding;

public class WarrantySerializeAdapter extends RecyclerView.Adapter<WarrantySerializeAdapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowWarrantySerializeBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RowWarrantySerializeBinding binding;

        public ViewHolder(@NonNull RowWarrantySerializeBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
