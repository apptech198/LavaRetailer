package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowSellOutReportBinding;
import com.google.android.material.textview.MaterialTextView;

public class SelloutReportAdapter extends RecyclerView.Adapter<SelloutReportAdapter.Viewholder> {


    Context context;
    private static final String TAG = "SelloutReportAdapter";
    int pos;


    public SelloutReportAdapter(int pos) {
        this.pos = pos;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new Viewholder(RowSellOutReportBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {



    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return pos;
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutReportBinding binding;

        public Viewholder(@NonNull RowSellOutReportBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
