package com.apptech.lava_retailer.ui.sell_out.report_sell_out_report;


import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.adapter.SelloutReportAdapter;
import com.apptech.lava_retailer.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.lava_retailer.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.lava_retailer.databinding.ReportSellOutReportFragmentBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.list.modelList.ModelList;
import com.apptech.lava_retailer.list.sell_out_report.SellOutReportList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomCategoryList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomModalList;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.filter.SellOutReportCategoryFilter;
import com.apptech.lava_retailer.ui.filter.SellOutReportModalFilter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportSellOutReportFragment extends Fragment implements EasyPermissions.PermissionCallbacks, View.OnClickListener , SellOutReportCategoryFilter.OnClickBackPress  , SellOutReportModalFilter.OnItemClickBackPress {

    private ReportSellOutReportViewModel mViewModel;
    ReportSellOutReportFragmentBinding binding;
    private static final String TAG = "ReportSellOutReportFrag";


    MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
    PopupWindow mypopupWindow;
    PopupWindow filterOpen;
    String StartDate = "", End_Date = "", TYPE = "";
    SellOutReportCategoryFilter sellOutReportCategoryFilter;
    SellOutReportModalFilter sellOutReportModalFilter;
    SelloutReportAdapter selloutReportAdapter;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    ProgressDialog progressDialog;
    String ID = "";
    List<SellOutReportList> sellOutReportLists = new ArrayList<>();
    List<SellOutReportList> sellOutReportFilterLists = new ArrayList<>();
    JSONObject CategoryJsonObject = new JSONObject();
    JSONObject ReturnCategoryJsonObject = new JSONObject();
    JSONObject ReturnModelJsonObject = new JSONObject();
    JSONObject ModalJsonObject = new JSONObject();
    String QTYSelect = "", VALUESelect = "";
    List<SellOutCustomCategoryList> sellOutCustomCategoryLists = new ArrayList<>();
    JSONArray model_list;
    JSONObject SelloutReportObject = new JSONObject();
    JSONObject SelloutReportModalObject = new JSONObject();
    JSONObject CategoryjJsonobject = new JSONObject();

    List<ModelList> modalList = new ArrayList<>();
    List<ComodityLists> categoryLists =new ArrayList<>();


    int Grandtotal_Qty = 0;
    int Grandtotal_Value = 0;

    public static ReportSellOutReportFragment newInstance() {
        return new ReportSellOutReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = ReportSellOutReportFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportSellOutReportViewModel.class);
        // TODO: Use the ViewModel

        sessionManage = SessionManage.getInstance(getContext());
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        ID = sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID);


        String[] TodayDate = TodayDate().split("#");
        StartDate = TodayDate[0];
        End_Date = TodayDate[1];

        if(new NetworkCheck().haveNetworkConnection(getActivity())){
            getModel();
            getselloutReport();
        }else {
            Toast.makeText(getContext(), "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v -> {
            mypopupWindow.showAsDropDown(v, -153, 0);
        });


        binding.PdfDownload.setOnClickListener(v -> {
            PDFDownload1();
        });



        binding.filterCategory.setOnClickListener(v -> {

            Log.e(TAG, "onActivityCreated: " +  ReturnCategoryJsonObject.toString());

            sellOutReportCategoryFilter = new SellOutReportCategoryFilter(this , categoryLists ,ReturnCategoryJsonObject);
            sellOutReportCategoryFilter.show(getChildFragmentManager(), "category filter");
        });

        binding.filterModel.setOnClickListener(v -> {
            sellOutReportModalFilter = new SellOutReportModalFilter(this , modalList , ReturnModelJsonObject);
            sellOutReportModalFilter.show(getChildFragmentManager(), "modal bottom sheet");
        });



        setupRadioFilter();
        binding.RadioBtn.setOnClickListener(v -> {
            filterOpen.showAsDropDown(v, -153, 0);
        });


        binding.PendingLayout.setOnClickListener(this);
        binding.ApprovedLayout.setOnClickListener(this);
        binding.CancelledLayout.setOnClickListener(this);

        binding.QtyCheckbox.setChecked(true);
        binding.ValueCheckbox.setChecked(true);
//
//        binding.FilterQtyLayout.setOnClickListener(v -> {
//            if (binding.QtyCheckbox.isChecked()) {
//                binding.QtyCheckbox.setChecked(false);
//                QTYSelect = "";
//                return;
//            }
//            binding.QtyCheckbox.setChecked(true);
//            QTYSelect = "YES";
//        });
//
//
//        binding.FilterValueLayout.setOnClickListener(v -> {
//            if (binding.ValueCheckbox.isChecked()) {
//                VALUESelect = "";
//                binding.ValueCheckbox.setChecked(false);
//                return;
//            }
//            binding.ValueCheckbox.setChecked(true);
//            VALUESelect = "YES";
//        });

    }


    private void setupRadioFilter() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_sell_out_qty_filter, null);
        filterOpen = new PopupWindow(view, 350, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

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
            getselloutReport();
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            getselloutReport();
        });

        this_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] thisMonth = ThisMonthdate().split("#");
            StartDate = thisMonth[1];
            End_Date = thisMonth[0];
            getselloutReport();
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
            Log.e(TAG, "datePicker: " + selection.first);
            Log.e(TAG, "datePicker: " + selection.second);
            binding.DatpickerRange.setClickable(true);
            StartDate = getTimeStamp(selection.first);
            End_Date = getTimeStamp(selection.second);
            getselloutReport();
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String TodayDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        String endDateStr = df.format(calendar1.getTime());
        return startDateStr + "#" + endDateStr;
    }

    private String ThisWeekDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String endDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK, -7);
        String startDateStr = df.format(calendar1.getTime());
        return startDateStr + "#" + endDateStr;
    }

    public String LastMonthdate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast", startDateStr + " " + endDateStr);
        return startDateStr + "#" + endDateStr;
    }

    public String ThisMonthdate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthLastDay = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(monthLastDay);
        String endDateStr = df.format(monthFirstDay);
        Log.e("DateFirstLast", startDateStr + " " + endDateStr);
        return startDateStr + "#" + endDateStr;
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
            FileOutputStream out = new FileOutputStream(File.createTempFile("LAVA", ".pdf", folder));
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("LAVA Retailder"));
            document.add(new Paragraph("Sellout Report"));
            document.add(new Paragraph("  "));

            PdfPTable pdfPTable = new PdfPTable(8);
            pdfPTable.setPaddingTop(10);
            PdfPCell pdfCell1 = new PdfPCell(new Phrase("Cell-1"));
            PdfPCell pdfCell2 = new PdfPCell(new Phrase("Cell-2"));
            PdfPCell pdfCell3 = new PdfPCell(new Phrase("Cell-3"));
            PdfPCell pdfCell4 = new PdfPCell(new Phrase("Cell-4"));
            PdfPCell pdfCell5 = new PdfPCell(new Phrase("CellCellCellCellCellCellCellCellCellCellCell-5"));
            PdfPCell pdfCell6 = new PdfPCell(new Phrase("Cell-6"));
            PdfPCell pdfCell7 = new PdfPCell(new Phrase("Cell-7"));
            PdfPCell pdfCell8 = new PdfPCell(new Phrase("Cell-8"));

//            pdfCell1.setPadding(20);
            pdfPTable.addCell(pdfCell1);
            pdfPTable.addCell(pdfCell2);
            pdfPTable.addCell(pdfCell3);
            pdfPTable.addCell(pdfCell4);
            pdfPTable.addCell(pdfCell5);
            pdfPTable.addCell(pdfCell6);
            pdfPTable.addCell(pdfCell7);
            pdfPTable.addCell(pdfCell8);

            for (int i = 0; i < 80; i++) {
                pdfPTable.addCell(new PdfPCell(new Phrase("cisdvushduvidv")));
            }

            document.add(pdfPTable);
            Toast.makeText(getContext(), "Pdf Create", Toast.LENGTH_SHORT).show();
            document.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }

    private void PDFDownload() {
/*

//      Folder Create right

        String root = Environment.getExternalStorageDirectory() + File.separator + "Vikas";
        File folder = new File(root);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

//        file create inndir

        try {
            File.createTempFile("RECORD" ,".txt" , folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


//      File Create
        String root_path = Environment.getExternalStorageDirectory() + File.separator;
        File file = new File(root_path + "PDFCreate.pdf");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();


            document.add(new Paragraph("LAVA Retailder"));
            document.add(new Paragraph("Sellout Report"));
            document.add(new Paragraph("  "));

            PdfPTable pdfPTable = new PdfPTable(8);
            pdfPTable.setPaddingTop(10);
            PdfPCell pdfCell1 = new PdfPCell(new Phrase("Cell-1"));
            PdfPCell pdfCell2 = new PdfPCell(new Phrase("Cell-2"));
            PdfPCell pdfCell3 = new PdfPCell(new Phrase("Cell-3"));
            PdfPCell pdfCell4 = new PdfPCell(new Phrase("Cell-4"));
            PdfPCell pdfCell5 = new PdfPCell(new Phrase("CellCellCellCellCellCellCellCellCellCellCell-5"));
            PdfPCell pdfCell6 = new PdfPCell(new Phrase("Cell-6"));
            PdfPCell pdfCell7 = new PdfPCell(new Phrase("Cell-7"));
            PdfPCell pdfCell8 = new PdfPCell(new Phrase("Cell-8"));

//            pdfCell1.setPadding(20);
            pdfPTable.addCell(pdfCell1);
            pdfPTable.addCell(pdfCell2);
            pdfPTable.addCell(pdfCell3);
            pdfPTable.addCell(pdfCell4);
            pdfPTable.addCell(pdfCell5);
            pdfPTable.addCell(pdfCell6);
            pdfPTable.addCell(pdfCell7);
            pdfPTable.addCell(pdfCell8);


            for (int i = 0; i < 1000; i++) {
                pdfPTable.addCell(new PdfPCell(new Phrase("cisdvushduvidv")));
            }


            document.add(pdfPTable);
            Toast.makeText(getContext(), "Pdf Create", Toast.LENGTH_SHORT).show();
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "not Create", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(R.string.dashboard);
    }


    @AfterPermissionGranted(123)
    private void openCamera() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            Log.e(TAG, "openCamera: " + "peris");
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that", 123, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsGranted: Permis");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsDenied: ");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.PendingLayout:
                TYPE = "DELIVERED";
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status, null));
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status, null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status, null));
                break;
            case R.id.ApprovedLayout:
                TYPE = "PENDING";
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status, null));
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status, null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status, null));

                break;
            case R.id.CancelledLayout:
                TYPE = "CANCEL";
                binding.CancelledLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.red_order_status, null));
                binding.PendingLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status, null));
                binding.ApprovedLayout.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.blac_order_status, null));
                break;
        }
    }


    @Override
    public void OnClickItem(JSONObject o) {
        sellOutReportCategoryFilter.dismiss();
        CategoryjJsonobject = new JSONObject();
        progressDialog.show();
        try {
            ReturnCategoryJsonObject = new JSONObject(o.toString());
            CategoryJsonObject = new JSONObject(String.valueOf(o));
            filterCategoryloop();
            ReturnModelJsonObject = new JSONObject();
            sellOutReportModalFilter = new SellOutReportModalFilter(this , modalList , ReturnModelJsonObject);
            sellOutReportModalFilter.show(getChildFragmentManager(), "modal bottom sheet");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void Onitem(JSONObject o) {
        sellOutReportModalFilter.dismiss();

        try {
            progressDialog.show();
            ModalJsonObject = new JSONObject(String.valueOf(o));
            ReturnModelJsonObject = new JSONObject(String.valueOf(o));
            filterModalloop();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getModel() {

        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        progressDialog.show();
        lavaInterface.SELL_OUT_CATEGORY_MODAL_FILTER(country_id , country_name).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {

                if(response.isSuccessful()){
                    JSONObject jsonObject;
                    try {
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
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Time out" , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getselloutReport() {

        ReturnCategoryJsonObject = new JSONObject();
        ReturnModelJsonObject = new JSONObject();


        String country_id = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_ID);
        String country_name = sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_NAME);

        binding.noData.setVisibility(View.GONE);
        progressDialog.show();

         lavaInterface.SELLOUT_REPORT(ID, StartDate, End_Date , country_id , country_name).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject;
                    try {

//                        Log.e(TAG, "onResponse: " + response.body().toString() );

                        jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if (error.equalsIgnoreCase("FALSE")) {
                            model_list = jsonObject.getJSONArray("fetch_list");

                            sellOutReportLists.clear();

                            for (int i = 0; i < model_list.length(); i++) {
                                JSONObject op = model_list.getJSONObject(i);

                                String QtySet = "";
                                if( op.optString("qty").isEmpty()){
                                    QtySet = "0";
                                }else {
                                    QtySet = op.optString("qty");
                                }


                                sellOutReportLists.add(new SellOutReportList(
                                        op.optString("id")
                                        , op.optString("product_id")
                                        , op.optString("distributor_id")
                                        , op.optString("distributor_name")
                                        , op.optString("imei")
                                        , op.optString("retailer_id")
                                        , op.optString("retailer_name")
                                        , op.optString("country_id")
                                        , op.optString("country_name")
                                        , op.optString("marketing_name")
                                        , op.optString("marketing_name_ar")
                                        , op.optString("marketing_name_fr")
                                        , op.optString("des_fr")
                                        , op.optString("des")
                                        , op.optString("des_ar")
                                        , op.optString("actual_price")
                                        , op.optString("dis_price")
                                        , op.optString("thumb")
                                        , op.optString("thumb_ar")
                                        , op.optString("sku")
                                        , op.optString("commodity_id")
                                        , op.optString("format")
                                        , op.optString("commodity")
                                        , op.optString("commodity_ar")
                                        , op.optString("brand_id")
                                        , op.optString("brand")
                                        , op.optString("brand_ar")
                                        , op.optString("model")
                                        , op.optString("model_ar")
                                        , op.optString("category")
                                        , op.optString("serialized")
                                        , op.optString("video")
                                        , op.optString("video_ar")
                                        , op.optString("warranty_type")
                                        , op.optString("prowar")
                                        , op.optString("pro_war_days")
                                        , op.optString("battery_war")
                                        , op.optString("battery_war_days")
                                        , op.optString("charging_adapter_war")
                                        , op.optString("charging_adapter_war_days")
                                        , op.optString("charger_war")
                                        , op.optString("charger_war_days")
                                        , op.optString("usb_war")
                                        , op.optString("usb_war_days")
                                        , op.optString("wired_earphone_war")
                                        , op.optString("wired_earphone_war_days")
                                        , op.optString("available_qty")
                                        , op.optString("hide")
                                        , op.optString("total_sale")
                                        , op.optString("seller_id")
                                        , op.optString("seller_name")
                                        , op.optString("time")
                                        , op.optString("sold_date")
                                        , op.optString("warranty_start_date")
                                        , op.optString("warranty_end_date")
                                        , op.optString("locality_id")
                                        , op.optString("locality")
                                        , op.optString("imei2")
                                        , op.optString("srno")
                                        , QtySet
                                        , op.optString("order_no")
                                        , op.optString("price_drop")
                                        , op.optString("sellout")
                                        , op.optString("order_dispatch_date")
                                        , op.optString("order_date")
                                ));
                            }
                            binding.noData.setVisibility(View.VISIBLE);
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
                        Log.e(TAG, "onResponse: " + e.getMessage());
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
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.tablayout.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void filterCategoryloop() {

        if (sellOutReportLists.size() > 0) {

            if (CategoryJsonObject.length() > 0) {

                SelloutReportObject = new JSONObject();
                sellOutReportFilterLists .clear();

                Iterator<String> iterator = CategoryJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        JSONObject issue = CategoryJsonObject.getJSONObject(key);
                        String Categoryname = issue.optString("name");

                        for (SellOutReportList sell : sellOutReportLists) {

                            if (Categoryname.trim().toUpperCase().contains(sell.getCommodity().trim().toUpperCase())) {

                                sellOutReportFilterLists.add(sell);
                                JSONObject object = new JSONObject();
                                object.put("commodity", sell.getCommodity());
                                object.put("commodity_ar", sell.getCommodityAr());
                                object.put("model", sell.getModel());
                                object.put("model_ar", sell.getModelAr());
                                object.put("qty", sell.getQty());
                                object.put("value", sell.getDisPrice());
                                object.put("count", "1");
                                SelloutReportObject.putOpt(sell.getCommodity(), object);

                            }else {
                                Log.e(TAG, "filterCategoryloop: " );
                                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                                binding.tablayout.setVisibility(View.GONE);
                                binding.noData.setVisibility(View.VISIBLE);
                            }

                            CategoryjJsonobject = new JSONObject(SelloutReportObject.toString());

                            if (SelloutReportModalObject.length() > 0) {
                                SetFilterDatModalaAdapter();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e(TAG, "filterCategoryloop: " + SelloutReportObject.toString());

                if (SelloutReportModalObject.length() > 0) {
                    filterModalloop();
                }

            } else {
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.tablayout.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        } else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.tablayout.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();


    }

    private void filterModalloop() {

        if (SelloutReportObject.length() > 0) {

            if (ModalJsonObject.length() > 0) {

                SelloutReportModalObject = new JSONObject();

                JSONObject object = new JSONObject();
                try {
                    SelloutReportObject = new JSONObject(CategoryjJsonobject.toString()) ;
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }


                Iterator<String> iterator = ModalJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        JSONObject issue = ModalJsonObject.getJSONObject(key);
                        String Modalname = issue.optString("name");

                        Log.e(TAG, "filterModalloop: " + Modalname);
                        Log.e(TAG, "filterModalloop: " + sellOutReportFilterLists.size());

                        String ModelCheck = "";
                        for (SellOutReportList sell : sellOutReportFilterLists) {

                            if (Modalname.trim().toUpperCase().contains(sell.getModel().trim().toUpperCase())) {


                                Log.e(TAG, "filterModalloop: " + sell.getModel() );
                                Log.e(TAG, "filterModalloop: " + sell.getModel() );


                                JSONObject aa = SelloutReportObject.getJSONObject(sell.getCommodity());

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("model", sell.getModel());
                                jsonObject.put("model_ar", sell.getModelAr());
                                if(!ModelCheck.equalsIgnoreCase(sell.getCommodity())) {
                                    jsonObject.put("qty", sell.getQty());
                                    jsonObject.put("value", sell.getDisPrice());
                                }else {
                                    JSONObject  ob = SelloutReportObject.getJSONObject(sell.getCommodity()).getJSONObject(sell.getCommodity() + "_" + sell.getModel());
                                    jsonObject.put("qty", ob.get("qty"));
                                    jsonObject.put("value", ob.get("value"));
                                }
                                jsonObject.put("category", sell.getCommodity());
                                jsonObject.put("count", "1");
                                aa.put(sell.getCommodity() + "_" + sell.getModel(), jsonObject);
                                object.put(sell.getCommodity() + "_" + sell.getModel() , sell.getCommodity() + "_" + sell.getModel() );
                                aa.put("Modal", object);



                                JSONObject object1;

                                try {

                                    object1 = SelloutReportObject.getJSONObject(sell.getCommodity()).getJSONObject(sell.getCommodity() + "_" + sell.getModel());
                                    if(ModelCheck.equalsIgnoreCase(sell.getCommodity())){


                                        int getQty = Integer.parseInt(String.valueOf(object1.get("qty")));
                                        int addQty = Integer.parseInt(sell.getQty());

                                        int addBothQty = getQty + addQty;
                                        object1.put("qty" , addBothQty);

                                        int getValue = Integer.parseInt(String.valueOf(object1.get("value")));
                                        int addValue = Integer.parseInt(sell.getDisPrice());

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


                Log.e(TAG, "filterCategoryloop: " + SelloutReportObject.toString());
                Log.e(TAG, "filterCategoryloop: " + SelloutReportObject.toString());

                if (SelloutReportObject.length() > 0) {
                    SetFilterDatModalaAdapter();
                }


            } else {
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.tablayout.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
                SelloutReportModalObject = new JSONObject();
            }
        } else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.tablayout.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }


        progressDialog.dismiss();

    }

    private void SetFilterDatModalaAdapter() {

        sellOutCustomCategoryLists.clear();
        Grandtotal_Qty = 0;
        Grandtotal_Value = 0;


        Iterator<String> iterator = SelloutReportObject.keys();
        while (iterator.hasNext()) {
            String key =  iterator.next();
            try {

                List<SellOutCustomModalList> sellOutCustomModalLists = new ArrayList<>();

                JSONObject Model_jsonObject = SelloutReportObject.getJSONObject(key).getJSONObject("Modal");

                int Qty = 0;
                int Value = 0;

                sellOutCustomModalLists.add(new SellOutCustomModalList(
                        "Model"
                        ,""
                        ,"Qty"
                        ,"Value"
                        ,""
                ));


                Iterator<String> iter = Model_jsonObject.keys();
                while (iter.hasNext()) {
                    String keys = iter.next();
                    try {
                        Object value = Model_jsonObject.get(keys);
                        String modalFind = value.toString();
                        try {
                            JSONObject MODELFetch =  SelloutReportObject.getJSONObject(key).getJSONObject(modalFind);

                            String qtyGet = MODELFetch.get("qty").toString();
                            String ValueGet = MODELFetch.get("value").toString();

                            Qty =+ Integer.parseInt(qtyGet ) + Qty;
                            Value =+ Integer.parseInt(ValueGet) + Value;

                            sellOutCustomModalLists.add(new SellOutCustomModalList(
                                    MODELFetch.getString("model")
                                    ,MODELFetch.getString("model_ar")
                                    ,MODELFetch.getString("qty")
                                    ,MODELFetch.getString("value")
                                    ,MODELFetch.getString("category")
                            ));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }

                SelloutReportObject.getJSONObject(key).put("qty" , String.valueOf(Qty));
                SelloutReportObject.getJSONObject(key).put("value" , String.valueOf(Value));

                sellOutCustomCategoryLists.add(new SellOutCustomCategoryList(
                        SelloutReportObject.getJSONObject(key).optString("commodity")
                        ,SelloutReportObject.getJSONObject(key).optString("commodity_ar")
                        ,SelloutReportObject.getJSONObject(key).optString("model")
                        ,SelloutReportObject.getJSONObject(key).optString("model_ar")
                        ,SelloutReportObject.getJSONObject(key).optString("qty")
                        ,SelloutReportObject.getJSONObject(key).optString("value")
                        ,SelloutReportObject.getJSONObject(key).optString("count")
                        ,sellOutCustomModalLists
                ));


                Grandtotal_Qty += Qty;
                Grandtotal_Value += Value;


            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }



        if(sellOutCustomCategoryLists.size() > 0){

            selloutReportAdapter = new SelloutReportAdapter(sellOutCustomCategoryLists);
            binding.ReportselloutRecyclerView.setAdapter(selloutReportAdapter);
            selloutReportAdapter.UpdateList(sellOutCustomCategoryLists);
            binding.ReportselloutRecyclerView.setVisibility(View.VISIBLE);
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


}








































