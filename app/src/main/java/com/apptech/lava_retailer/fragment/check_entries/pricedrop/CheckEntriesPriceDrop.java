package com.apptech.lava_retailer.fragment.check_entries.pricedrop;

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
import com.apptech.lava_retailer.databinding.CheckEntriesPriceDropFragmentBinding;
import com.apptech.lava_retailer.fragment.check_entries_price_drop_invalid_imei.CheckEntriesPriceDropInValidImeiFragment;
import com.apptech.lava_retailer.fragment.check_entries_price_drop_pending.CheckEntriesPriceDropPendingFragment;

public class CheckEntriesPriceDrop extends Fragment implements View.OnClickListener {

    private CheckEntriesPriceDropViewModel mViewModel;
    CheckEntriesPriceDropFragmentBinding binding;

    public static CheckEntriesPriceDrop newInstance() {
        return new CheckEntriesPriceDrop();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CheckEntriesPriceDropFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckEntriesPriceDropViewModel.class);
        // TODO: Use the ViewModel

        binding.validImei.setOnClickListener(this);
        binding.InvalidImei.setOnClickListener(this);
        binding.pendingverification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validImei:
//                loadfragment(new CheckEntriesPriceDropValidImeiFragment());
                // chnage white color
                binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
                binding.validImeiTextview.setTextColor(getResources().getColor(R.color.black));
                // valid chnage black color
                binding.InvalidImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.InvalidImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // pending chnage black color
                binding.pendingverification.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.pendingverificationTextview.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.InvalidImei:
                loadfragment(new CheckEntriesPriceDropInValidImeiFragment());
                // chnage white color
                binding.InvalidImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
                binding.InvalidImeiTextview.setTextColor(getResources().getColor(R.color.black));
                // valid chnage black color
                binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.validImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // pending chnage black color
                binding.pendingverification.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.pendingverificationTextview.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.pendingverification:
                loadfragment(new CheckEntriesPriceDropPendingFragment());
                // chnage white black color
                binding.InvalidImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.InvalidImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // valid chnage black color
                binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color_black, null));
                binding.validImeiTextview.setTextColor(getResources().getColor(R.color.white));
                // pending chnage  color
                binding.pendingverification.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
                binding.pendingverificationTextview.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        loadfragment(new CheckEntriesPriceDropValidImeiFragment());
        binding.validImei.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.left_corner_color, null));
        binding.validImeiTextview.setTextColor(getResources().getColor(R.color.black));
    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.CheckEntriesFrameLayout2, fragment).addToBackStack(null).commit();
    }
}




























