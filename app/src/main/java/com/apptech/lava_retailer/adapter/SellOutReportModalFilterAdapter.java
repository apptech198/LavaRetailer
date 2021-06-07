package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowSellOutCategoryFilterBinding;
import com.apptech.lava_retailer.list.modelList.ModelList;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.List;

public class SellOutReportModalFilterAdapter extends RecyclerView.Adapter<SellOutReportModalFilterAdapter.Viewholder> {

    OnItemClickInterface onItemClickInterface;
    SessionManage sessionManage;
    Context context;
    List<ModelList> modalList;

    public SellOutReportModalFilterAdapter(OnItemClickInterface onItemClickInterface, List<ModelList> modalList) {
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

        ModelList l = modalList.get(position);

        holder.binding.CategoryName.setText(l.getModel());


        if(l.isCheckable()){
            holder.binding.CheckBtn.setChecked(true);
        }

        holder.binding.Mainlayouot.setOnClickListener(v -> {
            if (holder.binding.CheckBtn.isChecked()){
                holder.binding.CheckBtn.setChecked(false);
                onItemClickInterface.RemoveItem(l.getModel() , position);
                return;
            }
            onItemClickInterface.AddItem(l.getModel() , position);
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
        void AddItem(String l , int pos);
        void RemoveItem(String l , int pos);
    }


}
