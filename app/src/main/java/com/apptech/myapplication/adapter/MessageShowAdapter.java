package com.apptech.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.myapplication.R;
import com.apptech.myapplication.Utils.MessageShowUtils;
import com.apptech.myapplication.databinding.RowMessageShowBinding;
import com.apptech.myapplication.modal.notification_list.NotificationListShow;
import com.apptech.myapplication.service.ApiClient;
import com.bumptech.glide.Glide;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.Viewholder> {

    List<NotificationListShow> frontMsgShowLists;
    Context context;
    MessageShowInterface messageShowInterface;
    private static final String TAG = "MessageShowAdapter";
    final ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();


    public MessageShowAdapter(List<NotificationListShow> frontMsgShowLists, MessageShowInterface messageShowInterface) {
        this.frontMsgShowLists = frontMsgShowLists;
        this.messageShowInterface = messageShowInterface;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowMessageShowBinding binding = RowMessageShowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        NotificationListShow list = frontMsgShowLists.get(position);

        holder.binding.msgTitle.setText(list.getHeading());
        holder.binding.msgfull.setText(list.getDes());

        expansionLayoutCollection.add(holder.binding.expansionLayout);
        expansionLayoutCollection.openOnlyOne(true);

        if(list.getImg() != null && !list.getImg().isEmpty()){
            holder.binding.img.setVisibility(View.VISIBLE);
            Log.e(TAG, "onBindViewHolder: " +  ApiClient.Image_URL + list.getImg());
            Glide.with(context).load(ApiClient.Image_URL + list.getImg()).placeholder(R.drawable.check_icon).into(holder.binding.img);
        }else {
            holder.binding.img.setVisibility(View.GONE);
        }

        holder.binding.img2.setOnClickListener(v -> messageShowInterface.removeitem(position, list));

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

        RowMessageShowBinding binding;

        public Viewholder(@NonNull @NotNull RowMessageShowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public interface MessageShowInterface {
        void itemClick(NotificationListShow list);

        void removeitem(int pos, NotificationListShow list);
    }

}









































