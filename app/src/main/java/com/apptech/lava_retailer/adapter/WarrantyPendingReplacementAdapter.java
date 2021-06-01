package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowSellOutPendingVerificationBinding;
import com.apptech.lava_retailer.databinding.RowWarranyReplacementBinding;
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
        return new Viewholder(RowWarranyReplacementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

            List l = lists.get(position);
            String status = l.getStatus();
            switch (status) {
                case "PENDING":
                    holder.binding.status.setTextColor(context.getResources().getColor(R.color.yellow));
                    break;
                case "REJECTED":
                    holder.binding.status.setTextColor(context.getResources().getColor(R.color.red));
                default:
                    holder.binding.status.setTextColor(context.getResources().getColor(R.color.green));
            }

            holder.binding.orderDate.setText(l.getTime().toString().trim().split(" ")[0]);

//            if(l.getSrno().isEmpty()){
                holder.binding.SNOR.setText(" : " + l.getSrno());
//            }else {
//                holder.binding.SNOR.setText(context.getString(R.string.n_a));
//            }

//            if(l.getImei() !){
                holder.binding.imei.setText(" : " + l.getImei());
//            }else {
//                holder.binding.imei.setText(context.getString(R.string.n_a));
//            }

           holder.binding.modalName.setText(" : " + l.getHandestReplace());
            holder.binding.status.setText(l.getStatus());

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowWarranyReplacementBinding binding;

        public Viewholder(@NonNull RowWarranyReplacementBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }
    }
}














