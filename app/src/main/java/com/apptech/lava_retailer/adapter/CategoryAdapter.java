package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.databinding.RowCategoryBinding;
import com.apptech.lava_retailer.list.comodity_list.ComodityLists;
import com.apptech.lava_retailer.other.SessionManage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewBinding> {

    Context context;
    List<ComodityLists> categoryLists;
    int noposition = RecyclerView.NO_POSITION;
    private static final String TAG = "CategoryAdapter";
    CategoryItemClickInterface categoryItemClickInterface;
    SessionManage sessionManage;


    public CategoryAdapter(List<ComodityLists> comodityLists, CategoryItemClickInterface categoryItemClickInterface) {
        this.categoryLists = comodityLists;
        this.categoryItemClickInterface = categoryItemClickInterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new ViewBinding(RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {

        ComodityLists list = categoryLists.get(position);

        String string = "";

        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":
                 string = list.getName().substring(0, 1).toUpperCase() + list.getName().substring(1).toLowerCase();
                holder.binding.categoryName.setText(string);
                break;
            case "fr":
                if(list.getName_fr().isEmpty()){
                    string = list.getName().substring(0, 1).toUpperCase() + list.getName().substring(1).toLowerCase();
                    holder.binding.categoryName.setText(string);
                }else {
                    string = list.getName_fr().substring(0, 1).toUpperCase() + list.getName_fr().substring(1).toLowerCase();
                    holder.binding.categoryName.setText(string);
                }
                break;
            case "ar":
                string = list.getName_ar().substring(0, 1).toUpperCase() + list.getName_ar().substring(1).toLowerCase();
                holder.binding.categoryName.setText(string);
                break;
        }



        holder.binding.CategoryLayout.setOnClickListener(v -> {
          categoryItemClickInterface.categoryItemClick(list);
        });
//
//        if (noposition == position) {
//            Log.e(TAG, "onBindViewHolder: " + String.valueOf(noposition));
//            binding.categoryRadio.setChecked(true);
//        }
//        Log.e(TAG, "onBindViewHolder1: " + String.valueOf(noposition));
//        binding.categoryRadio.setChecked(false);


    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class ViewBinding extends RecyclerView.ViewHolder {

        RowCategoryBinding binding;

        public ViewBinding(@NonNull @NotNull RowCategoryBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
        }
    }

    public interface CategoryItemClickInterface {
        void categoryItemClick(ComodityLists category);
    }


}





















