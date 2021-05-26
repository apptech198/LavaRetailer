package com.apptech.lava_retailer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowPassbookBinding;
import com.apptech.lava_retailer.list.OrderStatusList;

import java.util.List;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.Viewholder> {

    List<OrderStatusList> list;

    public PassbookAdapter(List<OrderStatusList> orderStatusLists) {
        this.list = orderStatusLists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RowPassbookBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        OrderStatusList l = list.get(position);
        holder.binding.o1.setText(l.getId());
        holder.binding.o2.setText(l.getDis_name());
        holder.binding.o3.setText(l.getProduct_name());
        holder.binding.o4.setText(l.getActual_price());
        holder.binding.o5.setText(l.getAddress());
        holder.binding.o6.setText(l.getDis_name());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowPassbookBinding binding;

        public Viewholder(@NonNull RowPassbookBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}

