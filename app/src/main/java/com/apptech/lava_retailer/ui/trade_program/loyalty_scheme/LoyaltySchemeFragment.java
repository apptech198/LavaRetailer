package com.apptech.lava_retailer.ui.trade_program.loyalty_scheme;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.LoyaltySchemeFragmentBinding;
import com.bumptech.glide.Glide;

public class LoyaltySchemeFragment extends Fragment {

    private LoyaltySchemeViewModel mViewModel;
    LoyaltySchemeFragmentBinding binding;

    public static LoyaltySchemeFragment newInstance() {
        return new LoyaltySchemeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoyaltySchemeFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoyaltySchemeViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/b3ec14fd15956b4595dc059019bf3932.jpeg").into(binding.imageView);

    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Loyalty scheme");
    }

}