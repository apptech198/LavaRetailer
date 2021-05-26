package com.apptech.lava_retailer.activity;

import android.content.Intent;
import android.os.Bundle;
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
                JSONObject jsonObject = new JSONObject(sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE"));
                mainJsonObject = jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        MessageShow1();

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

/*
                try {
                    if (sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
                        try {
                            mainJsonObject = new JSONObject(sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ID", list.getId());
                        mainJsonObject.putOpt(list.getId(), jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sessionManage.NotificationStore(mainJsonObject.toString());
                    List<NotificationListShow> notificationLists = new ArrayList<>(notificationListShows);
                    notificationLists.remove(pos);
                    messageShowAdapter.setData(notificationLists);
                    binding.msgCount.setText(String.valueOf(notificationLists.size()));
                    if (notificationLists.size() == 0) dialogOpen();


                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                    Log.e(TAG, "removeitem: " + e.getMessage() );
                    dialogOpen();
                }
*/


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


    private void dialogOpen() {
        binding.countLayout.setVisibility(View.GONE);
        binding.messageRecyclerview.setVisibility(View.GONE);
        binding.nomsg.setVisibility(View.VISIBLE);
        binding.nextBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setVisibility(View.VISIBLE);
    }


    private void MessageShow() {

        lavaInterface.NotificationList().enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                try {
                    String json = String.valueOf(response.body());

                    JSONObject jsonObject1 = new JSONObject(json);
                    String error = jsonObject1.getString("error");
                    String message = jsonObject1.getString("message");
                    JSONArray jsonElements = jsonObject1.getJSONArray("list");

                    if (error.equals("false")) {

//                        if (mainJsonObject.length() > 0 || sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
//
//                            for (int i = 0; i < jsonElements.length(); i++) {
//                                JSONObject object = jsonElements.getJSONObject(i);
//                                try {
//                                    mainJsonObject.getString(object.getString("id"));
//                                    NEXTACTITIVY += 1;
//                                } catch (JSONException e) {
//                                    Log.e(TAG, "onResponse: " + e.getMessage());
//                                    notificationListShows.add(new NotificationListShow(
//                                            object.getString("id"),
//                                            object.getString("heading"),
//                                            object.getString("des"),
//                                            object.getString("img"),
//                                            object.getString("time"),
//                                            object.getString("brand_id"),
//                                            object.getString("brand_name")
//                                    ));
//                                }
//                            }
//                            messageShowAdapter = new MessageShowAdapter(notificationListShows, messageShowInterface);
//                            binding.messageRecyclerview.setAdapter(messageShowAdapter);
//
//                            binding.msgCount.setText(String.valueOf(notificationListShows.size()));
//                            if (NEXTACTITIVY == jsonElements.length()) {
//                                if (sessionManage.getUserDetails().get("BRAND_ID") != null) {
//                                    startActivity(new Intent(MessageShowActivity.this, MainActivity.class));
//                                    finish();
//                                    return;
//                                }
//                                startActivity(new Intent(MessageShowActivity.this, BrandActivity.class));
//                                finish();
//                            }
//                            binding.progressbar.setVisibility(View.GONE);
//                            return;
//                        }


                        for (int i = 0; i < jsonElements.length(); i++) {

                            JSONObject object = jsonElements.getJSONObject(i);
                            notificationListShows.add(new NotificationListShow(
                                    object.getString("id"),
                                    object.getString("heading"),
                                    object.getString("des"),
                                    object.getString("img"),
//                                    object.getString("time"),
                                    "",
                                    object.getString("brand_id"),
                                    object.getString("brand_name")
                            ));
                        }

                        if (mainJsonObject.length() > 0 || sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
                            binding.msgCount.setText(String.valueOf(notificationListShows.size()));
                            if (mainJsonObject.length() == jsonElements.length()) {
                                if (sessionManage.getUserDetails().get("BRAND_ID") != null) {
                                    startActivity(new Intent(MessageShowActivity.this, MainActivity.class));
                                    finish();
                                    return;
                                }
                                startActivity(new Intent(MessageShowActivity.this, BrandActivity.class));
                                finish();
                            }
                        }

                        messageShowAdapter = new MessageShowAdapter(notificationListShows, messageShowInterface);
                        binding.messageRecyclerview.setAdapter(messageShowAdapter);
                        binding.msgCount.setText(String.valueOf(notificationListShows.size()));
                        binding.progressbar.setVisibility(View.GONE);
                        return;
                    }
                    binding.progressbar.setVisibility(View.GONE);
                    Toast.makeText(MessageShowActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: " + e.getMessage() );
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void MessageShow1() {

        lavaInterface.NotificationList1().enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));

                if(response.isSuccessful()){

                    if(!response.body().getError()){

                        Newslist = response.body().getList();

                        if (mainJsonObject.length() > 0 || sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
//
                            for (int i = 0; i < response.body().getList().size(); i++) {

                                com.apptech.lava_retailer.list.notificationList.List l = response.body().getList().get(i);
//                                try {
//                                    mainJsonObject.getString(l.getId());
//                                    NEXTACTITIVY += 1;
//                                } catch (JSONException e) {
//                                    Log.e(TAG, "onResponse: " + e.getMessage());
                                    notificationListShows.add(new NotificationListShow(
                                            l.getId(),
                                            l.getHeading(),
                                            l.getDes(),
                                            l.getImg(),
                                            l.getTime(),
                                            l.getBrandId(),
                                            l.getBrandName()
                                    ));
//                                }
                            }
                            messageShowAdapter = new MessageShowAdapter(notificationListShows, messageShowInterface);
                            binding.messageRecyclerview.setAdapter(messageShowAdapter);

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

            }
        });

    }


    private void OtpVerificationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.row_news , null);
        LinearLayout close = view.findViewById(R.id.close);
        LinearLayout submit = view.findViewById(R.id.submit);

        MaterialTextView msg = view.findViewById(R.id.msg);

        Log.e(TAG, "OtpVerificationDialog: " + Newslist.size());
        Log.e(TAG, "OtpVerificationDialog: " + mainJsonObject.length());



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

}






























