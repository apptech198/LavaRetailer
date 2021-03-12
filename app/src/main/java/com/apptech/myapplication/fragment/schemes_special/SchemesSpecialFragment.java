package com.apptech.myapplication.fragment.schemes_special;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.myapplication.R;

public class SchemesSpecialFragment extends Fragment {

    private SchemesSpecialViewModel mViewModel;

    public static SchemesSpecialFragment newInstance() {
        return new SchemesSpecialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schemes_special_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SchemesSpecialViewModel.class);
        // TODO: Use the ViewModel
    }

}