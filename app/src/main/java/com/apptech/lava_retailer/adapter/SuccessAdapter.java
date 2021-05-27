package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.modal.card.CardList;
import com.apptech.lava_retailer.other.NumberConvertArabic;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SuccessAdapter extends RecyclerView.Adapter<SuccessAdapter.Viewholder> {

    List<CardList> cardLists;
    SessionManage sessionManage;
    Context context;


    public SuccessAdapter(List<CardList> cartlist) {
        this.cardLists = cartlist;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_carts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        CardList list = cardLists.get(position);

        try {
            int a = Integer.parseInt(list.getActual_price());
            int b = Integer.parseInt(list.getDis_price());

            if(a == b){
                holder.ProductAmtDis.setVisibility(View.GONE);
            }else {
                holder.ProductAmtDis.setVisibility(View.VISIBLE);
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }


        if (sessionManage.getUserDetails().get("LANGUAGE").equals("ar")) {
            holder.ProductName.setText(list.getMarketing_name_ar());
            holder.ModalName.setText("Model : " + list.getModel_ar());
            Glide.with(context).load(ApiClient.Image_URL + list.getThumb_ar()).centerCrop().into(holder.img);

            try {
                holder.ProductAmt.setText(context.getResources().getString(R.string.egp) + new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getDis_price())));
                holder.ProductAmtDis.setText(context.getResources().getString(R.string.egp) + new NumberConvertArabic().NumberConvertArabic(Integer.parseInt(list.getActual_price())));
                int a = Integer.parseInt(list.getQty());
                holder.cartQty.setText(new NumberConvertArabic().NumberConvertArabic(a));

            }catch (NumberFormatException e){
                e.printStackTrace();
            }

        } else if(sessionManage.getUserDetails().get("LANGUAGE").equals("en")){
            holder.ProductName.setText(list.getMarketing_name());
            holder.ModalName.setText("Model : " + list.getModel());
            Glide.with(context).load(ApiClient.Image_URL + list.getThumb()).centerCrop().into(holder.img);
            holder.ProductAmt.setText(context.getResources().getString(R.string.egp) + list.getDis_price());
            holder.ProductAmtDis.setText(context.getResources().getString(R.string.egp) + list.getActual_price());
            holder.cartQty.setText(list.getQty());

        }else {
            if(list.getMarketing_name_fr().isEmpty()){
                holder.ProductName.setText(list.getMarketing_name());
            }else {
                holder.ProductName.setText(list.getMarketing_name_fr());
            }
            holder.ModalName.setText("Model : " + list.getModel());
            Glide.with(context).load(ApiClient.Image_URL + list.getThumb()).centerCrop().into(holder.img);
            holder.ProductAmt.setText(context.getResources().getString(R.string.egp) + list.getDis_price());
            holder.ProductAmtDis.setText(context.getResources().getString(R.string.egp) + list.getActual_price());
            holder.cartQty.setText(list.getQty());

        }
        holder.ProductAmtDis.setPaintFlags(holder.ProductAmtDis.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        holder.Carfaty.setVisibility(View.GONE);
        holder.Productremove.setVisibility(View.GONE);
        holder.plusQty.setVisibility(View.GONE);
        holder.minQty.setVisibility(View.GONE);
        holder.Productremove.setVisibility(View.GONE);


//        holder.Productremove.setOnClickListener(v -> cardInterface.removeItem(position, list));
//        holder.plusQty.setOnClickListener(v ->  cardInterface.addQty(position , list , holder.cartQty));
//        holder.minQty.setOnClickListener(v ->  cardInterface.minQty(position , list , holder.cartQty));

    }

    @Override
    public int getItemCount() {
        return cardLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView img ;
        TextView ProductName, ProductAmt, ProductAmtDis , cartQty  , ModalName;
        LinearLayout Productremove , plusQty , minQty;
        MaterialCardView Carfaty;

        public Viewholder(@NonNull View itemView) {
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
            Carfaty = itemView.findViewById(R.id.Carfaty);
        }
    }
}
