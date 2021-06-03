package com.apptech.lava_retailer.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowOrderStatusBinding;
import com.apptech.lava_retailer.list.ClaimTypeList;
import com.apptech.lava_retailer.list.OrderStatusList;
import com.apptech.lava_retailer.list.passbook.List;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.apptech.lava_retailer.service.LavaInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.Viewholder> {

    java.util.List<OrderStatusList> lists;
    Context context;
    SessionManage sessionManage;
    String currency;
    ProgressDialog dialog;
    String type;

    public OrderStatusAdapter(java.util.List<OrderStatusList> orderStatusLists , String type) {
        this.lists = orderStatusLists;
        this.type=type;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        dialog= new ProgressDialog(parent.getContext());
        dialog.setMessage("Please Wait...");
        sessionManage = SessionManage.getInstance(context);
        currency =  sessionManage.getUserDetails().get(SessionManage.LOGIN_COUNTRY_CURRENCY_SYMBOL);
        return new Viewholder(RowOrderStatusBinding.inflate(LayoutInflater.from(parent.getContext()) , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        OrderStatusList list = lists.get(position);

        holder.binding.productName.setText(list.getProduct_name());
        holder.binding.productQty.setText(list.getQty());
        holder.binding.modalName.setText(list.getModel_name());
        holder.binding.status.setText(list.getStatus());
        holder.binding.Imei.setText(list.getImei());
        holder.binding.orderDate.setText(list.getTime().substring(0 , 10));
        holder.binding.orderNumber.setText("order No : " + list.getOrder_no());


        switch (list.getStatus()){
            case "DELIVERED":
                holder.binding.status.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case "PENDING":
                holder.binding.status.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
            case "CANCEL":
                holder.binding.status.setTextColor(context.getResources().getColor(R.color.red));
                break;
        }


        try {
            int disAmt = Integer.parseInt(list.getDiscount_price()) * Integer.parseInt(list.getQty());
            holder.binding.productamt.setText(currency + disAmt);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        if(list.getStatus().toUpperCase().trim().equals("PENDING")){
            holder.binding.cancel.setVisibility(View.VISIBLE);
        }

        holder.binding.cancel.setOnClickListener(v -> {
            CencelOrder(list.getId(), position);
        });


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowOrderStatusBinding binding;

        public Viewholder(@NonNull RowOrderStatusBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
    
    
    void CencelOrder(String id, int pos){
        dialog.show();
        LavaInterface lavaInterface = ApiClient.getClient().create(LavaInterface.class);
        lavaInterface.CENCEL_ORDER(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(new Gson().toJson(response.body()));
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        if(error.equalsIgnoreCase("FALSE")){
                            lists.remove(pos);
                            notifyDataSetChanged();
                            dialog.cancel();
                            return;
                        }
                        dialog.cancel();
                        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.cancel();
                Toast.makeText(context, "" + context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }




            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                dialog.cancel();
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    
}
