package com.apptech.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowBrandBottomSheetBinding;
import com.apptech.myapplication.modal.brand.BrandList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewBinding> {

    RowBrandBottomSheetBinding binding;
    List<BrandList> brandLists;
    BrandItemClickInterface brandItemClick;

    public BrandAdapter(List<BrandList> brandLists , BrandItemClickInterface brandItemClick) {
        this.brandLists = brandLists;
        this.brandItemClick = brandItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public BrandAdapter.ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = RowBrandBottomSheetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setItemclick(brandItemClick);
        return new ViewBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BrandAdapter.ViewBinding holder, int position) {
        BrandList list = brandLists.get(position);
        binding.setList(list);
        binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return brandLists.size();
    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull @NotNull RowBrandBottomSheetBinding itemView) {
            super(itemView.getRoot());
        }
    }

    public interface BrandItemClickInterface{
        void onItemClick(BrandList list);
    }

}


























