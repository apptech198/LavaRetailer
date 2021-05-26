package com.apptech.lava_retailer.fragment.sellout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.SellOutFragmentBinding;
import com.apptech.lava_retailer.fragment.ScannerFragment;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SellOutFragment extends Fragment implements ScannerFragment.BackPress {

    private SellOutViewModel mViewModel;
    SellOutFragmentBinding binding;
    private static final String TAG = "SellOutFragment";
    DatePickerDialog picker;
    View rowView;
    LinearLayout removeBtn;
    ScannerFragment scannerFragment;
    boolean onetime = true;
    TextView textView;
    int imeiCount = 0;
    LavaInterface lavaInterface;
    SessionManage sessionManage;
    String USER_ID = "", TodayDate = "";
    private boolean NoData = true, Wrongdatra = true;
    JsonArray jsonElements = new JsonArray();
    JsonObject mainJsonObject = new JsonObject();
    LinearLayout mainLayout;


    public static SellOutFragment newInstance() {
        return new SellOutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SellOutFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SellOutViewModel.class);
        // TODO: Use the ViewModel

        binding.startDatetime.setText(getCurrentDate());
        scannerFragment = new ScannerFragment(this);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        sessionManage = SessionManage.getInstance(requireContext());

        USER_ID = sessionManage.getUserDetails().get("ID");
        TodayDate = getCurrentDate();


        binding.addBtn.setOnClickListener(v -> {
            addImei();
        });

        binding.startDatetime.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
//                            eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        binding.startDatetime.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        Log.e(TAG, "onDateSet: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

        binding.scanBtn.setOnClickListener(v -> {
            onetime = true;
            loadfragment(scannerFragment);
        });


        binding.submitBtn.setOnClickListener(v -> {

            binding.submitBtn.setClickable(false);
            binding.submitBtn.setEnabled(false);

            if (NoData) {
                if (new NetworkCheck().haveNetworkConnection(requireActivity())) {
                    binding.progressbar.setVisibility(View.VISIBLE);
                    jsonElements = new JsonArray();
                    for (int i = 0; i < binding.addLayout.getChildCount(); i++) {
                        JsonObject jsonObject = new JsonObject();
                        try {
                            int getid = i + 1;
                            TextView textView = binding.addLayout.findViewWithTag(String.valueOf(getid));
                            jsonObject.addProperty("imei", textView.getText().toString());
                            jsonElements.add(jsonObject);
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onActivityCreated: " + e.getMessage());
                        }

                    }
                    submitImei();
                    binding.addLayout.removeAllViews();
                }
                binding.submitBtn.setClickable(true);
                binding.submitBtn.setEnabled(true);
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.remove_first_invalid_imei), Toast.LENGTH_SHORT).show();
            binding.submitBtn.setClickable(true);
            binding.submitBtn.setEnabled(true);

        });

    }


    void submitImei() {
        Log.e(TAG, "submitImei: " + binding.startDatetime.getText().toString().trim());

        mainJsonObject.addProperty("date", binding.startDatetime.getText().toString().trim());
        mainJsonObject.addProperty("retailer_id", USER_ID);
        mainJsonObject.add("imei_list", jsonElements);

        Log.e(TAG, "submitImei: " + mainJsonObject);

        /*Call call = lavaInterface.SellOut_PriceDropEntry(mainJsonObject);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    int error_code = jsonObject.getInt("error_code");
                    if (error.equalsIgnoreCase("false")) {

                        imeiCount = 0;
                        binding.addLayout.removeAllViews();

                        try {
                            if (error_code == 401) {
                                String jsonArraySrtring = jsonObject.getString("dl");
                                JSONArray jsonArray = new JSONArray(jsonArraySrtring);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    wrongimei(jsonArray.getJSONObject(i).getString("imei"));
                                }
                                binding.msgShowWrongImei.setText(getResources().getString(R.string.imei_allready_exists));
                                binding.msgShowWrongImei.setVisibility(View.VISIBLE);
                                NoData = false;
                            } else {
                                binding.msgShowWrongImei.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }

                        binding.submitBtn.setClickable(true);
                        binding.submitBtn.setEnabled(true);
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    binding.submitBtn.setEnabled(true);
                    binding.submitBtn.setClickable(true);
                    Toast.makeText(requireContext(), "" + message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.submitBtn.setClickable(true);
                binding.submitBtn.setEnabled(true);
                binding.progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.submitBtn.setClickable(true);
                binding.submitBtn.setEnabled(true);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
        */
    }


    private void wrongimei(String imei) {
        int a = imeiCount += 1;
        Log.e(TAG, "addImei: idset" + a);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.add_view, null);
        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);
        textView = rowView.findViewById(R.id.imei);
        mainLayout = rowView.findViewById(R.id.mainLayout);
        mainLayout.setBackground(getResources().getDrawable(R.drawable.wrong_imei));
        textView.setText(imei);
        textView.setTag(String.valueOf(a));
        removeBtn = rowView.findViewById(R.id.remove);
        removeView();
        binding.ImeiEdittext.setText(null);
        Wrongdatra = false;
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


    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.LoadFragment, fragment).addToBackStack(null).commit();
    }


    @Override
    public void Onbackpress(String imei) {
        Log.e(TAG, "Onbackpress: " + imei);
        if (onetime) {
            binding.ImeiEdittext.setText(imei);
            getChildFragmentManager().beginTransaction().remove(scannerFragment).addToBackStack(null).commit();
            addImei();
        }
        onetime = false;
    }

    private void addImei() {
        if (ImeiValid(binding.ImeiEdittext.getText().toString().trim())) {
            if (Wrongdatra) {
                addView();
                return;
            }
            Toast.makeText(requireContext(), getResources().getString(R.string.remove_first_invalid_imei), Toast.LENGTH_SHORT).show();
        }
    }

    private void addView() {
        int a = imeiCount += 1;
        Log.e(TAG, "addImei: idset" + a);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.add_view, null);
        binding.addLayout.addView(rowView, binding.addLayout.getChildCount() - 1);
        textView = rowView.findViewById(R.id.imei);
        textView.setText(binding.ImeiEdittext.getText().toString().trim());
        textView.setTag(String.valueOf(a));
        removeBtn = rowView.findViewById(R.id.remove);
        removeView();
        binding.ImeiEdittext.setText(null);
        NoData = true;
        Wrongdatra = true;
    }

    private boolean ImeiValid(String imei) {
        if (imei.isEmpty()) {
            Toast.makeText(requireContext(), getResources().getString(R.string.field_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imei.length() != 15) {
            Toast.makeText(requireContext(), "Invalid Imei code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}




























