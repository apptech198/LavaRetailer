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

public class TradeProgramTabAdapter extends RecyclerView.Adapter<TradeProgramTabAdapter.Viewholder> {

    private int currentSelectedPosition = RecyclerView.NO_POSITION;
    Context context;
    private boolean FIRST_TIME_OPEN = true;
    TradeProgramInterface tradeProgramInterface;


    public TradeProgramTabAdapter(TradeProgramInterface tradeProgramInterface) {
        this.tradeProgramInterface = tradeProgramInterface;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Viewholder(RowTradeProgramTabBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {



        holder.binding.Layout.setOnClickListener(v -> {
            holder.binding.Layout.setEnabled(false);
            currentSelectedPosition = position;
            tradeProgramInterface.OnItemClick(position);
            notifyDataSetChanged();
            holder.binding.Layout.setEnabled(true);
        });

        if(currentSelectedPosition == position){
            holder.binding.Layout.setBackground(ResourcesCompat.getDrawable(context.getResources() , R.drawable.red_order_status , null));
        }else {
            holder.binding.Layout.setBackground(ResourcesCompat.getDrawable(context.getResources() , R.drawable.blac_order_status , null));
        }


        if(FIRST_TIME_OPEN){
            holder.binding.Layout.setBackground(ResourcesCompat.getDrawable(context.getResources() , R.drawable.red_order_status , null));
        }
        FIRST_TIME_OPEN = false;

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowTradeProgramTabBinding binding;

        public Viewholder(@NonNull RowTradeProgramTabBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface TradeProgramInterface{
        void OnItemClick(int pos);
    }

}
