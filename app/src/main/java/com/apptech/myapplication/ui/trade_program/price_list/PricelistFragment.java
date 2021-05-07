package com.apptech.myapplication.ui.trade_program.price_list;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.PricelistFragmentBinding;
import com.bumptech.glide.Glide;

public class PricelistFragment extends Fragment {

    private PricelistViewModel mViewModel;
    PricelistFragmentBinding binding;

    public static PricelistFragment newInstance() {
        return new PricelistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PricelistFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PricelistViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/fede1d85f86de8482853afd2de64d23b.jpeg").into(binding.imageView);

    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Price List");
    }
}