package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowBrandsTopBinding;
import com.apptech.lava_retailer.list.brand.Brandlist;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrandsTopAdapter extends RecyclerView.Adapter<BrandsTopAdapter.Viewholder> {


    List<Brandlist> brandLists;
    BrandTopInterfaces brandInterfaces;
    SessionManage sessionManage;
    Context context;


    public BrandsTopAdapter(List<Brandlist> list, BrandTopInterfaces brandInterfaces) {
        this.brandLists = list;
        this.brandInterfaces = brandInterfaces;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowBrandsTopBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {

        Brandlist list = brandLists.get(position);

        holder.binding.setLang(sessionManage.getUserDetails().get("LANGUAGE"));
        holder.binding.setList(list);
        holder.binding.market1.setOnClickListener(v -> {
            brandInterfaces.brandItemClick(list , list.getName() , list.getName_ar());
        });

        Glide.with(context).load(ApiClient.Image_URL + list.getImg()).into(holder.binding.img);

        holder.binding.executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return (brandLists.size() < 0) ? 3 : brandLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowBrandsTopBinding binding;
        public Viewholder(@NonNull @NotNull RowBrandsTopBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface BrandTopInterfaces {
        void brandItemClick(Brandlist list ,  String text , String text_ar );
    }


}

































