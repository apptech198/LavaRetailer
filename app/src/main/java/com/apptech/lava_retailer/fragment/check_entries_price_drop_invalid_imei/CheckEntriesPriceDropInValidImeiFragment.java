package com.apptech.lava_retailer.fragment.check_entries_price_drop_invalid_imei;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.apptech.lava_retailer.adapter.CheckEntriesSellOutInvalidAdapter;
import com.apptech.lava_retailer.databinding.CheckEntriesPriceDropInValidImeiFragmentBinding;
import com.apptech.lava_retailer.modal.CheckEntriesSellOutImeiMonthYearsList;

import java.util.ArrayList;

public class CheckEntriesPriceDropInValidImeiFragment extends Fragment {

    private CheckEntriesPriceDropInValidImeiViewModel mViewModel;
    CheckEntriesSellOutInvalidAdapter adapter;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> month;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> yearsLists;
    CheckEntriesPriceDropInValidImeiFragmentBinding binding;
    private static final String TAG = "CheckEntriesPriceDropIn";

    public static CheckEntriesPriceDropInValidImeiFragment newInstance() {
        return new CheckEntriesPriceDropInValidImeiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CheckEntriesPriceDropInValidImeiFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckEntriesPriceDropInValidImeiViewModel.class);
        // TODO: Use the ViewModel

        MonthinitList();
        YearsinitList();
        Customspinnerset();
    }



    private void Customspinnerset() {

        adapter = new CheckEntriesSellOutInvalidAdapter(getContext(), month);
        binding.Monthspinner.setAdapter(adapter);
        binding.Monthspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        binding.Yearsspinner.setAdapter(adapter);
        binding.Yearsspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Jan"));
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Feb"));
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Mar"));
    }

    private void YearsinitList() {
        yearsLists = new ArrayList<>();
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("2018"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("2019"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("2020"));
    }

}