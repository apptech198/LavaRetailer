package com.apptech.lava_retailer.ui.trade_program;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.TradeProgramCartAdapter;
import com.apptech.lava_retailer.adapter.TradeProgramTabAdapter;
import com.apptech.lava_retailer.databinding.TradeProgramFragmentBinding;
import com.apptech.lava_retailer.list.tradecatlist.TradingMenuList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradeProgramFragment extends Fragment {

    private TradeProgramViewModel mViewModel;
    private TradeProgramFragmentBinding binding;
    private PopupWindow mypopupWindow;
    private MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    private MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    private static final String TAG = "TradeProgramFragment";
    public String StartDate ="" , End_Date = "" , TYPE = "", ID="";
    private LavaInterface lavaInterface;
    private SessionManage sessionManage;
    private ProgressDialog progressDialog;
    private TradeProgramTabAdapter tradeProgramFragment;
    private TradeProgramCartAdapter tradeProgramCartAdapter;
    private TradeProgramTabAdapter.TradeProgramInterface tradeProgramInterface;
    private TradeProgramCartAdapter.TradeProgramCartInterface tradeProgramCartInterface;
    private NavController navController;
    Boolean firest= false;
    List<com.apptech.lava_retailer.list.tradecatlist.List> lists= new ArrayList<>();
    List<TradingMenuList> menuLists= new ArrayList<>();

    public static TradeProgramFragment newInstance() {
        return new TradeProgramFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TradeProgramFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TradeProgramViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        GetTab();

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v ->  {
            mypopupWindow.showAsDropDown(v,-153,0);
        });

        tradeProgramInterface = this::SetCart;
        tradeProgramCartInterface = () -> navController.navigate(R.id.tradeProgramImgOpenFragment);
        ThisWeekDate();
    }

    private void GetTab() throws NullPointerException{

            progressDialog.show();
            lavaInterface.TAB().enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if(response.isSuccessful()){
                        JSONObject jsonObject = null;
                        try {
                             jsonObject = new JSONObject(new Gson().toJson(response.body()));
                             String error = jsonObject.getString("error");
                             String message = jsonObject.getString("message");
                             menuLists.clear();
                             if(error.equalsIgnoreCase("FALSE")){

                                 JSONArray elements = jsonObject.optJSONArray("list");

                                 for (int i=0; i<elements.length(); i++){
                                     JSONObject object= elements.optJSONObject(i);
                                   menuLists.add(new TradingMenuList(
                                           object.optString("id")
                                           ,object.optString("name")
                                           ,object.optString("name_ar")
                                           ,object.optString("name_fr")
                                           ,object.optString("time")

                                   ));
                                 }
                                 tradeProgramFragment = new TradeProgramTabAdapter(tradeProgramInterface, menuLists);
                                 binding.TabRecyclerView.setAdapter(tradeProgramFragment);
                                 progressDialog.dismiss();
                                 return;
                             }
                             Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                             progressDialog.dismiss();
                             return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Time Out", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void SetCart(String pos){
        ID = pos;
//        progressDialog.show();

        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("trading_cat",pos);
        jsonObject.addProperty("start_date",StartDate);
        jsonObject.addProperty("end_date",End_Date);

        Map<String , String>map= new HashMap<>();
        map.put("trading_cat",pos);
        map.put("start_date",StartDate);
        map.put("end_date",End_Date);


        lavaInterface.GETTRADEDATALIST(map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){
                            lists.clear();
                            JSONArray elements = jsonObject.optJSONArray("list");

                            for (int i=0; i<elements.length(); i++){
                                JSONObject object= elements.optJSONObject(i);
                                lists.add(new com.apptech.lava_retailer.list.tradecatlist.List(
                                        object.optString("id")
                                        ,object.optString("trading_cat")
                                        ,object.optString("trading_cat_name")
                                        ,object.optString("name")
                                        ,object.optString("name_ar")
                                        ,object.optString("name_fr")
                                        ,object.optString("img_en")
                                        ,object.optString("img_ar")
                                        ,object.optString("img_fr")
                                        ,object.optString("time")
                                        ,object.optString("date")

                                ));
                            }

                            if(lists.isEmpty()){
                                binding.mainview.setVisibility(View.GONE);
                                binding.msg.setVisibility(View.VISIBLE);
                            }else {
                                binding.mainview.setVisibility(View.VISIBLE);
                                binding.msg.setVisibility(View.GONE);
                                tradeProgramCartAdapter = new TradeProgramCartAdapter(pos , tradeProgramCartInterface, lists);
                                binding.CartRecyclerView.setAdapter(tradeProgramCartAdapter);
                            }


                            progressDialog.dismiss();
                            return;
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Time Out", Toast.LENGTH_SHORT).show();
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
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[1];
            End_Date = thisMonth[0];
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

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
            SetCart(ID);

        });


    }

    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); // modify format
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


    public String ThisWeekDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String endDateStr  = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK , -7);
        String startDateStr = df.format(calendar1.getTime());
        StartDate = startDateStr;
        End_Date = endDateStr;
        if(firest){
            SetCart(ID);
        }else {
            firest= true;
        }

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
        StartDate = startDateStr;
        End_Date = endDateStr;
        SetCart(ID);
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
        StartDate = startDateStr;
        End_Date = endDateStr;
        SetCart(ID);
        return  startDateStr + "#" + endDateStr;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Trade_Program));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



































