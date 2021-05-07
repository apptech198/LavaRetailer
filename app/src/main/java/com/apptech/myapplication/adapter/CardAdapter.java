package com.apptech.myapplication.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.apptech.myapplication.other.SessionManage;
import com.apptech.myapplication.service.ApiClient;
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
    SessionManage sessionManage;


    public CardAdapter(List<CardList> cardData, CardInterface cardInterface) {
        this.cardLists = cardData;
        this.cardInterface = cardInterface;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_carts, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CardList list = cardLists.get(position);

        if (!sessionManage.getUserDetails().get("LANGUAGE").equals("en")) {
            holder.ProductName.setText(list.getMarketing_name_ar());
            holder.ModalName.setText("Model : " + list.getModel_ar());
            Glide.with(context).load(ApiClient.Image_URL + list.getThumb_ar()).centerCrop().into(holder.img);
        } else {
            holder.ProductName.setText(list.getMarketing_name());
            holder.ModalName.setText("Model : " + list.getModel());
            Glide.with(context).load(ApiClient.Image_URL + list.getThumb()).centerCrop().into(holder.img);
        }

        holder.ProductAmt.setText(context.getResources().getString(R.string.egp) + list.getDis_price());
        holder.ProductAmtDis.setText(context.getResources().getString(R.string.egp) + list.getActual_price());
        holder.cartQty.setText(list.getQty());
        holder.ProductAmtDis.setPaintFlags(holder.ProductAmtDis.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.Productremove.setOnClickListener(v -> cardInterface.removeItem(position, list));
        holder.plusQty.setOnClickListener(v ->  cardInterface.addQty(position , list , holder.cartQty));
        holder.minQty.setOnClickListener(v ->  cardInterface.minQty(position , list , holder.cartQty));

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

        ImageView img ;
        TextView ProductName, ProductAmt, ProductAmtDis , cartQty  , ModalName;
        LinearLayout Productremove , plusQty , minQty;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.ProductImg);
            ProductName = itemView.findViewById(R.id.ProductName);
            ProductAmt = itemView.findViewById(R.id.ProductAmt);
            ProductAmtDis = itemView.findViewById(R.id.ProductAmtDis);
            Productremove = itemView.findViewById(R.id.Productremove);
            cartQty = itemView.findViewById(R.id.cartQty);
            plusQty = itemView.findViewById(R.id.plusQty);
            minQty = itemView.findViewById(R.id.minQty);
            ModalName = itemView.findViewById(R.id.ModalName);

        }


    }

    public interface CardInterface {
        void removeItem(int postion, CardList list);
        void addQty(int postion, CardList list , TextView cartQty);
        void minQty(int postion, CardList list , TextView cartQty);
    }


}































