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
import com.apptech.lava_retailer.modal.CheckEntriesSellOutImeiMonthYearsList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.filter.SellOutReportCategoryFilter;
import com.apptech.lava_retailer.ui.filter.SellOutReportModalFilter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
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
    JSONObject CategoryJsonObject = new JSONObject();
    JSONObject ModalJsonObject = new JSONObject();
    String QTYSelect = "" , VALUESelect = "";

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





    private void dilogclick() {


        fromTextView = view.findViewById(R.id.fromTextView);
        toTextView = view.findViewById(R.id.toTextView);
        closeImg = view.findViewById(R.id.closeImg);


        closeImg.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


        fromDatetitle.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
//                            eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        fromTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

        toDatetitle.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        toTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

    }


    private void MonthinitList() {
        month = new ArrayList<>();
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Select Modal"));
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Month Scheme"));
        month.add(new CheckEntriesSellOutImeiMonthYearsList("Price Drop"));
    }

    private void YearsinitList() {
        yearsLists = new ArrayList<>();
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("Select Date"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("01-01-2018"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("01-02-2019"));
        yearsLists.add(new CheckEntriesSellOutImeiMonthYearsList("01-02-2020"));
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
            DataCategoryFilter();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void FilterListCategory() {
        selloutReportAdapter.notifyDataSetChanged();
    }

    private void FilterListModal() {
        selloutReportAdapter.notifyDataSetChanged();
    }

    @Override
    public void Onitem(JSONObject o) {
        sellOutReportModalFilter.dismiss();
        try {
            progressDialog.show();
            ModalJsonObject = new JSONObject(String.valueOf(o));
            DataModalFilter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getselloutReport() {

//        Log.e(TAG, "getselloutReport: " + StartDate);
//        Log.e(TAG, "getselloutReport: " + End_Date);

        binding.noData.setVisibility(View.GONE);
        progressDialog.show();


/*
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_sell_out_report_inner, null);
            binding.tablayout.addView(view);

            for (int x = 0; x < 10; x++) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.row_sell_out_report, null);
                binding.tablayout.addView(v);
            }

        }
*/



//            lavaInterface.SELLOUT_REPORT(ID, StartDate, End_Date).enqueue(new Callback<Object>() {
            lavaInterface.SELLOUT_REPORT("ff2326e2cf317a74fef52f267662a1ef", "01-01-2020", "01-01-2022").enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));
                            String error = jsonObject.getString("error");
                            String message = jsonObject.getString("message");

                            if (error.equalsIgnoreCase("FALSE")) {
                                JSONArray model_list = jsonObject.getJSONArray("fetch_list");

                                for (int i = 0; i < model_list.length(); i++) {
                                    JSONObject op = model_list.getJSONObject(i);
                                    sellOutReportLists.add(new SellOutReportList(
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

                                if(sellOutReportLists.size() > 0){
                                    selloutReportAdapter = new SelloutReportAdapter(sellOutReportLists);
                                    binding.ReportselloutRecyclerView.setAdapter(selloutReportAdapter);
                                    binding.ReportselloutRecyclerView.setVisibility(View.VISIBLE);
                                    binding.noData.setVisibility(View.GONE);

                                }else {
                                    binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                                    binding.noData.setVisibility(View.VISIBLE);
                                }
                                progressDialog.dismiss();
                                return;
                            }
                            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                            binding.noData.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage() );
                        }
                        binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                        binding.noData.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressDialog.dismiss();
                    binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });

    }




    private void DataCategoryFilter(){
//        Log.e(TAG, "DataFilter: " + QTYSelect);
//        Log.e(TAG, "DataFilter: " + VALUESelect);
//        Log.e(TAG, "OnClickItem: " + CategoryJsonObject.toString());
//        Log.e(TAG, "OnClickItem: " + ModalJsonObject.toString());


        if(QTYSelect.isEmpty() && VALUESelect.isEmpty()){
            filterCategoryloop();
        }else {


            if(QTYSelect.isEmpty()){
                Log.e(TAG, "DataFilter: " + "qty" );
            }

            if(VALUESelect.isEmpty()){
                Log.e(TAG, "DataFilter: " + "vaalue" );
            }

        }

    }

    private void DataModalFilter(){
//        Log.e(TAG, "DataFilter: " + QTYSelect);
//        Log.e(TAG, "DataFilter: " + VALUESelect);
//        Log.e(TAG, "OnClickItem: " + CategoryJsonObject.toString());
//        Log.e(TAG, "OnClickItem: " + ModalJsonObject.toString());


        if(QTYSelect.isEmpty() && VALUESelect.isEmpty()){
            filterModalloop();
        }else {


            if(QTYSelect.isEmpty()){
                Log.e(TAG, "DataFilter: " + "qty" );
            }

            if(VALUESelect.isEmpty()){
                Log.e(TAG, "DataFilter: " + "vaalue" );
            }

        }

    }

    private void filterCategoryloop(){

        Log.e(TAG, "OnClickItem: " + CategoryJsonObject.toString());
        Log.e(TAG, "OnClickItem: " + ModalJsonObject.toString());


        if(sellOutReportLists.size() > 0){

            if (CategoryJsonObject.length() > 0){
                sellOutReportFilterLists.clear();
                Iterator iterator = CategoryJsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    try {
                        JSONObject issue = CategoryJsonObject.getJSONObject(key);
                        String Categoryname = issue.optString("name");
                        Log.e(TAG, "filterloop: " + Categoryname);
                        for (SellOutReportList sell : sellOutReportLists){
                            if(Categoryname.trim().toUpperCase().contains(sell.getCommodity().trim().toUpperCase())){
                                sellOutReportFilterLists.add(sell);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                SetFilterDataAdapter();

            }else {
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        }else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();


//        if(sellOutReportLists.size() > 0){
//            if (ModalJsonObject.length() > 0){
//                Iterator iterator = ModalJsonObject.keys();
//                sellOutReportFilterLists.clear();
//                while (iterator.hasNext()) {
//                    String key = (String) iterator.next();
//                    try {
//                        JSONObject issue = ModalJsonObject.getJSONObject(key);
//                        String Categoryname = issue.optString("name");
//                        Log.e(TAG, "filterloop: " + Categoryname);
//                        for (SellOutReportList sell : sellOutReportLists){
//                            if(Categoryname.trim().toUpperCase().contains(sell.getModel().trim().toUpperCase())){
//                                sellOutReportFilterLists.add(sell);
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                SetFilterDataAdapter();
//                progressDialog.dismiss();
//                return;
//            }else {
//                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
//                binding.noData.setVisibility(View.VISIBLE);
//                progressDialog.dismiss();
//            }
//        }else {
//            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
//            binding.noData.setVisibility(View.VISIBLE);
//            progressDialog.dismiss();
//        }




    }

    private void filterModalloop(){

        Log.e(TAG, "OnClickItem: " + CategoryJsonObject.toString());
        Log.e(TAG, "OnClickItem: " + ModalJsonObject.toString());


        if(sellOutReportLists.size() > 0){
            if (ModalJsonObject.length() > 0){
                Iterator iterator = ModalJsonObject.keys();
                sellOutReportFilterLists.clear();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    try {
                        JSONObject issue = ModalJsonObject.getJSONObject(key);
                        String Categoryname = issue.optString("name");
                        Log.e(TAG, "filterloop: " + Categoryname);
                        for (SellOutReportList sell : sellOutReportLists){
                            if(Categoryname.trim().toUpperCase().contains(sell.getModel().trim().toUpperCase())){
                                sellOutReportFilterLists.add(sell);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                SetFilterDataAdapter();
            }else {
                binding.ReportselloutRecyclerView.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        }else {
            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }


        progressDialog.dismiss();

    }



    private void SetFilterDataAdapter(){

        Log.e(TAG, "SetFilterDataAdapter: " + sellOutReportFilterLists.size() );

        if(sellOutReportFilterLists.size() > 0){

            selloutReportAdapter = new SelloutReportAdapter(sellOutReportFilterLists);
            binding.ReportselloutRecyclerView.setAdapter(selloutReportAdapter);
            binding.ReportselloutRecyclerView.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);

        }else {

            binding.ReportselloutRecyclerView.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();

    }




}










































