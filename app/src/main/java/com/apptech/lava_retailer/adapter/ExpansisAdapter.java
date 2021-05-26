package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.databinding.RowExpanbleBinding;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpansisAdapter extends RecyclerView.Adapter<ExpansisAdapter.Viewholder> {


    List<com.apptech.lava_retailer.modal.message.List> messageLists;
    private static final String TAG = "ExpansisAdapter";
    final ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();
    Context context;


    public ExpansisAdapter(List<com.apptech.lava_retailer.modal.message.List> list) {
        this.messageLists = list;
    }


    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowExpanbleBinding binding = RowExpanbleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        com.apptech.lava_retailer.modal.message.List messageList = messageLists.get(position);

        expansionLayoutCollection.add(holder.binding.expansionLayout);
        expansionLayoutCollection.openOnlyOne(true);

        holder.binding.msgTitle.setText(messageList.getHeading());
        holder.binding.msgfull.setText(messageList.getDes());

        if(messageList.getImg() != null && !messageList.getImg().isEmpty()){
            holder.binding.img.setVisibility(View.VISIBLE);
            Glide.with(context).load(ApiClient.Image_URL + messageList.getImg()).placeholder(R.drawable.check_icon).into(holder.binding.img);
        }else {
            holder.binding.img.setVisibility(View.GONE);
        }

        holder.binding.expansionLayout.addListener((expansionLayout, expanded) -> {
            Log.e(TAG, "onBindViewHolder: " + expanded);
        });

    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        RowExpanbleBinding binding;

        public Viewholder(@NonNull @NotNull RowExpanbleBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
