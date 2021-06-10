package com.apptech.lava_retailer.ui.warranty.pending_request_accesories;

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
import com.apptech.lava_retailer.adapter.WarrantyPendingReplacementAdapter;
import com.apptech.lava_retailer.databinding.PendingRequestAccesoriesFragmentBinding;
import com.apptech.lava_retailer.list.pending_warranty.List;
import com.apptech.lava_retailer.list.pending_warranty.PendingWarrentyList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingRequestAccesoriesFragment extends Fragment implements View.OnClickListener {

    private PendingRequestAccesoriesViewModel mViewModel;
    PendingRequestAccesoriesFragmentBinding binding;
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
    private java.util.List<List> list = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static PendingRequestAccesoriesFragment newInstance() {
        return new PendingRequestAccesoriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PendingRequestAccesoriesFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PendingRequestAccesoriesViewModel.class);
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
        binding.DatpickerRange.setOnClickListener( v -> {
            mypopupWindow.showAsDropDown(v,-153,0);
        });


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

        progressDialog.show();

        Log.e(TAG, "getPendinfReplacement: " + StartDate );
        Log.e(TAG, "getPendinfReplacement: " + End_Date );
        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        lavaInterface.WARRANTY_PENDING(USER_ID, StartDate, End_Date , country_id , country_name).enqueue(new Callback<PendingWarrentyList>() {
            @Override
            public void onResponse(Call<PendingWarrentyList> call, Response<PendingWarrentyList> response) {


                if(response.isSuccessful()){

                    if (!response.body().getError()){

                        if (response.body().getList().size() > 0){
                            list.clear();
                            for (int i=0 ; i< response.body().getList().size(); i++){

                                Log.e(TAG, "onResponse: " +TYPE );
                                Log.e(TAG, "onResponse: " +response.body().getList().get(i).getStatus() );

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
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        String endDateStr = df.format(calendar1.getTime());
        return  startDateStr + "#" + endDateStr;
    }


    private String ThisWeekDate(){
        Calendar calendar = Calendar.getInstance();
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
        date = df.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Pending_replace_accesories));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DeliveredLayout:
                binding.DeliveredLayout.setEnabled(false);
                binding.DeliveredLayout.setClickable(false);

                binding.ProcessingLayout.setEnabled(true);
                binding.ProcessingLayout.setClickable(true);

                binding.CancelledLayout.setEnabled(true);
                binding.CancelledLayout.setClickable(true);


                TYPE = "APPROVED";
                getPendinfReplacement();
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
            case R.id.ProcessingLayout:

                binding.DeliveredLayout.setEnabled(true);
                binding.DeliveredLayout.setClickable(true);

                binding.ProcessingLayout.setEnabled(false);
                binding.ProcessingLayout.setClickable(false);

                binding.CancelledLayout.setEnabled(true);
                binding.CancelledLayout.setClickable(true);

                TYPE = "PENDING";
                getPendinfReplacement();
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));

                break;
            case R.id.CancelledLayout:


                binding.DeliveredLayout.setEnabled(true);
                binding.DeliveredLayout.setClickable(true);

                binding.ProcessingLayout.setEnabled(true);
                binding.ProcessingLayout.setClickable(true);

                binding.CancelledLayout.setEnabled(false);
                binding.CancelledLayout.setClickable(false);
                TYPE = "CANCELLED";
                getPendinfReplacement();
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
        }
    }








}
































