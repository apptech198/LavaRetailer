package com.apptech.myapplication.fragment.purchase_request_now;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.adapter.PurchaseNow1Adapter;
import com.apptech.myapplication.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.myapplication.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.myapplication.databinding.FragmentPurchaseRequestNow1Binding;
import com.apptech.myapplication.modal.product.ProductList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PurchaseRequestNowFragment1 extends Fragment {

    private PurchaseRequestNowViewModel mViewModel;
    List<ProductList> productLists = new ArrayList<>();
    FragmentPurchaseRequestNow1Binding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseRequestNow1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fakeProduct();
        binding.noproduct.setVisibility(View.GONE);
        binding.PurchaseNowRecyclerView.setAdapter(new PurchaseNow1Adapter(productLists));
        binding.PurchaseNowRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        binding.PurchaseNowRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL));


        binding.filterShort.setOnClickListener(v -> {
            new ShortFilterBottomSheetFragment().show(getChildFragmentManager(), "short bottom sheet");
        });
        binding.filterPrice.setOnClickListener(v -> {
            new CategoryFilterBottomSheetFragment().show(getChildFragmentManager(), "short bottom sheet");
        });

    }

    private void fakeProduct() {
        productLists.add(new ProductList("1" , "iphone 11", "https://assets.kogan.com/files/product/etail/Apple-/KHIP11128BLK_1.jpg?auto=webp&canvas=753%2C502&fit=bounds&height=502&quality=75&width=753",""));
        productLists.add(new ProductList("2" ,"oneplus 6", "https://cdn-files.kimovil.com/default/0002/40/thumb_139877_default_big.jpeg",""));
        productLists.add(new ProductList("3" ,"Xiaomi Phones in Kenya", "https://sokobest.co.ke/wp-content/uploads/2019/09/Mi-Xiaomi-Mi-A2-1024x1024.jpg",""));
        productLists.add(new ProductList("4" ,"iphone 11", "https://assets.kogan.com/files/product/etail/Apple-/KHIP11128BLK_1.jpg?auto=webp&canvas=753%2C502&fit=bounds&height=502&quality=75&width=753",""));
        productLists.add(new ProductList("5" ,"oneplus 6", "https://cdn-files.kimovil.com/default/0002/40/thumb_139877_default_big.jpeg",""));
        productLists.add(new ProductList("6" ,"Xiaomi Phones in Kenya", "https://sokobest.co.ke/wp-content/uploads/2019/09/Mi-Xiaomi-Mi-A2-1024x1024.jpg",""));
    }


}































