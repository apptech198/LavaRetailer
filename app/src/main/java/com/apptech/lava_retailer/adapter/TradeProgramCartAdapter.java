package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowTradeProgramCartBinding;
import com.apptech.lava_retailer.other.SessionManage;

import java.util.List;

public class TradeProgramCartAdapter extends RecyclerView.Adapter<TradeProgramCartAdapter.Viewholder> {

    String pos;
    TradeProgramCartInterface tradeProgramCartInterface;
    List<com.apptech.lava_retailer.list.tradecatlist.List>lists;
    SessionManage sessionManage;
    Context context;

    public TradeProgramCartAdapter(String pos , TradeProgramCartInterface tradeProgramCartInterface,List<com.apptech.lava_retailer.list.tradecatlist.List>lists) {
        this.pos = pos;
        this.tradeProgramCartInterface = tradeProgramCartInterface;
        this.lists=lists;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowTradeProgramCartBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            holder.binding.orderNumber.setText(lists.get(position).getName());
        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
            if(lists.get(position).getNameFr().isEmpty()){
                holder.binding.orderNumber.setText(lists.get(position).getName());
            }else {
                holder.binding.orderNumber.setText(lists.get(position).getNameFr());
            }
        } else {
            if(lists.get(position).getNameAr().isEmpty()){
                holder.binding.orderNumber.setText(lists.get(position).getName());
            }else {
                holder.binding.orderNumber.setText(lists.get(position).getNameAr());
            }
        }

        holder.binding.orderDate.setText(lists.get(position).getDate());
        holder.binding.orderTime.setText(lists.get(position).getDate());

        holder.binding.mainLayout.setOnClickListener(v -> {
//            tradeProgramCartInterface.OnItemClick();
            Bundle bundle = new Bundle();
            bundle.putString("name", lists.get(position).getName());
            bundle.putString("namear", lists.get(position).getNameAr());
            bundle.putString("namefr", lists.get(position).getNameFr());
            bundle.putString("image", lists.get(position).getImgEn());
            bundle.putString("file_type", lists.get(position).getFileType());
            NavController navController= Navigation.findNavController(v);
            navController.navigate(R.id.tradeProgramImgOpenFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
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










































