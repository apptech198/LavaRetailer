package com.apptech.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.RowOrderStatusBinding;
import com.apptech.myapplication.list.OrderStatusList;
import com.apptech.myapplication.modal.order_statusList.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.Viewholder> {

    java.util.List<OrderStatusList> lists;
    Context context;



    public OrderStatusAdapter(java.util.List<OrderStatusList> orderStatusLists) {
        this.lists = orderStatusLists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Viewholder(RowOrderStatusBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        OrderStatusList list = lists.get(position);

        holder.binding.productName.setText(list.getProduct_name());
        holder.binding.productQty.setText(list.getQty());
        holder.binding.modalName.setText(list.getModel_name());
        holder.binding.status.setText(list.getStatus());
        holder.binding.orderDate.setText(list.getTime().substring(0 , 10));
        holder.binding.orderNumber.setText("order No : " + list.getOrder_no());
        try {
            int disAmt = Integer.parseInt(list.getDiscount_price()) * Integer.parseInt(list.getQty());
            holder.binding.productamt.setText(context.getResources().getString(R.string.egp) + disAmt);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowOrderStatusBinding binding;

        public Viewholder(@NonNull RowOrderStatusBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
