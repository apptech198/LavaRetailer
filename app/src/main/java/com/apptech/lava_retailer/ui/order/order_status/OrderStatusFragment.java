package com.apptech.lava_retailer.ui.order.order_status;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.OrderStatusAdapter;
import com.apptech.lava_retailer.databinding.OrderStatusFragmentBinding;
import com.apptech.lava_retailer.modal.order_statusList.OrderStatusList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusFragment extends Fragment implements View.OnClickListener {

    private OrderStatusViewModel mViewModel;
    OrderStatusFragmentBinding binding;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String StartDate ="" , End_Date = "" , TYPE = "";
    private static final String TAG = "OrderStatusFragment";
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    PopupWindow mypopupWindow;
    OrderStatusAdapter orderStatusAdapter;
    List<com.apptech.lava_retailer.list.OrderStatusList> orderStatusLists = new ArrayList<>();

    public static OrderStatusFragment newInstance() {
        return new OrderStatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = OrderStatusFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderStatusViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());


        String[] date = TodayDate().split("#");
        StartDate = date[0];
        End_Date = date[1];
        TYPE = "PENDING";

        if (new NetworkCheck().haveNetworkConnection(getActivity())){
            getOrderStatus(StartDate , End_Date , TYPE);
        }else {
            binding.progressbar.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
            binding.OrderStatusRecyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }


        binding.DeliveredLayout.setOnClickListener(this);
        binding.ProcessingLayout.setOnClickListener(this);
        binding.CancelledLayout.setOnClickListener(this);

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v ->  {
            mypopupWindow.showAsDropDown(v,-153,0);
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
            getOrderStatus(StartDate , End_Date , TYPE);
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            getOrderStatus(StartDate , End_Date , TYPE);
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[1];
            End_Date = thisMonth[0];
            getOrderStatus(StartDate , End_Date , TYPE);
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

    }


    private String TodayDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        String endDateStr = df.format(calendar1.getTime());
        return  startDateStr + "#" + endDateStr;
    }



    private String ThisWeekDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
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
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
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
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
    }


    private void DateFilter() {
        PopupMenu popup = new PopupMenu(getContext(), binding.DatpickerRange);
        popup.getMenuInflater().inflate(R.menu.date_select, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            Toast.makeText(getContext(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
        popup.show();//showing popup menu

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
            StartDate = getTimeStamp(selection.second) ;
            End_Date = getTimeStamp(selection.first);
            getOrderStatus(StartDate , End_Date , TYPE);
        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }



    private void getOrderStatus(String strDate , String endDate , String type){

//        Toast.makeText(getContext(), "" + strDate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "" + endDate, Toast.LENGTH_SHORT).show();

        binding.progressbar.setVisibility(View.VISIBLE);

        String RetId = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);
        Log.e(TAG, "getOrderStatus: " + RetId );

        lavaInterface.ORDER_STATUS_LIST_CALL(RetId ,endDate , strDate).enqueue(new Callback<OrderStatusList>() {
            @Override
            public void onResponse(Call<OrderStatusList> call, Response<OrderStatusList> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if(response.isSuccessful()){
                    if(!response.body().getError()){
                        orderStatusLists.clear();
                        for (int i=0 ; i < response.body().getList().size() ; i++){
                            com.apptech.lava_retailer.modal.order_statusList.List l = response.body().getList().get(i);
                            if(type.toUpperCase().trim().equalsIgnoreCase(l.getstatus().toUpperCase().trim())){
                                orderStatusLists.add(new com.apptech.lava_retailer.list.OrderStatusList(
                                        l.getId()
                                        ,l.getProductId()
                                        ,l.getModelName()
                                        ,l.getProductName()
                                        ,l.getProductIvtId()
                                        ,l.getDisId()
                                        ,l.getRetId()
                                        ,l.getDisName()
                                        ,l.getAddress()
                                        ,l.getRetName()
                                        ,l.getRetMobile()
                                        ,l.getTime()
                                        ,l.getQty()
                                        ,l.getDiscountPrice()
                                        ,l.getActualPrice()
                                        ,l.getOrderNo()
                                        ,l.getPretotal()
                                        ,l.getDiscount()
                                        ,l.getOrderTotal()
                                        ,l.getItemTotal()
                                        ,l.getImei()
                                        ,l.getpType()
                                        ,l.getstatus()
                                ));
                            }
                        }

                        if(orderStatusLists.size() > 0){

                            Log.e(TAG, "onResponse: " + orderStatusLists.size());

                            orderStatusAdapter = new OrderStatusAdapter(orderStatusLists, type);
                            binding.OrderStatusRecyclerView.setAdapter(orderStatusAdapter);
                            orderStatusAdapter.notifyDataSetChanged();
                            binding.noData.setVisibility(View.GONE);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.OrderStatusRecyclerView.setVisibility(View.VISIBLE);

                        }else {
                            binding.progressbar.setVisibility(View.GONE);
                            binding.noData.setVisibility(View.VISIBLE);
                            binding.OrderStatusRecyclerView.setVisibility(View.GONE);
                        }
                        return;
                    }
                    binding.noData.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                    binding.OrderStatusRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.noData.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                binding.OrderStatusRecyclerView.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<OrderStatusList> call, Throwable t) {
                binding.noData.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getResources().getString(R.string.My_Orders));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DeliveredLayout:
                binding.DeliveredLayout.setClickable(false);
                TYPE = "DELIVERED";
                getOrderStatus(StartDate , End_Date , TYPE);
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.DeliveredLayout.setClickable(true);
                break;
            case R.id.ProcessingLayout:
                TYPE = "PENDING";
                getOrderStatus(StartDate , End_Date , TYPE);
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));

                break;
            case R.id.CancelledLayout:
                TYPE = "CANCEL";
                getOrderStatus(StartDate , End_Date , TYPE);
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.DeliveredLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.ProcessingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
        }
    }



}









































