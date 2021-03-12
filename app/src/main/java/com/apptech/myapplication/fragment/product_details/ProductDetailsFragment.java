package com.apptech.myapplication.fragment.product_details;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.databinding.ProductDetailsFragmentBinding;
import com.apptech.myapplication.modal.product.ProductList;
import com.bumptech.glide.Glide;

public class ProductDetailsFragment extends Fragment {

    private ProductDetailsViewModel mViewModel;
    ProductList list;
    ProductDetailsFragmentBinding binding;


    public ProductDetailsFragment(ProductList list) {
        this.list = list;
    }

    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment(newInstance().list);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProductDetailsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(binding.image)
                .load(list.getImg())
                .fitCenter()
                .into(binding.image);
        binding.productName.setText(list.getName());
        binding.productAmtDic.setPaintFlags(binding.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


    }

}


































