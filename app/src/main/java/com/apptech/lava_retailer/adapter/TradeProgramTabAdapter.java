package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowTradeProgramTabBinding;
import com.apptech.lava_retailer.list.tradecatlist.List;
import com.apptech.lava_retailer.list.tradecatlist.TradingMenuList;
import com.apptech.lava_retailer.other.LanguageChange;
import com.apptech.lava_retailer.other.SessionManage;

public class TradeProgramTabAdapter extends RecyclerView.Adapter<TradeProgramTabAdapter.Viewholder> {

    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    Context context;
    private boolean FIRST_TIME_OPEN = true;
    TradeProgramInterface tradeProgramInterface;
    java.util.List<TradingMenuList> lists;
    SessionManage sessionManage;


    public TradeProgramTabAdapter(TradeProgramInterface tradeProgramInterface , java.util.List<TradingMenuList> lists) {
        this.tradeProgramInterface = tradeProgramInterface;
        this.lists=lists;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowTradeProgramTabBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            holder.binding.name.setText(lists.get(position).getName());
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            if(lists.get(position).getName_fr().isEmpty()){
                holder.binding.name.setText(lists.get(position).getName());
            }else {
                holder.binding.name.setText(lists.get(position).getName_fr());
            }
        } else {
            if(lists.get(position).getName_ar().isEmpty()){
                holder.binding.name.setText(lists.get(position).getName());
            }else {
                holder.binding.name.setText(lists.get(position).getName_ar());
            }
        }



        holder.binding.Layout.setOnClickListener(v -> {
            holder.binding.Layout.setEnabled(false);
            currentSelectedPosition = position;
            tradeProgramInterface.OnItemClick(lists.get(position).getId());
            notifyDataSetChanged();
            holder.binding.Layout.setEnabled(true);
        });

        if(currentSelectedPosition == position){
            holder.binding.Layout.setBackground(ResourcesCompat.getDrawable(context.getResources() , R.drawable.red_order_status , null));
        }else {
            holder.binding.Layout.setBackground(ResourcesCompat.getDrawable(context.getResources() , R.drawable.blac_order_status , null));
        }

        if(FIRST_TIME_OPEN){
            tradeProgramInterface.OnItemClick(lists.get(position).getId());
            holder.binding.Layout.setBackground(ResourcesCompat.getDrawable(context.getResources() , R.drawable.red_order_status , null));
        }
        FIRST_TIME_OPEN = false;

    }

    @Override
    public int getItemCount() {

        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowTradeProgramTabBinding binding;

        public Viewholder(@NonNull RowTradeProgramTabBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface TradeProgramInterface{
        void OnItemClick(String pos);
    }

}
