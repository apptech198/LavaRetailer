package com.apptech.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apptech.myapplication.R;
import com.apptech.myapplication.modal.CheckEntriesSellOutImeiMonthYearsList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CheckEntriesSellOutInvalidAdapter extends ArrayAdapter<CheckEntriesSellOutImeiMonthYearsList> {

    public CheckEntriesSellOutInvalidAdapter(Context context, ArrayList<CheckEntriesSellOutImeiMonthYearsList> yearsLists) {
        super(context, 0, yearsLists);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_spinner_check_entries_valid_years, parent, false
            );
        }
//        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.TextView);
        CheckEntriesSellOutImeiMonthYearsList yearsList = getItem(position);
        if (yearsList != null) {
//            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewName.setText(yearsList.getDate());
        }
        return convertView;
    }


}
