package com.apptech.lava_retailer.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowPassbookBinding;
import com.apptech.lava_retailer.databinding.RowPassbookLayoutBinding;
import com.apptech.lava_retailer.list.OrderStatusList;
import com.apptech.lava_retailer.list.passbook.PassbookList;

import java.util.ArrayList;
import java.util.List;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.Viewholder> implements Filterable {

    List<com.apptech.lava_retailer.list.passbook.List> list;
    List<com.apptech.lava_retailer.list.passbook.List> fulldata= new ArrayList<>();
    getCount getCount;

    public PassbookAdapter(List<com.apptech.lava_retailer.list.passbook.List> orderStatusLists, getCount getCount) {
        this.list = orderStatusLists;
        this.fulldata.addAll(orderStatusLists);
        this.getCount= getCount;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RowPassbookLayoutBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }



    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<com.apptech.lava_retailer.list.passbook.List> filterdata = new ArrayList<>();
            if (constraint.toString().equals("Select ClaimType")) {
                filterdata.addAll(fulldata);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (com.apptech.lava_retailer.list.passbook.List list : fulldata) {
                    if (list.getClaimType().toLowerCase().trim().contains(filterPattern)) {
                        filterdata.add(list);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterdata;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            if(list.isEmpty()){
                getCount.getVisibility(true);
            }else {
                getCount.getVisibility(false);
            }

            notifyDataSetChanged();
        }
    };



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


    public interface getCount{
            public void getVisibility(Boolean aBoolean);
    }
}

