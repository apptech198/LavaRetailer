package com.apptech.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.apptech.myapplication.adapter.CardAdapter;
import com.apptech.myapplication.databinding.ActivityCartBinding;
import com.apptech.myapplication.modal.card.CardList;
import com.apptech.myapplication.other.SessionManage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CardAdapter.CardInterface {

    SessionManage sessionManage;
    ActivityCartBinding binding;
    private static final String TAG = "CartActivity";
    List<CardList> cardData = new ArrayList<>();
    CardAdapter cardAdapter;
    JSONObject MainjsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Cart");

//        sessionManage = new SessionManage(this);
        sessionManage = SessionManage.getInstance(this);

        String json = sessionManage.getUserDetails().get("CARD_DATA");


        if (json != null) {
            binding.PriceDetailsLayout.setVisibility(View.VISIBLE);
            binding.NoItem.setVisibility(View.GONE);
            JSONObject issueObj = null;
            try {
                JSONObject jsonObject = new JSONObject(json);
                MainjsonObject = jsonObject;

                issueObj = new JSONObject(json);
                Iterator iterator = issueObj.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = issueObj.getJSONObject(key);
                    String _pubKey = issue.optString("id");
                    cardData.add(new CardList(MainjsonObject.getJSONObject(_pubKey).getString("id"), MainjsonObject.getJSONObject(_pubKey).getString("name"), MainjsonObject.getJSONObject(_pubKey).getString("img"), MainjsonObject.getJSONObject(_pubKey).getString("qty")));
                }

                cardAdapter = new CardAdapter(cardData, this);
                binding.CardRecyclerView.setAdapter(cardAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            binding.PriceDetailsLayout.setVisibility(View.GONE);
            binding.NoItem.setVisibility(View.VISIBLE);
        }

        binding.PlaceOrder.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this , AddressActivity.class));
        });

    }

    @Override
    public void removeItem(int postion, CardList list) {


        cardData.remove(postion);
        MainjsonObject.remove(list.getId());
        sessionManage.addcard(MainjsonObject.toString());
        Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
        if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
        cardAdapter.notifyDataSetChanged();


//        List<CardList> newList = new ArrayList<>();
//        newList.addAll(cardData);
//        newList.remove(postion);
//        cardAdapter.setData(newList);


    }


}






































