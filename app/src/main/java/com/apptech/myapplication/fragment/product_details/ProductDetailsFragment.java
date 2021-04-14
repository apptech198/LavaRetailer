package com.apptech.myapplication.fragment.product_details;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.activity.CartActivity;
import com.apptech.myapplication.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.myapplication.databinding.ProductDetailsFragmentBinding;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.other.SessionManage;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ProductDetailsFragment extends Fragment {

    private ProductDetailsViewModel mViewModel;
    ProductList list;
    ProductDetailsFragmentBinding binding;
    SessionManage sessionManage;
    private static final String TAG = "ProductDetailsFragment";
    JSONObject MainjsonObject = new JSONObject();


    public ProductDetailsFragment(ProductList list) {
        this.list = list;
    }

    public static ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment(newInstance().list);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ProductDetailsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        // TODO: Use the ViewModel

//        sessionManage = new SessionManage(requireContext());
        sessionManage = SessionManage.getInstance(requireContext());
        qtyset();
        cardQuntyUpdate();


        Glide.with(binding.image)
                .load(list.getImg())
                .fitCenter()
                .into(binding.image);
        binding.productName.setText(list.getName());

        Glide.with(binding.mobImg)
                .load(list.getImg())
                .fitCenter()
                .into(binding.mobImg);
        binding.productName.setText(list.getName());
        binding.colorName.setText("Fantasy White");
        binding.sizeName.setText("6 Gb");
        binding.productAmtDic.setPaintFlags(binding.productAmtDic.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        binding.ProductAdd.setOnClickListener(v -> {

            binding.ProductAdd.setVisibility(View.GONE);
            binding.PlusMinusLayout.setVisibility(View.VISIBLE);

            binding.qtyCount.setText("1");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", list.getId());
                jsonObject.put("name", list.getName());
                jsonObject.put("img", list.getImg());
                jsonObject.put("qty", "1");
                MainjsonObject.put(list.getId(), jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sessionManage.addcard(MainjsonObject.toString());

            binding.ProductAdd.setVisibility(View.GONE);
            binding.PlusMinusLayout.setVisibility(View.VISIBLE);
            cardQuntyUpdate();
        });


        binding.plus.setOnClickListener(v -> {
            int add = Integer.parseInt(binding.qtyCount.getText().toString().trim()) + 1;
            binding.qtyCount.setText(String.valueOf(add));
            try {
                MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sessionManage.addcard(MainjsonObject.toString());
        });


        binding.sub.setOnClickListener(v -> {


            int a = Integer.parseInt(binding.qtyCount.getText().toString().trim());
            if (a > 1) {

                binding.ProductAdd.setVisibility(View.GONE);
                binding.PlusMinusLayout.setVisibility(View.VISIBLE);

                int add = Integer.parseInt(binding.qtyCount.getText().toString().trim()) - 1;
                binding.qtyCount.setText(String.valueOf(add));
                try {
                    MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "minus: " + MainjsonObject.toString());
                sessionManage.addcard(MainjsonObject.toString());

            } else {

                binding.ProductAdd.setVisibility(View.VISIBLE);
                binding.PlusMinusLayout.setVisibility(View.GONE);
                MainjsonObject.remove(list.getId());
                sessionManage.addcard(MainjsonObject.toString());
                Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
                if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
                cardQuntyUpdate();
            }


        });

        binding.viewCardFloat.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CartActivity.class));
        });
    }


    private void qtyset() {
        String json = sessionManage.getUserDetails().get("CARD_DATA");

        if (json != null) {
            JSONObject issueObj = null;
            try {
                JSONObject jsonObject = new JSONObject(json);
                MainjsonObject = jsonObject;
                issueObj = new JSONObject(json);
                Iterator iterator = issueObj.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    JSONObject issue = issueObj.getJSONObject(key);
                    //  get id from  issue
                    String _pubKey = issue.optString("id");
                    if (list.getId().equals(_pubKey)) {
                        binding.qtyCount.setText(jsonObject.getJSONObject(_pubKey).getString("qty"));
                        binding.PlusMinusLayout.setVisibility(View.VISIBLE);
                        binding.ProductAdd.setVisibility(View.GONE);
                        break;
                    } else {
                        binding.PlusMinusLayout.setVisibility(View.GONE);
                        binding.ProductAdd.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            binding.ProductAdd.setVisibility(View.VISIBLE);
            binding.PlusMinusLayout.setVisibility(View.GONE);
        }
    }

    void cardQuntyUpdate() {
        if (MainjsonObject.length() == 0) {
            binding.card.cardRound.setVisibility(View.GONE);
        } else {
            binding.card.cardRound.setVisibility(View.VISIBLE);
            binding.card.countCard.setText(String.valueOf(MainjsonObject.length()));
        }
    }

}


































