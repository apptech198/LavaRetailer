package com.apptech.lava_retailer.fragment.trade.loyalty_scheme;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.lava_retailer.databinding.LoyaltySchemeTradeFragmentBinding;
import com.bumptech.glide.Glide;

public class LoyaltySchemeTradeFragment extends Fragment {

    private LoyaltySchemeTradeViewModel mViewModel;
    LoyaltySchemeTradeFragmentBinding binding;

    public static LoyaltySchemeTradeFragment newInstance() {
        return new LoyaltySchemeTradeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoyaltySchemeTradeFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoyaltySchemeTradeViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/b3ec14fd15956b4595dc059019bf3932.jpeg").into(binding.imageView);


    }

}