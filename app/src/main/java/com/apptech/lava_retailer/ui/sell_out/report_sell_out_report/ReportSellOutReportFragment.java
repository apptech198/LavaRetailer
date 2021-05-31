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
import com.apptech.lava_retailer.adapter.CheckEntriesSellOutInvalidAdapter;
import com.apptech.lava_retailer.adapter.SellOutReportModalFilterAdapter;
import com.apptech.lava_retailer.adapter.SelloutReportAdapter;
import com.apptech.lava_retailer.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.lava_retailer.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.lava_retailer.databinding.ReportSellOutReportFragmentBinding;
import com.apptech.lava_retailer.list.sell_out_report.SellOutReportList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomCategoryList;
import com.apptech.lava_retailer.list.sellout_custom_list.SellOutCustomModalList;
import com.apptech.lava_retailer.modal.CheckEntriesSellOutImeiMonthYearsList;
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


    DatePickerDialog picker;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> month;
    ArrayList<CheckEntriesSellOutImeiMonthYearsList> yearsLists;
    CheckEntriesSellOutInvalidAdapter adapter;
    LinearLayout fromDatetitle, toDatetitle;
    View view;
    TextView fromTextView, toTextView;
    AlertDialog alertDialog;
    ImageView closeImg;
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
    String ID;
    List<SellOutReportList> sellOutReportLists = new ArrayList<>();
    List<SellOutReportList> sellOutReportFilterLists = new ArrayList<>();
    List<SellOutReportList> sellOutReportFilterModaleLists = new ArrayList<>();
    JSONObject CategoryJsonObject = new JSONObject();
    JSONObject ModalJsonObject = new JSONObject();
    String QTYSelect = "", VALUESelect = "";
    List<SellOutCustomCategoryList> sellOutCustomCategoryLists = new ArrayList<>();
    JSONArray model_list;
    JSONObject SelloutReportObject = new JSONObject();
    JSONObject SelloutReportModalObject = new JSONObject();
    JSONObject CalculationObject = new JSONObject();

    int Grandtotal_Qty = 0;
    int Grandtotal_Value = 0;

    public static ReportSellOutReportFragment newInstance() {
        return new ReportSellOutReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Reports sell out report");

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
        getselloutReport();

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v -> {
            mypopupWindow.showAsDropDown(v, -153, 0);
        });


        binding.PdfDownload.setOnClickListener(v -> {
            PDFDownload1();
        });


        binding.filterModel.setOnClickListener(v -> {
            sellOutReportModalFilter = new SellOutReportModalFilter(this);
            sellOutReportModalFilter.show(getChildFragmentManager(), "modal bottom sheet");
        });

        binding.filterCategory.setOnClickListener(v -> {
            sellOutReportCategoryFilter = new SellOutReportCategoryFilter(this);
            sellOutReportCategoryFilter.show(getChildFragmentManager(), "category filter");
        });


        setupRadioFilter();
        binding.RadioBtn.setOnClickListener(v -> {
            filterOpen.showAsDropDown(v, -153, 0);
        });


        binding.PendingLayout.setOnClickListener(this);
        binding.ApprovedLayout.setOnClickListener(this);
        binding.CancelledLayout.setOnClickListener(this);

        binding.FilterQtyLayout.setOnClickListener(v -> {
            if (binding.QtyCheckbox.isChecked()) {
                binding.QtyCheckbox.setChecked(false);
                QTYSelect = "";
                return;
            }
            binding.QtyCheckbox.setChecked(true);
            QTYSelect = "YES";
        });


        binding.FilterValueLayout.setOnClickListener(v -> {
            if (binding.ValueCheckbox.isChecked()) {
                VALUESelect = "";
                binding.ValueCheckbox.setChecked(false);
                return;
            }
            binding.ValueCheckbox.setChecked(true);
            VALUESelect = "YES";
        });

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
        FileOutputStream out = null;
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
        title.setText("Reports sell out report");
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
        progressDialog.show();
        try {
            CategoryJsonObject = new JSONObject(String.valueOf(o));
//            DataCategoryFilter();
            filterCategoryloop();
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
            Log.e(TAG, "Onitem: " + ModalJsonObject.toString());
            filterModalloop();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getselloutReport() {

//        Log.e(TAG, "getselloutReport: " + StartDate);
//        Log.e(TAG, "getselloutReport: " + End_Date);

        String json = "{\n" +
                "  \"fetch_list\": [\n" +
                "    {\n" +
                "      \"commodity\": \"SMART PHONE\",\n" +
                "      \"commodity_ar\": \"هاتف ذكي\",\n" +
                "      \"model\": \"Y31\",\n" +
                "      \"model_ar\": \"Y31\",\n" +
                "      \"dis_price\": \"1300\",\n" +
                "      \"qty\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"commodity\": \"SMART PHONE\",\n" +
                "      \"commodity_ar\": \"هاتف ذكي\",\n" +
                "      \"model\": \"Y31\",\n" +
                "      \"model_ar\": \"Y31\",\n" +
                "      \"dis_price\": \"1300\",\n" +
                "      \"qty\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"commodity\": \"WIRED EARPHONE\",\n" +
                "      \"commodity_ar\": \"هاتف ذكي\",\n" +
                "      \"model\": \"Y31\",\n" +
                "      \"model_ar\": \"Y31\",\n" +
                "      \"dis_price\": \"1300\",\n" +
                "      \"qty\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"commodity\": \"WIRED EARPHONE\",\n" +
                "      \"commodity_ar\": \"هاتف ذكي\",\n" +
                "      \"model\": \"Y31\",\n" +
                "      \"dis_price\": \"1300\",\n" +
                "      \"model_ar\": \"Y32\",\n" +
                "      \"qty\": \"1\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"commodity\": \"WIRED EARPHONE\",\n" +
                "      \"commodity_ar\": \"هاتف ذكي\",\n" +
                "      \"model\": \"Y32\",\n" +
                "      \"dis_price\": \"1300\",\n" +
                "      \"model_ar\": \"Y32\",\n" +
                "      \"qty\": \"1\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"error\": false,\n" +
                "  \"error_code\": 200,\n" +
                "  \"message\": \" all IMEI   \"\n" +
                "}";

        binding.noData.setVisibility(View.GONE);
        progressDialog.show();


//            lavaInterface.SELLOUT_REPORT(ID, StartDate, End_Date).enqueue(new Callback<Object>() {
        lavaInterface.SELLOUT_REPORT("ff2326e2cf317a74fef52f267662a1ef", "01-01-2020", "01-01-2022").enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {


//                            jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        jsonObject = new JSONObject(json);

                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if (error.equalsIgnoreCase("FALSE")) {
                            model_list = jsonObject.getJSONArray("fetch_list");

                            for (int i = 0; i < model_list.length(); i++) {
                                JSONObject op = model_list.getJSONObject(i);

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
                                        , op.optString("qty")
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
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void filterCategoryloop() {

        if (sellOutReportLists.size() > 0) {

            if (CategoryJsonObject.length() > 0) {

                SelloutReportObject = new JSONObject();

                Iterator iterator = CategoryJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    try {
                        JSONObject issue = CategoryJsonObject.getJSONObject(key);
                        String Categoryname = issue.optString("name");

                        int Countqty = 0;
                        int CountValue = 0;
                        int Count = 0;


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

                                try {

                                    JSONObject a = SelloutReportObject.getJSONObject(sell.getCommodity());

                                    int getcount = Integer.parseInt(a.optString("count"));
                                    int qty = Integer.parseInt(a.optString("qty"));
                                    int value = Integer.parseInt(a.optString("value"));

                                    Count = getcount + 1;
                                    Countqty = Countqty + qty;
                                    CountValue = CountValue + value;

                                    a.put("value", String.valueOf(CountValue));
                                    a.put("qty", String.valueOf(Countqty));
                                    a.put("count", String.valueOf(Count));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                            if (SelloutReportModalObject.length() > 0) {
                                SetFilterDatModalaAdapter();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


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


        if (sellOutReportLists.size() > 0) {
            if (ModalJsonObject.length() > 0) {

                SelloutReportModalObject = new JSONObject();


                Iterator iterator = ModalJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    try {
                        JSONObject issue = ModalJsonObject.getJSONObject(key);
                        String Modalname = issue.optString("name");

                        Log.e(TAG, "filterModalloop: " + sellOutReportFilterLists.size());
                        JSONObject object = new JSONObject();
                        int Count = 0;

                        for (SellOutReportList sell : sellOutReportFilterLists) {
                            if (Modalname.trim().toUpperCase().contains(sell.getModel().trim().toUpperCase())) {

                                Count++;

                                JSONObject jsonObject = new JSONObject();

                                object.put(sell.getCommodity() + "_" + sell.getModel() , sell.getCommodity() + "_" + sell.getModel());

                                jsonObject.put("model", sell.getModel());
                                jsonObject.put("model_ar", sell.getModelAr());
                                jsonObject.put("qty", sell.getQty());
                                jsonObject.put("value", sell.getDisPrice());
                                jsonObject.put("category", sell.getCommodity());
                                jsonObject.put("count", String.valueOf(Count));
                                JSONObject aa = SelloutReportObject.getJSONObject(sell.getCommodity());

                                aa.put("Modal", object);

                                aa.put(sell.getCommodity() + "_" + sell.getModel(), jsonObject);

                                CalculationObject.put(sell.getCommodity() + "_" + sell.getModel(), jsonObject);

  /*                              JSONObject jsonObject = new JSONObject();
                                jsonObject.put( "model" , sell.getModel());
                                jsonObject.put( "model_ar" , sell.getModelAr());
                                jsonObject.put( "qty" , sell.getQty());
                                jsonObject.put( "value" , sell.getDisPrice());
                                jsonObject.put( "category" , sell.getCommodity());
                                jsonObject.put( "count" , "1");
                                SelloutReportModalObject.put(sell.getCommodity() , jsonObject);


                                    int qty1 = Integer.parseInt(sell.getQty()) + ModalQty;
                                    ModalQty = qty1;
                                    Log.e(TAG, "filterModalloop: " + Modalname);
                                    Log.e(TAG, "filterModalloop: " + sell.getQty());
                                    Log.e(TAG, "filterModalloop: " + qty1);


                                try {

                                     JSONObject a = SelloutReportModalObject.getJSONObject(sell.getCommodity());
                                    CountModal++;
                                    a.put("value" , String.valueOf(CountModal));
//
                                        ModalQty = 0;
                                        ModalValue = 0;

                                        JSONObject a = SelloutReportModalObject.getJSONObject(sell.getModel());
                                        int qty = Integer.parseInt(a.getString("qty")) + ModalQty;
                                        int value = Integer.parseInt(a.getString("value")) + ModalValue;

                                        ModalQty = qty;
                                        ModalValue = value;

                                        a.put("qty" , String.valueOf(ModalQty));
                                        a.put("value" , String.valueOf(ModalValue));
//
//

                                }catch (JSONException e){
                                    e.printStackTrace();
                                }*/

                                Log.e(TAG, "filterModalloop: " + SelloutReportModalObject.toString());

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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

        Log.e(TAG, "SetFilterDatModalaAdapter: " + SelloutReportObject.toString());
        Log.e(TAG, "SetFilterDatModalaAdapter: " + SelloutReportObject.toString());

        Iterator iterator = SelloutReportObject.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            try {
                JSONObject Model_jsonObject = SelloutReportObject.getJSONObject(key).getJSONObject("Modal");
//                Log.e(TAG, "SetFilterDatModalaAdapter: " + Model_jsonObject.toString() );

                Iterator iterator1 = Model_jsonObject.keys();
                while (iterator1.hasNext()) {

                    try {

                        Log.e(TAG, "SetFilterDatModalaAdapter conftim : " + iterator1.next() );
                        String category_modal = String.valueOf(iterator1.next());
                        Log.e(TAG, "SetFilterDatModalaAdapter aaaaaaaaaaaaaaaaaaaaaaaaaaaaa: " + category_modal);

                    }catch (NoSuchElementException e ){

                    }

//                    try {
//
//                        JSONObject MainKeys = SelloutReportObject.getJSONObject(key).getJSONObject(key_value);
//                        Log.e(TAG, "SetFilterDatModalaAdapter: " + MainKeys.getString("model"));
//                        Log.e(TAG, "SetFilterDatModalaAdapter: " + MainKeys.getString("model_ar"));
//
//                    } catch (JSONException jsonException) {
//                        jsonException.printStackTrace();
//                    }

//                    sellOutCustomModalLists.add(new SellOutCustomModalList(
//                                object1.getString("model")
//                                ,object1.getString("model_ar")
//                                ,object1.getString("qty")
//                                ,object1.getString("value")
//                                ,object1.getString("category")
//                        ));
                }


                Log.e(TAG, "SetFilterDatModalaAdapter:   categor______categor______categor______categor______categor______categor");

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

        }


//
//        Iterator iterator = SelloutReportObject.keys();
//        while (iterator.hasNext()) {
//            String key = (String) iterator.next();
//            Log.e(TAG, "SetFilterDatModalaAdapter: " + key );
//            JSONObject object =null;
//            try {
//                 object=  SelloutReportObject.getJSONObject(key);
//
//
//                Iterator iterator7 = object.keys();
//                while (iterator7.hasNext()) {
//                    String key1 = (String) iterator7.next();
//
//
//                    Log.e(TAG, "SetFilterDatModalaAdapter: " + key1);
//                    Log.e(TAG, "SetFilterDatModalaAdapter: " + key1);
//                    List<SellOutCustomModalList> sellOutCustomModalLists = new ArrayList<>();
//
//
//                    try {
//
//                        JSONObject object1 =   SelloutReportObject.getJSONObject(key).getJSONObject(key1);
//                        Log.e(TAG, "SetFilterDatModalaAdapter: "+object1.getString("model") );
//                        object1.getString("model");
//
//                        sellOutCustomModalLists.add(new SellOutCustomModalList(
//                                object1.getString("model")
//                                ,object1.getString("model_ar")
//                                ,object1.getString("qty")
//                                ,object1.getString("value")
//                                ,object1.getString("category")
//                        ));
//
//                    }catch (JSONException jsonException) {
//                        jsonException.printStackTrace();
//                    }
//
//                }
////                 sellOutCustomCategoryLists.add(new SellOutCustomCategoryList(
////                         SelloutReportObject.getJSONObject(key).optString("commodity")
////                         ,SelloutReportObject.getJSONObject(key).optString("commodity_ar")
////                         ,SelloutReportObject.getJSONObject(key).optString("model")
////                         ,SelloutReportObject.getJSONObject(key).optString("model_ar")
////                         ,SelloutReportObject.getJSONObject(key).optString("qty")
////                         ,SelloutReportObject.getJSONObject(key).optString("value")
////                         ,SelloutReportObject.getJSONObject(key).optString("count")
////                         ,sellOutCustomModalLists
////                 ));
//
//
//
//
//            } catch (JSONException jsonException) {
//                jsonException.printStackTrace();
//            }
//
//            Log.e(TAG, "SetFilterDatModalaAdapter: " +  sellOutCustomCategoryLists );
//            Log.e(TAG, "SetFilterDatModalaAdapter: " +  sellOutCustomCategoryLists );
//
//
//
//
//
//
//
//
//
//
////            Iterator iterator1 = SelloutReportModalObject.keys();
////            List<SellOutCustomModalList> sellOutCustomModalLists = new ArrayList<>();
////
////            sellOutCustomModalLists.add(new SellOutCustomModalList(
////                    "Modal"
////                    ,""
////                    ,"Qty"
////                    ,"Value"
////                    ,""
////            ));
////
////
////            int Total_qty = 0;
////            int Total_value = 0;
////
////
////            while (iterator1.hasNext()) {
////                JSONObject issue = null;
////                try {
////                    String key1 = (String) iterator1.next();
////                    issue = SelloutReportModalObject.getJSONObject(key1);
////                    String model1 = issue.getString("model");
////                    JSONObject object = SelloutReportModalObject.getJSONObject(model1);
////
////                    String model = object.getString("model");
////
////                    Log.e(TAG, "SetFilterDatModalaAdapter: " + model );
////                    Log.e(TAG, "SetFilterDatModalaAdapter: " + model );
////
////                    String model_ar = object.getString("model_ar");
////                    String qty = object.getString("qty");
////                    String value = object.getString("value");
////                    String count = object.getString("count");
////
////
////                    Total_qty = Integer.parseInt(qty) + Total_qty;
////                    Total_value = Integer.parseInt(value) + Total_value;
////
////
////                    sellOutCustomModalLists.add(new SellOutCustomModalList(
////                            model
////                            ,model_ar
////                            ,qty
////                            ,value
////                            ,count
////                    ));
////
////                } catch (JSONException jsonException) {
////                    jsonException.printStackTrace();
////                }
////            }
//
////            try {
////                JSONObject issue = SelloutReportObject.getJSONObject(key);
////                String commodity = issue.getString("commodity");
////                String commodity_ar = issue.getString("commodity_ar");
////                String model = issue.getString("model");
////                String model_ar = issue.getString("model_ar");
////                String count = issue.getString("count");
//////                String value = issue.getString("value");
//////                String qty = issue.getString("qty");
////
////
////
////
////                String value = String.valueOf(Total_value);
////                String qty = String.valueOf(Total_qty);
////
////                Grandtotal_Qty = Grandtotal_Qty + Total_qty;
////                Grandtotal_Value = Grandtotal_Value + Total_value;
////
////                sellOutCustomCategoryLists.add(new SellOutCustomCategoryList(
////                        commodity
////                        ,commodity_ar
////                        ,model
////                        ,model_ar
////                        ,qty
////                        ,value
////                        ,count
////                        ,sellOutCustomModalLists
////                ));
//
////            } catch (JSONException jsonException) {
////                jsonException.printStackTrace();
////            }
//
//
//
////        }
//
//
//
//        if(sellOutCustomCategoryLists.size() > 0){
//
//            selloutReportAdapter = new SelloutReportAdapter(sellOutCustomCategoryLists);
//            binding.ReportselloutRecyclerView.setAdapter(selloutReportAdapter);
//            selloutReportAdapter.UpdateList(sellOutCustomCategoryLists);
//            binding.ReportselloutRecyclerView.setVisibility(View.VISIBLE);
//            binding.tablayout.setVisibility(View.VISIBLE);
//            binding.noData.setVisibility(View.GONE);
//
//
//            binding.qty.setText(String.valueOf(Grandtotal_Qty));
//            binding.value.setText(String.valueOf(Grandtotal_Value));
//
//
//        }else {
//            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
//            binding.tablayout.setVisibility(View.GONE);
//            binding.noData.setVisibility(View.VISIBLE);
//        }
//        progressDialog.dismiss();
//
//    }
//

    /*
    private void SetFilterDatModalaAdapter(){


        sellOutCustomCategoryLists.clear();;

        Iterator iterator = SelloutReportObject.keys();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();

            Iterator iterator1 = SelloutReportModalObject.keys();
            List<SellOutCustomModalList> sellOutCustomModalLists = new ArrayList<>();


            sellOutCustomModalLists.add(new SellOutCustomModalList(
                    "Modal"
                    ,""
                    ,"Qty"
                    ,"Value"
                    ,""
            ));


            int Total_qty = 0;
            int Total_value = 0;


            while (iterator1.hasNext()) {
                JSONObject issue = null;
                try {
                    String key1 = (String) iterator1.next();
                    issue = SelloutReportModalObject.getJSONObject(key1);
                    String model1 = issue.getString("model");
                    JSONObject object = SelloutReportModalObject.getJSONObject(model1);

                    String model = object.getString("model");

                    Log.e(TAG, "SetFilterDatModalaAdapter: " + model );
                    Log.e(TAG, "SetFilterDatModalaAdapter: " + model );

                    String model_ar = object.getString("model_ar");
                    String qty = object.getString("qty");
                    String value = object.getString("value");
                    String count = object.getString("count");


                    Total_qty = Integer.parseInt(qty) + Total_qty;
                    Total_value = Integer.parseInt(value) + Total_value;


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
                JSONObject issue = SelloutReportObject.getJSONObject(key);
                String commodity = issue.getString("commodity");
                String commodity_ar = issue.getString("commodity_ar");
                String model = issue.getString("model");
                String model_ar = issue.getString("model_ar");
                String count = issue.getString("count");
//                String value = issue.getString("value");
//                String qty = issue.getString("qty");
                String value = String.valueOf(Total_value);
                String qty = String.valueOf(Total_qty);

                Grandtotal_Qty = Grandtotal_Qty + Total_qty;
                Grandtotal_Value = Grandtotal_Value + Total_value;

                sellOutCustomCategoryLists.add(new SellOutCustomCategoryList(
                        commodity
                        ,commodity_ar
                        ,model
                        ,model_ar
                        ,qty
                        ,value
                        ,count
                        ,sellOutCustomModalLists
                ));

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
*/


    }


}








































