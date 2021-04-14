package com.apptech.myapplication.fragment.purchase_request_now;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.activity.CartActivity;
import com.apptech.myapplication.adapter.PurchaseNowAdapter;
import com.apptech.myapplication.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.myapplication.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.myapplication.databinding.PurchaseRequestNowFragmentBinding;
import com.apptech.myapplication.modal.card.CardList;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.other.SpacesItemDecoration;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestNowFragment extends Fragment implements PurchaseNowAdapter.PurchaseNowIterface {

    private PurchaseRequestNowViewModel mViewModel;
    PurchaseRequestNowFragmentBinding binding;
    List<ProductList> productLists = new ArrayList<>();
    List<CardList> cardLists = new ArrayList<>();
    private static final String TAG = "PurchaseRequestNowFragm";
    SessionManage sessionManage;
    String json;
    onbackFragmentLoad onbackFragmentLoad;
    JSONObject MainjsonObject = new JSONObject();

//    public static PurchaseRequestNowFragment newInstance() {
//        return new PurchaseRequestNowFragment(b);
//    }

    public PurchaseRequestNowFragment(onbackFragmentLoad onbackFragmentLoad) {
        this.onbackFragmentLoad = onbackFragmentLoad;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PurchaseRequestNowFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PurchaseRequestNowViewModel.class);
        // TODO: Use the ViewModel
//        sessionManage = new SessionManage(requireContext());
        sessionManage = SessionManage.getInstance(requireContext());
//        sessionManage.clearaddcard( );
        cardQuntyUpdate();
        productLists.clear();
        fakeProduct();


        json = sessionManage.getUserDetails().get("CARD_DATA");
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                MainjsonObject = jsonObject;
                cardQuntyUpdate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        binding.viewCardFloat.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), CartActivity.class));
        });
        binding.noproduct.setVisibility(View.GONE);
        binding.PurchaseNowRecyclerView.setAdapter(new PurchaseNowAdapter(productLists, this));
        binding.PurchaseNowRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

        binding.filterShort.setOnClickListener(v -> {
            new ShortFilterBottomSheetFragment().show(getChildFragmentManager(), "short bottom sheet");
        });
        binding.filterPrice.setOnClickListener(v -> {
            new CategoryFilterBottomSheetFragment().show(getChildFragmentManager(), "category filter");
        });


    }


    private void fakeProduct() {
        productLists.add(new ProductList("1", "iphone 11", "https://assets.kogan.com/files/product/etail/Apple-/KHIP11128BLK_1.jpg?auto=webp&canvas=753%2C502&fit=bounds&height=502&quality=75&width=753", ""));
        productLists.add(new ProductList("2", "oneplus 6", "https://cdn-files.kimovil.com/default/0002/40/thumb_139877_default_big.jpeg", ""));
        productLists.add(new ProductList("3", "Xiaomi Phones in Kenya", "https://sokobest.co.ke/wp-content/uploads/2019/09/Mi-Xiaomi-Mi-A2-1024x1024.jpg", ""));
        productLists.add(new ProductList("4", "iphone 11", "https://assets.kogan.com/files/product/etail/Apple-/KHIP11128BLK_1.jpg?auto=webp&canvas=753%2C502&fit=bounds&height=502&quality=75&width=753", ""));
        productLists.add(new ProductList("5", "oneplus 6", "https://cdn-files.kimovil.com/default/0002/40/thumb_139877_default_big.jpeg", ""));
        productLists.add(new ProductList("6", "Xiaomi Phones in Kenya", "https://sokobest.co.ke/wp-content/uploads/2019/09/Mi-Xiaomi-Mi-A2-1024x1024.jpg", ""));
    }


    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.framelayout1, fragment).addToBackStack(null).commit();
    }


    @Override
    public void itemClick(ProductList list) {
        onbackFragmentLoad.onBack(list);
    }

    @Override
    public void addItem(ProductList list, int pos, TextView countTextView) {

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
        cardQuntyUpdate();
    }

    @Override
    public void minus(ProductList list, int position, TextView count) {
        int add = Integer.parseInt(count.getText().toString().trim()) - 1;
        count.setText(String.valueOf(add));
        try {
            MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "minus: " + MainjsonObject.toString());
        sessionManage.addcard(MainjsonObject.toString());
    }


    @Override
    public void RemoveItem(ProductList list, int postion, TextView countqty) {
        MainjsonObject.remove(list.getId());
        sessionManage.addcard(MainjsonObject.toString());
        Log.e(TAG, "RemoveItem: " + MainjsonObject.length());
        if (MainjsonObject.length() == 0) sessionManage.clearaddcard();
        cardQuntyUpdate();
    }


    @Override
    public void QtyAdd(ProductList list, int position, TextView count) {
        int add = Integer.parseInt(count.getText().toString().trim()) + 1;
        count.setText(String.valueOf(add));
        try {
            MainjsonObject.getJSONObject(list.getId()).put("qty", String.valueOf(add));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sessionManage.addcard(MainjsonObject.toString());


    }

    void cardQuntyUpdate() {
        if (MainjsonObject.length() == 0) {
            binding.card.cardRound.setVisibility(View.GONE);
        } else {
            binding.card.cardRound.setVisibility(View.VISIBLE);
            binding.card.countCard.setText(String.valueOf(MainjsonObject.length()));
        }
    }

    public interface onbackFragmentLoad {
        void onBack(ProductList list);
    }


}






























































