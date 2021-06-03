package com.apptech.lava_retailer.ui.message_centre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.activity.ClientDatashowActivity;
import com.apptech.lava_retailer.activity.LoginActivity;
import com.apptech.lava_retailer.adapter.ExpansisAdapter;
import com.apptech.lava_retailer.adapter.MessageAdapter;
import com.apptech.lava_retailer.databinding.MessageCentreFragmentBinding;
import com.apptech.lava_retailer.modal.message.MessageList;
import com.apptech.lava_retailer.modal.message.NotificationListBrandWise;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageCentreFragment extends Fragment  {

    private MessageCentreViewModel mViewModel;
    MessageCentreFragmentBinding binding;
    List<MessageList> messageLists = new ArrayList<>();
    private static final String TAG = "MessageCentreFragment";
    MessageAdapter messageAdapter;
    SessionManage sessionManage;
    LavaInterface lavaInterface;
    PopupWindow mypopupWindow;
    String StartDate ="" , End_Date = "" , TYPE = "";
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    String Country_id = "";

    public static MessageCentreFragment newInstance() {
        return new MessageCentreFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Message Centre");

        binding = MessageCentreFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MessageCentreViewModel.class);
        // TODO: Use the ViewModel




        sessionManage = SessionManage.getInstance(requireContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);


        if(sessionManage.getUserDetails().get("LOGIN_COUNTRY_ID") != null){
            Country_id =  sessionManage.getUserDetails().get("LOGIN_COUNTRY_ID");
        }

        String[] date = TodayDate().split("#");
        StartDate = date[0];
        End_Date = date[1];

        if(new NetworkCheck().haveNetworkConnection(requireActivity())){
            MessageCenter();

            if (sessionManage.getUserDetails().get("PROFILE_VERIFY_CHECK").equalsIgnoreCase("NO")) {
                CheckProfileverify();
            }
        }else {
            binding.progressbar.setVisibility(View.GONE);
            binding.noDatafound.setVisibility(View.VISIBLE);
            Toast.makeText(requireContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }



        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select date");

        setPopUpWindow();
        binding.datepicker.setOnClickListener(v -> {
            mypopupWindow.showAsDropDown(v,-153,0);
        });

        binding.msgCount.setText(String.valueOf(messageLists.size()));


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
            StartDate = last_7[1];
            End_Date = last_7[0];
            MessageCenter();
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            MessageCenter();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[0];
            End_Date = thisMonth[1];
            MessageCenter();
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

    private void datePicker() {
        builder.setTitleText("Select date");
        binding.datepicker.setClickable(false);
        materialDatePicker.show(getChildFragmentManager(), "");

        materialDatePicker.addOnCancelListener(dialog -> {
            binding.datepicker.setClickable(true);
        });


        materialDatePicker.addOnDismissListener(dialog -> {
            binding.datepicker.setClickable(true);
        });


        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Log.e(TAG, "datePicker: " + selection.first );
            Log.e(TAG, "datePicker: " + selection.second );
            binding.datepicker.setClickable(true);
            StartDate = getTimeStamp(selection.first) ;
            End_Date = getTimeStamp(selection.second);
            MessageCenter();
        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
    }



    private void MessageCenter() {


        binding.progressbar.setVisibility(View.VISIBLE);

        Log.e(TAG, "MessageCenter: " + Country_id);

        lavaInterface.NotificationListBrandWise(sessionManage.getUserDetails().get("BRAND_ID") , Country_id ,StartDate ,End_Date).enqueue(new Callback<NotificationListBrandWise>() {
            @Override
            public void onResponse(Call<NotificationListBrandWise> call, Response<NotificationListBrandWise> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    if (!response.body().getError()) {

                        try {

                            if (response.body().getList().size() > 0) {
                                binding.messageRecyclerView.setAdapter(new ExpansisAdapter(response.body().getList()));
                                binding.msgCount.setText(String.valueOf(response.body().getList().size()));
                                binding.messageRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                binding.noDatafound.setVisibility(View.VISIBLE);
                                binding.countLayout.setVisibility(View.GONE);
                                binding.messageRecyclerView.setVisibility(View.GONE);
                            }
                            binding.progressbar.setVisibility(View.GONE);
                            return;

                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage() );
                        }
                        try {
                            binding.noDatafound.setVisibility(View.VISIBLE);
                            binding.countLayout.setVisibility(View.GONE);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.messageRecyclerView.setVisibility(View.GONE);
                        }catch (NullPointerException e){

                        }
                        return;
                    }
                    binding.noDatafound.setVisibility(View.VISIBLE);
                    Toast.makeText(requireContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.countLayout.setVisibility(View.GONE);
                    binding.messageRecyclerView.setVisibility(View.GONE);
                    return;
                }
                binding.noDatafound.setVisibility(View.VISIBLE);
                binding.progressbar.setVisibility(View.GONE);
                binding.countLayout.setVisibility(View.GONE);
                binding.messageRecyclerView.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<NotificationListBrandWise> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.noDatafound.setVisibility(View.VISIBLE);
                binding.countLayout.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Time out", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.message_center));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    private void CheckProfileverify(){

        lavaInterface.PROFILE_DETAILS(sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID)).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                try {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.toString()) );

                        String error = jsonObject.optString("error");
                        String message = jsonObject.optString("message");
                        if (error.equalsIgnoreCase("false")) {

                            JSONObject jsonObject1 = jsonObject.getJSONObject("user_detail");

                            String backend_register = jsonObject1.getString("backend_register");
                            String backend_verify = jsonObject1.getString("backend_verify");

                            if(
                                    backend_register.equalsIgnoreCase("YES") && backend_verify.equalsIgnoreCase("YES")
                                    || backend_register.equalsIgnoreCase("NO") && backend_verify.equalsIgnoreCase("YES")
                            ){
                                sessionManage.PROFILE_VERIFY_CHECK(backend_verify);
                                return;
                            }

                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);

                }catch (NullPointerException e){
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                    binding.progressbar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });
    }









}




























