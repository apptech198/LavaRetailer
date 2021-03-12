package com.apptech.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.databinding.RowMessageBinding;
import com.apptech.myapplication.modal.message.MessageList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewBinding> {

    RowMessageBinding binding;
    List<MessageList> messageLists;

    public MessageAdapter(List<MessageList> messageLists) {
        this.messageLists = messageLists;
    }

    @NonNull
    @NotNull
    @Override
    public ViewBinding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = RowMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewBinding(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position) {
        MessageList messageList = messageLists.get(position);

        boolean isExpanded = messageList.isExpanble();
        binding.desLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        binding.setList(messageList);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull @NotNull RowMessageBinding itemView) {
            super(itemView.getRoot());


            binding.mainLayout.setOnClickListener(v -> {
                MessageList messageList = messageLists.get(getAdapterPosition());
                messageList.setExpanble(!messageList.isExpanble());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }
}
































