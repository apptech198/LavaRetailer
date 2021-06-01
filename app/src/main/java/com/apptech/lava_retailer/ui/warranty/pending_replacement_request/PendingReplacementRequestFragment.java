package com.apptech.lava_retailer.ui.warranty.pending_replacement_request;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutPendingVerificationAdapter;
import com.apptech.lava_retailer.adapter.WarrantyPendingReplacementAdapter;
import com.apptech.lava_retailer.databinding.PendingReplacementRequestFragmentBinding;
import com.apptech.lava_retailer.list.pending_warranty.List;
import com.apptech.lava_retailer.list.pending_warranty.PendingWarrentyList;
import com.apptech.lava_retailer.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingReplacementRequestFragment extends Fragment implements View.OnClickListener{

    private PendingReplacementRequestViewModel mViewModel;
    PendingReplacementRequestFragmentBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String StartDate ="" , End_Date = "" , TYPE = "";
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    PopupWindow mypopupWindow;
    private static final String TAG = "PendingReplacementReque";
    String USER_ID = "";
    private ProgressDialog progressDialog;
    WarrantyPendingReplacementAdapter warrantyPendingReplacementAdapter;
    private java.util.List<List> list;

    public static PendingReplacementRequestFragment newInstance() {
        return new PendingReplacementRequestFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PendingReplacementRequestFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PendingReplacementRequestViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());

        USER_ID = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        String[] date = TodayDate().split("#");
        StartDate = date[0];
        End_Date = date[1];
        TYPE = "PENDING";

        binding.DeliveredLayout.setOnClickListener(this);
        binding.ProcessingLayout.setOnClickListener(this);
        binding.CancelledLayout.setOnClickListener(this);


        setPopUpWindow();
        if (new NetworkCheck().haveNetworkConnection(getActivity())){
            getPendinfReplacement();
        }else {
            progressDialog.cancel();
            binding.noData.setVisibility(View.VISIBLE);
            binding.PendingReplacementRecyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void getPendinfReplacement() {
        lavaInterface.WARRANTY_PENDING(USER_ID, StartDate, End_Date).enqueue(new Callback<PendingWarrentyList>() {
            @Override
            public void onResponse(Call<PendingWarrentyList> call, Response<PendingWarrentyList> response) {


                if(response.isSuccessful()){

                    if (!response.body().getError()){

                        if (response.body().getList().size() > 0){

                            list = response.body().getList();
                            for (int i=0 ; i< response.body().getList().size(); i++){
                                if (TYPE.equalsIgnoreCase(response.body().getList().get(i).getStatus())){
                                    list.add(response.body().getList().get(i));
                                }
                            }

                            if(list.size() > 0){
                                warrantyPendingReplacementAdapter = new WarrantyPendingReplacementAdapter(list);
                                binding.PendingReplacementRecyclerView.setAdapter(warrantyPendingReplacementAdapter);
                                binding.PendingReplacementRecyclerView.setVisibility(View.VISIBLE);
                                binding.noData.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                return;
                            }
                        }
                        binding.PendingReplacementRecyclerView.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        return;
                    }
                    binding.PendingReplacementRecyclerView.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.PendingReplacementRecyclerView.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<PendingWarrentyList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressDialog.cancel();
                binding.noData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Time out" , Toast.LENGTH_SHORT).show();

            }
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
            getPendinfReplacement();
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            getPendinfReplacement();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[0];
            End_Date = thisMonth[1];
            getPendinfReplacement();
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
        binding.DatpickerRange.setClickable(false);
        materialDatePicker.show(getChildFragmentManager(), "");

        materialDatePicker.addOnCancelListener(dialog -> {
            binding.DatpickerRange.setClickable(true);
        });


        materialDatePicker.addOnDismissListener(dialog -> {
            binding.DatpickerRange.setClickable(true);
        });


        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Log.e(TAG, "datePicker: " + selection.first );
            Log.e(TAG, "datePicker: " + selection.second );
            binding.DatpickerRange.setClickable(true);
            StartDate = getTimeStamp(selection.first) ;
            End_Date = getTimeStamp(selection.second);
            getPendinfReplacement();
        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DeliveredLayout:
                binding.DeliveredLayout.setClickable(false);
                TYPE = "APPROVED";
                getPendinfReplacement();
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.DeliveredLayout.setClickable(true);
                break;
            case R.id.ProcessingLayout:
                TYPE = "PENDING";
                getPendinfReplacement();
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));

                break;
            case R.id.CancelledLayout:
                TYPE = "CANCELLED";
                getPendinfReplacement();
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
        }
    }
}









































