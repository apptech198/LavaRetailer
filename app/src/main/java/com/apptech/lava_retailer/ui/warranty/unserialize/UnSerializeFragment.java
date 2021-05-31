package com.apptech.lava_retailer.ui.warranty.unserialize;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.activity.MainActivity;
import com.apptech.lava_retailer.databinding.UnSerializeFragmentBinding;
import com.apptech.lava_retailer.fragment.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.apptech.lava_retailer.ui.barcode_scanner.BarCodeScannerFragment;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;

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
    private String SELECT_DATE ="";
    private Uri fileUri;
    MultipartBody.Part filePart= null;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    NavController navController;
    ScannerFragment scannerFragment;
    BarCodeScannerFragment barCodeScannerFragment;
    boolean onetime = true;


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
                if (validation()) {
                    submit();
                    return;
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
                    break;
                case R.id.unsearilize:
                    binding.Searilizelayout.setVisibility(View.GONE);
                    binding.note.setVisibility(View.VISIBLE);
            }
        });

        binding.scanBtn.setOnClickListener(v -> {
            onetime = true;
            Log.e(TAG, "onActivityCreated: " + "clicked" );
            loadfragment(barCodeScannerFragment);
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
            filePart = MultipartBody.Part.createFormData("img_url", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));


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
        return DateSelectValidation() && DescriptionSearchValidation(binding.description.getText().toString().trim()) && FileValidation() ;
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

        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(sessionManage.getUserDetails().get("ID")));
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(sessionManage.getUserDetails().get("NAME")));
        RequestBody des = RequestBody.create(MediaType.parse("multipart/form-data"), binding.description.getText().toString().trim());
        RequestBody date = RequestBody.create(MediaType.parse("multipart/form-data"), SELECT_DATE);


        lavaInterface.WARRANTY_NO_SERIALIZED(filePart , id ,name , date,des).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.optString("error");
                    String message = jsonObject.optString("message");

                    if(error.equalsIgnoreCase("false")){
                        startActivity(new Intent(getContext() , MainActivity.class));
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }

                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                binding.progressbar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
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
        }
        onetime = false;
        Log.e(TAG, "OnbackpressBarcode: "+ imei );
    }

    @Override
    public void OnbackpressBarcode(String imei) {
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(barCodeScannerFragment).addToBackStack(null).commit();
        }
        onetime = false;
        Log.e(TAG, "OnbackpressBarcode: "+ imei );
    }
}
















































