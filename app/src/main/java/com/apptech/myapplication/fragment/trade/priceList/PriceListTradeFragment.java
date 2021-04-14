package com.apptech.myapplication.fragment.trade.priceList;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.PriceListTradeFragmentBinding;
import com.bumptech.glide.Glide;

public class PriceListTradeFragment extends Fragment {

    private PriceListTradeViewModel mViewModel;
    PriceListTradeFragmentBinding binding;

    public static PriceListTradeFragment newInstance() {
        return new PriceListTradeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PriceListTradeFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PriceListTradeViewModel.class);
        // TODO: Use the ViewModel


        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/fede1d85f86de8482853afd2de64d23b.jpeg").into(binding.imageView);

    }

}