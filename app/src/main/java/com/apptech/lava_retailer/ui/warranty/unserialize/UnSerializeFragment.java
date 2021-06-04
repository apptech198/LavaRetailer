package com.apptech.lava_retailer.ui.warranty.unserialize;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.UnSerializeFragmentBinding;
import com.apptech.lava_retailer.ui.qr.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnSerializeFragment extends Fragment implements ScannerFragment.BackPress , BarCodeScannerFragment.BackPressBarCode {

    private UnSerializeViewModel mViewModel;
    UnSerializeFragmentBinding binding;
    DatePickerDialog picker;
    private static final String TAG = "UnSerializeFragment";
    private String SELECT_DATE ="", TYPE="SERIALIZE";
    private Uri fileUri;
    MultipartBody.Part filePart= null;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    NavController navController;
    ScannerFragment scannerFragment;
    BarCodeScannerFragment barCodeScannerFragment;
    boolean onetime = true;
    int imeiCount = 0;
    View rowView;
    TextView textView , Modal , ModalTitle;
    LinearLayout mainLayout;
    LinearLayout removeBtn;
    private boolean NoData = true, Wrongdatra = true;


    public static UnSerializeFragment newInstance() {
        return new UnSerializeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = UnSerializeFragmentBinding.inflate(inflater , container , false);
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
        mViewModel = new ViewModelProvider(this).get(UnSerializeViewModel.class);
        // TODO: Use the ViewModel

        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(getContext());
        scannerFragment = new ScannerFragment(this);
        barCodeScannerFragment = new BarCodeScannerFragment(this);

        binding.selectDatePicker.setOnClickListener(v -> DatePickerOpen());
        binding.PhotoSelect.setOnClickListener(v -> Photoselect());

        binding.submit.setOnClickListener(v -> {
            if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                if(TYPE.equals("SERIALIZE")){
                    if(!binding.ImeiEdittext.getText().toString().isEmpty()){
                        if(!SELECT_DATE.isEmpty()){
                            binding.submit.setCheckable(false);
                            binding.submit.setFocusable(false);
                            submit();
                        }else {
                            Snackbar.make(binding.getRoot(),"Select Date",5000).show();
                        }
                    }else {Snackbar.make(binding.getRoot(),"Add Serial Number",5000).show();}
                }else {
                    if(!SELECT_DATE.isEmpty()){
                        binding.selectDatePicker.setError(null);
                        if(filePart!=null){
                            binding.PhotoSelect.setError(null);
                            binding.submit.setCheckable(false);
                            binding.submit.setFocusable(false);
                            submit();
                        }else {
                            binding.PhotoSelect.setError("Upload invoice");
                            Snackbar.make(binding.getRoot(),"Upload Invoice",5000).show();
                        }
                    }else {
                        binding.selectDatePicker.setError("Select Date");
                        Snackbar.make(binding.getRoot(),"Select Date",5000).show();
                    }
                }

                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        });

        binding.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.DescriptionError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.searilize:
                    binding.Searilizelayout.setVisibility(View.VISIBLE);
                    binding.note.setVisibility(View.GONE);
                    TYPE= "SERIALIZE";
                    break;
                case R.id.unsearilize:
                    binding.Searilizelayout.setVisibility(View.GONE);
                    binding.note.setVisibility(View.VISIBLE);
                    TYPE= "NONSERIALIZE";
                    binding.ImeiEdittext.setText("");
                    SELECT_DATE ="";
                    break;
            }
        });

        binding.scanBtn.setOnClickListener(v -> {
            onetime = true;
            Log.e(TAG, "onActivityCreated: " + "clicked" );
            binding.ImeiEdittext.setError(null);
            loadfragment(barCodeScannerFragment);
        });

        binding.addBtn.setOnClickListener(v -> {
            if(binding.ImeiEdittext.getText().toString().isEmpty()){
                binding.ImeiEdittext.setError("Entet serial number");
              Snackbar.make(binding.getRoot(),"Enter Serial Number",5000).show();
            }else {
                binding.ImeiEdittext.setError(null);
                binding.addBtn.setFocusable(false);
                binding.addBtn.setClickable(false);
                CheckWarrenty(binding.ImeiEdittext.getText().toString());
            }
        });

    }

    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.LoadFragment, fragment).addToBackStack(null).commit();
    }

    private void Photoselect() {
        ImagePicker.Companion.with(requireActivity())
                .crop()
                .compress(64)
                .maxResultSize(1080, 1080)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Accessories));

        if (resultCode == getActivity().RESULT_OK) {

            binding.ImageLayout.setVisibility(View.VISIBLE);
            fileUri = data.getData();
            binding.img.setImageURI(fileUri);
            File file =  ImagePicker.Companion.getFile(data);
            filePart = MultipartBody.Part.createFormData("invoice", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        TextView title = getActivity().findViewById(R.id.Actiontitle);
        title.setText(getActivity().getString(R.string.Accessories));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void DatePickerOpen(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    SELECT_DATE = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    binding.selectDatePicker.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                }, year, month, day);
        picker.show();

    }


    private boolean validation() {
        return DateSelectValidation() && FileValidation() ;
    }

    private boolean DateSelectValidation() {
        if(SELECT_DATE.isEmpty()){

            return false;
        }
        return true;
    }
    private boolean DescriptionSearchValidation(String text) {
        if(text.isEmpty()){
            binding.description.setError(getString(R.string.field_required));
            binding.DescriptionError.setVisibility(View.VISIBLE);
            return false;
        }
        binding.description.setError(null);
        binding.DescriptionError.setVisibility(View.GONE);
        return true;
    }

    private boolean FileValidation(){
        if (filePart == null) {
            Toast.makeText(getContext(), "Upload Image", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return  true;
    }




    private void submit(){

        binding.progressbar.setVisibility(View.VISIBLE);

        RequestBody srno = RequestBody.create(MediaType.parse("multipart/form-data"),binding.ImeiEdittext.getText().toString());
        RequestBody sell_date = RequestBody.create(MediaType.parse("multipart/form-data"), SELECT_DATE);
        RequestBody type = RequestBody.create(MediaType.parse("multipart/form-data"), TYPE);
        RequestBody retailer_id = RequestBody.create(MediaType.parse("multipart/form-data"), sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID));
        RequestBody retailer_name = RequestBody.create(MediaType.parse("multipart/form-data"), sessionManage.getUserDetails().get(SessionManage.NAME));
        RequestBody locality_id = RequestBody.create(MediaType.parse("multipart/form-data"), sessionManage.getUserDetails().get(SessionManage.LOCALITY_ID));
        RequestBody locality_name = RequestBody.create(MediaType.parse("multipart/form-data"), sessionManage.getUserDetails().get(SessionManage.LOCALITY));




        Log.e(TAG, "submit: "+  binding.ImeiEdittext.getText().toString());
        Log.e(TAG, "submit: "+  SELECT_DATE);
        Log.e(TAG, "submit: "+  TYPE);
        Log.e(TAG, "submit: "+  sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID));
        Log.e(TAG, "submit: "+  sessionManage.getUserDetails().get(SessionManage.NAME));
        Log.e(TAG, "submit: "+  sessionManage.getUserDetails().get(SessionManage.LOCALITY_ID));
        Log.e(TAG, "submit: "+  sessionManage.getUserDetails().get(SessionManage.LOCALITY));

        lavaInterface.ACCESORIES_REPLACEMENT_WARRENTY(filePart , sell_date ,type , srno,retailer_id, retailer_name,locality_id,locality_name).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                binding.submit.setCheckable(true);
                binding.submit.setFocusable(true);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");
                    if(error.equalsIgnoreCase("false")){

                        switch (error_code){
                            case 200:
                                AlertDialogfailure(message);
                                break;
                            case 301:
                                AlertDialogfailure(message);
                                break;
                            case 500:
                                AlertDialogfailure(message);
                                break;
                        }
                        binding.submit.setCheckable(true);
                        binding.submit.setFocusable(true);
//                        startActivity(new Intent(getContext() , MainActivity.class));
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

//                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    AlertDialogfailure(message);
                    binding.progressbar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                binding.submit.setCheckable(true);
                binding.submit.setFocusable(true);
                binding.progressbar.setVisibility(View.GONE);
//                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.submit.setCheckable(true);
                binding.submit.setFocusable(true);
                Toast.makeText(getContext(), "Time out", Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.VISIBLE);
            }
        });
    }





    @Override
    public void Onbackpress(String imei) {
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
            CheckWarrenty(imei);
        }
        onetime = false;
        Log.e(TAG, "OnbackpressBarcode: "+ imei );
    }

    @Override
    public void OnbackpressBarcode(String imei) {
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
            CheckWarrenty(imei);
        }
        onetime = false;
        Log.e(TAG, "OnbackpressBarcode: "+ imei );
    }



    void CheckWarrenty(String imei){
        Map<String , String> map = new HashMap<>();
        map.put("retailer_id",sessionManage.getUserDetails().get(SessionManage.USER_UNIQUE_ID));
        map.put("srno",imei);
        binding.progressbar.setVisibility(View.VISIBLE);
        lavaInterface.ACCESORIES_WARENTY_CHECK(map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                binding.addBtn.setFocusable(true);
                binding.addBtn.setClickable(true);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.optString("message");
                    int error_code = jsonObject.getInt("error_code");

                    if (error.equalsIgnoreCase("false")) {
                        JSONObject data = jsonObject.getJSONObject("detail");
                        String pName= data.optString("marketing_name");
                        String psku= data.optString("sku");
                        String pModel= data.optString("model");
                        String pNamear= data.optString("marketing_name_fr");
                        String pNamefr= data.optString("marketing_name_fr");
                        String Modelar= data.optString("model_ar");
                        SELECT_DATE  = data.optString("sell_out_date").substring(0,10);
                        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

                            AlertDialog(pName,psku,pModel);

                        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){
                            if(pNamefr.isEmpty()){
                                AlertDialog(pName,psku,pModel);
                            }else {
                                AlertDialog(pName,psku,pModel);
                            }

                        } else {
                            if(pNamear.isEmpty()){
                                AlertDialog(pName,psku,pModel);
                            }else {
                                AlertDialog(pNamear,psku,Modelar);
                            }
                        }
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
                    Snackbar.make(binding.getRoot(),message,5000).show();
                    AlertDialogfailure(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                binding.submitBtn.setClickable(true);
//                binding.submitBtn.setEnabled(true);
                binding.addBtn.setFocusable(true);
                binding.addBtn.setClickable(true);
                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.addBtn.setFocusable(true);
                binding.addBtn.setClickable(true);
                Snackbar.make(binding.getRoot(),t.getMessage(),5000).show();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    private void AlertDialogfailure(String msg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_imei_not_exits , null );
        builder.setView(v);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView des = v.findViewById(R.id.des);

        des.setText(msg);


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){

        } else {

        }




        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});



    }


    private void AlertDialog(String pname, String pSku, String pModel){
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() , R.style.CustomDialogstyple);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_imei_check , null );
        builder.setView(v);
        LinearLayout submit = v.findViewById(R.id.submit);
        LinearLayout no = v.findViewById(R.id.close);
        MaterialTextView name = v.findViewById(R.id.des);
        MaterialTextView sku = v.findViewById(R.id.sku);
        MaterialTextView model = v.findViewById(R.id.model);


        name.setText(pname);
        sku.setText("SKU - "+ pSku);
        model.setText("Model - "+ pModel);



        if (sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {

        }else if(sessionManage.getUserDetails().get("LANGUAGE").equals("fr")){

        } else {

        }




        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        submit.setOnClickListener(view -> {
            submit.setEnabled(false);
            submit.setClickable(false);
            alertDialog.dismiss();
        });
        no.setOnClickListener(view -> {alertDialog.dismiss();});



    }


    private void addView(Drawable drawable , int type , String modals , String imeis , String distributor_name , String mess ) {

        int a = imeiCount += 1;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.add_view, null);
        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);

        textView = rowView.findViewById(R.id.imei);
        mainLayout = rowView.findViewById(R.id.mainLayout);
        Modal = rowView.findViewById(R.id.Modal);
        TextView ModalTitle = rowView.findViewById(R.id.ModalTitle);
        TextView imei = rowView.findViewById(R.id.imei);
        TextView distributorName = rowView.findViewById(R.id.distributorName);
        TextView msg = rowView.findViewById(R.id.msg);
        ImageView icon  = rowView.findViewById(R.id.icon);

        mainLayout.setBackground(drawable);
        textView.setText(binding.ImeiEdittext.getText().toString().trim());
        textView.setTag(String.valueOf(a));
        removeBtn = rowView.findViewById(R.id.remove);

        if (type == 200) {
            icon.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_check, null));
            distributorName.setText(distributor_name);
            msg.setText(mess);
            msg.setTextColor(getResources().getColor(R.color.green));
        } else {
            icon.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic__check, null));
            distributorName.setText(distributor_name);
            msg.setText(mess);
            msg.setTextColor(getResources().getColor(R.color.yellow));
        }
        Modal.setText(modals);
        imei.setText(imeis);


        removeView();
        binding.ImeiEdittext.setText(null);
        NoData = true;
        Wrongdatra = true;
    }

    private void removeView() {
        removeBtn.setOnClickListener(v -> {
            binding.addLayout.removeView((View) v.getParent().getParent().getParent());
            if (binding.addLayout.getChildCount() == 0) {
                NoData = true;
                Wrongdatra = true;
                imeiCount = 0;
                binding.msgShowWrongImei.setVisibility(View.GONE);
            }
        });
    }

}
















































