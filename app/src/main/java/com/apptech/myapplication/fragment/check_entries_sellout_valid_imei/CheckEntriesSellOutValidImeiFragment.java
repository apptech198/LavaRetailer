package com.apptech.myapplication.fragment.check_entries_sellout_valid_imei;

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

import com.apptech.myapplication.adapter.CheckEntriesSellOutValidImeiAdapter;
import com.apptech.myapplication.databinding.CheckEntriesSellOutValidImeiFragmentBinding;
import com.apptech.myapplication.modal.CheckEntriesSellOutImeiMonthYearsList;

import java.util.ArrayList;

public class CheckEntriesSellOutValidImeiFragment extends Fragment {

    private CheckEntriesSellOutValidImeiViewModel mViewModel;
    CheckEntriesSellOutValidImeiFragmentBinding binding;
    CheckEntriesSellOutValidImeiAdapter adapter;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> month;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> yearsLists;
    private static final String TAG = "CheckEntriesSellOutVali";

    public static CheckEntriesSellOutValidImeiFragment newInstance() {
        return new CheckEntriesSellOutValidImeiFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CheckEntriesSellOutValidImeiFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CheckEntriesSellOutValidImeiViewModel.class);
        // TODO: Use the ViewModel
        MonthinitList();
        YearsinitList();
        Customspinnerset();
    }

    private void Customspinnerset() {

        adapter = new CheckEntriesSellOutValidImeiAdapter(getContext(), month);
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


        adapter = new CheckEntriesSellOutValidImeiAdapter(getContext(), yearsLists);
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































