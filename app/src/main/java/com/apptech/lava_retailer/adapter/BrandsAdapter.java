package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowBrandsBinding;
import com.apptech.lava_retailer.list.brand.Brandlist;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.Viewholder> {


    List<Brandlist> brandLists;
    BrandInterfaces brandInterfaces;
    SessionManage sessionManage;
    Context context;


    public BrandsAdapter(List<Brandlist> list, BrandInterfaces brandInterfaces) {
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
        Brandlist list = brandLists.get(position);
//        holder.binding.setLang(sessionManage.getUserDetails().get("LANGUAGE"));
//        holder.binding.setList(list);



        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                holder.binding.name.setText(list.getName());
                break;
            case "fr":
                if(list.getName_fr().isEmpty()){
                    holder.binding.name.setText(list.getName());
                }else {
                    holder.binding.name.setText(list.getName_fr());
                }
                break;
            case "ar":
                holder.binding.name.setText(list.getName_ar());
                break;
        }






        Glide.with(context).load(ApiClient.Image_URL + list.getImg()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.binding.GipLoader.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.binding.GipLoader.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.binding.BrandImg);

        holder.binding.market1.setOnClickListener(v -> {
            brandInterfaces.brandItemClick(list , list.getName() , list.getName_ar());
        });
//        holder.binding.executePendingBindings();
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
        void brandItemClick(Brandlist list , String text , String text_ar );
    }


}

































