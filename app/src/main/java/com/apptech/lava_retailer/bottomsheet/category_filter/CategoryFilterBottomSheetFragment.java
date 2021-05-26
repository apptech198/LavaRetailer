package com.apptech.lava_retailer.bottomsheet.category_filter;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.lava_retailer.adapter.CategoryAdapter;
import com.apptech.lava_retailer.databinding.CategoryFilterBottomSheetFragmentBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.modal.category.CategoryList;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class CategoryFilterBottomSheetFragment extends BottomSheetDialogFragment implements CategoryAdapter.CategoryItemClickInterface {

    private CategoryFilterBottomSheetViewModel mViewModel;
    CategoryFilterBottomSheetFragmentBinding binding;
    List<ComodityLists> comodityLists;


    public CategoryFilterBottomSheetFragment(List<ComodityLists> comodityLists) {
        this.comodityLists = comodityLists;
    }

//    public static CategoryFilterBottomSheetFragment newInstance(List<ComodityLists> comodityLists) {
//        return new CategoryFilterBottomSheetFragment(comodityLists);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CategoryFilterBottomSheetFragmentBinding.inflate(inflater , container , false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoryFilterBottomSheetViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getCategory().observe(requireActivity() , categoryLists -> {
            binding.categoryRecyclerView.setAdapter(new CategoryAdapter(categoryLists , this));
        });

        binding.categoryRecyclerView.setAdapter(new CategoryAdapter(comodityLists,this));

    }


    @Override
    public void categoryItemClick(CategoryList category) {

    }
}




























