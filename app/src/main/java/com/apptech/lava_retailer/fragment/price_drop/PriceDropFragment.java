package com.apptech.lava_retailer.fragment.price_drop;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutPendingVerificationAdapter;
import com.apptech.lava_retailer.databinding.PriceDropFragmentBinding;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.List;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PriceDropFragment extends Fragment {

    private PriceDropViewModel mViewModel;
    AlertDialog alertDialog;
    View view;
    LinearLayout fromDatetitle, toDatetitle;
    TextView fromTextView, toTextView;
    ImageView closeImg;
    DatePickerDialog picker;
    private static final String TAG = "SellOut_PendingVerifica";
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String USER_ID;
    String StartDate = null, EndDate = null;
    Button searchBtn;
    java.util.List<List> lists;
    SellOutPendingVerificationAdapter sellOutPendingVerificationAdapter;
    PriceDropFragmentBinding binding;

    public static PriceDropFragment newInstance() {
        return new PriceDropFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PriceDropFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PriceDropViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        USER_ID = sessionManage.getUserDetails().get("ID");


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


        StockList(getCurrentDate(), getCurrentDate());

        binding.validsipnner.setOnSpinnerItemSelectedListener((i, o, i1, t1) -> {
            Log.e(TAG, "onActivityCreated: " + t1.toString().trim());
            filtervalid(t1.toString());
        });

    }


    private void StockList(String startdate, String enddate) {
        Log.e(TAG, "onActivityCreated: " + USER_ID);

        lavaInterface.PRICE_DROP_IMEI_LIST(USER_ID, startdate, enddate).enqueue(new Callback<SellOutPendingVerificationList>() {
            @Override
            public void onResponse(Call<SellOutPendingVerificationList> call, Response<SellOutPendingVerificationList> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        if (response.body().getList().size() == 0) {
                            binding.noStock.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        lists = new ArrayList<>(response.body().getList());
                        sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(response.body().getList());
                        binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.noStock.setVisibility(View.VISIBLE);
                }
                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SellOutPendingVerificationList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

                binding.progressbar.setVisibility(View.GONE);
                binding.noStock.setVisibility(View.VISIBLE);

            }
        });

    }


    private void dilogclick() {


        fromTextView = view.findViewById(R.id.fromTextView);
        toTextView = view.findViewById(R.id.toTextView);
        closeImg = view.findViewById(R.id.closeImg);
        searchBtn = view.findViewById(R.id.searchBtn);

        closeImg.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        searchBtn.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                if (fromTextView.getText().toString().trim().equalsIgnoreCase("From")) {
                    Snackbar snackbar = Snackbar
                            .make(binding.getRoot(), "from date", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                if (toTextView.getText().toString().trim().equalsIgnoreCase("To")) {
                    Snackbar snackbar = Snackbar
                            .make(binding.getRoot(), "to dater", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                dateFilter1(fromTextView.getText().toString().trim(), toTextView.getText().toString().trim());
                alertDialog.dismiss();

                return;
            }
            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
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
                        StartDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
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
                        EndDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }, year, month, day);
            picker.show();
        });

    }

    private void dateFilter1(String from, String to) {

        binding.progressbar.setVisibility(View.VISIBLE);
        binding.noStock.setVisibility(View.GONE);


        lavaInterface.PRICE_DROP_IMEI_LIST(USER_ID, from, to).enqueue(new Callback<SellOutPendingVerificationList>() {
            @Override
            public void onResponse(Call<SellOutPendingVerificationList> call, Response<SellOutPendingVerificationList> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        lists = new ArrayList<>(response.body().getList());
                        sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(response.body().getList());
                        binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
                        sellOutPendingVerificationAdapter.notifyDataSetChanged();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.noStock.setVisibility(View.VISIBLE);
                }
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<SellOutPendingVerificationList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

                binding.progressbar.setVisibility(View.GONE);

            }
        });
    }

    private void filtervalid(String valid) {
        binding.progressbar.setVisibility(View.VISIBLE);
        java.util.List<List> list = new ArrayList<>();

//        for (int i = 0; i < lists.size(); i++) {
//            com.apptech.lava_retailer.modal.sellOutPendingVerification.List l = lists.get(i);
//            if (valid.equalsIgnoreCase(l.getValid())) {
//                list.add(l);
//            }
//        }
//
//        Log.e(TAG, "filtervalid: " + list.size());
//        Log.e(TAG, "filtervalid: " + list);
//
//        if (list.size() == 0) {
//            binding.noStock.setVisibility(View.VISIBLE);
//        } else {
//            binding.noStock.setVisibility(View.GONE);
//        }
//        sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(list);
//        binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
//        sellOutPendingVerificationAdapter.notifyDataSetChanged();
//        binding.progressbar.setVisibility(View.GONE);

    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


}