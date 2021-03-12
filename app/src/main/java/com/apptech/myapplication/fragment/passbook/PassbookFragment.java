package com.apptech.myapplication.fragment.passbook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.CheckEntriesSellOutInvalidAdapter;
import com.apptech.myapplication.databinding.PassbookFragmentBinding;
import com.apptech.myapplication.modal.CheckEntriesSellOutImeiMonthYearsList;

import java.util.ArrayList;


public class PassbookFragment extends Fragment {

    private PassbookViewModel mViewModel;
    PassbookFragmentBinding binding;
    CheckEntriesSellOutInvalidAdapter adapter;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> month;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> yearsLists;
    private static final String TAG = "PassbookFragment";

    public static PassbookFragment newInstance() {
        return new PassbookFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PassbookFragmentBinding.inflate(inflater , container ,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PassbookViewModel.class);
        // TODO: Use the ViewModel


        MonthinitList();
        YearsinitList();
        Customspinnerset();
    }



    private void Customspinnerset() {

        adapter = new CheckEntriesSellOutInvalidAdapter(getContext(), month);
        binding.PaymentTypespinner.setAdapter(adapter);
        binding.PaymentTypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CheckEntriesSellOutImeiMonthYearsList yearsList = (CheckEntriesSellOutImeiMonthYearsList) parent.getItemAtPosition(position);
                String clickedCountryName = yearsList.getDate();
                Log.e(TAG, "onItemSelected: " + clickedCountryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter = new CheckEntriesSellOutInvalidAdapter(getContext(), yearsLists);
        binding.Datespinner.setAdapter(adapter);
        binding.Datespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CheckEntriesSellOutImeiMonthYearsList yearsList = (CheckEntriesSellOutImeiMonthYearsList) parent.getItemAtPosition(position);
                String clickedCountryName = yearsList.getDate();
                Log.e(TAG, "onItemSelected: " + clickedCountryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void MonthinitList() {
        month = new ArrayList<>();
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Select Payment"));
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Month Scheme"));
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Price Drop"));
    }

    private void YearsinitList() {
        yearsLists = new ArrayList<>();
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("jan ,2018"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("fef ,2019"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("mar ,2020"));
    }



}