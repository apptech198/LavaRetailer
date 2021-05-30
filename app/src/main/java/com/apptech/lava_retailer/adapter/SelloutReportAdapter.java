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
import com.apptech.lava_retailer.list.sell_out_report.SellOutReportList;
import com.apptech.lava_retailer.other.SessionManage;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class SelloutReportAdapter extends RecyclerView.Adapter<SelloutReportAdapter.Viewholder> {


    Context context;
    private static final String TAG = "SelloutReportAdapter";
    List<SellOutReportList> sellOutReportLists;
    SessionManage sessionManage;

    public SelloutReportAdapter(List<SellOutReportList> sellOutReportLists) {
        this.sellOutReportLists = sellOutReportLists;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(RowSellOutReportBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        SellOutReportList l = sellOutReportLists.get(position);


        switch (sessionManage.getUserDetails().get("LANGUAGE")){
            case "en":

                break;
            case "fr":
//                if(countryLists.get(0).getName_fr().isEmpty()){
////                    popupMenu1.getMenu().add(i, id, i ,object.getString("name"));
//                }else {
////                    popupMenu1.getMenu().add(i, id, i ,object.getString("name_fr"));
//                }
                break;
            case "ar":

                break;
        }



        holder.binding.modal.setText(l.getModel());
        holder.binding.Category.setText(l.getCommodity());

    }


    @Override
    public int getItemCount() {
        return sellOutReportLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowSellOutReportBinding binding;

        public Viewholder(@NonNull RowSellOutReportBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}












