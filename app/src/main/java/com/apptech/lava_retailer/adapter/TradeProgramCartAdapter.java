package com.apptech.lava_retailer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowTradeProgramCartBinding;

public class TradeProgramCartAdapter extends RecyclerView.Adapter<TradeProgramCartAdapter.Viewholder> {

    int pos;
    TradeProgramCartInterface tradeProgramCartInterface;

    public TradeProgramCartAdapter(int pos , TradeProgramCartInterface tradeProgramCartInterface) {
        this.pos = pos;
        this.tradeProgramCartInterface = tradeProgramCartInterface;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RowTradeProgramCartBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binding.mainLayout.setOnClickListener(v -> {
            tradeProgramCartInterface.OnItemClick();
        });
    }

    @Override
    public int getItemCount() {
        return pos;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowTradeProgramCartBinding binding;

        public Viewholder(@NonNull RowTradeProgramCartBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


    public interface TradeProgramCartInterface{
        void OnItemClick();
    }


}










































