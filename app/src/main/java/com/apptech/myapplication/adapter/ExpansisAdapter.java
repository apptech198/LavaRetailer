package com.apptech.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowExpanbleBinding;
import com.apptech.myapplication.modal.message.MessageList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpansisAdapter extends RecyclerView.Adapter<ExpansisAdapter.Viewholder> {


    RowExpanbleBinding binding;
    List<com.apptech.myapplication.modal.message.List> messageLists;
    private static final String TAG = "ExpansisAdapter";


    public ExpansisAdapter(List<com.apptech.myapplication.modal.message.List> list) {
        this.messageLists = list;
    }


    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = RowExpanbleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        com.apptech.myapplication.modal.message.List messageList = messageLists.get(position);
        binding.setList(messageList);
        binding.expansionLayout.addListener((expansionLayout, expanded) -> {
            Log.e(TAG, "onBindViewHolder: " + expanded);
//            if (expanded) {
//                binding.msg.setVisibility(View.GONE);
//            } else {
//                binding.msg.setVisibility(View.VISIBLE);
//            }
        });
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull @NotNull RowExpanbleBinding itemView) {
            super(itemView.getRoot());
        }
    }
}
