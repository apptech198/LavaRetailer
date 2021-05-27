package com.apptech.lava_retailer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.adapter.SuccessAdapter;
import com.apptech.lava_retailer.modal.card.CardList;

import java.util.List;

public class SuccessActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.OrderDetailsShow);

        String amt = getIntent().getStringExtra("amt");
        TextView amount = findViewById(R.id.amount);
        amount.setText(amt);

        LinearLayout btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(SuccessActivity.this , MainActivity.class));
            finish();
        });

        List<CardList> cartlist = ((CardList) getApplicationContext()).getCardLists();
        recyclerView.setAdapter(new SuccessAdapter(cartlist));

    }





    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}