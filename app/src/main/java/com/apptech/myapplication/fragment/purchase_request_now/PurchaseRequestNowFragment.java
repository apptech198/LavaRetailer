package com.apptech.myapplication.fragment.purchase_request_now;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.PurchaseNowAdapter;
import com.apptech.myapplication.bottomsheet.category_filter.CategoryFilterBottomSheetFragment;
import com.apptech.myapplication.bottomsheet.short_filter.ShortFilterBottomSheetFragment;
import com.apptech.myapplication.databinding.PurchaseRequestNowFragmentBinding;
import com.apptech.myapplication.modal.product.ProductList;
import com.apptech.myapplication.other.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestNowFragment extends Fragment implements PurchaseNowAdapter.PurchaseNowIterface {

    private PurchaseRequestNowViewModel mViewModel;
    PurchaseRequestNowFragmentBinding binding;
    List<ProductList> productLists = new ArrayList<>();
    private static final String TAG = "PurchaseRequestNowFragm";
    BackInterface backInterface;

//    public static PurchaseRequestNowFragment newInstance() {
//        return new PurchaseRequestNowFragment(b);
//    }

    public PurchaseRequestNowFragment(BackInterface back) {
        this.backInterface = back;
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
        fakeProduct();

        binding.viewCardFloat.setOnClickListener(v -> {
            loadfragment(new PurchaseRequestNowFragment1());
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
        productLists.add(new ProductList("iphone 11", "https://assets.kogan.com/files/product/etail/Apple-/KHIP11128BLK_1.jpg?auto=webp&canvas=753%2C502&fit=bounds&height=502&quality=75&width=753"));
        productLists.add(new ProductList("oneplus 6", "https://cdn-files.kimovil.com/default/0002/40/thumb_139877_default_big.jpeg"));
        productLists.add(new ProductList("Xiaomi Phones in Kenya", "https://sokobest.co.ke/wp-content/uploads/2019/09/Mi-Xiaomi-Mi-A2-1024x1024.jpg"));
        productLists.add(new ProductList("iphone 11", "https://assets.kogan.com/files/product/etail/Apple-/KHIP11128BLK_1.jpg?auto=webp&canvas=753%2C502&fit=bounds&height=502&quality=75&width=753"));
        productLists.add(new ProductList("oneplus 6", "https://cdn-files.kimovil.com/default/0002/40/thumb_139877_default_big.jpeg"));
        productLists.add(new ProductList("Xiaomi Phones in Kenya", "https://sokobest.co.ke/wp-content/uploads/2019/09/Mi-Xiaomi-Mi-A2-1024x1024.jpg"));
    }


    private void loadfragment(Fragment fragment) {
        if (fragment != null)
            getChildFragmentManager().beginTransaction().replace(R.id.framelayout1, fragment).addToBackStack(null).commit();
    }


    @Override
    public void itemClick(ProductList list) {
        Log.e(TAG, "itemClick: " + list.toString());
        backInterface.onback(list);
    }


    public interface BackInterface {
        void onback(ProductList list);
    }

}






























































