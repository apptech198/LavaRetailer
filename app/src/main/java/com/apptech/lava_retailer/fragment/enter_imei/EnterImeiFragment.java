package com.apptech.lava_retailer.fragment.enter_imei;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.EnterImeiFragmentBinding;


public class EnterImeiFragment extends Fragment implements View.OnClickListener {

    private EnterImeiViewModel mViewModel;
    EnterImeiFragmentBinding binding;

    public static EnterImeiFragment newInstance() {
        return new EnterImeiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = EnterImeiFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EnterImeiViewModel.class);
        // TODO: Use the ViewModel


        binding.sellOut.setOnClickListener(this);
        binding.PriceDrop.setOnClickListener(this);
        binding.Pending.setOnClickListener(this);
        binding.Warranty.setOnClickListener(this);


    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.ImeiFrameLayout, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.sell_out:
//                loadfragment(new SellOutFragment());
//                break;
//            case R.id.PriceDrop:
//                loadfragment(new PriceDropFragment());
//                break;
//            case R.id.Pending:
//                loadfragment(new PendingFragment());
//                break;
//            case R.id.Warranty:
//                loadfragment(new WarrantyFragment());
//                break;
//        }
    }


}




















