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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutReportInnerAdapter;
import com.apptech.lava_retailer.databinding.ReportsFragmentBinding;
import com.apptech.lava_retailer.databinding.RowSellOutReportBinding;
import com.apptech.lava_retailer.list.announcelist.PriceDrop;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.list.modelList.ModelList;
import com.apptech.lava_retailer.list.price_drop_report.PriceDropReportList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomCategoryList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomModalList;
import com.apptech.lava_retailer.other.NetworkCheck;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
    String ID = "";
    SellOutReportCategoryFilter sellOutReportCategoryFilter;
    SellOutReportModalFilter sellOutReportModalFilter;
    List<PriceDropReportList> pricedropMainlist = new ArrayList<>();
    List<PriceDropReportList> PriceDropReportFilterLists = new ArrayList<>();
    JSONObject PriceDropReportCategoryObject = new JSONObject();
    JSONObject PriceDropReportReturnCategoryObject = new JSONObject();
    JSONObject PriceDropReportModalObject = new JSONObject();
    JSONObject PriceDropReportReturnModalObject = new JSONObject();

    List<SellOutCustomCategoryList> PriceDropCustomCategoryLists = new ArrayList<>();

    List<ModelList> modalList = new ArrayList<>();
    List<ComodityLists> categoryLists =new ArrayList<>();

    JSONObject CategoryjJsonobject = new JSONObject();


    int Grandtotal_Qty = 0;
    int Grandtotal_Value = 0;
    NavController navController;

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

        ID = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);

        String[] TodayDate = TodayDate().split("#");
        StartDate = TodayDate[0];
        End_Date = TodayDate[1];

        if(new NetworkCheck().haveNetworkConnection(requireActivity())){
            getAnnounceList();
            GerReport();
        }else {
            CheckInternetAleart();
        }

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v -> mypopupWindow.showAsDropDown(v,-153,0));


        binding.PendingLayout.setOnClickListener(this);
        binding.ApprovedLayout.setOnClickListener(this);
        binding.CancelledLayout.setOnClickListener(this);
        binding.QtyCheckbox.setChecked(true);
        binding.ValueCheckbox.setChecked(true);

/*

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


*/



        binding.filterModel.setOnClickListener(v -> {
            sellOutReportModalFilter = new SellOutReportModalFilter(this , modalList , PriceDropReportReturnModalObject);
            sellOutReportModalFilter.show(getChildFragmentManager(), "modal bottom sheet");
        });

        binding.filterCategory.setOnClickListener(v -> {
            sellOutReportCategoryFilter = new SellOutReportCategoryFilter(this , categoryLists , PriceDropReportReturnCategoryObject);
            sellOutReportCategoryFilter.show(getChildFragmentManager(), "category filter");
        });




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup, null);
        TextView last_7_day = view.findViewById(R.id.last_7_day);
        TextView this_month = view.findViewById(R.id.this_month);
        TextView last_month = view.findViewById(R.id.last_month);
        TextView CustomDate = view.findViewById(R.id.CustomDate);
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

        if(new NetworkCheck().haveNetworkConnection(getActivity())){
            getModel();
            getPriceDropReport();
        }else {
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }


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

        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        lavaInterface.GetAnnounceList(country_id , country_name).enqueue(new Callback<Object>() {
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


    private void getModel() {


        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        progressDialog.show();
        lavaInterface.SELL_OUT_CATEGORY_MODAL_FILTER(country_id , country_name).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.isSuccessful()){
                    try {
                        JSONObject jsonObject  = new JSONObject(new Gson().toJson(response.body()));
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){
                            JSONArray model_list = jsonObject.getJSONArray("model_list");
                            modalList.clear();
                            categoryLists.clear();

                            for (int i=0; i<model_list.length(); i++){
                                JSONObject op = model_list.getJSONObject(i);
                                modalList.add(new ModelList(op.optString("model")));
                            }


                            JSONArray comodity_list = jsonObject.getJSONArray("comodity_list");
                            for (int i=0; i<comodity_list.length(); i++){
                                JSONObject op = comodity_list.getJSONObject(i);
                                categoryLists.add(new ComodityLists(
                                        op.optString("id")
                                        ,op.optString("name")
                                        ,op.optString("name_ar")
                                        ,op.optString("name_fr")
                                        ,op.optString("brand_id")
                                        ,op.optString("brand_name")
                                        ,op.optString("form_type")
                                        ,op.optString("time")
                                ));
                            }


                            progressDialog.dismiss();
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }


    private void getPriceDropReport() {

        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        binding.noData.setVisibility(View.GONE);
        progressDialog.show();

        lavaInterface.PRICE_DROP_REPORT(ID, StartDate, End_Date ,"","" , country_id , country_name).enqueue(new Callback<Object>() {
//        lavaInterface.PRICE_DROP_REPORT("0216d15a0c80fe463b30ea94fa492f89", "2019-01-01", "2023-12-01" ,"","" ).enqueue(new Callback<Object>() {

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

                                String Qtyset = "0";
                                if (op.optString("qty").isEmpty()){
                                    Qtyset = "0";
                                }else {
                                    Qtyset =  op.optString("qty");
                                }

                                pricedropMainlist.add(new PriceDropReportList(
                                        op.optString("id")
                                        ,op.optString("type")
                                        ,op.optString("imei")
                                        ,op.optString("date")
                                        ,op.optString("retailer_id")
                                        ,op.optString("retailer_name")
                                        ,op.optString("time")
                                        ,op.optString("product_id")
                                        ,op.optString("model")
                                        ,op.optString("commodity_id")
                                        ,op.optString("commodity")
                                        ,op.optString("check_status")
                                        ,op.optString("status")
                                        ,op.optString("price_drop_id")
                                        ,op.optString("price_drop_name")
                                        ,Qtyset
                                        ,op.optString("price")
                                        ,op.optString("drop_amount")
                                        ,op.optString("locality_id")
                                        ,op.optString("locality_name")
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

    void CheckInternetAleart(){

        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())

                .setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("No Internet")

                .setMessage("Please Check Your Internet Connection!")

                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    navController.popBackStack();
                    navController.navigate(R.id.reportsFragment);
                })
                .show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

    }



    @Override
    public void OnClickItem(JSONObject jsonObject) {
        Log.e(TAG, "OnClickItem: " + jsonObject );
        sellOutReportCategoryFilter.dismiss();
        try {
            PriceDropReportReturnCategoryObject = new JSONObject(String.valueOf(jsonObject));

            PriceDropReportReturnCategoryObject = new JSONObject();
            sellOutReportCategoryFilter = new SellOutReportCategoryFilter(this , categoryLists , PriceDropReportReturnCategoryObject);
            sellOutReportCategoryFilter.show(getChildFragmentManager(), "category filter");

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        PriceDropCategoryFilterObject(jsonObject);
    }

    private void PriceDropCategoryFilterObject(JSONObject CategoryObject) {
        if(pricedropMainlist.size() > 0){
                if (CategoryObject.length() > 0) {

                    PriceDropReportCategoryObject = new JSONObject();
                    PriceDropReportFilterLists.clear();

                    Iterator iterator = CategoryObject.keys();
                    while (iterator.hasNext()) {
                        String key = (String) iterator.next();
                        try {
                            JSONObject issue = CategoryObject.getJSONObject(key);
                            String Categoryname = issue.optString("name");


                            for (PriceDropReportList sell : pricedropMainlist){

                                if(Categoryname.trim().toUpperCase().contains(sell.getCommodity().trim().toUpperCase())){

                                    PriceDropReportFilterLists.add(sell);
                                    JSONObject object = new JSONObject();
                                    object.put("commodity" , sell.getCommodity());
                                    object.put("commodity_ar" , "");
                                    object.put("model" , sell.getModel());
                                    object.put("model_ar" , "");
                                    object.put("qty" , sell.getQty());
                                    object.put("value" , sell.getDrop_amount());
                                    object.put("count" , "1");
                                    PriceDropReportCategoryObject.putOpt(sell.getCommodity() , object);

                                }else {
                                    binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                                    binding.tablayout.setVisibility(View.GONE);
                                    binding.noData.setVisibility(View.VISIBLE);
                                }

                                CategoryjJsonobject = new JSONObject(PriceDropReportCategoryObject.toString());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    Log.e(TAG, "PriceDropCategoryFilterObject: " + PriceDropReportCategoryObject.toString() );


                    if(PriceDropReportModalObject.length() > 0){
                        FilterMainObjec();
                    }

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
        try {
            PriceDropReportReturnModalObject = new JSONObject(String.valueOf(object));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        PriceDropModalFilterObject(object);
    }

    private void PriceDropModalFilterObject(JSONObject modalObject) {

        if(pricedropMainlist.size() > 0){

            if (modalObject.length() > 0){

                PriceDropReportModalObject = new JSONObject();
                JSONObject object = new JSONObject();
                try {
                    PriceDropReportCategoryObject = new JSONObject(CategoryjJsonobject.toString()) ;
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }


                Iterator iterator = modalObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    try {
                        JSONObject issue = modalObject.getJSONObject(key);
                        String Modalname = issue.optString("name");

                        String ModelCheck = "";

                        for (PriceDropReportList sell : PriceDropReportFilterLists){
                            if(Modalname.trim().toUpperCase().contains(sell.getModel().trim().toUpperCase())){

                                JSONObject aa = PriceDropReportCategoryObject.getJSONObject(sell.getCommodity());

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put( "model" , sell.getModel());
                                jsonObject.put( "model_ar" , "");
                                if(!ModelCheck.equalsIgnoreCase(sell.getCommodity().toString())) {
                                    jsonObject.put("qty", sell.getQty());
                                    jsonObject.put("value", sell.getDrop_amount());
                                }else {
                                    JSONObject  ob = PriceDropReportCategoryObject.getJSONObject(sell.getCommodity()).getJSONObject(sell.getCommodity() + "_" + sell.getModel());
                                    jsonObject.put("qty", ob.get("qty"));
                                    jsonObject.put("value", ob.get("value"));
                                }
                                jsonObject.put( "count" , "1");
                                aa.put(sell.getCommodity() + "_" + sell.getModel(), jsonObject);
                                object.put(sell.getCommodity() + "_" + sell.getModel() , sell.getCommodity() + "_" + sell.getModel() );
                                aa.put("Modal", object);
                                PriceDropReportModalObject.put(sell.getModel() , jsonObject);

                                JSONObject object1 = null;

                                try {

                                    object1 = PriceDropReportCategoryObject.getJSONObject(sell.getCommodity()).getJSONObject(sell.getCommodity() + "_" + sell.getModel());
                                    if(ModelCheck.equalsIgnoreCase(sell.getCommodity().toString())){

                                        int getQty = Integer.parseInt(String.valueOf(object1.get("qty")));
                                        int addQty = Integer.parseInt(sell.getQty());

                                        int addBothQty = getQty + addQty;
                                        object1.put("qty" , addBothQty);

                                        int getValue = Integer.parseInt(String.valueOf(object1.get("value")));
                                        int addValue = Integer.parseInt(sell.getDrop_amount());

                                        int addBothValue = getValue + addValue;
                                        object1.put("value" , addBothValue);

                                    }

                                    ModelCheck = sell.getCommodity();


                                } catch (JSONException e) {
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

        PriceDropCustomCategoryLists.clear();
        Grandtotal_Qty = 0;
        Grandtotal_Value = 0;

        Iterator<String> iterator = PriceDropReportCategoryObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            try {

                List<SellOutCustomModalList> sellOutCustomModalLists = new ArrayList<>();

                JSONObject Model_jsonObject = PriceDropReportCategoryObject.getJSONObject(key).getJSONObject("Modal");

                int Qty = 0;
                int Value = 0;

                sellOutCustomModalLists.add(new SellOutCustomModalList(
                        "Model"
                        ,""
                        ,"Qty"
                        ,"Value"
                        ,""
                        ,true
                ));


                Iterator<String> iter = Model_jsonObject.keys();
                while (iter.hasNext()) {
                    String keys = iter.next();
                    try {
                        Object value = Model_jsonObject.get(keys);
                        String modalFind = value.toString();
                        try {

                            JSONObject MODELFetch =  PriceDropReportCategoryObject.getJSONObject(key).getJSONObject(modalFind);

                            Log.e(TAG, "FilterMainObjec: " + MODELFetch.toString() );
                            Log.e(TAG, "FilterMainObjec: " + MODELFetch.toString() );

                            String qtyGet = MODELFetch.get("qty").toString();
                            String ValueGet = MODELFetch.get("value").toString();

                            Qty =+ Integer.parseInt(qtyGet ) + Qty;
                            Value =+ Integer.parseInt(ValueGet) + Value;

                            sellOutCustomModalLists.add(new SellOutCustomModalList(
                                    MODELFetch.getString("model")
                                    ,MODELFetch.getString("model_ar")
                                    ,MODELFetch.getString("qty")
                                    ,MODELFetch.getString("value")
                                    ,"MODELFetch.getString(Categor)"
                                    ,false
                            ));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }

                PriceDropReportCategoryObject.getJSONObject(key).put("qty" , String.valueOf(Qty));
                PriceDropReportCategoryObject.getJSONObject(key).put("value" , String.valueOf(Value));

                PriceDropCustomCategoryLists.add(new SellOutCustomCategoryList(
                        PriceDropReportCategoryObject.getJSONObject(key).optString("commodity")
                        ,PriceDropReportCategoryObject.getJSONObject(key).optString("commodity_ar")
                        ,PriceDropReportCategoryObject.getJSONObject(key).optString("model")
                        ,PriceDropReportCategoryObject.getJSONObject(key).optString("model_ar")
                        ,PriceDropReportCategoryObject.getJSONObject(key).optString("qty")
                        ,PriceDropReportCategoryObject.getJSONObject(key).optString("value")
                        ,PriceDropReportCategoryObject.getJSONObject(key).optString("count")
                        ,sellOutCustomModalLists
                ));


                Grandtotal_Qty += Qty;
                Grandtotal_Value += Value;


            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }



        if(PriceDropCustomCategoryLists.size() > 0){

            binding.ReportselloutRecyclerView.setAdapter(new PriceDropReportAdapter(PriceDropCustomCategoryLists));
            binding.ReportselloutRecyclerView.setVisibility(View.VISIBLE);
            binding.tablayout.setVisibility(View.VISIBLE);
            binding.tablayout.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);

            binding.qty.setText(String.valueOf(Grandtotal_Qty));
            binding.value.setText(String.valueOf(Grandtotal_Value));

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
        title.setText(getString(R.string.report));
    }





}







































