package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowSellOutCategoryFilterBinding;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.List;

public class SellOutReportModalFilterAdapter extends RecyclerView.Adapter<SellOutReportModalFilterAdapter.Viewholder> {

    OnItemClickInterface onItemClickInterface;
    SessionManage sessionManage;
    Context context;
    List<String> modalList;

    public SellOutReportModalFilterAdapter(OnItemClickInterface onItemClickInterface, List<String> modalList) {
        this.onItemClickInterface = onItemClickInterface;
        this.modalList = modalList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowSellOutCategoryFilterBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        String l = modalList.get(position);

        holder.binding.CategoryName.setText(l);

        holder.binding.Mainlayouot.setOnClickListener(v -> {
            if (holder.binding.CheckBtn.isChecked()){
                holder.binding.CheckBtn.setChecked(false);
                onItemClickInterface.RemoveItem(l);
                return;
            }
            onItemClickInterface.AddItem(l);
            holder.binding.CheckBtn.setChecked(true);
        });


    }

    @Override
    public int getItemCount() {
        return modalList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutCategoryFilterBinding binding;

        public Viewholder(@NonNull RowSellOutCategoryFilterBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


    public interface OnItemClickInterface{
        void AddItem(String l);
        void RemoveItem(String l);
    }


}
