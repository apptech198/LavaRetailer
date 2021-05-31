package com.apptech.lava_retailer.ui.passbook;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import android.os.Environment;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.PassbookAdapter;
import com.apptech.lava_retailer.adapter.TradeProgramTabAdapter;
import com.apptech.lava_retailer.databinding.PassbookFragmentBinding;
import com.apptech.lava_retailer.list.ClaimTypeList;
import com.apptech.lava_retailer.list.announcelist.PriceDrop;
import com.apptech.lava_retailer.list.passbook.PassbookList;
import com.apptech.lava_retailer.list.tradecatlist.TradingMenuList;
import com.apptech.lava_retailer.modal.order_statusList.OrderStatusList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.snatik.storage.Storage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class PassbookFragment extends Fragment {

    private PassbookViewModel mViewModel;
    PassbookFragmentBinding binding;
    private static final String TAG = "PassbookFragment";
    SessionManage sessionManage;
    LavaInterface lavaInterface;
    List<com.apptech.lava_retailer.list.OrderStatusList> orderStatusLists = new ArrayList<>();
    String StartDate ="" , End_Date = "" , TYPE = "";
    PopupWindow mypopupWindow;
    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    private static final int PERMISSION_REQUEST_CODE = 200;
    List<com.apptech.lava_retailer.list.passbook.List>lists= new ArrayList<>();
    List<ClaimTypeList>claimTypeLists= new ArrayList<>();
    PassbookAdapter.getCount getCount;



    String  path;
    public Storage storage;
    String newDir;
    FileWriter writer;
    String all;
    File fpath;
    String number="8519079634";
    String msgData = "";


    public static PassbookFragment newInstance() {
        return new PassbookFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Passbook));

        binding = PassbookFragmentBinding.inflate(inflater , container ,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PassbookViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());

        getCount= aBoolean -> {
          if(aBoolean) {
              binding.recycle.setVisibility(View.GONE);
              binding.noData.setVisibility(View.VISIBLE);
          }else {
              binding.recycle.setVisibility(View.VISIBLE);
              binding.noData.setVisibility(View.GONE);
          }
        };


        ThisWeekDate();
        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v ->  {
            mypopupWindow.showAsDropDown(v,-153,0);
//            generatePDF2();
        });
//
//        binding.PdfDownload.setOnClickListener(v -> {
//            PDFDownload1();
//        });

        if (checkPermission()) {
//            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }


    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission Denined.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
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
            getPassbook();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[0];
            End_Date = thisMonth[1];
            getPassbook();
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

    }

    void getClaimtype(PassbookAdapter adapter){

        lavaInterface.GETCLAIMTYPE().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");
                        claimTypeLists.clear();
                        if(error.equalsIgnoreCase("FALSE")){

                            JSONArray elements = jsonObject.optJSONArray("list");
                            claimTypeLists.add(new ClaimTypeList(
                                    "Select ClaimType"
                            ));
                            for (int i=0; i<elements.length(); i++){
                                JSONObject object= elements.optJSONObject(i);
                                claimTypeLists.add(new ClaimTypeList(
                                   object.optString("claim_type")
                                ));
                            }
                            if(claimTypeLists.isEmpty()){

                            }else {
                                ArrayAdapter<ClaimTypeList> arrayAdapter = new ArrayAdapter<ClaimTypeList>(getContext(),
                                        android.R.layout.simple_list_item_1, claimTypeLists);
                                binding.claimType.setAdapter(arrayAdapter);
                                binding.claimType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                        adapter.getFilter().filter(claimTypeLists.get(position).getClaim_type());
//                                        Log.d(TAG, "onItemSelected() returned: " + adapter.getItemCount()  );
//                                        if(adapter.getItemCount()<0){
//                                            binding.recycle.setVisibility(View.GONE);
//                                            binding.noData.setVisibility(View.VISIBLE);
//                                        }else {
//                                            binding.recycle.setVisibility(View.VISIBLE);
//                                            binding.noData.setVisibility(View.GONE);
//                                        }

                                        List<com.apptech.lava_retailer.list.passbook.List> ClaiList = new ArrayList<>();
                                        for (com.apptech.lava_retailer.list.passbook.List lisr : lists){
                                            if(lisr.getClaimType().trim().toUpperCase().contains(claimTypeLists.get(position).getClaim_type().trim().toUpperCase())){
                                                ClaiList.add(lisr);
                                            }
                                        }
                                        if(ClaiList.isEmpty()){
                                            binding.recycle.setVisibility(View.GONE);
                                            binding.noData.setVisibility(View.VISIBLE);
                                        }else {
                                           PassbookAdapter passbookAdapter= new PassbookAdapter(ClaiList,getCount);
                                           binding.recycle.setAdapter(passbookAdapter);
                                            binding.recycle.setVisibility(View.VISIBLE);
                                            binding.noData.setVisibility(View.GONE);
                                        }





                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                         adapter.getFilter().filter("Select ClaimType");
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


    private void getPassbook(){

            binding.progressbar.setVisibility(View.VISIBLE);


            lavaInterface.GetPASSBOOK(StartDate,End_Date).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if(response.isSuccessful()){
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");
                            lists.clear();
                            if(error.equalsIgnoreCase("FALSE")){

                                JSONArray elements = jsonObject.optJSONArray("list");

                                for (int i=0; i<elements.length(); i++){
                                    JSONObject object= elements.optJSONObject(i);
                                    lists.add(new com.apptech.lava_retailer.list.passbook.List(
                                            object.optString("id")
                                            ,object.optString("claim_type")
                                            ,object.optString("claim_code")
                                            ,object.optString("value")
                                            ,object.optString("status")
                                            ,object.optString("payment_reference")
                                            ,object.optString("payment_date")
                                            ,object.optString("time")
                                    ));
                                }
                                if(lists.isEmpty()){
                                    binding.noData.setVisibility(View.VISIBLE);
                                    binding.recycle.setVisibility(View.GONE);
                                }else {
                                    binding.noData.setVisibility(View.GONE);
                                    binding.recycle.setVisibility(View.GONE);
                                    PassbookAdapter adapter= new PassbookAdapter(lists, getCount);
                                    binding.recycle.setAdapter(adapter);
                                    getClaimtype(adapter);
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
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Passbook));
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
            getPassbook();
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
        getPassbook();
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
        StartDate = endDateStr;
        End_Date = startDateStr;
        return  startDateStr + "#" + endDateStr;
    }



    private void PDFDownload1() {

        String root = Environment.getExternalStorageDirectory() + File.separator + "LAVARetailer";
        File folder = new File(root);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

//        file create inndir
        try {
            FileOutputStream out = new FileOutputStream(File.createTempFile("LAVA" ,".pdf" , folder));
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("LAVA Retailder"));
            document.add(new Paragraph("Sellout Report"));
            document.add(new Paragraph("  ") );

            PdfPTable pdfPTable =new PdfPTable(8);
            pdfPTable.setPaddingTop(10);
            PdfPCell pdfCell1 = new PdfPCell(new Phrase("Date of Entry"));
            PdfPCell pdfCell2 = new PdfPCell(new Phrase("Claim Type"));
            PdfPCell pdfCell3 = new PdfPCell(new Phrase("Claim Code"));
            PdfPCell pdfCell4 = new PdfPCell(new Phrase("Value"));
            PdfPCell pdfCell5 = new PdfPCell(new Phrase("Status"));
            PdfPCell pdfCell6 = new PdfPCell(new Phrase("Payment Date"));
            PdfPCell pdfCell7 = new PdfPCell(new Phrase("Payment Refernece"));



            pdfPTable.addCell(pdfCell1);
            pdfPTable.addCell(pdfCell2);
            pdfPTable.addCell(pdfCell3);
            pdfPTable.addCell(pdfCell4);
            pdfPTable.addCell(pdfCell5);
            pdfPTable.addCell(pdfCell6);
            pdfPTable.addCell(pdfCell7);


            for (int i=0 ; i<8; i++){
                pdfPTable.addCell(new PdfPCell(new Phrase("cisdvushduvidv")));
            }

            document.add(pdfPTable);
            Toast.makeText(getContext(), "Pdf Create", Toast.LENGTH_SHORT).show();
            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }

}