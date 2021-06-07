package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowSellOutCategoryFilterBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.List;

public class SellOutReportCategoryFilterAdapter extends RecyclerView.Adapter<SellOutReportCategoryFilterAdapter.Viewholder> {


    Context context;
    SessionManage sessionManage;
    OnItemClickCategoryInterface onItemClickInterface;
    List<ComodityLists> categoryLists;
    boolean aBoolean;

    public SellOutReportCategoryFilterAdapter(OnItemClickCategoryInterface onItemClickInterface, List<ComodityLists> categoryLists, boolean allBTN_CLICK) {
        this.onItemClickInterface = onItemClickInterface;
        this.categoryLists = categoryLists;
        this.aBoolean = allBTN_CLICK;
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

        ComodityLists l = categoryLists.get(position);

        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                holder.binding.CategoryName.setText(l.getName());
                break;
            case "fr":
                if(l.getName_fr().isEmpty()){
                    holder.binding.CategoryName.setText(l.getName());
                }else {
                    holder.binding.CategoryName.setText(l.getName_fr());
                }
                break;
            case "ar":
                holder.binding.CategoryName.setText(l.getName_ar());
                break;
        }

        if (l.isCheckable()){
            holder.binding.CheckBtn.setChecked(true);
        }



        holder.binding.Mainlayouot.setOnClickListener(v -> {
            if (holder.binding.CheckBtn.isChecked()){
                holder.binding.CheckBtn.setChecked(false);
                onItemClickInterface.RemoveItem(l , position);
                return;
            }
            onItemClickInterface.AddItem(l , position);
            holder.binding.CheckBtn.setChecked(true);
        });





    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutCategoryFilterBinding binding;

        public Viewholder(@NonNull RowSellOutCategoryFilterBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }



    public interface OnItemClickCategoryInterface{
        void AddItem(ComodityLists lists , int pos);
        void RemoveItem(ComodityLists lists ,int pos);
    }





}
























