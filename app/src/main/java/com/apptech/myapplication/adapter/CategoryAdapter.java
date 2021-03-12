package com.apptech.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowCategoryBinding;
import com.apptech.myapplication.modal.category.CategoryList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewBinding> {

    Context context;
    RowCategoryBinding binding;
    List<CategoryList> categoryLists;
    int noposition = RecyclerView.NO_POSITION;
    private static final String TAG = "CategoryAdapter";
    CategoryItemClickInterface categoryItemClickInterface;

    public CategoryAdapter(List<CategoryList> categoryLists, CategoryItemClickInterface categoryItemClickInterface) {
        this.categoryLists = categoryLists;
        this.categoryItemClickInterface = categoryItemClickInterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {
        CategoryList list = categoryLists.get(position);
        binding.setList(list);

        binding.CategoryLayout.setOnClickListener(v -> {
            noposition = position;
            notifyDataSetChanged();
        });

        if (noposition == position) {
            Log.e(TAG, "onBindViewHolder: " + String.valueOf(noposition));
            binding.categoryRadio.setChecked(true);
        }
        Log.e(TAG, "onBindViewHolder1: " + String.valueOf(noposition));
        binding.categoryRadio.setChecked(false);


        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull @NotNull RowCategoryBinding itemView) {
            super(itemView.getRoot());
        }
    }

    public interface CategoryItemClickInterface {
        void categoryItemClick(CategoryList category);
    }


}





















