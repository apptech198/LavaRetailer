package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowSellOutPendingVerificationBinding;
import com.apptech.lava_retailer.list.pending_warranty.List;
import com.apptech.lava_retailer.other.SessionManage;


public class WarrantyPendingReplacementAdapter extends RecyclerView.Adapter<WarrantyPendingReplacementAdapter.Viewholder> {


    Context context;
    SessionManage sessionManage;
    java.util.List<List> lists;

    public WarrantyPendingReplacementAdapter(java.util.List<List> list) {
        this.lists=list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowSellOutPendingVerificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

            List l = lists.get(position);
            String status = l.getStatus();
            switch (status) {
                case "PENDING":
                    holder.binding.validCheck.setTextColor(context.getResources().getColor(R.color.yellow));
                    break;
                case "REJECTED":
                default:
            }

            holder.binding.datetime.setText(l.getTime().toString().trim().split(" ")[0]);
           holder.binding.validCheck.setText(l.getStatus());
           holder.binding.ValidImeiText.setText(l.getImei());


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutPendingVerificationBinding binding;

        public Viewholder(@NonNull RowSellOutPendingVerificationBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}














