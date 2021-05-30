package com.apptech.lava_retailer.ui.price_drop.entry_pending_verification;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
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

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutPendingVerificationAdapter;
import com.apptech.lava_retailer.databinding.EntryPendingVerificationFragmentBinding;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.List;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
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

public class EntryPendingVerificationFragment extends Fragment implements View.OnClickListener {

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
    java.util.List<List> DuplicateList = new ArrayList<>();


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

            StartDate = getCurrentDate();
            End_Date = getCurrentDate();
            TYPE = "NO";

            StockList();
        } else {
            binding.progressbar.setVisibility(View.GONE);
            binding.noStock.setVisibility(View.GONE);
            Toast.makeText(requireContext(), getContext().getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

//        binding.validsipnner.setOnSpinnerItemSelectedListener((i, o, i1, t1) -> {
//            Log.e(TAG, "onActivityCreated: " + t1.toString().trim());
//            filtervalid(t1.toString());
//        });

        binding.PendingLayout.setOnClickListener(this);
        binding.ApprovedLayout.setOnClickListener(this);
        binding.RejectedLayout.setOnClickListener(this);

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
            StockList();
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            StockList();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[0];
            End_Date = thisMonth[1];
            StockList();
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

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
        String endDateStr  = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK , -7);
        String startDateStr = df.format(calendar1.getTime());
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
            StartDate = getTimeStamp(selection.first) ;
            End_Date = getTimeStamp(selection.second);
            StockList();
        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }

    private void StockList() {
        Log.e(TAG, "onActivityCreated: " + USER_ID);

        Log.e(TAG, "StockList: " + StartDate );
        Log.e(TAG, "StockList: " + End_Date );


        lavaInterface.PRICE_DROP_IMEI_LIST(USER_ID, StartDate, End_Date).enqueue(new Callback<SellOutPendingVerificationList>() {
            @Override
            public void onResponse(Call<SellOutPendingVerificationList> call, Response<SellOutPendingVerificationList> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                try {

                    if (response.isSuccessful()) {
                        if (!response.body().getError()) {
                            if (response.body().getList().size() > 0) {

                                lists = new ArrayList<>(response.body().getList());

                                DuplicateList.clear();
                                for (int i=0; i < response.body().getList().size(); i++){
                                    if(TYPE.equalsIgnoreCase(lists.get(i).getValid())){
                                        List l = lists.get(i);
                                        DuplicateList.add(l);
                                    }
                                }

                                if(DuplicateList.size() > 0){
                                    sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(DuplicateList);
                                    binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
                                    binding.ImeiRecyclerView.setVisibility(View.VISIBLE);
                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.noStock.setVisibility(View.GONE);
                                    return;
                                }
                                binding.ImeiRecyclerView.setVisibility(View.GONE);
                                binding.noStock.setVisibility(View.VISIBLE);
                                binding.progressbar.setVisibility(View.GONE);
                                return;
                            }
                            binding.ImeiRecyclerView.setVisibility(View.GONE);
                            binding.noStock.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        binding.ImeiRecyclerView.setVisibility(View.GONE);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.noStock.setVisibility(View.VISIBLE);
                        return;
                    }
                    Toast.makeText(getContext(), getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noStock.setVisibility(View.VISIBLE);
                    binding.ImeiRecyclerView.setVisibility(View.GONE);

                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                }
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
            com.apptech.lava_retailer.modal.sellOutPendingVerification.List l = lists.get(i);
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
        title.setText(getActivity().getString(R.string.Entery_pending_verification));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.PendingLayout:
                TYPE = "PENDING";
                StockList();
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.RejectedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
            case R.id.ApprovedLayout:
                TYPE = "APPROVED";
                StockList();
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.RejectedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));

                break;
            case R.id.RejectedLayout:
                TYPE = "REJECTED";
                StockList();
                binding.RejectedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
        }

    }
}