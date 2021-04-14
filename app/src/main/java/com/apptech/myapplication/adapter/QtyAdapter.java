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
import com.apptech.myapplication.modal.QtyList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QtyAdapter extends ArrayAdapter<QtyList> {


    public QtyAdapter(@NonNull Context context , ArrayList<QtyList> qtyLists) {
        super(context, 0, qtyLists);
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
                    R.layout.row_qty, parent, false
            );
        }
//        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.TextView);
        QtyList yearsList = getItem(position);
        if (yearsList != null) {
//            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewName.setText(yearsList.getName());
        }
        return convertView;
    }

}
