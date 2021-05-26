package com.apptech.lava_retailer.fragment.check_entries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.CheckEntriesFragmentBinding;
import com.apptech.lava_retailer.fragment.check_entries.pricedrop.CheckEntriesPriceDrop;
import com.apptech.lava_retailer.fragment.check_entries.sellout.CheckEntriesSellOutFragment;


public class CheckEntriesFragment extends Fragment {

    private CheckEntriesViewModel mViewModel;
    CheckEntriesFragmentBinding binding;

    public static CheckEntriesFragment newInstance() {
        return new CheckEntriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = CheckEntriesFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckEntriesViewModel.class);
        // TODO: Use the ViewModel

        loadfragment(new CheckEntriesSellOutFragment());
        binding.sellOut.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
        binding.sellOut.setTextColor(getResources().getColor(R.color.black));

        binding.sellOut.setOnClickListener(v -> {
            binding.sellOut.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
            binding.sellOut.setTextColor(getResources().getColor(R.color.black));

            binding.Pricedrop.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.right_corner_round_black, null));
            binding.Pricedrop.setTextColor(getResources().getColor(R.color.white));

            loadfragment(new CheckEntriesSellOutFragment());
        });
        binding.Pricedrop.setOnClickListener(v -> {
            binding.Pricedrop.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.right_corner_color, null));
            binding.Pricedrop.setTextColor(getResources().getColor(R.color.black));

            binding.sellOut.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
            binding.sellOut.setTextColor(getResources().getColor(R.color.white));

            loadfragment(new CheckEntriesPriceDrop());
        });

    }


    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.CheckEntriesFrameLayout, fragment).addToBackStack(null).commit();
    }

}