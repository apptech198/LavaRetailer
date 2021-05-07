package com.apptech.myapplication.ui.passbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.CheckEntriesSellOutInvalidAdapter;
import com.apptech.myapplication.databinding.PassbookFragmentBinding;
import com.apptech.myapplication.modal.CheckEntriesSellOutImeiMonthYearsList;

import java.util.ArrayList;
import java.util.Calendar;


public class PassbookFragment extends Fragment {

    private PassbookViewModel mViewModel;
    PassbookFragmentBinding binding;
    CheckEntriesSellOutInvalidAdapter adapter;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> month;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> yearsLists;
    private static final String TAG = "PassbookFragment";
    LinearLayout fromDatetitle, toDatetitle;
    View view;
    TextView fromTextView, toTextView;
    AlertDialog alertDialog;
    ImageView closeImg;
    DatePickerDialog picker;

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


        binding.datetimefilter.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
            view = LayoutInflater.from(requireContext()).inflate(R.layout.row_dialog_open, null);
            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();
            fromDatetitle = view.findViewById(R.id.fromDatetitle);
            toDatetitle = view.findViewById(R.id.toDatetitle);
            dilogclick();
        });

    }


    private void dilogclick() {


        fromTextView = view.findViewById(R.id.fromTextView);
        toTextView = view.findViewById(R.id.toTextView);
        closeImg = view.findViewById(R.id.closeImg);


        closeImg.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


        fromDatetitle.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
//                            eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        fromTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

        toDatetitle.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        toTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

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
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Select Payout"));
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