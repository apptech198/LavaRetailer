package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowSellOutPendingVerificationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SellOutPendingVerificationAdapter extends RecyclerView.Adapter<SellOutPendingVerificationAdapter.Viewholder> {

    private final List<com.apptech.lava_retailer.modal.sellOutPendingVerification.List> sellOutPendingVerificationLists;
    RowSellOutPendingVerificationBinding binding;
    Context context;


    public SellOutPendingVerificationAdapter(List<com.apptech.lava_retailer.modal.sellOutPendingVerification.List> list) {
        this.sellOutPendingVerificationLists = list;
    }


    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = RowSellOutPendingVerificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        com.apptech.lava_retailer.modal.sellOutPendingVerification.List list = sellOutPendingVerificationLists.get(position);
        String status = list.getStatus().trim();

        switch (status) {
            case "PENDING":
                binding.validCheck.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
            case "REJECTED":
            default:
                binding.validCheck.setTextColor(context.getResources().getColor(R.color.red));
        }
        binding.datetime.setText(list.getDate().toString().trim().split(" ")[0]);
        binding.validCheck.setText(list.getStatus());
        binding.setList(list);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return sellOutPendingVerificationLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull @NotNull RowSellOutPendingVerificationBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
