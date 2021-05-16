package com.apptech.myapplication.ui.sell_out.report_sell_out_report;

import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.CheckEntriesSellOutInvalidAdapter;
import com.apptech.myapplication.adapter.SellOutPendingVerificationAdapter;
import com.apptech.myapplication.databinding.PriceDropFragmentBinding;
import com.apptech.myapplication.databinding.ReportSellOutReportFragmentBinding;
import com.apptech.myapplication.databinding.ReportsSellOutReportFragmentBinding;
import com.apptech.myapplication.fragment.price_drop.PriceDropViewModel;
import com.apptech.myapplication.fragment.warranty.WarrantyViewModel;
import com.apptech.myapplication.modal.CheckEntriesSellOutImeiMonthYearsList;
import com.apptech.myapplication.modal.sellOutPendingVerification.SellOutPendingVerificationList;
import com.apptech.myapplication.other.NetworkCheck;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportSellOutReportFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

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
    String StartDate ="" , End_Date = "" , TYPE = "";

    public static ReportSellOutReportFragment newInstance() {
        return new ReportSellOutReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ReportSellOutReportFragmentBinding.inflate(inflater ,container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportSellOutReportViewModel.class);
        // TODO: Use the ViewModel


        MonthinitList();
        YearsinitList();
        Customspinnerset();

        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
//            view = LayoutInflater.from(requireContext()).inflate(R.layout.row_dialog_open, null);
//            builder.setView(view);
//            alertDialog = builder.create();
//            alertDialog.show();
//            fromDatetitle = view.findViewById(R.id.fromDatetitle);
//            toDatetitle = view.findViewById(R.id.toDatetitle);
//            dilogclick();
            mypopupWindow.showAsDropDown(v,-153,0);
        });


        binding.PdfDownload.setOnClickListener(v -> {
            PDFDownload1();
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
            StartDate = getTimeStamp(selection.second) ;
            End_Date = getTimeStamp(selection.first);
        });


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

            for (int i=0 ; i<80; i++){
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
            document.add(new Paragraph("  ") );

            PdfPTable pdfPTable =new PdfPTable(8);
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


            for (int i=0 ; i<1000; i++){
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



    private void Customspinnerset() {

//        adapter = new CheckEntriesSellOutInvalidAdapter(getContext(), month);
//        binding.PaymentTypespinner.setAdapter(adapter);
//        binding.PaymentTypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CheckEntriesSellOutImeiMonthYearsList yearsList = (CheckEntriesSellOutImeiMonthYearsList) parent.getItemAtPosition(position);
//                String clickedCountryName = yearsList.getDate();
//                Log.e(TAG, "onItemSelected: " + clickedCountryName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//
//        adapter = new CheckEntriesSellOutInvalidAdapter(getContext(), yearsLists);
//        binding.Datespinner.setAdapter(adapter);
//        binding.Datespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                CheckEntriesSellOutImeiMonthYearsList yearsList = (CheckEntriesSellOutImeiMonthYearsList) parent.getItemAtPosition(position);
//                String clickedCountryName = yearsList.getDate();
//                Log.e(TAG, "onItemSelected: " + clickedCountryName);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


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
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            Log.e(TAG, "openCamera: " + "peris" );
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that", 123, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsGranted: Permis" );
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsDenied: ");
    }


}





































