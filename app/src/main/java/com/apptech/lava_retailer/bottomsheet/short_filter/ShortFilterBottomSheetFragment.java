package com.apptech.lava_retailer.bottomsheet.short_filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.ShortFilterBottomSheetFragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShortFilterBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private ShortFilterBottomSheetViewModel mViewModel;
    ShortFilterBottomSheetFragmentBinding binding;
    ShortItemClck shortItemClck;


    public static ShortFilterBottomSheetFragment newInstance(ShortItemClck shortItemClck) {
        return new ShortFilterBottomSheetFragment(shortItemClck);
    }


    public ShortFilterBottomSheetFragment(ShortItemClck shortItemClck) {
        this.shortItemClck = shortItemClck;
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

        binding.popularityLayout.setOnClickListener(this);
        binding.lowHightLayout.setOnClickListener(this);
        binding.highLowLayout.setOnClickListener(this);
        binding.newestFirstLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popularityLayout:
                shortItemClck.onItemClick("POPULARITY");
                break;
            case R.id.lowHightLayout:
                shortItemClck.onItemClick("LOW_TO_HIGHT");
                break;
            case R.id.highLowLayout:
                shortItemClck.onItemClick("HIGH_TO_LOW");
                break;
            case R.id.newestFirstLayout:
                shortItemClck.onItemClick("NEWES_FIRST");
                break;
        }
    }


    public interface ShortItemClck{
        void onItemClick(String text);
    }

}































