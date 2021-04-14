package com.apptech.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.Utils.MessageCenterUtil;
import com.apptech.myapplication.databinding.RowMessageBinding;
import com.apptech.myapplication.modal.message.MessageList;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewBinding> {

    RowMessageBinding binding;
    List<MessageList> messageLists;
    private static final String TAG = "MessageAdapter";
    ExpInterface expInterface;

    public MessageAdapter(List<MessageList> messageLists, ExpInterface expInterface) {
        this.messageLists = messageLists;
        this.expInterface = expInterface;
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
    public void onBindViewHolder(@NonNull @NotNull ViewBinding holder, int position, @NonNull @NotNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Log.e(TAG, "onBindViewHolder: check" + "adcscsdcsdc" );
        }
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }


    public void setData(List<MessageList> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MessageCenterUtil(newData, messageLists));
        diffResult.dispatchUpdatesTo(this);
        messageLists.clear();
        this.messageLists.addAll(newData);

    }

    public class ViewBinding extends RecyclerView.ViewHolder {
        public ViewBinding(@NonNull @NotNull RowMessageBinding itemView) {
            super(itemView.getRoot());

            binding.mainLayout.setOnClickListener(v -> {
                MessageList messageList = messageLists.get(getAdapterPosition());
                Log.e(TAG, "ViewBinding: " + messageList.isExpanble());
                messageList.setExpanble(!messageList.isExpanble());
                notifyItemChanged(getAdapterPosition());
                expInterface.onitemClick(messageList, getAdapterPosition());
            });

        }
    }

    public interface ExpInterface {
        void onitemClick(MessageList list, int pos);
    }

}
































