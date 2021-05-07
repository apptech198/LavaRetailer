package com.apptech.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowBrandsBinding;
import com.apptech.myapplication.other.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.Viewholder> {


    List<com.apptech.myapplication.modal.brand.List> brandLists;
    BrandInterfaces brandInterfaces;
    SessionManage sessionManage;
    Context context;


    public BrandsAdapter(List<com.apptech.myapplication.modal.brand.List> list, BrandInterfaces brandInterfaces) {
        this.brandLists = list;
        this.brandInterfaces = brandInterfaces;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowBrandsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        com.apptech.myapplication.modal.brand.List list = brandLists.get(position);
        holder.binding.setLang(sessionManage.getUserDetails().get("LANGUAGE"));
        holder.binding.setList(list);
        holder.binding.market1.setOnClickListener(v -> {
            brandInterfaces.brandItemClick(list , list.getName() , list.getNameAr());
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return brandLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowBrandsBinding binding;
        public Viewholder(@NonNull @NotNull RowBrandsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface BrandInterfaces {
        void brandItemClick(com.apptech.myapplication.modal.brand.List list ,  String text , String text_ar );
    }


}

































