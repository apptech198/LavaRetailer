package com.apptech.myapplication.ui.trade_program.selling_program;

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
import com.apptech.myapplication.databinding.SellingProgramFragmentBinding;
import com.bumptech.glide.Glide;

public class SellingProgramFragment extends Fragment {

    private SellingProgramViewModel mViewModel;
    SellingProgramFragmentBinding binding;
    private static final String TAG = "SellingProgramFragment";

    public static SellingProgramFragment newInstance() {
        return new SellingProgramFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SellingProgramFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SellingProgramViewModel.class);
        // TODO: Use the ViewModel

        Glide.with(getContext()).load("https://lavamobilesafrica.com/lavaretail/lava//uploads/b3ec14fd15956b4595dc059019bf3932.jpeg").into(binding.imageView);


    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Selling Program");
    }

}