package com.apptech.myapplication.ui.price_drop.entry_pending_verification;

import androidx.core.util.Pair;
import androidx.databinding.adapters.ToolbarBindingAdapter;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.SellOutPendingVerificationAdapter;
import com.apptech.myapplication.databinding.EntryPendingVerificationFragmentBinding;
import com.apptech.myapplication.databinding.SellOutPendingVerificationFragmentBinding;
import com.apptech.myapplication.fragment.sellOut_PendingVerification.SellOutPendingVerificationViewModel;
import com.apptech.myapplication.modal.sellOutPendingVerification.List;
import com.apptech.myapplication.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.apptech.myapplication.other.NetworkCheck;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryPendingVerificationFragment extends Fragment {

    private EntryPendingVerificationViewModel mViewModel;
    EntryPendingVerificationFragmentBinding binding;
    private static final String TAG = "EntryPendingVerificatio";


    AlertDialog alertDialog;
    View view;
    LinearLayout fromDatetitle, toDatetitle;
    TextView fromTextView, toTextView;
    ImageView closeImg;
    DatePickerDialog picker;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String USER_ID;
    String StartDate1 = null, EndDate = null;
    Button searchBtn;
    java.util.List<List> lists;
    SellOutPendingVerificationAdapter sellOutPendingVerificationAdapter;
    PopupWindow mypopupWindow;
    String StartDate ="" , End_Date = "" , TYPE = "";
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


    public static EntryPendingVerificationFragment newInstance() {
        return new EntryPendingVerificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = EntryPendingVerificationFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EntryPendingVerificationViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        USER_ID = sessionManage.getUserDetails().get("ID");


        setPopUpWindow();
        binding.datetimefilter.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
//            view = LayoutInflater.from(requireContext()).inflate(R.layout.row_dialog_open, null);
//            builder.setView(view);
//            alertDialog = builder.create();
//            alertDialog.show();
//            fromDatetitle = view.findViewById(R.id.fromDatetitle);
//            toDatetitle = view.findViewById(R.id.toDatetitle);
//            dilogclick();
            mypopupWindow.showAsDropDown(v,-153,0);
        });

        if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
            StockList(getCurrentDate(), getCurrentDate());
        } else {
            binding.progressbar.setVisibility(View.GONE);
            binding.noStock.setVisibility(View.GONE);
            Toast.makeText(requireContext(), getContext().getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

        binding.validsipnner.setOnSpinnerItemSelectedListener((i, o, i1, t1) -> {
            Log.e(TAG, "onActivityCreated: " + t1.toString().trim());
            filtervalid(t1.toString());
        });

    }

    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup, null);
        TextView last_7_day = (TextView) view.findViewById(R.id.last_7_day);
        TextView this_month = (TextView) view.findViewById(R.id.this_month);
        TextView last_month = (TextView) view.findViewById(R.id.last_month);
        TextView CustomDate = (TextView) view.findViewById(R.id.CustomDate);
        mypopupWindow = new PopupWindow(view, 300, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        last_7_day.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] last_7 = ThisWeekDate().split("#");
            StartDate = last_7[0];
            End_Date = last_7[1];
            StockList(End_Date , StartDate );
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            StockList(StartDate , End_Date );
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[1];
            End_Date = thisMonth[0];
            StockList(End_Date , StartDate );
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

    }

    private void datePicker() {
        builder.setTitleText("Select date");
        binding.datetimefilter.setClickable(false);
        materialDatePicker.show(getChildFragmentManager(), "");

        materialDatePicker.addOnCancelListener(dialog -> {
            binding.datetimefilter.setClickable(true);
        });


        materialDatePicker.addOnDismissListener(dialog -> {
            binding.datetimefilter.setClickable(true);
        });


        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Log.e(TAG, "datePicker: " + selection.first );
            Log.e(TAG, "datePicker: " + selection.second );
            binding.datetimefilter.setClickable(true);
            StartDate = getTimeStamp(selection.second) ;
            End_Date = getTimeStamp(selection.first);
            StockList(End_Date , StartDate );
        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // modify formatSimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }



    private String TodayDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        String endDateStr = df.format(calendar1.getTime());
        return  startDateStr + "#" + endDateStr;
    }



    private String ThisWeekDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK , -7);
        String endDateStr = df.format(calendar1.getTime());
        return  startDateStr + "#" + endDateStr;
    }


    public String FirstAndLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
    }

    public String LastMonthdate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
    }

    public String ThisMonthdate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
    }

    private void StockList(String startdate, String enddate) {
        Log.e(TAG, "onActivityCreated: " + USER_ID);

//        Toast.makeText(getContext(), "startdate : " + startdate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "enddate : " + enddate, Toast.LENGTH_SHORT).show();

        lavaInterface.PRICE_DROP_IMEI_LIST(USER_ID, startdate, enddate).enqueue(new Callback<SellOutPendingVerificationList>() {
            @Override
            public void onResponse(Call<SellOutPendingVerificationList> call, Response<SellOutPendingVerificationList> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        if (response.body().getList().size() > 0) {
                            lists = new ArrayList<>(response.body().getList());
                            sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(response.body().getList());
                            binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noStock.setVisibility(View.GONE);
                            binding.ImeiRecyclerView.setVisibility(View.VISIBLE);
                            return;
                        }
                        binding.noStock.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.ImeiRecyclerView.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.noStock.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }
                binding.noStock.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SellOutPendingVerificationList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

                binding.progressbar.setVisibility(View.GONE);
                binding.noStock.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Time out" , Toast.LENGTH_SHORT).show();

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
                        if (response.body().getList().size() > 0) {
                            sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(response.body().getList());
                            binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
                            sellOutPendingVerificationAdapter.notifyDataSetChanged();
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noStock.setVisibility(View.GONE);
                            binding.ImeiRecyclerView.setVisibility(View.VISIBLE);
                            return;
                        }
                        binding.noStock.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.ImeiRecyclerView.setVisibility(View.GONE);
                        return;
                    }
                    Toast.makeText(requireContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.noStock.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }
                binding.progressbar.setVisibility(View.GONE);
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SellOutPendingVerificationList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void filtervalid(String valid) {
        binding.progressbar.setVisibility(View.VISIBLE);
        java.util.List<List> list = new ArrayList<>();

        for (int i = 0; i < lists.size(); i++) {
            com.apptech.myapplication.modal.sellOutPendingVerification.List l = lists.get(i);
            if (valid.equalsIgnoreCase(l.getValid())) {
                list.add(l);
            }
        }

        Log.e(TAG, "filtervalid: " + list.size());
        Log.e(TAG, "filtervalid: " + list);

        if (list.size() == 0) {
            binding.noStock.setVisibility(View.VISIBLE);
        } else {
            binding.noStock.setVisibility(View.GONE);
        }
        binding.progressbar.setVisibility(View.GONE);
        sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(list);
        binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
        sellOutPendingVerificationAdapter.notifyDataSetChanged();

    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    private String dateSplite(String date) {
        return date.split(" ")[0];
    }

    private Date dateConvert(String dates) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dates);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Entery pending verification");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}