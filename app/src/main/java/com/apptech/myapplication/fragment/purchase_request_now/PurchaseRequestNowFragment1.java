package com.apptech.myapplication.fragment.purchase_request_now;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.adapter.PurchaseNow1Adapter;
import com.apptech.myapplication.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.myapplication.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.myapplication.databinding.FragmentPurchaseRequestNow1Binding;
import com.apptech.myapplication.modal.product.ProductList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PurchaseRequestNowFragment1 extends Fragment {

    private PurchaseRequestNowViewModel mViewModel;
    FragmentPurchaseRequestNow1Binding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseRequestNow1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }




}































