package com.apptech.lava_retailer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowPassbookBinding;
import com.apptech.lava_retailer.databinding.RowPassbookLayoutBinding;
import com.apptech.lava_retailer.list.OrderStatusList;
import com.apptech.lava_retailer.list.passbook.PassbookList;

import java.util.List;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.Viewholder> {

    List<com.apptech.lava_retailer.list.passbook.List> list;

    public PassbookAdapter(List<com.apptech.lava_retailer.list.passbook.List> orderStatusLists) {
        this.list = orderStatusLists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RowPassbookLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
          holder.binding.ctype.setText("Claim Type : "+list.get(position).getClaimType());
          holder.binding.ccode.setText("Claim Code : "+list.get(position).getClaimCode());
          holder.binding.dateofapproval.setText("Date of Approval : "+list.get(position).getTime().substring(0,10));
          holder.binding.value.setText("Value : "+list.get(position).getValue());
          holder.binding.paymentdetail.setText("Payment Detail : "+list.get(position).getPaymentReference());
          holder.binding.paymentdate.setText("Payment Date : "+list.get(position).getPaymentDate().substring(0,10));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowPassbookLayoutBinding binding;

        public Viewholder(@NonNull RowPassbookLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}

