package com.apptech.lava_retailer.ui.price_drop.reports;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
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
import com.apptech.lava_retailer.adapter.SellOutReportInnerAdapter;
import com.apptech.lava_retailer.adapter.SelloutReportAdapter;
import com.apptech.lava_retailer.databinding.ReportsFragmentBinding;
import com.apptech.lava_retailer.databinding.RowPriceDropReportBinding;
import com.apptech.lava_retailer.databinding.RowSellOutReportBinding;
import com.apptech.lava_retailer.list.announcelist.PriceDrop;
import com.apptech.lava_retailer.list.sell_out_report.SellOutReportList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomCategoryList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomModalList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.filter.PriceDropCategoryFilterFragment;
import com.apptech.lava_retailer.ui.filter.PriceDropModalFilterFragment;
import com.apptech.lava_retailer.ui.filter.SellOutReportCategoryFilter;
import com.apptech.lava_retailer.ui.filter.SellOutReportModalFilter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsFragment extends Fragment implements View.OnClickListener , PriceDropModalFilterFragment.PriceDropModalInterFace
        , PriceDropCategoryFilterFragment.PriceDropCategoryInterFace  , SellOutReportCategoryFilter.OnClickBackPress  , SellOutReportModalFilter.OnItemClickBackPress {

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

    SessionManage sessionManage;
    ProgressDialog progressDialog;

    SellOutReportCategoryFilter sellOutReportCategoryFilter;
    SellOutReportModalFilter sellOutReportModalFilter;
    List<SellOutReportList> pricedropMainlist = new ArrayList<>();
    List<SellOutReportList> PriceDropReportFilterLists = new ArrayList<>();
    JSONObject PriceDropReportCategoryObject = new JSONObject();
    JSONObject PriceDropReportModalObject = new JSONObject();
    List<SellOutCustomCategoryList> PriceDropCustomCategoryLists = new ArrayList<>();



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
        sessionManage = SessionManage.getInstance(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

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
            sellOutReportModalFilter = new SellOutReportModalFilter(this);
            sellOutReportModalFilter.show(getChildFragmentManager(), "modal bottom sheet");
        });

        binding.filterCategory.setOnClickListener(v -> {
            sellOutReportCategoryFilter = new SellOutReportCategoryFilter(this);
            sellOutReportCategoryFilter.show(getChildFragmentManager(), "category filter");
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
        getselloutReport();

//        binding.PriceDropReportRecyclerView.setAdapter(new PriceDropReportadapter());
//        binding.PriceDropReportRecyclerView.setAdapter(new PriceDropReportAdapter());


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
    public void CategoryResult(JSONObject object) {
        Log.e(TAG, "CategoryResult: " + object );
    }

    @Override
    public void ModalResult(JSONObject object) {
        Log.e(TAG, "CategoryResult: " + object );
    }


    private void getselloutReport() {




//        Log.e(TAG, "getselloutReport: " + StartDate);
//        Log.e(TAG, "getselloutReport: " + End_Date);

        binding.noData.setVisibility(View.GONE);
        progressDialog.show();

//            lavaInterface.SELLOUT_REPORT(ID, StartDate, End_Date).enqueue(new Callback<Object>() {
        lavaInterface.SELLOUT_REPORT("ff2326e2cf317a74fef52f267662a1ef", "01-01-2020", "01-01-2022").enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if (error.equalsIgnoreCase("FALSE")) {
                            JSONArray model_list = jsonObject.getJSONArray("fetch_list");
                            pricedropMainlist.clear();
                            for (int i = 0; i < model_list.length(); i++) {
                                JSONObject op = model_list.getJSONObject(i);

                                pricedropMainlist.add(new SellOutReportList(
                                        op.optString("id")
                                        ,op.optString("product_id")
                                        ,op.optString("distributor_id")
                                        ,op.optString("distributor_name")
                                        ,op.optString("imei")
                                        ,op.optString("retailer_id")
                                        ,op.optString("retailer_name")
                                        ,op.optString("country_id")
                                        ,op.optString("country_name")
                                        ,op.optString("marketing_name")
                                        ,op.optString("marketing_name_ar")
                                        ,op.optString("marketing_name_fr")
                                        ,op.optString("des_fr")
                                        ,op.optString("des")
                                        ,op.optString("des_ar")
                                        ,op.optString("actual_price")
                                        ,op.optString("dis_price")
                                        ,op.optString("thumb")
                                        ,op.optString("thumb_ar")
                                        ,op.optString("sku")
                                        ,op.optString("commodity_id")
                                        ,op.optString("format")
                                        ,op.optString("commodity")
                                        ,op.optString("commodity_ar")
                                        ,op.optString("brand_id")
                                        ,op.optString("brand")
                                        ,op.optString("brand_ar")
                                        ,op.optString("model")
                                        ,op.optString("model_ar")
                                        ,op.optString("category")
                                        ,op.optString("serialized")
                                        ,op.optString("video")
                                        ,op.optString("video_ar")
                                        ,op.optString("warranty_type")
                                        ,op.optString("prowar")
                                        ,op.optString("pro_war_days")
                                        ,op.optString("battery_war")
                                        ,op.optString("battery_war_days")
                                        ,op.optString("charging_adapter_war")
                                        ,op.optString("charging_adapter_war_days")
                                        ,op.optString("charger_war")
                                        ,op.optString("charger_war_days")
                                        ,op.optString("usb_war")
                                        ,op.optString("usb_war_days")
                                        ,op.optString("wired_earphone_war")
                                        ,op.optString("wired_earphone_war_days")
                                        ,op.optString("available_qty")
                                        ,op.optString("hide")
                                        ,op.optString("total_sale")
                                        ,op.optString("seller_id")
                                        ,op.optString("seller_name")
                                        ,op.optString("time")
                                        ,op.optString("sold_date")
                                        ,op.optString("warranty_start_date")
                                        ,op.optString("warranty_end_date")
                                        ,op.optString("locality_id")
                                        ,op.optString("locality")
                                        ,op.optString("imei2")
                                        ,op.optString("srno")
                                        ,op.optString("qty")
                                        ,op.optString("order_no")
                                        ,op.optString("price_drop")
                                        ,op.optString("sellout")
                                        ,op.optString("order_dispatch_date")
                                        ,op.optString("order_date")
                                ));
                            }
                            binding.tablayout.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            return;
                        }
                        binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                        binding.tablayout.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onResponse: " + e.getMessage() );
                    }
                    binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                    binding.tablayout.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.tablayout.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }


    @Override
    public void OnClickItem(JSONObject jsonObject) {
        Log.e(TAG, "OnClickItem: " + jsonObject );
        sellOutReportCategoryFilter.dismiss();
        PriceDropCategoryFilterObject(jsonObject);
    }

    private void PriceDropCategoryFilterObject(JSONObject CategoryObject) {
        if(pricedropMainlist.size() > 0){
                if (CategoryObject.length() > 0) {

                    PriceDropReportCategoryObject = new JSONObject();

                    Iterator iterator = CategoryObject.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        try {
                            JSONObject issue = CategoryObject.getJSONObject(key);
                            String Categoryname = issue.optString("name");

                            int Countqty = 0;
                            int CountValue = 0;
                            int Count = 0;

                            for (SellOutReportList sell : pricedropMainlist){

                                if(Categoryname.trim().toUpperCase().contains(sell.getCommodity().trim().toUpperCase())){

                                    PriceDropReportFilterLists.add(sell);
                                    JSONObject object = new JSONObject();
                                    object.put("commodity" , sell.getCommodity());
                                    object.put("commodity_ar" , sell.getCommodityAr());
                                    object.put("model" , sell.getModel());
                                    object.put("model_ar" , sell.getModelAr());
                                    object.put("qty" , sell.getQty());
                                    object.put("value" , sell.getDisPrice());
                                    object.put("count" , "1");
                                    PriceDropReportCategoryObject.putOpt(sell.getCommodity() , object);

                                    try {

                                        JSONObject a = PriceDropReportCategoryObject.getJSONObject(sell.getCommodity());
                                        int getcount = Integer.parseInt(a.optString("count"));
                                        int qty = Integer.parseInt(a.optString("qty"));
                                        int value = Integer.parseInt(a.optString("value"));

                                        Count = getcount + 1;
                                        Countqty = Countqty +  qty;
                                        CountValue = CountValue + value;

                                        a.put("value" , String.valueOf(CountValue));
                                        a.put("qty" , String.valueOf(Countqty));
                                        a.put("count" , String.valueOf(Count));

                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    if(PriceDropReportModalObject.length() > 0){
                        FilterMainObjec();
                    }
//                    Log.e(TAG, "PriceDropCategoryFilterObject: " + PriceDropReportCategoryObject .toString());
//                    Log.e(TAG, "PriceDropCategoryFilterObject: " + PriceDropReportCategoryObject .toString());

                }else {
                    binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                    binding.tablayout.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                }
            }else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.tablayout.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }

        progressDialog.dismiss();





    }


    @Override
    public void Onitem(JSONObject object) {
        sellOutReportModalFilter.dismiss();
        Log.e(TAG, "OnClickItem: " + object );
        PriceDropModalFilterObject(object);
    }

    private void PriceDropModalFilterObject(JSONObject modalObject) {

        if(pricedropMainlist.size() > 0){

            if (modalObject.length() > 0){

                PriceDropReportModalObject = new JSONObject();

                Iterator iterator = modalObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    try {
                        JSONObject issue = modalObject.getJSONObject(key);
                        String Modalname = issue.optString("name");
                        Log.e(TAG, "filterloop: " + Modalname);
                        for (SellOutReportList sell : PriceDropReportFilterLists){
                            if(Modalname.trim().toUpperCase().contains(sell.getModel().trim().toUpperCase())){

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put( "model" , sell.getModel());
                                jsonObject.put( "model_ar" , sell.getModelAr());
                                jsonObject.put( "qty" , "");
                                jsonObject.put( "value" , "");
                                jsonObject.put( "count" , "1");
                                PriceDropReportModalObject.put(sell.getModel() , jsonObject);

                                try {
                                    JSONObject a = PriceDropReportModalObject.getJSONObject(sell.getModel());
                                    int getcount = Integer.parseInt(a.getString("count"));
                                    int plus = getcount + 1;
                                    a.put("count" , String.valueOf(plus));
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }


                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if(PriceDropReportCategoryObject.length() > 0){
                    FilterMainObjec();
                }


            }else {
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.tablayout.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                PriceDropReportModalObject = new JSONObject();
            }
        }else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.tablayout.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }


        progressDialog.dismiss();
    }


    private void FilterMainObjec(){
        Log.e(TAG, "FilterMainObjec: " + PriceDropReportCategoryObject.toString() );
        Log.e(TAG, "FilterMainObjec: " + PriceDropReportModalObject.toString() );


        PriceDropCustomCategoryLists.clear();

        Iterator iterator = PriceDropReportCategoryObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();



            Iterator iterator1 = PriceDropReportModalObject.keys();
            List<SellOutCustomModalList> sellOutCustomModalLists = new ArrayList<>();

            sellOutCustomModalLists.add(new SellOutCustomModalList(
                    "Modal"
                    ,""
                    ,"Qty"
                    ,"Value"
                    ,""
            ));

            while (iterator1.hasNext()) {
                JSONObject issue = null;
                try {
                    String key1 = (String) iterator1.next();
                    issue = PriceDropReportModalObject.getJSONObject(key1);
                    String model1 = issue.getString("model");
                    JSONObject object = PriceDropReportModalObject.getJSONObject(model1);

                    String model = object.getString("model");

                    Log.e(TAG, "SetFilterDatModalaAdapter: " + model );
                    Log.e(TAG, "SetFilterDatModalaAdapter: " + model );

                    String model_ar = object.getString("model_ar");
                    String qty = object.getString("qty");
                    String value = object.getString("value");
                    String count = object.getString("count");


                    sellOutCustomModalLists.add(new SellOutCustomModalList(
                            model
                            ,model_ar
                            ,qty
                            ,value
                            ,count
                    ));

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            try {
                JSONObject issue = PriceDropReportCategoryObject.getJSONObject(key);
                String commodity = issue.getString("commodity");
                String commodity_ar = issue.getString("commodity_ar");
                String model = issue.getString("model");
                String model_ar = issue.getString("model_ar");
                String count = issue.getString("count");
                String value = issue.getString("value");
                String qty = issue.getString("qty");

//                PriceDropCustomCategoryLists.add(new SellOutCustomCategoryList(
//                        commodity
//                        ,commodity_ar
//                        ,model
//                        ,model_ar
//                        ,qty
//                        ,value
//                        ,count
//                        ,sellOutCustomModalLists
//                ));

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }



        }


        Log.e(TAG, "FilterMainObjec: " + PriceDropCustomCategoryLists );
        Log.e(TAG, "FilterMainObjec: " + PriceDropCustomCategoryLists );

        if(PriceDropCustomCategoryLists.size() > 0){


            binding.ReportselloutRecyclerView.setAdapter(new PriceDropReportAdapter(PriceDropCustomCategoryLists));
            binding.ReportselloutRecyclerView.setVisibility(View.VISIBLE);
            binding.tablayout.setVisibility(View.VISIBLE);
            binding.tablayout.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);


        }else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.tablayout.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();


    }


    private class PriceDropReportAdapter extends RecyclerView.Adapter<ReportsFragment.Viewholder>{

        List<SellOutCustomCategoryList> sellOutCustomCategoryLists;

        public PriceDropReportAdapter(List<SellOutCustomCategoryList> priceDropCustomCategoryLists) {
            this.sellOutCustomCategoryLists = priceDropCustomCategoryLists;
        }

        @NonNull
        @Override
        public ReportsFragment.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Viewholder(RowSellOutReportBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
        }

        @Override
        public void onBindViewHolder(@NonNull ReportsFragment.Viewholder holder, int position) {

            SellOutCustomCategoryList l = sellOutCustomCategoryLists.get(position);
            switch (sessionManage.getUserDetails().get("LANGUAGE")){
                case "en":
                    holder.binding.commodityName.setText(l.getCommodity());
                    break;
                case "ar":
                    holder.binding.commodityName.setText(l.getCommodity_ar());
                    break;
            }

            holder.binding.qty.setText(l.getQty());
            holder.binding.value.setText(l.getValue());

            holder.binding.InnerRecyclerView.setAdapter(new SellOutReportInnerAdapter(l.getSellOutCustomModalLists()));

        }

        @Override
        public int getItemCount() {
            return sellOutCustomCategoryLists.size();
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutReportBinding binding;

        public Viewholder(@NonNull RowSellOutReportBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Report");
    }





}







































