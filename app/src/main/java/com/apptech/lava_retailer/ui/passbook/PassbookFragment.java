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
import com.apptech.lava_retailer.databinding.PassbookFragmentBinding;
import com.apptech.lava_retailer.modal.order_statusList.OrderStatusList;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.snatik.storage.Storage;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        title.setText("Passbook");

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


        PassbookAdapter passbookAdapter = new PassbookAdapter(orderStatusLists);
        binding.PassbookRecyclerView.setAdapter(passbookAdapter);
        binding.noData.setVisibility(View.GONE);
        binding.progressbar.setVisibility(View.GONE);
        binding.PassbookRecyclerView.setVisibility(View.VISIBLE);

//        getPassbook();
        setPopUpWindow();
        binding.DatpickerRange.setOnClickListener(v ->  {
            mypopupWindow.showAsDropDown(v,-153,0);
//            generatePDF2();
        });

        binding.PdfDownload.setOnClickListener(v -> {
//            generatePDF2();
            PDFDownload1();
        });

//        if (checkPermission()) {
//            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//        } else {
//            requestPermission();
//        }


    }

    private void generatePDF2() {


        OutputStream file = null;
        try {
//            File path = new File(Environment.getExternalStorageDirectory(),"MYPDF.pdf");
            String path = Environment.getExternalStorageDirectory() + File.separator + "TollCulator";

            file = new FileOutputStream(path);

            // Create a new Document object
            Document document = new Document();

            // You need PdfWriter to generate PDF document
            PdfWriter.getInstance(document, file);

            // Opening document for writing PDF
            document.open();
/*
            // Writing content
            document.add(new Paragraph("Hello World, Creating PDF document in Java is easy"));
            document.add(new Paragraph("You are customer # 2345433"));
            document.add(new Paragraph(new Date(new java.util.Date().getTime()).toString()));

            // Add meta data information to PDF file
            document.addCreationDate();
            document.addAuthor("Javarevisited");
            document.addTitle("How to create PDF document in Java");
            document.addCreator("Thanks to iText, writing into PDF is easy");

*/

//            PdfPTable pdfPTable =new PdfPTable(4);
//            PdfPCell pdfCell1 = new PdfPCell(new Phrase("Cell-1"));
//            PdfPCell pdfCell2 = new PdfPCell(new Phrase("Cell-2"));
//            PdfPCell pdfCell3 = new PdfPCell(new Phrase("Cell-3"));
//            PdfPCell pdfCell4 = new PdfPCell(new Phrase("Cell-4"));
//            PdfPCell pdfCell5 = new PdfPCell(new Phrase("CellCellCellCellCellCellCellCellCellCellCell-5"));
//            PdfPCell pdfCell6 = new PdfPCell(new Phrase("Cell-6"));
//            PdfPCell pdfCell7 = new PdfPCell(new Phrase("Cell-7"));
//            PdfPCell pdfCell8 = new PdfPCell(new Phrase("Cell-8"));
//
//            pdfPTable.addCell(pdfCell1);
//            pdfPTable.addCell(pdfCell2);
//            pdfPTable.addCell(pdfCell3);
//            pdfPTable.addCell(pdfCell4);
//            pdfPTable.addCell(pdfCell5);
//            pdfPTable.addCell(pdfCell6);
//            pdfPTable.addCell(pdfCell7);
//            pdfPTable.addCell(pdfCell8);
//
//            PdfPCell pdf = new PdfPCell(new Phrase("cdscsdcdscsdcdscdcdcd"));
//            pdf.setColspan(2);
//            pdfCell1.setHorizontalAlignment(HorizontalScrollView.TEXT_ALIGNMENT_CENTER);
//            pdfPTable.addCell(pdf);



//            PdfPCell pdfCell3 = new PdfPCell(new Phrase("Cell-21"));
//            pdfCell3.setColspan(2);
////            pdfCell3.setBackgroundColor(BaseColor.DARK_GRAY);
////            pdfCell3.setBorderColor(BaseColor.RED);
//            pdfCell3.setRotation(90);
//            pdfPTable.addCell(pdfCell3);
//
//            pdfPTable.setWidthPercentage(70);

//            document.add(pdfPTable);


            // close the document
            document.close();

            Toast.makeText(getContext(), "Your PDF File is succesfully created", Toast.LENGTH_SHORT).show();
            System.out.println("Your PDF File is succesfully created");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "not create", Toast.LENGTH_SHORT).show();
        } finally {

            // closing FileOutputStream
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException io) {/*Failed to close*/

            }
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generatePDF1() {


        PdfDocument pdfDocument = new PdfDocument();
        Paint myPaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100, 100, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Document doc = new Document();

        Canvas canvas = page.getCanvas();
        canvas.drawText("Welcome " , 40 , 50 , myPaint);



        pdfDocument.finishPage(page);



//        myPaint.setTextSize(12f);


        File path = new File(Environment.getExternalStorageDirectory(),"MYPDF.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();

    }

        private void generatePDF(){

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        Paint myPaint = new Paint();
        Paint title = new Paint();

        Canvas canvas = myPage.getCanvas();

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200));

        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);
        pdfDocument.finishPage(myPage);

            String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile.pdf";
            File myFile = new File(myFilePath);
            try {
                pdfDocument.writeTo(new FileOutputStream(myFile));
            }
            catch (Exception e){
                e.printStackTrace();
            }



//        try {
//            pdfDocument.writeTo(new FileOutputStream(file));
//            Toast.makeText(requireContext(), "PDF file generated succesfully.", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        pdfDocument.close();


    }


    public Storage writeFileOnInternalStorage(){
        Storage storage = new Storage(getActivity());
        path = storage.getExternalStorageDirectory();
        newDir = path + File.separator + "My Sample Directory";
        storage.createDirectory(newDir);
        return storage;
    }

    public void Writefile(String sBody) {
        try{
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public Writer create(File file) {
        fpath  = new File(file, "Callhistory.docs");
        try {
            writer = new FileWriter(fpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  writer;
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
        TextView last_month = (TextView) view.findViewById(R.id.last_month);
        TextView CustomDate = (TextView) view.findViewById(R.id.CustomDate);
        TextView this_month = (TextView) view.findViewById(R.id.this_month);
        mypopupWindow = new PopupWindow(view, 300, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        last_7_day.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            getPassbook();
        });
        last_month.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            String[] lastMonth = LastMonthdate().split("#");
            StartDate = lastMonth[0];
            End_Date = lastMonth[1];
            getPassbook();
        });

        CustomDate.setOnClickListener(v -> {
            mypopupWindow.dismiss();
            datePicker();
        });

    }





    private String ThisWeekDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = df.format(calendar.getTime());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_WEEK , -14);
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
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String startDateStr = df.format(monthFirstDay);
        String endDateStr = df.format(monthLastDay);
        Log.e("DateFirstLast",startDateStr+" "+endDateStr);
        return  startDateStr + "#" + endDateStr;
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


    private void getPassbook(){


//        Toast.makeText(getContext(), "" + strDate, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "" + endDate, Toast.LENGTH_SHORT).show();

            binding.progressbar.setVisibility(View.VISIBLE);

            String RetId = sessionManage.getUserDetails().get("ID");

            lavaInterface.ORDER_STATUS_LIST_CALL("94" ,"12-01-2021" , "12-06-2021").enqueue(new Callback<OrderStatusList>() {
                @Override
                public void onResponse(Call<OrderStatusList> call, Response<OrderStatusList> response) {

                    try {

                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if(response.isSuccessful()){
                            if(!response.body().getError()){
                                orderStatusLists.clear();
                                for (int i=0 ; i < response.body().getList().size() ; i++){
                                    com.apptech.lava_retailer.modal.order_statusList.List l = response.body().getList().get(i);

                                    orderStatusLists.add(new com.apptech.lava_retailer.list.OrderStatusList(
                                            l.getId()
                                            ,l.getProductId()
                                            ,l.getModelName()
                                            ,l.getProductName()
                                            ,l.getProductIvtId()
                                            ,l.getDisId()
                                            ,l.getRetId()
                                            ,l.getDisName()
                                            ,l.getAddress()
                                            ,l.getRetName()
                                            ,l.getRetMobile()
                                            ,l.getTime()
                                            ,l.getQty()
                                            ,l.getDiscountPrice()
                                            ,l.getActualPrice()
                                            ,l.getOrderNo()
                                            ,l.getPretotal()
                                            ,l.getDiscount()
                                            ,l.getOrderTotal()
                                            ,l.getItemTotal()
                                            ,l.getImei()
                                            ,l.getpType()
                                            ,l.getstatus()
                                    ));

                                }

                                if(orderStatusLists.size() > 0){

                                    Log.e(TAG, "onResponse: " + orderStatusLists.size());

                                    PassbookAdapter passbookAdapter = new PassbookAdapter(orderStatusLists);
                                    binding.PassbookRecyclerView.setAdapter(passbookAdapter);
                                    binding.noData.setVisibility(View.GONE);
                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.PassbookRecyclerView.setVisibility(View.VISIBLE);

                                }else {
                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.noData.setVisibility(View.VISIBLE);
                                    binding.PassbookRecyclerView.setVisibility(View.GONE);
                                }
                                return;
                            }
                            binding.noData.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            binding.PassbookRecyclerView.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        binding.noData.setVisibility(View.VISIBLE);
                        binding.progressbar.setVisibility(View.GONE);
                        binding.PassbookRecyclerView.setVisibility(View.GONE);
                        Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }catch (NullPointerException e){
                        e.printStackTrace();
                        Log.e(TAG, "onResponse: " + e.getMessage() );
                    }

                }

                @Override
                public void onFailure(Call<OrderStatusList> call, Throwable t) {
                    binding.noData.setVisibility(View.VISIBLE);
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
                }
            });

    }


    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText("Passbook");
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