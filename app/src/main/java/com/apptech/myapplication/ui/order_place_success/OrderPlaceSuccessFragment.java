package com.apptech.myapplication.ui.order_place_success;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.myapplication.R;

public class OrderPlaceSuccessFragment extends Fragment {

    private OrderPlaceSuccessViewModel mViewModel;

    public static OrderPlaceSuccessFragment newInstance() {
        return new OrderPlaceSuccessFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_place_success_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderPlaceSuccessViewModel.class);
        // TODO: Use the ViewModel
    }

}