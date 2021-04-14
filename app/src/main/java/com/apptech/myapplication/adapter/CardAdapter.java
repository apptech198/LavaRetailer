package com.apptech.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.R;
import com.apptech.myapplication.Utils.CartDiffUtils;
import com.apptech.myapplication.modal.QtyList;
import com.apptech.myapplication.modal.card.CardList;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    List<CardList> cardLists;
    private static final String TAG = "CardAdapter";
    Context context;
    CardInterface cardInterface;
    ArrayList<QtyList> qtyLists = new ArrayList<>();
    boolean openQty = false;

    public CardAdapter(List<CardList> cardData, CardInterface cardInterface) {
        this.cardLists = cardData;
        this.cardInterface = cardInterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_carts, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CardList list = cardLists.get(position);
        Glide.with(context).load(list.getImg()).centerCrop().into(holder.img);
        holder.ProductName.setText(list.getName());
        holder.ProductAmt.setText(context.getResources().getString(R.string.rs_symbol) + "1200");
        holder.ProductAmtDis.setText(context.getResources().getString(R.string.rs_symbol) + "2000");
        holder.Productremove.setOnClickListener(v -> cardInterface.removeItem(position, list));
    }

    @Override
    public int getItemCount() {
        return cardLists.size();
    }

    public void setData(List<CardList> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CartDiffUtils(newData, cardLists));
        diffResult.dispatchUpdatesTo(this);
        cardLists.clear();
        this.cardLists.addAll(newData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView ProductName, ProductAmt, ProductAmtDis;
        LinearLayout Productremove;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.ProductImg);
            ProductName = itemView.findViewById(R.id.ProductName);
            ProductAmt = itemView.findViewById(R.id.ProductAmt);
            ProductAmtDis = itemView.findViewById(R.id.ProductAmtDis);
            Productremove = itemView.findViewById(R.id.Productremove);
            CardView qtySpinnerOpen = itemView.findViewById(R.id.qtySpinnerOpen);
            CardView mainLayout = itemView.findViewById(R.id.mainLayout);
            CardView QtyOpenLOayout = itemView.findViewById(R.id.QtyOpenLOayout);


            qtySpinnerOpen.setOnClickListener(v ->{
                QtyOpenLOayout.setVisibility(View.VISIBLE);
            });
            mainLayout.setOnClickListener(v ->{
//                if(openQty)
                QtyOpenLOayout.setVisibility(View.INVISIBLE);
            });


        }

        private void qtyList() {
            qtyLists.add(new QtyList(0, "Select Qty"));
            qtyLists.add(new QtyList(1, "1"));
            qtyLists.add(new QtyList(2, "2"));
            qtyLists.add(new QtyList(3, "3"));
            qtyLists.add(new QtyList(4, "4"));
        }


    }

    public interface CardInterface {
        void removeItem(int postion, CardList list);
    }


}































