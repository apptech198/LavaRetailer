package com.apptech.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.Utils.MessageShowUtils;
import com.apptech.myapplication.databinding.RowMessageShowBinding;
import com.apptech.myapplication.modal.notification_list.NotificationListShow;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.Viewholder> {

    RowMessageShowBinding binding;
    List<NotificationListShow> frontMsgShowLists;
    Context context;
    MessageShowInterface messageShowInterface;

    public MessageShowAdapter(List<NotificationListShow> frontMsgShowLists, MessageShowInterface messageShowInterface) {
        this.frontMsgShowLists = frontMsgShowLists;
        this.messageShowInterface = messageShowInterface;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        binding = RowMessageShowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.setMessageinterface(messageShowInterface);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        NotificationListShow list = frontMsgShowLists.get(position);
        binding.setList(list);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return frontMsgShowLists.size();
    }


    public void setData(List<NotificationListShow> newData) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MessageShowUtils(newData, frontMsgShowLists));
        diffResult.dispatchUpdatesTo(this);
        frontMsgShowLists.clear();
        this.frontMsgShowLists.addAll(newData);

    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull @NotNull RowMessageShowBinding itemView) {
            super(itemView.getRoot());
            binding.img2.setOnClickListener(v -> {
                messageShowInterface.removeitem(getAdapterPosition(), frontMsgShowLists.get(getAdapterPosition()));
            });
        }
    }

    public interface MessageShowInterface {
        void itemClick(NotificationListShow list);

        void removeitem(int pos, NotificationListShow list);
    }

}









































