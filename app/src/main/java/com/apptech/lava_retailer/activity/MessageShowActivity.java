package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.MessageShowAdapter;
import com.apptech.lava_retailer.databinding.ActivityMessageShowBinding;
import com.apptech.lava_retailer.list.notificationList.NotificationModel;
import com.apptech.lava_retailer.modal.notification_list.NotificationListShow;
import com.apptech.lava_retailer.other.NetworkCheck;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageShowActivity extends AppCompatActivity {

    ActivityMessageShowBinding binding;
    private static final String TAG = "MessageShowActivity";
    MessageShowAdapter messageShowAdapter;
    SessionManage sessionManage;
    LavaInterface lavaInterface;
    MessageShowAdapter.MessageShowInterface messageShowInterface;
    JSONObject mainJsonObject = new JSONObject();
    java.util.List<NotificationListShow> notificationListShows = new ArrayList<>();
    int NEXTACTITIVY = 0;
    AlertDialog alertDialog1;
    private List<com.apptech.lava_retailer.list.notificationList.List> Newslist;
    private boolean isFirstBackPressed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        sessionManage = SessionManage.getInstance(this);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
            try {
                mainJsonObject = new JSONObject(sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(new NetworkCheck().haveNetworkConnection(this)){
            binding.noproduct.setVisibility(View.GONE);
            MessageShow1();
        }else {
            binding.noproduct.setVisibility(View.VISIBLE);
            Toast.makeText(this, "" + getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }

        binding.nextBtn.setOnClickListener(v -> {
            if (sessionManage.getUserDetails().get("BRAND_ID") != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }
            startActivity(new Intent(this, BrandActivity.class));
            finish();
        });

        binding.nextScreenbutton.setOnClickListener(v -> {
            OtpVerificationDialog();
        });

        messageShowInterface = new MessageShowAdapter.MessageShowInterface() {
            @Override
            public void itemClick(NotificationListShow list) {

            }

            @Override
            public void removeitem(int pos, NotificationListShow list) {

            }

            @Override
            public void ExpansionLayout(int pos, NotificationListShow list , ImageView img1) {

                    try {

                        if (sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
                            mainJsonObject = new JSONObject(Objects.requireNonNull(sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE")));
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("POS" , pos);
                        jsonObject.put("ID" , list.getId());
                        mainJsonObject.putOpt(list.getId() , jsonObject);
                        Log.e(TAG, "ExpansionLayout: " + mainJsonObject.toString());
                        sessionManage.NotificationStore(mainJsonObject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        };


    }


    private void MessageShow1() {

        lavaInterface.NotificationList1().enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if(response.isSuccessful()){

                    if(!response.body().getError()){

                        binding.nextScreenbutton.setVisibility(View.VISIBLE);

                        Newslist = response.body().getList();

                        if (mainJsonObject.length() > 0 || sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
//


                            for (int i = 0; i < response.body().getList().size(); i++) {

                                com.apptech.lava_retailer.list.notificationList.List l = response.body().getList().get(i);

                                String id = l.getId();

                                try {
                                    mainJsonObject.getString(id);
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();

                                    notificationListShows.add(new NotificationListShow(
                                            l.getId(),
                                            l.getHeading(),
                                            l.getDes(),
                                            l.getImg(),
                                            l.getTime(),
                                            l.getBrandId(),
                                            l.getBrandName()
                                    ));

                                }

                            }
                            messageShowAdapter = new MessageShowAdapter(notificationListShows, messageShowInterface);
                            binding.messageRecyclerview.setAdapter(messageShowAdapter);

                            binding.nextScreenbutton.setVisibility(View.VISIBLE);

                            binding.msgCount.setText(String.valueOf(notificationListShows.size()));
                            if (mainJsonObject.length() == response.body().getList().size()) {
                                if (sessionManage.getUserDetails().get("BRAND_ID") != null) {
                                    startActivity(new Intent(MessageShowActivity.this, MainActivity.class));
                                    finish();
                                    return;
                                }
                                startActivity(new Intent(MessageShowActivity.this, BrandActivity.class));
                                finish();
                            }
                            binding.progressbar.setVisibility(View.GONE);
                            return;
                        }


                        for (int i = 0; i < response.body().getList().size(); i++) {
                            com.apptech.lava_retailer.list.notificationList.List l = response.body().getList().get(i);
                            notificationListShows.add(new NotificationListShow(
                                    l.getId(),
                                    l.getHeading(),
                                    l.getDes(),
                                    l.getImg(),
                                    l.getTime(),
                                    l.getBrandId(),
                                    l.getBrandName()
                            ));
                        }
                        messageShowAdapter = new MessageShowAdapter(notificationListShows, messageShowInterface);
                        binding.messageRecyclerview.setAdapter(messageShowAdapter);
                        binding.msgCount.setText(String.valueOf(notificationListShows.size()));
                        binding.progressbar.setVisibility(View.GONE);
                        binding.nextScreenbutton.setVisibility(View.VISIBLE);
                        return;
                    }
                    Toast.makeText(MessageShowActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    return;
                }

                Toast.makeText(MessageShowActivity.this, "" + getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                binding.progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                binding.progressbar.setVisibility(View.GONE);
                binding.nomsg.setVisibility(View.VISIBLE);
                Toast.makeText(MessageShowActivity.this, "Time out", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OtpVerificationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.row_news , null);
        LinearLayout close = view.findViewById(R.id.close);
        LinearLayout submit = view.findViewById(R.id.submit);

        MaterialTextView msg = view.findViewById(R.id.msg);

//        Log.e(TAG, "OtpVerificationDialog: " + Newslist.size());
//        Log.e(TAG, "OtpVerificationDialog: " + mainJsonObject.length());

//        if(Newslist.size() == mainJsonObject.length()){
//            msg.setText("Read all your are latest news");
//        }else {
            msg.setText("Please read all the latest news");
//        }

        close.setOnClickListener(v -> {alertDialog1.dismiss();});
        submit.setOnClickListener(v -> {

        });

        if(Newslist.size() == mainJsonObject.length()){
            startActivity(new Intent(MessageShowActivity.this, BrandActivity.class));
            finish();
            return;
        }

        builder.setView(view);
        alertDialog1 = builder.create();
        alertDialog1.show();
    }


    @Override
    public void onBackPressed() {
        if (isFirstBackPressed) {

//            super.onBackPressed();

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
            System.exit(0);

        } else {
            isFirstBackPressed = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                    public void run() {
                        isFirstBackPressed = false;
                    }
                }, 1500);
        }
    }
}






























