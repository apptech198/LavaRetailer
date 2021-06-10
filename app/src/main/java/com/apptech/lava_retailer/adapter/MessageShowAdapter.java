package com.apptech.lava_retailer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apptech.lava_retailer.R;
import com.apptech.lava_retailer.Utils.MessageShowUtils;
import com.apptech.lava_retailer.databinding.RowMessageShowBinding;
import com.apptech.lava_retailer.modal.notification_list.NotificationListShow;
import com.apptech.lava_retailer.other.SessionManage;
import com.apptech.lava_retailer.service.ApiClient;
import com.bumptech.glide.Glide;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class MessageShowAdapter extends RecyclerView.Adapter<MessageShowAdapter.Viewholder> {

    List<NotificationListShow> frontMsgShowLists;
    Context context;
    MessageShowInterface messageShowInterface;
    private static final String TAG = "MessageShowAdapter";
    final ExpansionLayoutCollection expansionLayoutCollection = new ExpansionLayoutCollection();
    SessionManage sessionManage;
    JSONObject mainJsonObject = new JSONObject();


    public MessageShowAdapter(List<NotificationListShow> frontMsgShowLists, MessageShowInterface messageShowInterface) {
        this.frontMsgShowLists = frontMsgShowLists;
        this.messageShowInterface = messageShowInterface;
    }

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        sessionManage = SessionManage.getInstance(context);
        RowMessageShowBinding binding = RowMessageShowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        NotificationListShow list = frontMsgShowLists.get(position);

        holder.binding.msgTitle.setText(list.getHeading());
        holder.binding.msgfull.setText(list.getDes());

        if(list.getImg() != null && !list.getImg().isEmpty()){
            holder.binding.img.setVisibility(View.VISIBLE);
            Log.e(TAG, "onBindViewHolder: " +  ApiClient.Image_URL + list.getImg());
            Glide.with(context).load(ApiClient.Image_URL + list.getImg()).into(holder.binding.img);
        }else {
            holder.binding.img.setVisibility(View.GONE);
        }

        holder.binding.expansionLayout.addListener((expansionLayout, expanded) -> {
//            Drawable new_image = context.getResources().getDrawable(R.drawable.ic_check);
//            holder.binding.img1.setBackgroundDrawable(new_image);
            holder.binding.dotImg.setVisibility(View.GONE);
            holder.binding.topMainLayout.setBackgroundColor(Color.parseColor("#323232"));
            messageShowInterface.ExpansionLayout(position , list , holder.binding.img1);
        });

        try {
                if (sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE") != null) {
                    mainJsonObject = new JSONObject(Objects.requireNonNull(sessionManage.getUserDetails().get("NOTIFICATION_LIST_STORE")));

                    String JsonId = mainJsonObject.getJSONObject(list.getId()).getString("ID");

                    Log.e(TAG, "onBindViewHolder: " + JsonId );
                    Log.e(TAG, "onBindViewHolderddvddvdvd: " + list.getId());

                    if(JsonId.equalsIgnoreCase(list.getId())){
//                        Drawable new_image = context.getResources().getDrawable(R.drawable.ic_check);
//                        holder.binding.img1.setBackgroundDrawable(new_image);
                        holder.binding.dotImg.setVisibility(View.GONE);
                        holder.binding.topMainLayout.setBackgroundColor(Color.parseColor("#323232"));
                        return;
                    }
                } else {
//                    Drawable new_image = context.getResources().getDrawable(R.drawable.ic_baseline_info_24);
//                    holder.binding.img1.setBackgroundDrawable(new_image);
                    holder.binding.dotImg.setVisibility(View.VISIBLE);
                }
            }catch (NullPointerException | JSONException e){
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: " + e.getMessage() );
//            Drawable new_image = context.getResources().getDrawable(R.drawable.ic_baseline_info_24);
//            holder.binding.img1.setBackgroundDrawable(new_image);
            holder.binding.dotImg.setVisibility(View.VISIBLE);
        }

        expansionLayoutCollection.add(holder.binding.expansionLayout);
        expansionLayoutCollection.openOnlyOne(true);



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

        void ExpansionLayout(int pos, NotificationListShow list , ImageView img);
    }

}









































