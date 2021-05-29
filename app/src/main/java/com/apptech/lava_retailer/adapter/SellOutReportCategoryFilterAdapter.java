package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.other.SessionManage;

public class SellOutReportCategoryFilterAdapter extends RecyclerView.Adapter<SellOutReportCategoryFilterAdapter.Viewholder> {


    Context context;
    SessionManage sessionManage;
    OnItemClickCategoryInterface onItemClickInterface;

    public SellOutReportCategoryFilterAdapter(OnItemClickCategoryInterface onItemClickInterface) {
        this.onItemClickInterface = onItemClickInterface;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }



    public interface OnItemClickCategoryInterface{
        void OnItemClick();
    }





}
























