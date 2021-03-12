package com.apptech.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.activity.ProductDetailsActivity;
import com.apptech.myapplication.databinding.RowPurachaseRequestNowBinding;
import com.apptech.myapplication.modal.product.ProductList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PurchaseNowAdapter extends RecyclerView.Adapter<PurchaseNowAdapter.ViewBinding> {

    RowPurachaseRequestNowBinding binding;
    List<ProductList> productLists;
    Context context;
    PurchaseNowIterface purchaseNowIterface;

    public PurchaseNowAdapter(List<ProductList> productLists , PurchaseNowIterface purchaseNowIterface) {
        this.productLists = productLists;
        this.purchaseNowIterface = purchaseNowIterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = RowPurachaseRequestNowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setIterface(purchaseNowIterface);
        return new ViewBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {
        ProductList list = productLists.get(position);
        binding.setList(list);
        binding.productAmtDic.setPaintFlags(binding.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.mainLayout.setOnClickListener(v -> {
            context.startActivity(new Intent(context , ProductDetailsActivity.class));
        });
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull @NotNull RowPurachaseRequestNowBinding itemView) {
            super(itemView.getRoot());
        }
    }


    public interface PurchaseNowIterface{
        void itemClick(ProductList list);
    }



}




































