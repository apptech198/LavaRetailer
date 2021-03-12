package com.apptech.myapplication.bottomsheet.category_filter;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.myapplication.R;
import com.apptech.myapplication.adapter.CategoryAdapter;
import com.apptech.myapplication.databinding.CategoryFilterBottomSheetFragmentBinding;
import com.apptech.myapplication.modal.category.CategoryList;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryFilterBottomSheetFragment extends BottomSheetDialogFragment implements CategoryAdapter.CategoryItemClickInterface {

    private CategoryFilterBottomSheetViewModel mViewModel;
    CategoryFilterBottomSheetFragmentBinding binding;

    public static CategoryFilterBottomSheetFragment newInstance() {
        return new CategoryFilterBottomSheetFragment();
    }

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


    }


    @Override
    public void categoryItemClick(CategoryList category) {

    }
}




























