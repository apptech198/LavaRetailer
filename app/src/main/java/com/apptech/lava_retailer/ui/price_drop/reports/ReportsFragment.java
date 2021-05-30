package com.apptech.lava_retailer.ui.price_drop.reports;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.PriceDropReportadapter;
import com.apptech.lava_retailer.databinding.ReportsFragmentBinding;
import com.apptech.lava_retailer.databinding.RowPriceDropReportBinding;
import com.apptech.lava_retailer.list.announcelist.PriceDrop;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.filter.PriceDropCategoryFilterFragment;
import com.apptech.lava_retailer.ui.filter.PriceDropModalFilterFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
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

public class ReportsFragment extends Fragment implements View.OnClickListener {

    private ReportsViewModel mViewModel;
    ReportsFragmentBinding binding;
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    PopupWindow mypopupWindow;
    String StartDate ="" , End_Date = "" , TYPE = "";
    LavaInterface lavaInterface;
    private static final String TAG = "ReportsFragment";
    List<PriceDrop> announcelist= new ArrayList<>();
    PriceDrop selectAnnounce;
    String announce_start_date ="" , announce_end_date ="" ,  announce_drop_amount ="" ,   announce_active ="";
    String QTYSelect = "" , VALUESelect = "";
    PriceDropCategoryFilterFragment proceDropCategoryFilterFragment;
    PriceDropModalFilterFragment proceDropModalFilterFragment;

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ReportsFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

        String[] TodayDate = TodayDate().split("#");
        StartDate = TodayDate[0];
        End_Date = TodayDate[1];

        getAnnounceList();
        GerReport();

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v -> mypopupWindow.showAsDropDown(v,-153,0));


        binding.PendingLayout.setOnClickListener(this);
        binding.ApprovedLayout.setOnClickListener(this);
        binding.CancelledLayout.setOnClickListener(this);


        binding.FilterQtyLayout.setOnClickListener(v -> {
            if (binding.QtyCheckbox.isChecked()){
                binding.QtyCheckbox.setChecked(false);
                QTYSelect = "";
                return;
            }
            binding.QtyCheckbox.setChecked(true);
            QTYSelect = "YES";




        });


        binding.FilterValueLayout.setOnClickListener(v -> {
            if (binding.ValueCheckbox.isChecked()){
                VALUESelect = "";
                binding.ValueCheckbox.setChecked(false);
                return;
            }
            binding.ValueCheckbox.setChecked(true);
            VALUESelect = "YES";
        });




        binding.filterModel.setOnClickListener(v -> {
            proceDropModalFilterFragment = new PriceDropModalFilterFragment();
            proceDropModalFilterFragment.show(getChildFragmentManager(), "category filter");
        });

        binding.filterCategory.setOnClickListener(v -> {
            proceDropCategoryFilterFragment = new PriceDropCategoryFilterFragment();
            proceDropCategoryFilterFragment.show(getChildFragmentManager(), "modal bottom sheet");
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
            GerReport();
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            GerReport();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[1];
            End_Date = thisMonth[0];
            GerReport();
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();

        });

    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); // modify format
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);
        return date;
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
            GerReport();
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
        String endDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK , -7);
        String startDateStr = df.format(calendar1.getTime());
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
        String startDateStr = df.format(monthLastDay);
        String endDateStr = df.format(monthFirstDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
    }

    private void GerReport(){

        Log.e(TAG, "GerReport start date: " + StartDate );
        Log.e(TAG, "GerReport end date: " + End_Date );
        binding.noData.setVisibility(View.GONE);
        binding.progressbar.setVisibility(View.GONE);
//
//        binding.PriceDropReportRecyclerView.setAdapter(new PriceDropReportadapter());

        binding.PriceDropReportRecyclerView.setAdapter(new PriceDropReportAdapter());


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.PendingLayout:
                TYPE = "DELIVERED";
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
            case R.id.ApprovedLayout:
                TYPE = "PENDING";
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));

                break;
            case R.id.CancelledLayout:
                TYPE = "CANCEL";
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status , null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status , null));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    void getAnnounceList(){
        lavaInterface.GetAnnounceList().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        announcelist.clear();
                        if(error.equalsIgnoreCase("FALSE")){

                            JSONArray elements = jsonObject.optJSONArray("price_drop_list");

                            for (int i=0; i<elements.length(); i++){
                                JSONObject object= elements.optJSONObject(i);
                                announcelist.add(new PriceDrop(
                                        object.optString("id")
                                        ,object.optString("drop_amount")
                                        ,object.optString("name")
                                        ,object.optString("start_date")
                                        ,object.optString("end_date")
                                        ,object.optString("name_ar")
                                        ,object.optString("name_fr")
                                        ,object.optString("time")
                                        ,object.optString("active")
                                        ,getContext()
                                ));
                            }
                            if(announcelist.isEmpty()){

                            }else {
                                ArrayAdapter<PriceDrop> arrayAdapter = new ArrayAdapter<PriceDrop>(getContext(),
                                        android.R.layout.simple_list_item_1, announcelist);
                                binding.announce.setAdapter(arrayAdapter);
                                binding.announce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                         selectAnnounce.clear();
//                                         selectAnnounce = Collections.singletonList(announcelist.get(position));
                                        announce_start_date = announcelist.get(position).getStartDate();
                                        announce_end_date = announcelist.get(position).getEndDate();
                                        announce_drop_amount = announcelist.get(position).getDrop_amount();
                                        announce_active = announcelist.get(position).getActive();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                            }
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }
                        binding.progressbar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    private class PriceDropReportAdapter extends RecyclerView.Adapter<ReportsFragment.Viewholder>{

        @NonNull
        @Override
        public ReportsFragment.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Viewholder(RowPriceDropReportBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
        }

        @Override
        public void onBindViewHolder(@NonNull ReportsFragment.Viewholder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull RowPriceDropReportBinding itemView) {
            super(itemView.getRoot());

        }
    }
}







































