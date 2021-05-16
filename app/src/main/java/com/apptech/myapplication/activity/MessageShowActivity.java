package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.MessageShowAdapter;
import com.apptech.myapplication.databinding.ActivityMessageShowBinding;
import com.apptech.myapplication.list.notificationList.NotificationModel;
import com.apptech.myapplication.modal.notification_list.NotificationListShow;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
import com.apptech.myapplication.service.LavaInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        sessionManage = SessionManage.getInstance(this);
        lavaInterface = ApiClient.getClient().create(LavaInterface.class);

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

        messageShowInterface = new MessageShowAdapter.MessageShowInterface() {
            @Override
            public void itemClick(NotificationListShow list) {

            }

            @Override
            public void removeitem(int pos, NotificationListShow list) {

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

                        if (mainJsonObject.length() > 0 || sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {

                            for (int i = 0; i < jsonElements.length(); i++) {
                                JSONObject object = jsonElements.getJSONObject(i);
                                try {
                                    mainJsonObject.getString(object.getString("id"));
                                    NEXTACTITIVY += 1;
                                } catch (JSONException e) {
                                    Log.e(TAG, "onResponse: " + e.getMessage());
                                    notificationListShows.add(new NotificationListShow(
                                            object.getString("id"),
                                            object.getString("heading"),
                                            object.getString("des"),
                                            object.getString("img"),
                                            object.getString("time"),
                                            object.getString("brand_id"),
                                            object.getString("brand_name")
                                    ));
                                }
                            }
                            messageShowAdapter = new MessageShowAdapter(notificationListShows, messageShowInterface);
                            binding.messageRecyclerview.setAdapter(messageShowAdapter);

                            binding.msgCount.setText(String.valueOf(notificationListShows.size()));
                            if (NEXTACTITIVY == jsonElements.length()) {
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

                        if (mainJsonObject.length() > 0 || sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {

                            for (int i = 0; i < response.body().getList().size(); i++) {

                                com.apptech.myapplication.list.notificationList.List l = response.body().getList().get(i);
                                try {

                                    mainJsonObject.getString(l.getId());
                                    NEXTACTITIVY += 1;
                                } catch (JSONException e) {
                                    Log.e(TAG, "onResponse: " + e.getMessage());
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

                            binding.msgCount.setText(String.valueOf(notificationListShows.size()));
                            if (NEXTACTITIVY == response.body().getList().size()) {
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
                            com.apptech.myapplication.list.notificationList.List l = response.body().getList().get(i);
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


}






























