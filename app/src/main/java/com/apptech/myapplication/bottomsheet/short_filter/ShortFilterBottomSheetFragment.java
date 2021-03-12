package com.apptech.myapplication.bottomsheet.short_filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.databinding.ShortFilterBottomSheetFragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShortFilterBottomSheetFragment extends BottomSheetDialogFragment {

    private ShortFilterBottomSheetViewModel mViewModel;
    ShortFilterBottomSheetFragmentBinding binding;

    public static ShortFilterBottomSheetFragment newInstance() {
        return new ShortFilterBottomSheetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ShortFilterBottomSheetFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShortFilterBottomSheetViewModel.class);
        // TODO: Use the ViewModel


        binding.popularityLayout.setOnClickListener(v -> {
            binding.popularityRadio.setChecked(true);
            binding.newestRadio.setChecked(false);
        });
        binding.newestFirstLayout.setOnClickListener(v -> {
            binding.popularityRadio.setChecked(false);
            binding.newestRadio.setChecked(true);
        });

    }

}































