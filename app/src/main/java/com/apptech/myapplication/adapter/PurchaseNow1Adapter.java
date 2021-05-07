package com.apptech.myapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowPurachaseRequestNow1Binding;
import com.apptech.myapplication.modal.product.ProductList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PurchaseNow1Adapter extends RecyclerView.Adapter<PurchaseNow1Adapter.ViewBinding> {

    RowPurachaseRequestNow1Binding binding;
    Context context;
    List<ProductList> productLists;

    public PurchaseNow1Adapter(List<ProductList> productLists) {
        this.productLists = productLists;
    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = RowPurachaseRequestNow1Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {
//        ProductList list = productLists.get(position);
//        binding.setList(list);
//        binding.productAmtDic.setPaintFlags(binding.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull @NotNull RowPurachaseRequestNow1Binding itemView) {
            super(itemView.getRoot());
        }
    }
}
