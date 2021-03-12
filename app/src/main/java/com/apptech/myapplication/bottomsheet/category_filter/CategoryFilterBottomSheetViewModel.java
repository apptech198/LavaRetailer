package com.apptech.myapplication.bottomsheet.category_filter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.apptech.myapplication.modal.category.CategoryList;

import java.util.ArrayList;
import java.util.List;

public class CategoryFilterBottomSheetViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<List<CategoryList>> listLiveData = new MutableLiveData<>();

    public CategoryFilterBottomSheetViewModel() {
        fakeCateogry();
    }

    public MutableLiveData<List<CategoryList>> getCategory() {
        return listLiveData;
    }

    private void fakeCateogry() {
        List<CategoryList> categoryLists = new ArrayList<>();
        categoryLists.add(new CategoryList("1", "Category1"));
        categoryLists.add(new CategoryList("2", "Category2"));
        categoryLists.add(new CategoryList("3", "Category3"));
        categoryLists.add(new CategoryList("4", "Category4"));
        categoryLists.add(new CategoryList("5", "Category5"));
        listLiveData.setValue(categoryLists);
    }


}