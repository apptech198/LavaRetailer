package com.apptech.lava_retailer.ui.sell_out.pending_verification;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import com.apptech.lava_retailer.databinding.PendingVerificationFragmentBinding;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.List;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingVerificationFragment extends Fragment implements View.OnClickListener {

    private PendingVerificationViewModel mViewModel;
    PendingVerificationFragmentBinding binding;
    private static final String TAG = "SellOut_PendingVerifica";
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String USER_ID;
    java.util.List<List> lists = new ArrayList<>();

    SellOutPendingVerificationAdapter sellOutPendingVerificationAdapter;
    PopupWindow mypopupWindow;
    String StartDate ="" , End_Date = "" , TYPE = "";
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy" );

    public static PendingVerificationFragment newInstance() {
        return new PendingVerificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = PendingVerificationFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PendingVerificationViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());
        USER_ID = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);

        setPopUpWindow();
        binding.datetimefilter.setOnClickListener(v -> mypopupWindow.showAsDropDown(v,-153,0));

        String[] date = TodayDate().split("#");
        StartDate = date[0];
        End_Date = date[1];
        TYPE = "PENDING";
        submitData();


        binding.PendingLayout.setOnClickListener(this);
        binding.RejectedLayout.setOnClickListener(this);



    }

    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup, null);
        TextView last_7_day = (TextView) view.findViewById(R.id.last_7_day);
        TextView this_month = (TextView) view.findViewById(R.id.this_month);
        TextView last_month = (TextView) view.findViewById(R.id.last_month);
        TextView CustomDate = (TextView) view.findViewById(R.id.CustomDate);
        mypopupWindow = new PopupWindow(view, 300, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        last_7_day.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] last_7 = ThisWeekDate().split("#");
            StartDate = last_7[1];
            End_Date = last_7[0];
            submitData();
        });

        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            submitData();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[0];
            End_Date = thisMonth[1];
            submitData();
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

    }

    private void  submitData(){
        if (new NetworkCheck().haveNetworkConnection(requireActivity())){
            StockList();
        }else {
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private String TodayDate(){
        Calendar calendar = Calendar.getInstance();
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        String endDateStr = df.format(calendar1.getTime());
        return  startDateStr + "#" + endDateStr;
    }

    private String ThisWeekDate(){
        Calendar calendar = Calendar.getInstance();
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK , -7);
        String endDateStr = df.format(calendar1.getTime());
        return  startDateStr + "#" + endDateStr;
    }

    public String LastMonthdate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();
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
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
    }

    private void datePicker() {
        builder.setTitleText("Select date");
        binding.datetimefilter.setClickable(false);
        materialDatePicker.show(getChildFragmentManager(), "");

        materialDatePicker.addOnCancelListener(dialog -> binding.datetimefilter.setClickable(true));


        materialDatePicker.addOnDismissListener(dialog -> binding.datetimefilter.setClickable(true));


        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            binding.datetimefilter.setClickable(true);
            StartDate = getTimeStamp(selection.first) ;
            End_Date = getTimeStamp(selection.second);
            submitData();
        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date;
        date = df.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }

    private void StockList() {

        try {

            binding.progressbar.setVisibility(View.VISIBLE);
            String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
            String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

            lavaInterface.SELL_OUT_IMEI_LIST(USER_ID, StartDate, End_Date , country_id , country_name).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {

                    try {

                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String error_code = jsonObject.getString("error_code");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("false")){

                            JSONArray jsonArray = jsonObject.getJSONArray("list");

                            if(jsonArray.length() > 0){

                                lists.clear();
                                for (int i=0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String status = object.optString("status");

                                    if(TYPE.toUpperCase().equalsIgnoreCase(status.trim().toUpperCase())){
                                        lists.add(new List(
                                                object.optString("id")
                                                ,object.optString("type")
                                                ,object.optString("imei")
                                                ,object.optString("date")
                                                ,object.optString("retailer_id")
                                                ,object.optString("time")
                                                ,object.optString("product_id")
                                                ,object.optString("model")
                                                ,object.optString("check_status")
                                                ,object.optString("status")
                                                ,object.optString("price_drop_id")
                                                ,object.optString("price_drop_name")
                                                ,object.optString("qty")
                                                ,object.optString("price")
                                        ));
                                    }
                                }

                                if(lists.size() > 0){
                                    sellOutPendingVerificationAdapter = new SellOutPendingVerificationAdapter(lists);
                                    binding.ImeiRecyclerView.setAdapter(sellOutPendingVerificationAdapter);
                                    binding.ImeiRecyclerView.setVisibility(View.VISIBLE);
                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.noStock.setVisibility(View.GONE);
                                    return;
                                }

                            }

                            binding.ImeiRecyclerView.setVisibility(View.GONE);
                            binding.noStock.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        binding.noStock.setVisibility(View.VISIBLE);
                        binding.ImeiRecyclerView.setVisibility(View.GONE);
                        return;
                    }catch (NullPointerException e){
                        e.printStackTrace();
                        Log.e(TAG, "onResponse: " + e.getMessage() );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    if(binding != null){
                        binding.progressbar.setVisibility(View.GONE);
                        binding.noStock.setVisibility(View.VISIBLE);
                        binding.ImeiRecyclerView.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    binding.progressbar.setVisibility(View.GONE);
                    binding.noStock.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
                }
            });


        }catch (NullPointerException e){
            e.printStackTrace();
            Log.e(TAG, "StockList: " + e.getMessage() );
        }

    }




    @Override
    public void onStart() {
        super.onStart();
        TextView title = requireActivity().findViewById(R.id.Actiontitle);
        title.setText(getString(R.string.pending_verification));
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
                submitData();

                binding.PendingLayout.setEnabled(false);
                binding.PendingLayout.setClickable(false);

                binding.RejectedLayout.setClickable(true);
                binding.RejectedLayout.setEnabled(true);

                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.RejectedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
            case R.id.RejectedLayout:

                binding.PendingLayout.setEnabled(true);
                binding.PendingLayout.setClickable(true);

                binding.RejectedLayout.setClickable(false);
                binding.RejectedLayout.setEnabled(false);



                TYPE = "REJECTED";
                submitData();
                binding.RejectedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
        }

    }



}






































