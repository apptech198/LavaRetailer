package com.apptech.lava_retailer.ui.warranty.pending_replacement_request;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.lava_retailer.R;

public class PendingReplacementRequestFragment extends Fragment {

    private PendingReplacementRequestViewModel mViewModel;

    public static PendingReplacementRequestFragment newInstance() {
        return new PendingReplacementRequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pending_replacement_request_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PendingReplacementRequestViewModel.class);
        // TODO: Use the ViewModel
    }

}