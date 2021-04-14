package com.apptech.myapplication.fragment.purchase_request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.databinding.PurchaseRequestFragmentBinding;
import com.apptech.myapplication.fragment.product_details.ProductDetailsFragment;
import com.apptech.myapplication.fragment.purchase_request_now.PurchaseRequestNowFragment;
import com.apptech.myapplication.fragment.purchase_request_past.PurchaseRequestPastFragment;
import com.apptech.myapplication.modal.product.ProductList;


public class PurchaseRequestFragment extends Fragment  {

    private PurchaseRequestViewModel mViewModel;
    PurchaseRequestFragmentBinding binding;
    private static final String TAG = "PurchaseRequestFragment";

    public static PurchaseRequestFragment newInstance() {
        return new PurchaseRequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PurchaseRequestFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PurchaseRequestViewModel.class);
        // TODO: Use the ViewModel

        binding.purchaseNow.setBackground(getResources().getDrawable(R.drawable.left_corner_color));
        binding.purchaseNow.setTextColor(getResources().getColor(R.color.black));

        binding.purchaseNow.setOnClickListener(v -> {

//            loadfragment(new PurchaseRequestNowFragment());

            binding.purchaseNow.setBackground(getResources().getDrawable(R.drawable.left_corner_color));
            binding.purchaseNow.setTextColor(getResources().getColor(R.color.black));

            binding.pastPurchases.setBackground(getResources().getDrawable(R.drawable.right_corner_round_black));
            binding.pastPurchases.setTextColor(getResources().getColor(R.color.white));

        });

        binding.pastPurchases.setOnClickListener(v -> {
            loadfragment(new PurchaseRequestPastFragment());

            binding.pastPurchases.setBackground(getResources().getDrawable(R.drawable.right_corner_color));
            binding.pastPurchases.setTextColor(getResources().getColor(R.color.black));

            binding.purchaseNow.setBackground(getResources().getDrawable(R.drawable.left_corner_color_black));
            binding.purchaseNow.setTextColor(getResources().getColor(R.color.white));


        });


    }

    @Override
    public void onStart() {
        super.onStart();
//        loadfragment(new PurchaseRequestNowFragment());
    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.PurchRequestFrameLayout, fragment).addToBackStack(null).commit();
    }



}






























