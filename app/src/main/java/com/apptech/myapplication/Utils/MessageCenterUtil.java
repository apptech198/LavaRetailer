package com.apptech.myapplication.Utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.apptech.myapplication.modal.message.MessageList;

import java.util.List;

public class MessageCenterUtil extends DiffUtil.Callback {


    List<MessageList> oldList;
    List<MessageList> newList;


    public MessageCenterUtil(List<MessageList> newlist, List<MessageList> oldlist) {
        this.oldList = oldlist;
        this.newList = newlist;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition) == oldList.get(oldItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        return result == 0;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        MessageList newModel = newList.get(newItemPosition);
        MessageList oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (newModel.isExpanble() != oldModel.isExpanble()) {
            diff.putBoolean("msg", newModel.isExpanble());
        }

        return diff;

    }
}

















