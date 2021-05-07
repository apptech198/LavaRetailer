package com.apptech.myapplication.ui.trade_program.sellout_program;

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
import com.apptech.myapplication.databinding.SellOutProgramFragmentBinding;
import com.bumptech.glide.Glide;

public class SellOutProgramFragment extends Fragment {

    private SellOutProgramViewModel mViewModel;
    SellOutProgramFragmentBinding binding;

    public static SellOutProgramFragment newInstance() {
        return new SellOutProgramFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SellOutProgramFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SellOutProgramViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/fede1d85f86de8482853afd2de64d23b.jpeg").into(binding.imageView);

    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Sellout Program");
    }
}