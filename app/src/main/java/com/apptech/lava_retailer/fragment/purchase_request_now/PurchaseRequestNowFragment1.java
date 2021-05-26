package com.apptech.lava_retailer.fragment.purchase_request_now;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apptech.lava_retailer.databinding.FragmentPurchaseRequestNow1Binding;

import org.jetbrains.annotations.NotNull;


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































